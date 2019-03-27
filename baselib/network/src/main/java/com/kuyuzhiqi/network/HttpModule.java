package com.kuyuzhiqi.network;

import com.kuyuzhiqi.network.entity.HttpDownloadResult;
import com.kuyuzhiqi.network.util.HttpException;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Http网络模块
 */
public interface HttpModule {
    /**
     * 文件下载请求(同步-不支持进度)
     */
    HttpDownloadResult download(String urlStr, String savePath) throws HttpException;

    /**
     * 文件下载请求(异步-单向线程-支持进度)
     */
    Observable<HttpDownloadResult> download(final String urlStr, final String savePath, Scheduler subscribeScheduler);

    /**
     * 文件下载请求(异步-双向线程-支持进度)
     */
    Observable<HttpDownloadResult> download(final String urlStr, final String savePath, Scheduler subscribeScheduler, Scheduler observeScheduler);

    /**
     * 获取host
     */
    String getHost();

    /**
     * 更新host
     */
    void refreshHost(String host);

    /**
     * 生成api实例
     */
    <T> T createApi(Class<T> cls);
}
