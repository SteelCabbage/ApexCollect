package com.chinapex.android.datacollect.executor.runnable;

import android.os.Build;
import android.util.DisplayMetrics;

import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.bean.event.ColdEventData;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.GsonUtils;
import com.chinapex.android.datacollect.utils.PhoneStateUtils;

/**
 * @author SteelCabbage
 * @date 2018/11/26
 */
public class GenerateColdEventData implements Runnable {

    private static final String TAG = GenerateColdEventData.class.getSimpleName();

    @Override
    public void run() {
        // 上报冷启动事件
        ColdEventData.ValueBean valueBean = new ColdEventData.ValueBean();
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
        coldEventData.setTimeStamp(System.currentTimeMillis());
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
