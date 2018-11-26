package com.chinapex.android.datacollect.aop;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.runnable.GenerateActivityPvEventData;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.utils.ATLog;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public class ApexCollectActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = ApexCollectActivityLifecycleCallbacks.class.getSimpleName();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ATLog.v(TAG, "onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ATLog.v(TAG, "onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        long pvStartTime = System.currentTimeMillis();
        ATLog.i(TAG, "onActivityResumed() -> pvStartTime:" + pvStartTime);
        ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
        if (null == pvDurationTimes) {
            ATLog.e(TAG, "onActivityResumed() -> pvDurationTimes is null!");
            return;
        }

        String activityName = activity.getClass().getName();
        if (TextUtils.isEmpty(activityName)) {
            ATLog.e(TAG, "onActivityResumed() -> activityName is null or empty!");
            return;
        }

        pvDurationTimes.put(activityName, pvStartTime);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        long pvEndTime = System.currentTimeMillis();
        ATLog.i(TAG, "onActivityPaused() -> pvEndTime:" + pvEndTime);
        String reference = AssembleXpath.getReference(activity);
        TaskController.getInstance().submit(new GenerateActivityPvEventData(activity, reference, pvEndTime));
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ATLog.v(TAG, "onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        ATLog.v(TAG, "onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ATLog.i(TAG, "onActivityDestroyed");
    }
}
