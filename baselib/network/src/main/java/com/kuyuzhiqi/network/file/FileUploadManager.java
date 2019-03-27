package com.kuyuzhiqi.network.file;

import android.text.TextUtils;
import android.util.Log;
import com.kuyuzhiqi.network.HttpFacade;
import com.kuyuzhiqi.network.HttpModule;
import com.kuyuzhiqi.network.api.FileApi;
import com.kuyuzhiqi.network.entity.FileUploadInfo;
import com.kuyuzhiqi.network.reponse.HttpResponse;
import com.kuyuzhiqi.network.reponse.UploadFileResponse;
import com.kuyuzhiqi.network.util.HttpUtil;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件上传模块
 */
public class FileUploadManager {

    private final static String TAG = FileUploadManager.class.getSimpleName();
    private static final int MAX_RETRY_TIME = 3;

    private BlockingQueue<FileUploadInfo> mQueue;
    private AtomicInteger mTotal;
    private Thread mUploadMainThread;
    private ExecutorService mExecutorService;
    private final List<String> mSuccessList = new ArrayList<>();

    private String mToken;
    private String mHost;
    private List<FileUploadInfo> mInitialUploadList;
    private FileUploadListener mFileUploadListener;

    private FileUploadManager() {
    }

    public void upload() {
        mUploadMainThread = Thread.currentThread();

        //初始化下载数量
        mTotal = new AtomicInteger(0);
        if (mInitialUploadList != null && !mInitialUploadList.isEmpty()) {
            mTotal.set(mInitialUploadList.size());
        }
        mFileUploadListener.init(mTotal.intValue());
        Log.d(TAG, "需要处理的数量" + mTotal.intValue());
        //为空直接结束
        if (mTotal.intValue() == 0) {
            mFileUploadListener.complete(true, mSuccessList);
            return;
        }

        //提交下载信息到队列
        mQueue = new ArrayBlockingQueue<>(mTotal.intValue());
        for (FileUploadInfo fileUploadInfo : mInitialUploadList) {
            String md5 = fileUploadInfo.getMd5();
            String path = fileUploadInfo.getPath();
            if (TextUtils.isEmpty(md5) || TextUtils.isEmpty(path)) {
                continue;
            }
            mQueue.add(fileUploadInfo);
        }
        if (mQueue.isEmpty()) {
            mFileUploadListener.complete(true, mSuccessList);
            return;
        }

        //暂时单线程上传
        mExecutorService = Executors.newSingleThreadExecutor();
        final HttpModule httpModule = new HttpFacade(mHost);
        final FileApi api = httpModule.createApi(FileApi.class);
        for (; ; ) {
            FileUploadInfo fileUploadInfo = null;
            try {
                fileUploadInfo = mQueue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();     //维持中断状态
                // TODO：停止所有http请求
                complete(checkFinish());
                break;
            }

            final FileUploadInfo finalFileUploadInfo = fileUploadInfo;
            final String md5 = finalFileUploadInfo.getMd5();
            String path = finalFileUploadInfo.getPath();
            File file = new File(path);
            if (!file.exists()) {
                return;
            }

            //参数
            TreeMap<String, String> treeMap = new TreeMap<>();
            treeMap.put("token", mToken);
            treeMap.put("md5", md5);
            TreeMap<String, RequestBody> requestBodyMap = HttpUtil.paramMapToRequestBodyMap(treeMap);
            //文件
            String key = "userfile";
            MultipartBody.Part filePart = HttpUtil.fileToFilePart(key, file);

            if (mExecutorService.isShutdown()) {
                return;
            }

            api.doUploadFile(requestBodyMap, filePart)
                    .subscribeOn(Schedulers.from(mExecutorService))
                    .map(new Function<HttpResponse<UploadFileResponse>, UploadFileResponse>() {

                        @Override
                        public UploadFileResponse apply(@NonNull HttpResponse<UploadFileResponse> httpResponse) throws Exception {
                            return httpResponse.getData();
                        }
                    })
                    .subscribe(new Consumer<UploadFileResponse>() {
                        @Override
                        public void accept(@NonNull UploadFileResponse uploadFileResponse) throws Exception {
                            successOne(md5);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            if (finalFileUploadInfo.getRetryTime() >= MAX_RETRY_TIME) {
                                fail(md5, throwable);
                            } else {
                                //重新加入队列
                                Log.d(TAG, "重试" + md5 + "，次数" + (finalFileUploadInfo.getRetryTime() + 1));
                                finalFileUploadInfo.setRetryTime(finalFileUploadInfo.getRetryTime() + 1);
                                mQueue.add(finalFileUploadInfo);
                            }
                        }
                    });
        }
    }

    private boolean checkFinish() {
        int total = mTotal.intValue();
        int successNum = mSuccessList.size();
        //LogUtils.d("判断上传是否完成，总数量:" + total + "，成功:" + successNum + "，剩余数量:" + (total - successNum));

        if (total == successNum) {
            return true;
        }
        return false;
    }

    private void successOne(String md5) {
        if (mUploadMainThread.isInterrupted()) {
            return;
        }

        mSuccessList.add(md5);
        mFileUploadListener.successOne(md5);

        if (checkFinish()) {
            cancel();
        }
    }

    private void fail(String md5, Throwable throwable) {
        if (mUploadMainThread.isInterrupted()) {
            return;
        }

        mFileUploadListener.fail(md5, throwable);

        if (checkFinish()) {
            cancel();
        }
    }

    private void complete(boolean isFinish) {
        Log.d(TAG, "上传完成" + isFinish + "，共" + mTotal + "，成功" + mSuccessList.size());
        mFileUploadListener.complete(isFinish, mSuccessList);
    }

    public void cancel() {
        if (null != mUploadMainThread) {
            Log.d(TAG, "停止上传");
            mUploadMainThread.interrupt();
        }
    }

    public interface FileUploadListener {
        void init(int total);

        void successOne(String md5);

        void fail(String md5, Throwable throwable);

        void complete(boolean isFinish, List<String> successMd5List);
    }

    public static class Builder {
        private FileUploadManager manager;

        public Builder() {
            manager = new FileUploadManager();
        }

        public Builder setUploadList(List<FileUploadInfo> list) {
            manager.setUploadList(list);
            return this;
        }

        public Builder setListener(FileUploadListener listener) {
            manager.setListener(listener);
            return this;
        }

        public Builder setHost(String host) {
            manager.setHost(host);
            return this;
        }

        public Builder setToken(String token) {
            manager.setToken(token);
            return this;
        }

        public FileUploadManager build() {
            return manager;
        }
    }

    public void setListener(FileUploadListener listener) {
        this.mFileUploadListener = listener;
    }

    public void setUploadList(List<FileUploadInfo> list) {
        this.mInitialUploadList = list;
    }

    public void setHost(String host) {
        this.mHost = host;
    }

    public void setToken(String token) {
        this.mToken = token;
    }
}