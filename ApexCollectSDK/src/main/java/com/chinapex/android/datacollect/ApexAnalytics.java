package com.chinapex.android.datacollect;

import android.content.Context;
import android.text.TextUtils;

import com.chinapex.android.datacollect.changelistener.AnalyticsListenerController;
import com.chinapex.android.datacollect.controller.BaseController;
import com.chinapex.android.datacollect.controller.IController;
import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.runnable.GenerateCustomEventData;
import com.chinapex.android.datacollect.executor.runnable.UpdateConfig;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.AnalyticsSettings;
import com.chinapex.android.datacollect.model.bean.ApexLocation;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.PhoneStateUtils;
import com.chinapex.android.datacollect.utils.SpUtils;

import java.util.HashMap;

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
     * @param analyticsSettings 初始化参数
     */
    public void init(AnalyticsSettings analyticsSettings) {
        if (mIsInit) {
            ATLog.e(TAG, "ApexAnalytics can only be initialized once!");
            return;
        }

        if (null == analyticsSettings) {
            ATLog.e(TAG, "analyticsSettings is null!");
            return;
        }

        Context applicationContext = analyticsSettings.getApplicationContext();
        if (null == applicationContext) {
            ATLog.e(TAG, "applicationContext is null!");
            return;
        }

        HashMap<String, IController> iControllerHashMap = BaseController.getInstance().getIControllerHashMap();
        if (null == iControllerHashMap) {
            ATLog.e(TAG, "iControllerHashMap is null!");
            return;
        }

        // 初始化线程池
        TaskController.getInstance().doInit();
        iControllerHashMap.put(Constant.CONTROLLER_TASK, TaskController.getInstance());

        // 初始化全局监听管理类
        AnalyticsListenerController.getInstance().doInit();
        iControllerHashMap.put(Constant.CONTROLLER_ANALYTICS_LISTENER, AnalyticsListenerController.getInstance());

        // 初始化全局缓存对象ApexCache
        ApexCache.getInstance().doInit();

        // 设置应用的applicationContext
        ApexCache.getInstance().setContext(applicationContext);

        // 可自定义的Settings
        setUuid(analyticsSettings.getUuid());
        setReportMaxNum(analyticsSettings.getReportMaxNum());
        setDelayReportInterval(analyticsSettings.getDelayReportInterval());
        setCheckInstantErrInterval(analyticsSettings.getCheckInstantErrInterval());
        setUrlDelay(analyticsSettings.getUrlDelay());
        setUrlInstant(analyticsSettings.getUrlInstant());
        setLogLevel(analyticsSettings.getLogLevel());

        // 获取并设置当前配置文件版本号
        String configVersion = (String) SpUtils.getParam(ApexCache.getInstance().getContext(),
                Constant.SP_KEY_CONFIG_VERSION,
                Constant.SP_DEF_VAL_CONFIG_VERSION);
        ApexCache.getInstance().setConfigVersion(configVersion);

        // 更新配置文件
        TaskController.getInstance().submit(new UpdateConfig());

        // 初始化BaseController
        BaseController.getInstance().doInit();

        mIsInit = true;
    }


    /* *************************************************************************************************************
     *                                  Track, SignIn, SignOut, SetApexLocation                                    *
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

        ATLog.i(TAG, "track() -> trackEvent:" + trackEvent);
        TaskController.getInstance().submit(new GenerateCustomEventData(trackEvent));
    }

    public void signIn(String userId) {
        if (!mIsInit) {
            ATLog.e(TAG, "signIn() -> please initialize!");
            return;
        }

        ApexCache.getInstance().setUserId(userId);
    }

    public void signOut() {
        if (!mIsInit) {
            ATLog.e(TAG, "signOut() -> please initialize!");
            return;
        }

        ApexCache.getInstance().setUserId("");
    }

    public void setApexLocation(ApexLocation apexLocation) {
        if (!mIsInit) {
            ATLog.e(TAG, "setApexLocation() -> please initialize!");
            return;
        }

        if (null == apexLocation) {
            ATLog.w(TAG, "setApexLocation() -> apexLocation is null!");
            return;
        }

        ApexCache.getInstance().setCountry(apexLocation.getCountry());
        ApexCache.getInstance().setProvince(apexLocation.getProvince());
        ApexCache.getInstance().setCity(apexLocation.getCity());
    }


    /* *************************************************************************************************************
     *                                                Settings                                                     *
     * *************************************************************************************************************
     */

    /**
     * 设置UUID, 若无，将会以AndroidId替代
     */
    private void setUuid(String uuid) {
        if (TextUtils.isEmpty(uuid)) {
            ATLog.w(TAG, "uuid can not be null! android id will be used");
            String androidId = PhoneStateUtils.getAndroidId(ApexCache.getInstance().getContext());
            ApexCache.getInstance().setUuid(androidId);
            return;
        }

        ApexCache.getInstance().setUuid(uuid);
    }

    /**
     * setMaxNum 设置上报条数
     *
     * @param reportMaxNum Default: 30 (默认30条)
     */
    private void setReportMaxNum(int reportMaxNum) {
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
    private void setDelayReportInterval(long delayReportInterval) {
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
    private void setCheckInstantErrInterval(long checkInstantErrInterval) {
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
    private void setUrlDelay(String urlDelay) {
        if (TextUtils.isEmpty(urlDelay)) {
            ATLog.w(TAG, "setUrlDelay() -> urlDelay is null or empty!");
            return;
        }

        ApexCache.getInstance().setUrlDelay(urlDelay);
    }

    /**
     * setUrlInstant 设置即时上报的url，默认为测试url
     *
     * @param urlInstant
     */
    private void setUrlInstant(String urlInstant) {
        if (TextUtils.isEmpty(urlInstant)) {
            ATLog.w(TAG, "setUrlInstant() -> urlInstant is null or empty!");
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
    private void setLogLevel(int logLevel) {
        if (logLevel < ATLog.VERBOSE) {
            ATLog.w(TAG, "logLevel must be >= " + ATLog.VERBOSE + ", default: " + ATLog.WARN + " are currently used");
            return;
        }

        ATLog.setLevel(logLevel);
    }

}
