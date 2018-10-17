package com.chinapex.android.apexcollect;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.chinapex.android.apexcollect.metrics.ApexCollectActivityLifecycleCallbacks;
import com.chinapex.android.apexcollect.utils.ATLog;

/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public class ApexAnalytics {

    private static final String TAG = ApexAnalytics.class.getSimpleName();
    private boolean mIsInit;
    private Context mContext;

    private ApexAnalytics() {

    }

    private static class ApexAnalyticsHolder {
        private static final ApexAnalytics APEX_ANALYTICS = new ApexAnalytics();
    }

    public static ApexAnalytics getInstance() {
        return ApexAnalyticsHolder.APEX_ANALYTICS;
    }

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

        mContext = applicationContext;

        registerApexCollectActivityLifecycleCallbacks();

        mIsInit = true;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void registerApexCollectActivityLifecycleCallbacks() {
        if (mContext.getApplicationContext() instanceof Application) {
            Application app = (Application) mContext.getApplicationContext();
            ApexCollectActivityLifecycleCallbacks lifecycleCallbacks = new
                    ApexCollectActivityLifecycleCallbacks();
            app.registerActivityLifecycleCallbacks(lifecycleCallbacks);
        } else {
            ATLog.w(TAG, "Context is not an Application!");
        }
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
