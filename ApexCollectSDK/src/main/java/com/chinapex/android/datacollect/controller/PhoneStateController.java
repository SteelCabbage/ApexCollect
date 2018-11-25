package com.chinapex.android.datacollect.controller;

import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.runnable.InstantEvent;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
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
        // 获取并设置uuid
        String androidId = PhoneStateUtils.getAndroidId(ApexCache.getInstance().getContext());
        if (!TextUtils.isEmpty(androidId)) {
            ApexCache.getInstance().setUuid(androidId);
        }

        // 获取并设置deviceIds
        List<String> deviceIds = PhoneStateUtils.getDeviceIds(ApexCache.getInstance().getContext());
        if (null != deviceIds && !deviceIds.isEmpty()) {
            ApexCache.getInstance().setDeviceIds(deviceIds);
        }

        reportColdEvent();
    }

    @Override
    public void onDestroy() {

    }

    private void reportColdEvent() {
        // 上报冷启动事件
        ColdEventData.ValueBean valueBean = new ColdEventData.ValueBean();
        valueBean.setTimeStamp(System.currentTimeMillis());
        // 预留字段
        valueBean.setApiKey("");
        // 暂取不到厂商定制系统的版本号
        valueBean.setCustomVersion("");
        valueBean.setOs(System.getProperty("os.name"));
        valueBean.setOsVersion(Build.VERSION.RELEASE);
        valueBean.setDeviceModel(Build.MODEL);
        valueBean.setManufacturer(Build.MANUFACTURER);
        valueBean.setBrandName(Build.BRAND);
        valueBean.setAppName(PhoneStateUtils.getAppName(ApexCache.getInstance().getContext()));
        valueBean.setAppVersion(PhoneStateUtils.getVersionName(ApexCache.getInstance().getContext()));

        DisplayMetrics displayMetrics = PhoneStateUtils.getDisplayMetrics(ApexCache.getInstance().getContext());
        valueBean.setScreenWidth(String.valueOf(displayMetrics.widthPixels));
        valueBean.setScreenHeight(String.valueOf(displayMetrics.heightPixels));
        valueBean.setScreenDensity(String.valueOf(displayMetrics.density));

        ColdEventData coldEventData = new ColdEventData();
        coldEventData.setEventType(Constant.EVENT_TYPE_COLD);
        coldEventData.setLabel(Constant.EVENT_LABEL_COLD);
        coldEventData.setUserId(ApexCache.getInstance().getUserId());
        coldEventData.setCountry(ApexCache.getInstance().getCountry());
        coldEventData.setProvince(ApexCache.getInstance().getProvince());
        coldEventData.setCity(ApexCache.getInstance().getCity());
        coldEventData.setValue(valueBean);

        String coldEventDataJson = GsonUtils.toJsonStr(coldEventData);
        ATLog.d(TAG, "coldEventDataJson:" + coldEventDataJson);

        TrackEvent trackEvent = new TrackEvent.EventBuilder()
                .setMode(Constant.MODE_INSTANT)
                .setEventType(Constant.EVENT_TYPE_COLD)
                .setLabel(Constant.EVENT_LABEL_COLD)
                .setValue(coldEventDataJson)
                .build();

        TaskController.getInstance().submit(new InstantEvent(trackEvent));
    }

}
