package com.kuyuzhiqi.network;

import android.content.Context;
import com.kuyuzhiqi.network.entity.HttpDownloadResult;
import com.kuyuzhiqi.network.util.HttpException;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Http网络模块入口
 */
public class HttpFacade implements HttpModule {

    private HttpManager httpManager;

    public HttpFacade(String host) {
        this.httpManager = new HttpManager(host);
    }

    public HttpFacade(String host, Context context) {
        this.httpManager = new HttpManager(host, context);
    }

    @Override
    public HttpDownloadResult download(String urlStr, String savePath) throws HttpException {
        return httpManager.download(urlStr, savePath);
    }

    @Override
    public Observable<HttpDownloadResult> download(String urlStr, String savePath, Scheduler subscribeScheduler) {
        return httpManager.download(urlStr, savePath, subscribeScheduler);
    }

    @Override
    public Observable<HttpDownloadResult> download(String urlStr, String savePath, Scheduler subscribeScheduler,
                                                   Scheduler observeScheduler) {
        return httpManager.download(urlStr, savePath, subscribeScheduler, observeScheduler);
    }

    @Override
    public String getHost() {
        return httpManager.getHost();
    }

    @Override
    public void refreshHost(String host) {
        httpManager.refreshHost(host);
    }

    @Override
    public <T> T createApi(Class<T> cls) {
        return httpManager.createApi(cls);
    }
}
