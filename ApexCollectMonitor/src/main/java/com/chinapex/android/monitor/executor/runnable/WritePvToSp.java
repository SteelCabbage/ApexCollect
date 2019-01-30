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
public class WritePvToSp implements Runnable {

    private static final String TAG = WritePvToSp.class.getSimpleName();

    public WritePvToSp() {
    }

    @Override
    public void run() {
        String pvBeanJsonString = GsonUtils.toJsonStr(MonitorCache.getInstance().getPvBeans());
        if (TextUtils.isEmpty(pvBeanJsonString)) {
            MLog.i(TAG, "run() ->  pvBeanJsonString is null or empty !");
            return;
        }

        MLog.i(TAG, "run() ->  pvBeanJsonString:" + pvBeanJsonString);
        SpUtils.putParam(MonitorCache.getInstance().getContext(), Constant.SP_KEY_EVENT_PV, pvBeanJsonString);
    }

}
