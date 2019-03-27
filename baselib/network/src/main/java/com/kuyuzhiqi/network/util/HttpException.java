package com.kuyuzhiqi.network.util;

import java.io.PrintStream;
import java.io.PrintWriter;

public class HttpException extends RuntimeException {

    private String host;
    private String urlPath;
    private String param;
    private Throwable originalException;

    public HttpException(String host, String urlPath, String param, Throwable originalException) {
        this.host = host;
        this.urlPath = urlPath;
        this.param = param;
        this.originalException = originalException;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream err) {
        super.printStackTrace(err);
    }

    @Override
    public void printStackTrace(PrintWriter err) {
        super.printStackTrace(err);
    }
}
