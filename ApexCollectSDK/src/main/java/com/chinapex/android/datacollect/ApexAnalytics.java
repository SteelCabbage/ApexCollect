package com.chinapex.android.datacollect;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.runnable.InstantEvent;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.metrics.ApexCollectActivityLifecycleCallbacks;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.db.DbConstant;
import com.chinapex.android.datacollect.model.db.DbDao;
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

        // 初始化线程池
        TaskController.getInstance().doInit();

        // 设置应用的applicationContext
        ApexCache.getInstance().setContext(applicationContext);

        // 注册activity的生命周期回调
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
     * 上报
     *
     * @param trackEvent
     */
    public void track(TrackEvent trackEvent) {
        if (!mIsInit) {
            ATLog.e(TAG, "track() -> please initialize!");
            return;
        }

        if (null == trackEvent) {
            ATLog.e(TAG, "track() -> trackEvent is null!");
            return;
        }

        ATLog.i(TAG, "track() -> trackEvent:" + trackEvent);

        switch (trackEvent.getMode()) {
            case Constant.MODE_DELAY:
                DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
                if (null == dbDao) {
                    ATLog.e(TAG, "track() -> dbDao is null!");
                    return;
                }

                dbDao.insert(DbConstant.TABLE_DELAY_REPORT, trackEvent, System.currentTimeMillis());
                break;
            case Constant.MODE_INSTANT:
                TaskController.getInstance().submit(new InstantEvent(trackEvent, System.currentTimeMillis()));
                break;
            default:
                break;
        }
    }

    /* *************************************************************************************************************
     *                                                Settings                                                     *
     * *************************************************************************************************************
     */

    // TODO: 2018/11/14 0014 各种设置

    /**
     * setMaxNum 设置上报条数
     *
     * @param reportMaxNum Default: 30 (默认30条)
     */
    public void setReportMaxNum(int reportMaxNum) {
        ApexCache.getInstance().setReportMaxNum(reportMaxNum);
    }


    /**
     * setTimeInterval 设置延时上报的时间间隔
     *
     * @param delayReportInterval Default: 1000 * 60 * 60 (默认1小时)
     */
    public void setDelayReportInterval(long delayReportInterval) {
        ApexCache.getInstance().setDelayReportInterval(delayReportInterval);
    }


    /**
     * 设置延时上报的url,默认为测试url
     *
     * @param urlDelay
     */
    public void setUrlDelay(String urlDelay) {
        if (TextUtils.isEmpty(urlDelay)) {
            ATLog.e(TAG, "setUrlDelay() -> urlDelay is null or empty!");
            return;
        }

        ApexCache.getInstance().setUrlDelay(urlDelay);
    }


    /**
     * 设置即时上报的url，默认为测试url
     *
     * @param urlInstant
     */
    public void setUrlInstant(String urlInstant) {
        if (TextUtils.isEmpty(urlInstant)) {
            ATLog.e(TAG, "setUrlInstant() -> urlInstant is null or empty!");
            return;
        }

        ApexCache.getInstance().setUrlInstant(urlInstant);
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
