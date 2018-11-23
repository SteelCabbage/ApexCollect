package com.chinapex.android.datacollect;

import android.content.Context;
import android.text.TextUtils;

import com.chinapex.android.datacollect.controller.BaseController;
import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.runnable.InstantEvent;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
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

        // 设置应用的applicationContext
        ApexCache.getInstance().setContext(applicationContext);

        // 初始化BaseController
        BaseController.getInstance().doInit();

        mIsInit = true;
    }


    /* *************************************************************************************************************
     *                                          Track, SignIn, SignOut                                             *
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
                TaskController.getInstance().submit(new InstantEvent(trackEvent));
                break;
            default:
                break;
        }
    }

    public void signIn(String userId) {
        ApexCache.getInstance().setUserId(userId);
    }

    public void signOut() {
        ApexCache.getInstance().setUserId("");
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
        if (reportMaxNum < Constant.REPORT_MIN_NUM) {
            ATLog.w(TAG, "reportMaxNum must be >= " + Constant.REPORT_MIN_NUM
                    + ", default: " + Constant.REPORT_MAX_NUM_DEFAULT + " are currently used");
            return;
        }

        ApexCache.getInstance().setReportMaxNum(reportMaxNum);
    }

    /**
     * setTimeInterval 设置延时上报的时间间隔
     *
     * @param delayReportInterval Default: 1000 * 60 * 5 (默认5分钟)
     */
    public void setDelayReportInterval(long delayReportInterval) {
        if (delayReportInterval < Constant.DELAY_REPORT_INTERVAL_MIN) {
            ATLog.w(TAG, "delayReportInterval must be >= " + Constant.DELAY_REPORT_INTERVAL_MIN
                    + ", default: " + Constant.DELAY_REPORT_INTERVAL_DEFAULT + " are currently used");
            return;
        }

        ApexCache.getInstance().setDelayReportInterval(delayReportInterval);
    }

    /**
     * setCheckInstantErrInterval 设置检查即时上报是否存在异常的时间间隔, 默认2分钟
     *
     * @param checkInstantErrInterval
     */
    public void setCheckInstantErrInterval(long checkInstantErrInterval) {
        if (checkInstantErrInterval < Constant.CHECK_INSTANT_ERR_INTERVAL_MIN) {
            ATLog.w(TAG, "checkInstantErrInterval must be >= " + Constant.CHECK_INSTANT_ERR_INTERVAL_MIN
                    + ", default: " + Constant.CHECK_INSTANT_ERR_INTERVAL_DEFAULT + " are currently used");
            return;
        }

        ApexCache.getInstance().setCheckInstantErrInterval(checkInstantErrInterval);
    }

    /**
     * setUrlDelay 设置延时上报的url,默认为测试url
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
     * setUrlInstant 设置即时上报的url，默认为测试url
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
