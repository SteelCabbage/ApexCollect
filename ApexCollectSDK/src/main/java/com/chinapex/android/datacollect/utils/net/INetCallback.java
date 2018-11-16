package com.chinapex.android.datacollect.utils.net;

/**
 * @author SteelCabbage
 * @date 2018/11/16
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
