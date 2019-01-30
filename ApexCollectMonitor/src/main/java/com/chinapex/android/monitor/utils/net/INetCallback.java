package com.chinapex.android.monitor.utils.net;

/**
 * @author : Seven Qiu
 * @date : 2019/1/17
 */
public interface INetCallback<T> {

    /**
     * 成功回调
     *
     * @param statusCode
     * @param msg
     * @param result
     */
    void onSuccess(int statusCode, String msg, String result);

    /**
     * 失败回调
     *
     * @param failedCode
     * @param msg
     */
    void onFailed(int failedCode, String msg);

}
