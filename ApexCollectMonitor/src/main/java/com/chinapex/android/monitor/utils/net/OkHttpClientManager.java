package com.chinapex.android.monitor.utils.net;

import android.text.TextUtils;

import com.chinapex.android.monitor.global.Constant;
import com.chinapex.android.monitor.global.MonitorCache;
import com.chinapex.android.monitor.utils.MLog;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author : Seven Qiu
 * @date : 2019/1/17
 */
public class OkHttpClientManager {

    private static final String TAG = OkHttpClientManager.class.getSimpleName();
    private static OkHttpClientManager sOkHttpClientManager;
    private OkHttpClient mOkHttpClient;
    private MediaType mediaTypeJson = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Constant.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constant.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllManager())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .build();
    }


    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return sSLSocketFactory;
    }

    private class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return MonitorCache.getInstance().getHostnameVerifier().equals(hostname);
        }
    }

    public static OkHttpClientManager getInstance() {
        if (null == sOkHttpClientManager) {
            synchronized (OkHttpClientManager.class) {
                if (null == sOkHttpClientManager) {
                    sOkHttpClientManager = new OkHttpClientManager();
                }
            }
        }

        return sOkHttpClientManager;
    }

    public void postJson(String url, String jsonStr, final INetCallback iNetCallback) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(jsonStr)) {
            MLog.e(TAG, "postJson() -> url or jsonStr is null or empty!");
            return;
        }

        if (null == iNetCallback) {
            MLog.e(TAG, "postJson() -> iNetCallback is null!");
            return;
        }

        RequestBody requestBody = RequestBody.create(mediaTypeJson, jsonStr);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iNetCallback.onFailed(Constant.NET_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (null == responseBody) {
                    iNetCallback.onFailed(Constant.NET_BODY_NULL, "NET_BODY_NULL");
                    MLog.e(TAG, "postJson onResponse() -> responseBody is null");
                    return;
                }

                String result = responseBody.string();
                iNetCallback.onSuccess(Constant.NET_SUCCESS, "NET_SUCCESS", result);
            }
        });
    }

    public void postJsonByAuth(String url, String jsonStr, final INetCallback iNetCallback) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(jsonStr)) {
            MLog.e(TAG, "postJson() -> url or jsonStr is null or empty!");
            return;
        }

        if (null == iNetCallback) {
            MLog.e(TAG, "postJson() -> iNetCallback is null!");
            return;
        }

        RequestBody requestBody = RequestBody.create(mediaTypeJson, jsonStr);
        Request request = new Request.Builder()
                .url(url)
                .addHeader(Constant.HEADER_KEY, Constant.HEADER_VALUE)
                .post(requestBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iNetCallback.onFailed(Constant.NET_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (null == responseBody) {
                    iNetCallback.onFailed(Constant.NET_BODY_NULL, "NET_BODY_NULL");
                    MLog.e(TAG, "postJsonByAuth onResponse() -> responseBody is null");
                    return;
                }

                String result = responseBody.string();
                iNetCallback.onSuccess(Constant.NET_SUCCESS, "NET_SUCCESS", result);
            }
        });
    }

    public void get(String url, final INetCallback iNetCallback) {
        if (TextUtils.isEmpty(url)) {
            MLog.e(TAG, "get() -> url is null!");
            return;
        }

        if (null == iNetCallback) {
            MLog.e(TAG, "get() -> iNetCallback is null!");
            return;
        }

        Request request = new Request.Builder().url(url).build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iNetCallback.onFailed(Constant.NET_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (null == responseBody) {
                    iNetCallback.onFailed(Constant.NET_BODY_NULL, "NET_BODY_NULL");
                    MLog.e(TAG, "get onResponse() -> responseBody is null");
                    return;
                }

                String result = responseBody.string();
                iNetCallback.onSuccess(Constant.NET_SUCCESS, "NET_SUCCESS", result);
            }
        });
    }

}
