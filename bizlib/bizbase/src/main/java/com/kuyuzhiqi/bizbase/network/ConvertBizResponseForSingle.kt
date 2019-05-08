package com.kuyuzhiqi.bizbase.network

import com.kuyuzhiqi.bizbase.entity.RequestPortBO
import com.kuyuzhiqi.network.NetworkConstant
import com.kuyuzhiqi.network.reponse.BaseBizResponse
import com.kuyuzhiqi.network.reponse.EmptyResponse
import com.kuyuzhiqi.network.reponse.HttpResponse
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function

class ConvertBizResponseForSingle<T : BaseBizResponse>(private val requestPortBO: RequestPortBO) :
    SingleTransformer<HttpResponse<T>, T> {

    override fun apply(@NonNull single: Single<HttpResponse<T>>): SingleSource<T> {
        return single.flatMap(Function { httpResponse ->
            if (httpResponse.result != NetworkConstant.ServerRespond.RESULT_OK) {
                val result = httpResponse.result
                val msg = httpResponse.message

                //                    return Single.error(BizExceptionHelper.createBizException(result, msg, requestPortBO));
                return@Function Single.error<T>(Throwable("$result||$msg"))
            }
            val baseBizResponse: BaseBizResponse
            if (httpResponse.data == null) {
                baseBizResponse = EmptyResponse()
                httpResponse.setData(baseBizResponse as T)
            } else {
                baseBizResponse = httpResponse.data
            }
            baseBizResponse.httpResponse = httpResponse
            Single.just(httpResponse.data)
        })
    }
}
