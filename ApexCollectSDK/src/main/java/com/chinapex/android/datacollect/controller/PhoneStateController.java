package com.chinapex.android.datacollect.controller;

import android.text.TextUtils;

import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.runnable.GenerateColdEventData;
import com.chinapex.android.datacollect.global.ApexCache;
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
        // 获取并设置deviceIds
        List<String> deviceIds = PhoneStateUtils.getDeviceIds(ApexCache.getInstance().getContext());
        if (null != deviceIds && !deviceIds.isEmpty()) {
            ApexCache.getInstance().setDeviceIds(deviceIds);
        }

        TaskController.getInstance().submit(new GenerateColdEventData());
    }

    @Override
    public void onDestroy() {

    }

}
