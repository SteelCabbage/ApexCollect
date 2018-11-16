package com.chinapex.android.datacollect;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.metrics.ApexCollectActivityLifecycleCallbacks;
import com.chinapex.android.datacollect.report.EventReport;
import com.chinapex.android.datacollect.report.TrackEvent;
import com.chinapex.android.datacollect.utils.ATLog;

/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public class ApexAnalytics {

    private static final String TAG = ApexAnalytics.class.getSimpleName();
    private boolean mIsInit;

    private ApexAnalytics() {

    }

    private static class ApexAnalyticsHolder {
        private static final ApexAnalytics APEX_ANALYTICS = new ApexAnalytics();
    }

    public static ApexAnalytics getInstance() {
        return ApexAnalyticsHolder.APEX_ANALYTICS;
    }

    /* *************************************************************************************************************
     *                                                Init                                                         *
     * *************************************************************************************************************
     */

    /**
     * Initialize 初始化
     *
     * @param applicationContext 必须为Application的Context
     */
    public void init(Context applicationContext) {
        if (mIsInit) {
            ATLog.e(TAG, "ApexAnalytics can only be initialized once!");
            return;
        }

        if (null == applicationContext) {
            ATLog.e(TAG, "applicationContext is null!");
            return;
        }

        ApexCache.getInstance().setContext(applicationContext);

        registerApexCollectActivityLifecycleCallbacks();

        mIsInit = true;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void registerApexCollectActivityLifecycleCallbacks() {
        if (ApexCache.getInstance().getContext().getApplicationContext() instanceof Application) {
            Application app = (Application) ApexCache.getInstance().getContext().getApplicationContext();
            ApexCollectActivityLifecycleCallbacks lifecycleCallbacks = new ApexCollectActivityLifecycleCallbacks();
            app.registerActivityLifecycleCallbacks(lifecycleCallbacks);
        } else {
            ATLog.w(TAG, "Context is not an Application!");
        }
    }


    /* *************************************************************************************************************
     *                                                Track                                                        *
     * *************************************************************************************************************
     */

    /**
     * 延时上报
     *
     * @param trackEvent
     */
    public void track(TrackEvent trackEvent) {
        if (null == trackEvent) {
            ATLog.e(TAG, "trackEvent is null!");
            return;
        }

        // TODO: 2018/11/16 0016  延时上报
    }

    /**
     * 即时上报
     *
     * @param trackEvent
     */
    public void trackAtOnce(TrackEvent trackEvent) {
        if (null == trackEvent) {
            ATLog.e(TAG, "trackEvent is null!");
            return;
        }

        EventReport eventReport = new EventReport(trackEvent);
        TaskController.getInstance().submit(eventReport);
        ATLog.v(TAG, "track() -> prepare SbEventReport!");
    }


    /* *************************************************************************************************************
     *                                                Settings                                                     *
     * *************************************************************************************************************
     */

    // TODO: 2018/11/14 0014 各种设置

    /**
     * setMaxNum 设置上报条数
     *
     * @param maxNum Default: 30 (默认30条)
     */
    public void setMaxNum(int maxNum) {

    }

    /**
     * setTimeInterval 设置上报的时间间隔
     *
     * @param timeInterval Default: 1000 * 60 * 60 * 1 (默认1小时)
     */
    public void setTimeInterval(long timeInterval) {

    }

    /**
     * setLogLevel 设置日志输出等级，默认为WARN
     *
     * @param logLevel One of {@link ATLog#VERBOSE}, {@link ATLog#DEBUG}, {@link ATLog#INFO}
     *                 ,{@link ATLog#WARN}, or {@link ATLog#ERROR}.
     */
    public void setLogLevel(int logLevel) {
        ATLog.setLevel(logLevel);
    }

}
