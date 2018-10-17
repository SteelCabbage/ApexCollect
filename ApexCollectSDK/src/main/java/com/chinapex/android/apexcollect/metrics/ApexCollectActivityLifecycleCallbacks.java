package com.chinapex.android.apexcollect.metrics;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.chinapex.android.apexcollect.utils.ATLog;

/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public class ApexCollectActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = ApexCollectActivityLifecycleCallbacks.class.getSimpleName();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ATLog.i(TAG, "onActivityCreated");
        ATLog.w(TAG, "onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ATLog.i(TAG, "onActivityStarted");
        ATLog.w(TAG, "onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ATLog.i(TAG, "onActivityResumed");
        ATLog.w(TAG, "onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ATLog.i(TAG, "onActivityPaused");
        ATLog.w(TAG, "onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ATLog.i(TAG, "onActivityStopped");
        ATLog.w(TAG, "onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        ATLog.i(TAG, "onActivitySaveInstanceState");
        ATLog.w(TAG, "onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ATLog.i(TAG, "onActivityDestroyed");
        ATLog.w(TAG, "onActivityDestroyed");
    }
}
