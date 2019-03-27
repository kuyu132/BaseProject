package com.kuyuzhiqi.network;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.kuyuzhiqi.network.api.FileApi;
import com.kuyuzhiqi.network.entity.HttpDownloadResult;
import com.kuyuzhiqi.network.factory.Tls12SocketFactory;
import com.kuyuzhiqi.network.util.HttpException;
import com.kuyuzhiqi.network.util.HttpUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HttpManager implements HttpModule {

    public static final String TAG = "http";

    private static String mHost;
    private static OkHttpClient mOkHttpClient;
    private static Retrofit mRetrofit;

    /**
     * HTTP超时
     */
    private static final int HTTP_CONNECT_TIMEOUT = 15;
    private static final int HTTP_READ_TIMEOUT = 120;
    private static final int HTTP_WRITE_TIMEOUT = 30;

    public HttpManager(String host, Context context) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS);

        if (context != null) {
            mOkHttpClient = enableTls12OnPreLollipop(okHttpClientBuilder)
                    .sslSocketFactory(HttpUtil.getSslSocketFactory(context))
                    .hostnameVerifier(new UnSafeHostnameVerifier())
                    .build();

        } else {
            mOkHttpClient = enableTls12OnPreLollipop(okHttpClientBuilder)
                    .build();
        }

        refreshHost(host);
    }

    public HttpManager(String host) {
        this(host, null);
    }

    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            TrustManager[] trustAllCerts = {new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    // Not implemented
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    // Not implemented
                }
            }};

            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }

    @Override
    public String getHost() {
        return mHost;
    }

    @Override
    public void refreshHost(String host) {
        this.mHost = host;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(host)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();

    }

    @Override
    public <T> T createApi(Class<T> cls) {
        return mRetrofit.create(cls);
    }

    @Override
    public HttpDownloadResult download(String urlStr, String savePath) throws HttpException {
        return this.doDownload(urlStr, savePath);
    }

    @Override
    public Observable<HttpDownloadResult> download(final String urlStr, final String savePath, Scheduler subscribeScheduler) {
        return this.download(urlStr, savePath, subscribeScheduler, AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<HttpDownloadResult> download(final String urlStr, final String savePath, Scheduler subscribeScheduler, Scheduler observeScheduler) {
        return Observable.create(new ObservableOnSubscribe<HttpDownloadResult>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<HttpDownloadResult> emitter) throws Exception {
                try {
                    String diskPath;
                    Call<ResponseBody> call = mRetrofit.create(FileApi.class).download(urlStr);
                    retrofit2.Response<ResponseBody> retrofitResponse = call.execute();

                    final HttpDownloadResult result = new HttpDownloadResult();
                    if (retrofitResponse.isSuccessful()) {
                        HttpUtil.saveFileAtDisk(retrofitResponse.body(), savePath, new HttpUtil.IFileDownloadListener() {
                            @Override
                            public void onRefreshProgress(double per) {
                                result.setPercent(per);
                                result.setFinish(false);
                                emitter.onNext(result);
                            }

                            @Override
                            public void onFinish(String diskPath) {
                                //下载成功
                                result.setFinish(true);
                                result.setDiskPath(diskPath);
                                emitter.onNext(result);
                                emitter.onComplete();
                            }
                        });
                    } else {
                        //ResponseBody errorBody = retrofitResponse.errorBody();
                        throw new Exception(urlStr + ":" + retrofitResponse.message());
                    }
                } catch (Exception e) {
                    emitter.onError(new HttpException(mHost, urlStr, null, e));
                }
            }
        }).subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler);
    }

    private HttpDownloadResult doDownload(String urlStr, String savePath) throws HttpException {
        final HttpDownloadResult result = new HttpDownloadResult();
        try {
            String diskPath;
            Call<ResponseBody> call = mRetrofit.create(FileApi.class).download(urlStr);
            retrofit2.Response<ResponseBody> retrofitResponse = call.execute();

            if (retrofitResponse.isSuccessful()) {
                //下载成功
                diskPath = HttpUtil.saveFileAtDisk(retrofitResponse.body(), savePath);
            } else {
                //ResponseBody errorBody = retrofitResponse.errorBody();
                throw new Exception(urlStr + ":" + retrofitResponse.message());
            }
            result.setDiskPath(diskPath);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "文件下载错误,host：" + mHost + "地址：" + urlStr);
        }
        return result;
    }


    public static class UnSafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            if (mHost.equals(hostname)) {
                return true;
            } else {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify(hostname, session);
            }
        }
    }
}