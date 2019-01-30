package com.chinapex.android.datacollect.aop;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.chinapex.android.datacollect.changelistener.AnalyticsListenerController;
import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.runnable.GenerateActivityPvEventData;
import com.chinapex.android.datacollect.executor.runnable.ListPvEventsListener;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.monitor.MonitorTools;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public class ApexCollectActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = ApexCollectActivityLifecycleCallbacks.class.getSimpleName();
    private int visibleActivityCount;


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        String activityName = activity.getClass().getName();
        ATLog.v(TAG, "onActivityCreated:" + activityName);

        // 处理入栈操作
        Map<Integer, Stack<Map<String, Boolean>>> tasks = ApexCache.getInstance().getTasks();
        Map<Integer, Integer> top = ApexCache.getInstance().getTop();
        if (null == tasks) {
            ATLog.e(TAG, "onActivityCreated()-> tasks is null");
            return;
        }
        if (null == top) {
            ATLog.e(TAG, "onActivityCreated()-> top is null");
            return;
        }

        int taskId = activity.getTaskId();
        // 未有activity所属的task栈，创建一个栈，将activity入栈
        if (!tasks.keySet().contains(taskId)) {
            Stack<Map<String, Boolean>> task = new Stack<>();
            Map<String, Boolean> activityMap = new HashMap<>();
            activityMap.put(activityName, true);
            task.push(activityMap);
            tasks.put(taskId, task);
            top.put(taskId, task.size() - 1);
            return;
        }
        // 已有activity所属的栈
        Stack<Map<String, Boolean>> task = tasks.get(taskId);
        if (null == task) {
            ATLog.e(TAG, "onActivityCreater()-> task[" + taskId + "] is null");
            return;
        }
        Map<String, Boolean> map = new HashMap<>();
        map.put(activityName, true);
        task.push(map);
        top.put(taskId, task.size() - 1);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ATLog.v(TAG, "onActivityStarted:" + activity.getClass().getName());
        visibleActivityCount++;
        if (visibleActivityCount == 1) {
            MonitorTools.showFloatingWindow(ApexCache.getInstance().getContext(), ApexCache.getInstance(),
                    ApexCache.getInstance().getTasks(),
                    ApexCache.getInstance().getTop(),
                    ApexCache.getInstance().getForegroudTask());
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        int[] foregroundTaskId = ApexCache.getInstance().getForegroudTask();
        if (null == foregroundTaskId) {
            ATLog.e(TAG, "onActivityResumed() -> can not get foreground task pointer");
            return;
        }
        foregroundTaskId[0] = activity.getTaskId();

        //上报事件
        long pvStartTime = System.currentTimeMillis();
        String activityName = activity.getClass().getName();
        ATLog.v(TAG, "onActivityResumed:" + activityName + ", pvStartTime:" + pvStartTime);

        ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
        if (null == pvDurationTimes) {
            ATLog.e(TAG, "onActivityResumed() -> pvDurationTimes is null!");
            return;
        }

        if (TextUtils.isEmpty(activityName)) {
            ATLog.e(TAG, "onActivityResumed() -> activityName is null or empty!");
            return;
        }

        pvDurationTimes.put(activityName, pvStartTime);

        // 处理该页面的list事件
        TaskController.getInstance().submit(new ListPvEventsListener(activityName));
    }

    @Override
    public void onActivityPaused(Activity activity) {
        long pvEndTime = System.currentTimeMillis();
        String activityName = activity.getClass().getName();
        ATLog.v(TAG, "onActivityPaused:" + activityName + ", pvEndTime:" + pvEndTime);
        TaskController.getInstance().submit(new GenerateActivityPvEventData(activityName, getReference(), pvEndTime));
        AnalyticsListenerController.getInstance().notifyListPvEventsOnPageExit(activityName);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ATLog.v(TAG, "onActivityStopped:" + activity.getClass().getName());
        visibleActivityCount--;
        if (visibleActivityCount == 0) {
            MonitorTools.dismissFloatingWindow();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        ATLog.v(TAG, "onActivitySaveInstanceState:" + activity.getClass().getName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        String activityName = activity.getClass().getName();
        ATLog.i(TAG, "onActivityDestroyed:" + activityName);

        // 处理出栈操作
        Map<Integer, Stack<Map<String, Boolean>>> tasks = ApexCache.getInstance().getTasks();
        Map<Integer, Integer> top = ApexCache.getInstance().getTop();
        if (null == tasks) {
            ATLog.e(TAG, "onActivityDestroyed()-> tasks is null");
            return;
        }
        if (null == top) {
            ATLog.e(TAG, "onActivityDestroyed()-> top is null");
            return;
        }
        int taskId = activity.getTaskId();
        if (!tasks.containsKey(taskId)) {
            ATLog.e(TAG, "onActivityDestroyed()-> tasks do not contain task[" + taskId + "]");
            return;
        }
        Stack<Map<String, Boolean>> task = tasks.get(taskId);
        if (task.isEmpty()) {
            ATLog.e(TAG, "onActivityDestroyed()-> task[" + taskId + "] is empty");
            return;
        }

        // 弹出此activity以及在这之上的栈
        boolean isActivity;
        do {
            Map<String, Boolean> map = task.pop();
            if (null == map) {
                ATLog.e(TAG, "onActivityDestroyed()-> map should not be null");
                return;
            }
            Set<String> keySet = map.keySet();
            if (keySet.size() != 1) {
                ATLog.e(TAG, "onActivityDestroyed()-> key.size only can be 1");
                return;
            }
            String[] key = keySet.toArray(new String[1]);
            isActivity = map.get(key[0]);
            if (isActivity) {
                if (!key[0].equals(activityName)) {
                    ATLog.e(TAG, "onActivityDestroyed()-> pop activity[" + key[0] + "] is not the activity[" + activityName + "] we want to pop");
                    return;
                }
            }
        } while (!isActivity);

        // destroy的activity是栈中最后一个activiy
        if (task.size() == 0) {
            tasks.remove(taskId);
            top.remove(taskId);
            return;
        }

        // 栈中还有其他activity，将top指向栈顶的activity
        for (int i = task.size() - 1; i >= 0; i--) {
            Map<String, Boolean> map = task.get(i);
            if (null == map) {
                ATLog.e(TAG, "onActivityDestroyed()-> map should not be null");
                return;
            }
            Set<String> keySet = map.keySet();
            if (keySet.size() != 1) {
                ATLog.e(TAG, "onActivityDestroyed()-> key.size only can be 1");
                return;
            }
            String[] key = keySet.toArray(new String[1]);
            if (map.get(key[0])) {
                top.put(taskId, i);
            }
        }

        ATLog.v(TAG, "onActivityDestroyed stack remove " + activityName);
    }

    private String getReference() {
        Map<Integer, Stack<Map<String, Boolean>>> tasks = ApexCache.getInstance().getTasks();
        int[] foregroundTask = ApexCache.getInstance().getForegroudTask();
        if (null == tasks) {
            ATLog.e(TAG, "getReference()-> tasks is null");
            return "";
        }
        if (null == foregroundTask) {
            ATLog.e(TAG, "getReference() foreground is null");
            return "";
        }
        Stack<Map<String, Boolean>> task = tasks.get(foregroundTask[0]);
        if (task.isEmpty()) {
            ATLog.e(TAG, "getReference()-> task is empty");
            return "";
        }
        Stack<String> activityStack = new Stack<>();
        for (Map<String, Boolean> map : task) {
            if (null == map) {
                ATLog.e(TAG, "getReference()-> map is null");
                return "";
            }
            Set<String> keySet = map.keySet();
            if (keySet.size() != 1) {
                ATLog.e(TAG, "getReference()-> key.size only can be 1");
                return "";
            }
            String[] key = keySet.toArray(new String[1]);
            if (map.get(key[0])) {
                activityStack.push(key[0]);
            }
        }
        if (activityStack.size() == 0 || activityStack.size() == 1) {
            return "";
        }
        activityStack.pop();
        String reference = activityStack.peek();
        return reference == null ? "" : reference;
    }
}
