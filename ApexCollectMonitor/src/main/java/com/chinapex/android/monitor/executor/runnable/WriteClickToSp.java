package com.chinapex.android.monitor.executor.runnable;

import android.text.TextUtils;

import com.chinapex.android.monitor.global.Constant;
import com.chinapex.android.monitor.global.MonitorCache;
import com.chinapex.android.monitor.utils.GsonUtils;
import com.chinapex.android.monitor.utils.MLog;
import com.chinapex.android.monitor.utils.SpUtils;

/**
 * @author : Seven Qiu
 * @date : 2019/1/25
 */
public class WriteClickToSp implements Runnable {

    private static final String TAG = WriteClickToSp.class.getSimpleName();

    public WriteClickToSp() {
    }

    @Override
    public void run() {
        String clickBeanJsonString = GsonUtils.toJsonStr(MonitorCache.getInstance().getClickBeans());
        if (TextUtils.isEmpty(clickBeanJsonString)) {
            MLog.i(TAG, "run() ->  clickBeanJsonString is null or empty !");
            return;
        }

        SpUtils.putParam(MonitorCache.getInstance().getContext(), Constant.SP_KEY_EVENT_CLICK, clickBeanJsonString);
    }
}
