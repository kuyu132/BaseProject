package com.kuyuzhiqi.bizbase.entity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestPortBO {
    private String portKey;
    private String host;
    private String url;
    private Map<String, String> paramMap;

    public RequestPortBO(String portKey, String host, String url) {
        this.portKey = portKey;
        this.host = host;
        this.url = url;
    }

    public RequestPortBO(String portKey, String host, String url, Map<String, String> paramMap) {
        this.portKey = portKey;
        this.host = host;
        this.url = url;
        this.paramMap = paramMap;
    }

    public String getPortKey() {
        return portKey;
    }

    public void setPortKey(String portKey) {
        this.portKey = portKey;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public String getParamStr() {
        if (paramMap == null || paramMap.isEmpty()) {
            return "";
        }

        List<String> paramList = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            paramList.add(key + "=" + value);
        }
        return TextUtils.join("&", paramList);
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    @Override
    public String toString() {
        return "RequestPortBO{" +
                "portKey='" + portKey + '\'' +
                ", host='" + host + '\'' +
                ", url='" + url + '\'' +
                ", param=" + getParamStr() +
                '}';
    }
}
