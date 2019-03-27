package com.kuyuzhiqi.bizbase.network;

import com.kuyuzhiqi.bizbase.entity.RequestPortBO;
import com.kuyuzhiqi.network.NetworkConstant;
import com.kuyuzhiqi.network.reponse.BaseBizResponse;
import com.kuyuzhiqi.network.reponse.EmptyResponse;
import com.kuyuzhiqi.network.reponse.HttpResponse;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class ConvertBizResponseForSingle<T extends BaseBizResponse> implements SingleTransformer<HttpResponse<T>, T> {

    private RequestPortBO requestPortBO;

    public ConvertBizResponseForSingle(RequestPortBO requestPortBO) {
        this.requestPortBO = requestPortBO;
    }

    @Override
    public SingleSource<T> apply(@NonNull Single<HttpResponse<T>> single) {
        return single.flatMap(new Function<HttpResponse<T>, SingleSource<? extends T>>() {
            @Override
            public SingleSource<? extends T> apply(@NonNull HttpResponse<T> httpResponse) throws Exception {
                if (httpResponse.getResult() != NetworkConstant.ServerRespond.RESULT_OK) {
                    int result = httpResponse.getResult();
                    String msg = httpResponse.getMessage();

//                    return Single.error(BizExceptionHelper.createBizException(result, msg, requestPortBO));
                    return Single.error(new Throwable(result + "||" + msg));
                }
                BaseBizResponse baseBizResponse;
                if (httpResponse.getData() == null) {
                    baseBizResponse = new EmptyResponse();
                    httpResponse.setData((T) baseBizResponse);
                } else {
                    baseBizResponse = httpResponse.getData();
                }
                baseBizResponse.setHttpResponse(httpResponse);
                return Single.just(httpResponse.getData());
            }
        });
    }
}
