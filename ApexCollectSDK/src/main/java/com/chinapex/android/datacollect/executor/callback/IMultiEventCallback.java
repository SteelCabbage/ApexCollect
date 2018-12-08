package com.chinapex.android.datacollect.executor.callback;

/**
 * @author SteelCabbage
 * @date 2018/12/08
 */
public interface IMultiEventCallback {

    /**
     * 网络正常返回时, 再继续查询数据库并发送
     *
     * @param offset 查询时跳过的条数
     */
    void continueSend(int offset);

    /**
     * 网络异常返回时, 终止查询数据库, 避免内存溢出
     *
     * @param errMsg
     */
    void stopSend(String errMsg);
}
