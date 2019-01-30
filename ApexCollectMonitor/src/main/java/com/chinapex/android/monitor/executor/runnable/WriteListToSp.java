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
public class WriteListToSp implements Runnable {

    private static final String TAG = WriteListToSp.class.getSimpleName();

    public WriteListToSp() {
    }

    @Override
    public void run() {
        String listBeanJsonString = GsonUtils.toJsonStr(MonitorCache.getInstance().getListBeans());
        if (TextUtils.isEmpty(listBeanJsonString)) {
            MLog.i(TAG, "run() ->  listBeanJsonString is null or empty !");
            return;
        }

        SpUtils.putParam(MonitorCache.getInstance().getContext(), Constant.SP_KEY_EVENT_LIST, listBeanJsonString);
    }

}
