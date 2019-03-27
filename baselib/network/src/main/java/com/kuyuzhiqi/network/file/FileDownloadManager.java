package com.kuyuzhiqi.network.file;

import android.text.TextUtils;
import android.util.Log;
import com.kuyuzhiqi.network.HttpFacade;
import com.kuyuzhiqi.network.HttpModule;
import com.kuyuzhiqi.network.entity.FileDownloadInfo;
import com.kuyuzhiqi.network.entity.HttpDownloadResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件下载模块
 */
public class FileDownloadManager {

    public final static String TAG = FileDownloadManager.class.getSimpleName();

    private BlockingQueue<FileDownloadInfo> mWaitDownloadQueue;
    private AtomicInteger mTotal;  //待处理下载数量
    private Thread mDownloadMainThread;
    private final List<DownloadSuccess> mSuccessList = Collections.synchronizedList(new ArrayList<DownloadSuccess>());
    private final List<DownloadFail> mFailList = Collections.synchronizedList(new ArrayList<DownloadFail>());

    private String mHost;
    private List<FileDownloadInfo> mInitialDownloadList;
    private ExecutorService mExecutorService;
    private FileDownloadListener mFileDownloadListener;

    public void download() {
        if (TextUtils.isEmpty(mHost)) {
            Log.w(TAG, "host is empty");
            return;
        }

        mDownloadMainThread = Thread.currentThread();
        final HttpModule httpModule = new HttpFacade(mHost);

        //初始化下载数量
        mTotal = new AtomicInteger(0);
        if (mInitialDownloadList != null && !mInitialDownloadList.isEmpty()) {
            mTotal.set(mInitialDownloadList.size());
        }
        mFileDownloadListener.downloadInit(mTotal.intValue());
        Log.d(TAG, "下载数量" + mTotal.intValue());
        //为空直接结束
        if (mTotal.intValue() == 0) {
            mFileDownloadListener.downloadComplete(true, mSuccessList, mFailList);
            return;
        }

        //提交下载信息到队列
        mWaitDownloadQueue = new ArrayBlockingQueue<>(mTotal.intValue());
        for (FileDownloadInfo fileDownloadInfo : mInitialDownloadList) {
            final String md5 = fileDownloadInfo.getMd5();
            //没有url直接失败
            if (fileDownloadInfo.isUrlEmpty()) {
                failOne(new DownloadFail(md5, null));
                continue;
            }

            mWaitDownloadQueue.add(fileDownloadInfo);
        }

        //提交下载
        for (; ; ) {
            FileDownloadInfo fileDownloadInfo = null;
            try {
                fileDownloadInfo = mWaitDownloadQueue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();     //维持中断状态
                stop();
                break;
            }

            final String url = fileDownloadInfo.nextUrl();
            final String path = fileDownloadInfo.getPath();
            final String md5 = fileDownloadInfo.getMd5();

            final FileDownloadInfo finalFileDownloadInfo = fileDownloadInfo;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mExecutorService.isShutdown()) {
                            return;
                        }
                        HttpDownloadResult httpDownloadResult = httpModule.download(url, path);
                        String path = httpDownloadResult.getDiskPath();
                        successOne(new DownloadSuccess(md5, url, path));
                    } catch (Exception e) {
                        if (finalFileDownloadInfo.isUrlEmpty()) {
                            failOne(new DownloadFail(md5, url));
                        } else {
                            //重新加入待下载队列
                            mWaitDownloadQueue.add(finalFileDownloadInfo);

                            mFileDownloadListener.downloadOneRetry(finalFileDownloadInfo);
                        }
                    }
                }
            };
            if (null != mExecutorService) {
                if (!mExecutorService.isShutdown()) {
                    mExecutorService.submit(runnable);
                }
            } else {
                new Thread(runnable).start();
            }
        }
    }

    private boolean isFinish() {
        int total = mTotal.intValue();
        int successNum = mSuccessList.size();
        int failNum = mFailList.size();
        Log.d(TAG, "判断下载是否完成，总数量" + total + "，成功" + successNum + "，失败" + failNum + "，剩余数量" + (total - successNum - failNum));

        if (total == successNum + failNum) {
            return true;
        }
        return false;
    }

    private void successOne(DownloadSuccess downloadSuccess) {
        if (mDownloadMainThread.isInterrupted()) {
            return;
        }

        mSuccessList.add(downloadSuccess);
        mFileDownloadListener.downloadOneSuccess(downloadSuccess);

        if (isFinish()) {
            cancel();
        }
    }

    private void failOne(DownloadFail downloadFail) {
        if (mDownloadMainThread.isInterrupted()) {
            return;
        }

        mFailList.add(downloadFail);
        mFileDownloadListener.downloadOneFail(downloadFail);

        if (isFinish()) {
            cancel();
        }
    }

    /**
     * 停止所有http请求
     */
    private void stop() {
        complete(isFinish());
    }

    private void complete(boolean isFinish) {
        Log.d(TAG, "下载完成" + isFinish + "，共" + mTotal + "，成功" + mSuccessList.size() + "，失败" + mFailList.size());
        mFileDownloadListener.downloadComplete(isFinish, mSuccessList, mFailList);
    }

    public void cancel() {
        if (null != mDownloadMainThread) {
            Log.d(TAG, "取消下载");
            mDownloadMainThread.interrupt();
        }
    }

    public interface FileDownloadListener {
        void downloadInit(int total);

        void downloadOneSuccess(DownloadSuccess downloadSuccess);

        void downloadOneFail(DownloadFail downloadFail);

        void downloadOneRetry(FileDownloadInfo fileDownloadInfo);

        void downloadComplete(boolean isFinish, List<DownloadSuccess> successList, List<DownloadFail> failList);
    }

    public static final class Builder {
        private FileDownloadManager manager;

        public Builder() {
            manager = new FileDownloadManager();
        }

        public Builder downloadInfoList(List<FileDownloadInfo> fileDownloadInfoList) {
            manager.setDownloadList(fileDownloadInfoList);
            return this;
        }

        public Builder runByThreadPool(ExecutorService es) {
            manager.setExecutorService(es);
            return this;
        }

        public Builder downloadFileListener(FileDownloadListener listener) {
            manager.setFileDownloadListener(listener);
            return this;
        }

        public Builder setHost(String host) {
            manager.setHost(host);
            return this;
        }

        public FileDownloadManager build() {
            return manager;
        }
    }

    public class DownloadSuccess {
        private String md5;
        private String url;
        private String path;

        public DownloadSuccess(String md5, String url, String path) {
            this.md5 = md5;
            this.url = url;
            this.path = path;
        }

        public String getMd5() {
            return md5;
        }

        public String getUrl() {
            return url;
        }

        public String getPath() {
            return path;
        }
    }

    public class DownloadFail {
        private String md5;
        private String url;

        public DownloadFail(String md5, String url) {
            this.md5 = md5;
            this.url = url;
        }

        public String getMd5() {
            return md5;
        }

        public String getUrl() {
            return url;
        }
    }

    public void setHost(String host) {
        this.mHost = host;
    }

    public void setDownloadList(List<FileDownloadInfo> list) {
        this.mInitialDownloadList = list;
    }

    public void setExecutorService(ExecutorService es) {
        this.mExecutorService = es;
    }

    public void setFileDownloadListener(FileDownloadListener listener) {
        this.mFileDownloadListener = listener;
    }
}