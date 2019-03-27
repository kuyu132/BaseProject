package com.kuyuzhiqi.network.util;


import android.content.Context;
import android.content.res.AssetManager;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.math.BigDecimal;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpUtil {

    /**
     * 获取下载文件的文件名
     */
    // TODO: 修改文件名为md5
    public static String createFileName() {
        //用“时间加随机数”生成文件名
        int randomNum = new Random().nextInt(1000) + 9000;
        String dateStr = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

        StringBuilder newFileName = new StringBuilder();
        newFileName.append(dateStr);
        newFileName.append(String.valueOf(randomNum));
        return newFileName.toString();
    }

    /**
     * 文件上传,参数需要从普通paramMap转成合适的requestBodyMap
     */
    public static TreeMap<String, RequestBody> paramMapToRequestBodyMap(TreeMap<String, String> paramMap) {
        TreeMap<String, RequestBody> requestBodyMap = new TreeMap<>();

        MediaType mediaType = MediaType.parse("multipart/form-data");
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            RequestBody requestParam = RequestBody.create(mediaType, value);
            requestBodyMap.put(key, requestParam);
        }

        return requestBodyMap;
    }

    /**
     * 文件上传,转成需要的FilePart类型
     */
    public static MultipartBody.Part fileToFilePart(String key, File file) {
        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody requestFile = RequestBody.create(mediaType, file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(key, file.getName(), requestFile);
        return filePart;
    }

    /**
     * 获取Post请求的请求参数
     */
    public static String getHttpPostParam(Request request) {
        StringBuilder paramBuilder = new StringBuilder();
        RequestBody requestBody = request.body();
        if (request.method().toUpperCase().equals("POST")) {
            if (requestBody instanceof FormBody) {
                FormBody formBody = (FormBody) requestBody;
                for (int i = 0; i < formBody.size(); i++) {
                    paramBuilder.append(formBody.name(i)).append("=").append(formBody.value(i)).append("&");
                }
            }
        }
        return paramBuilder.toString();
    }

    /**
     * 从response保存文件到磁盘
     */
    public static String saveFileAtDisk(ResponseBody responseBody, String fileSavePath)
            throws Exception {
        return saveFileAtDisk(responseBody, fileSavePath, null);
    }

    public static String saveFileAtDisk(ResponseBody responseBody, String fileSavePath, IFileDownloadListener listener)
            throws Exception {
        File targetFile;

        if (fileSavePath.endsWith("/")) {
            // 目标路径是文件夹
            File dir = new File(fileSavePath);
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            // 创建文件名
            String newFileName = createFileName();
            targetFile = new File(dir, newFileName);
        } else {
            // 目标路径是文件
            targetFile = new File(fileSavePath);
            if (!targetFile.exists()) {
                File dir = targetFile.getParentFile();
                if (dir.exists() || dir.mkdirs()) {
                    targetFile.createNewFile();
                }
            }
        }

        //写文件
        FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
        InputStream inputStream = responseBody.byteStream();
        byte[] buffer = new byte[1024 * 100];
        long totalLength = responseBody.contentLength();
        long downloadFileLength = 0;
        int currentLength;
        double lastPer = 0;
        while ((currentLength = inputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, currentLength);
            downloadFileLength += currentLength;

            BigDecimal b = new BigDecimal((double) downloadFileLength / totalLength);
            double per = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (null != listener && lastPer != per) {
                //LogUtils.d("更新百分比：" + per);
                listener.onRefreshProgress(per);
            }
            lastPer = per;
        }

        //close
        if (null != inputStream) {
            inputStream.close();
        }
        if (null != fileOutputStream) {
            fileOutputStream.close();
        }

        if (null != listener) {
            listener.onRefreshProgress(1);
            listener.onFinish(targetFile.getAbsolutePath());
        }

        return targetFile.getAbsolutePath();
    }

    /**
     * 拼接Get方法的url和参数
     */
    public static String buildGetUrlWithParams(String baseUrl, Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        builder.append(baseUrl);
        builder.append("?");
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            if (iterator.hasNext()) {
                builder.append("&");
            }
        }
        return builder.toString();
    }

    public interface IFileDownloadListener {
        void onRefreshProgress(double per);

        void onFinish(String diskPath);
    }

    public static SSLSocketFactory getSslSocketFactory(Context context) {
        InputStream inputStream = null;
        TrustManager[] trustManagers;
        SSLSocketFactory sslSocketFactory = null;
        try {
            // 取到证书的输入流
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            AssetManager assetManager = context.getResources().getAssets();
            inputStream = assetManager.open("*.buildingqm.com.cer");
            Certificate certificate = certificateFactory.generateCertificate(inputStream);

            // 创建 Keystore 包含我们的证书
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null);
            keyStore.setCertificateEntry("ca", certificate);

            // 创建一个 TrustManager 仅把 Keystore 中的证书 作为信任的锚点
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
            trustManagerFactory.init(keyStore);
            trustManagers = trustManagerFactory.getTrustManagers();

            // 用 TrustManager 初始化一个 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sslSocketFactory;
    }

}
