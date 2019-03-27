package com.kuyuzhiqi.network.reponse;

/**
 * newhttp 响应的统一Response，包括业务data
 * @param <T>
 */
public class HttpResponse<T extends BaseBizResponse>{
    private int result;
    private String message;
    private T data;
    private long timestamp;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}