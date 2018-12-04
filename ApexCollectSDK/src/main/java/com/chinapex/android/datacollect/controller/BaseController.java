package com.chinapex.android.datacollect.controller;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
import android.text.TextUtils;

import com.chinapex.android.datacollect.broadcast.BroadcastController;
import com.chinapex.android.datacollect.changelistener.AnalyticsListenerController;
import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.runnable.DelayEvent;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.aop.ApexCollectActivityLifecycleCallbacks;
import com.chinapex.android.datacollect.model.db.DbConstant;
import com.chinapex.android.datacollect.utils.ATLog;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SteelCabbage
 * @date 2018/11/20
 */

public class BaseController {

    private static final String TAG = BaseController.class.getSimpleName();
    private HashMap<String, IController> mIControllerHashMap;

    private BaseController() {
        mIControllerHashMap = new HashMap<>();
    }

    private static class BaseControllerHolder {
        private static final BaseController BASE_CONTROLLER = new BaseController();
    }

    public static BaseController getInstance() {
        return BaseControllerHolder.BASE_CONTROLLER;
    }

    public HashMap<String, IController> getIControllerHashMap() {
        return mIControllerHashMap;
    }

    public IController getIController(String controllerName) {
        if (TextUtils.isEmpty(controllerName)) {
            ATLog.e(TAG, "getIController() -> controllerName is null or empty!");
            return null;
        }

        switch (controllerName) {
            case Constant.CONTROLLER_TASK:
                return TaskController.getInstance();
            case Constant.CONTROLLER_PHONE_STATE:
                return PhoneStateController.getInstance();
            default:
                break;
        }

        ATLog.e(TAG, "getIController() -> there is no class:" + controllerName);
        return null;
    }

    public void doInit() {
        // 注册广播
        BroadcastController.getInstance().doInit();
        mIControllerHashMap.put(Constant.CONTROLLER_BROADCAST, BroadcastController.getInstance());

        // 开启延时上报
        TaskController.getInstance().schedule(new DelayEvent(DbConstant.TABLE_DELAY_REPORT),
                0,
                ApexCache.getInstance().getDelayReportInterval());

        // 开启即时上报检查，重发失败请求
        TaskController.getInstance().schedule(new DelayEvent(DbConstant.TABLE_INSTANT_ERR),
                0,
                ApexCache.getInstance().getCheckInstantErrInterval());

        // 注册activity的生命周期回调
        registerApexCollectActivityLifecycleCallbacks();

        // 获取手机基本状态信息
        PhoneStateController.getInstance().doInit();
        mIControllerHashMap.put(Constant.CONTROLLER_PHONE_STATE, PhoneStateController.getInstance());

        // test location
        LocationController.getInstance().doInit();
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

    public void onDestroy() {
        if (null == mIControllerHashMap || mIControllerHashMap.isEmpty()) {
            ATLog.e(TAG, "onDestroy() -> mIControllerHashMap is null or empty!");
            return;
        }

        for (Map.Entry<String, IController> iControllerEntry : mIControllerHashMap.entrySet()) {
            if (null == iControllerEntry
                    || TextUtils.isEmpty(iControllerEntry.getKey())
                    || null == iControllerEntry.getValue()) {
                ATLog.e(TAG, "onDestroy() -> iControllerEntry is null!");
                continue;
            }

            ATLog.i(TAG, "onDestroy() -> " + iControllerEntry.getValue());
            iControllerEntry.getValue().onDestroy();
        }

        mIControllerHashMap = null;
    }

}
