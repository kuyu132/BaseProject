package com.kuyuzhiqi.baseproject.network;

import com.kuyuzhiqi.baseproject.network.response.LoginResponse;
import com.kuyuzhiqi.bizbase.entity.RequestPortBO;
import com.kuyuzhiqi.network.HttpFacade;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import  com.kuyuzhiqi.bizbase.network.ConvertBizResponseForSingle;

import java.util.TreeMap;

public class HttpService {
    private String mHost;
    private HttpService instance;
    private Api api;

    public void register(String host) {
        mHost = host;
        instance = new HttpService();
        api = new HttpFacade(host).createApi(Api.class);
    }

    private static TreeMap<String, String> buildDefaultParamMap() {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("device_id", "deviceId");
        return treeMap;
    }

    public Single<LoginResponse> applyLoginIn(String userName, String password) {
        RequestPortBO requestPortBO = new RequestPortBO(Api.LOGIN_IN_PORT, mHost, Api.LOGIN_IN_URL);
        TreeMap<String, String> treeMap = buildDefaultParamMap();
        treeMap.put("userName", userName);
        treeMap.put("password", password);
        requestPortBO.setParamMap(treeMap);
        return api.doLogin(treeMap)
                .subscribeOn(Schedulers.io())
                .compose(new ConvertBizResponseForSingle(requestPortBO));
    }

}
