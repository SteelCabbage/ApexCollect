package com.chinapex.android.datacollect.controller;

import android.os.Build;
import android.text.TextUtils;

import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.runnable.InstantEvent;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.Identity;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.bean.event.ColdEventData;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.GsonUtils;
import com.chinapex.android.datacollect.utils.PhoneStateUtils;

import java.util.List;

/**
 * @author SteelCabbage
 * @date 2018/11/20
 */

public class PhoneStateController implements IController {

    private static final String TAG = PhoneStateController.class.getSimpleName();

    private PhoneStateController() {

    }

    private static class PhoneStateControllerHolder {
        private static final PhoneStateController PHONE_STATE_CONTROLLER = new PhoneStateController();
    }

    public static PhoneStateController getInstance() {
        return PhoneStateControllerHolder.PHONE_STATE_CONTROLLER;
    }

    @Override
    public void doInit() {
        Identity identity = new Identity();
        // 获取并设置uid

        // 获取并设置uuid
        String androidId = PhoneStateUtils.getAndroidId(ApexCache.getInstance().getContext());
        if (!TextUtils.isEmpty(androidId)) {
            identity.setUuid(androidId);
        }

        // 获取并设置deviceIds
        List<String> deviceIds = PhoneStateUtils.getDeviceIds(ApexCache.getInstance().getContext());
        if (null != deviceIds && !deviceIds.isEmpty()) {
            identity.setDeviceIds(deviceIds);
        }

        // 将Identity放入缓存中，避免使用时重复计算
        ApexCache.getInstance().setIdentity(identity);

        // 上报冷启动事件
        ColdEventData coldEventData = new ColdEventData();
        coldEventData.setEventType(Constant.EVENT_TYPE_COLD);
        coldEventData.setLabel(Constant.EVENT_LABEL_COLD);

        ColdEventData.ValueBean valueBean = new ColdEventData.ValueBean();
//        valueBean.setUid();
        valueBean.setAppName(PhoneStateUtils.getAppName(ApexCache.getInstance().getContext()));
        valueBean.setAppVersion(PhoneStateUtils.getVersionName(ApexCache.getInstance().getContext()));
//        valueBean.setScreenInfo();
        valueBean.setOs(System.getProperty("os.name"));
        valueBean.setOsVersion(Build.VERSION.RELEASE);
        valueBean.setBrandName(Build.BRAND);
//        valueBean.setCustomVersion();
        valueBean.setManufacturer(Build.MANUFACTURER);
        valueBean.setDeviceModel(Build.MODEL);
//        valueBean.setApiKey();

        coldEventData.setValue(valueBean);

        String coldEventDataJson = GsonUtils.toJsonStr(coldEventData);
        ATLog.d(TAG, "coldEventDataJson:" + coldEventDataJson);

        TrackEvent trackEvent = new TrackEvent.EventBuilder()
                .setMode(Constant.MODE_INSTANT)
                .setEventType(Constant.EVENT_TYPE_COLD)
                .setLabel(Constant.EVENT_LABEL_COLD)
                .setValue(coldEventDataJson)
                .build();
        TaskController.getInstance().submit(new InstantEvent(trackEvent, System.currentTimeMillis()));
    }

    @Override
    public void onDestroy() {

    }

}
