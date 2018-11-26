package com.chinapex.android.datacollect.aop;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.runnable.GenerateActivityPvEventData;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.utils.ATLog;

import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public class ApexCollectActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = ApexCollectActivityLifecycleCallbacks.class.getSimpleName();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        String activityName = activity.getClass().getName();
        ATLog.v(TAG, "onActivityCreated:" + activityName);

        Stack<String> references = ApexCache.getInstance().getReferences();
        if (null == references) {
            ATLog.e(TAG, "onActivityResumed references is null!");
            return;
        }

        boolean add = references.add(activityName);
        ATLog.v(TAG, "onActivityCreated stack add " + activityName + "," + add);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ATLog.v(TAG, "onActivityStarted:" + activity.getClass().getName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        long pvStartTime = System.currentTimeMillis();
        ATLog.v(TAG, "onActivityResumed:" + activity.getClass().getName() + ", pvStartTime:" + pvStartTime);
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
        ATLog.v(TAG, "onActivityPaused:" + activity.getClass().getName() + ", pvEndTime:" + pvEndTime);
        TaskController.getInstance().submit(new GenerateActivityPvEventData(activity, getReference(), pvEndTime));
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ATLog.v(TAG, "onActivityStopped:" + activity.getClass().getName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        ATLog.v(TAG, "onActivitySaveInstanceState:" + activity.getClass().getName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        String activityName = activity.getClass().getName();
        ATLog.i(TAG, "onActivityDestroyed:" + activityName);

        Stack<String> references = ApexCache.getInstance().getReferences();
        if (null == references) {
            ATLog.e(TAG, "onActivityResumed references is null!");
            return;
        }

        boolean remove = references.remove(activityName);
        ATLog.v(TAG, "onActivityDestroyed stack remove " + activityName + "," + remove);
    }

    private String getReference() {
        Stack<String> references = ApexCache.getInstance().getReferences();
        if (null == references) {
            ATLog.e(TAG, "getReference() -> references is null!");
            return "";
        }

        int size = references.size();
        if (size <= 1) {
            ATLog.w(TAG, "getReference() -> references.size is <= 1!");
            return "";
        }

        String reference = references.get(size - 2);
        return null == reference ? "" : reference;
    }
}
