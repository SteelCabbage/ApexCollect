package com.chinapex.android.datacollect.executor.callback;

/**
 * @author SteelCabbage
 * @date 2018/12/12
 */
public interface IUpdateConfigCallback {

    /**
     * 配置文件是否更新成功, 若成功取消轮询任务, 失败则继续
     *
     * @param isUpdateSuccessful
     */
    void updateConfig(boolean isUpdateSuccessful);
}
