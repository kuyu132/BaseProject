package com.kuyuzhiqi.network.api;

import com.kuyuzhiqi.network.reponse.HttpResponse;
import com.kuyuzhiqi.network.reponse.UploadFileResponse;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.TreeMap;


public interface FileApi {

    /** 文件下载 */
    @GET
    @Streaming
    Call<ResponseBody> download(@Url String url);

    /** 文件上传 */
    String UPLOAD_FILE_PORT = "xxx";
    String UPLOAD_FILE_URL = "xxxx";

    @Multipart
    @POST(UPLOAD_FILE_URL)
    Single<HttpResponse<UploadFileResponse>> doUploadFile(@PartMap TreeMap<String, RequestBody> paramMap, @Part MultipartBody.Part filePart);
}
