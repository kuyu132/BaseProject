package com.kuyuzhiqi.network.entity;

/**
 * 待上传的文件信息
 */
public class FileUploadInfo {
    private int retryTime = 0;  //重试次数
    private String md5;
    private String path;
    private Exception exception;

    public int getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    public FileUploadInfo(String md5, String path) {
        this.md5 = md5;
        this.path = path;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
