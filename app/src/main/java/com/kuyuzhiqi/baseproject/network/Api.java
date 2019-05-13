package com.kuyuzhiqi.baseproject.network;

import com.kuyuzhiqi.baseproject.network.response.LoginResponse;
import com.kuyuzhiqi.network.reponse.HttpResponse;
import io.reactivex.Single;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import java.util.TreeMap;

public interface Api {
    String LOGIN_IN_PORT="";
    String LOGIN_IN_URL = "xx";

    @FormUrlEncoded
    @POST(LOGIN_IN_URL)
    Single<HttpResponse<LoginResponse>> doLogin(@FieldMap TreeMap<String, String> paramMap);
}
