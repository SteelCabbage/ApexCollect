package com.chinapex.android.monitor.callback;

/**
 * @author SteelCabbage
 * @date 2018/12/14
 */
public interface IMonitorCallback {

    /**
     * 圈选模式是否开启
     *
     * @param isMonitor true圈选模式打开，false圈选模式关闭
     * @param monitorMode 圈选模式
     */
    void isMonitor(boolean isMonitor, int monitorMode);
}
