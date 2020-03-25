package com.kuyuzhiqi.baseproject.network

import com.kuyuzhiqi.baseproject.network.response.LoginResponse
import com.kuyuzhiqi.bizbase.entity.RequestPortBO
import com.kuyuzhiqi.network.HttpFacade
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import com.kuyuzhiqi.bizbase.network.ConvertBizResponseForSingle

import java.util.TreeMap

class HttpService {
    private var mHost: String? = null
    private var instance: HttpService? = null
    private var api: Api? = null

    fun register(host: String) {
        mHost = host
        instance = HttpService()
        api = HttpFacade(host).createApi(Api::class.java)
    }

    private fun buildDefaultParamMap(): TreeMap<String, String> {
        val treeMap = TreeMap<String, String>()
        treeMap["device_id"] = "deviceId"
        return treeMap
    }

    fun applyLoginIn(userName: String, password: String): Single<LoginResponse> {
        val requestPortBO = RequestPortBO(Api.LOGIN_IN_PORT, mHost, Api.LOGIN_IN_URL)
        val treeMap = buildDefaultParamMap()
        treeMap["userName"] = userName
        treeMap["password"] = password
        requestPortBO.paramMap = treeMap
        return api!!.doLogin(treeMap)
            .subscribeOn(Schedulers.io())
            .compose(ConvertBizResponseForSingle(requestPortBO))
    }

}
