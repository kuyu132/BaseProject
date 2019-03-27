package com.kuyuzhiqi.network.reponse;

/**
 * 业务用的response,包含HttpResponse
 */
public class BaseBizResponse {
    private HttpResponse httpResponse;

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }
}