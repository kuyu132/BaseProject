package com.kuyuzhiqi.network.entity;

import android.text.TextUtils;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 待下载的文件信息
 */
public class FileDownloadInfo {
    private static final int URL_RETRY_TIME = 3;   //每个URL重试次数
    private String md5;
    private String path;
    private Queue<String> urlQueue = new ArrayDeque<>();  //可使用的url队列
    private Exception exception;    //下载失败的异常

    public FileDownloadInfo(String md5, String path, String... urls) {
        this.md5 = md5;
        this.path = path;

        for (String url : urls) {
            if (TextUtils.isEmpty(url)) {
                continue;
            }

            //每个url需要试几次
            for (int i = 0; i < URL_RETRY_TIME; i++) {
                urlQueue.add(url);
            }
        }
    }

    public String nextUrl() {
        return urlQueue.poll();
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean isUrlEmpty() {
        return urlQueue.isEmpty();
    }

    public String getMd5() {
        return md5;
    }

    public String getPath() {
        return path;
    }
}
