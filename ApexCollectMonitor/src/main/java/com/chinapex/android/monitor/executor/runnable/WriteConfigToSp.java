package com.chinapex.android.monitor.executor.runnable;

import android.text.TextUtils;

import com.chinapex.android.monitor.bean.UploadConfigRequest;
import com.chinapex.android.monitor.global.Constant;
import com.chinapex.android.monitor.global.MonitorCache;
import com.chinapex.android.monitor.utils.GsonUtils;
import com.chinapex.android.monitor.utils.MLog;
import com.chinapex.android.monitor.utils.PhoneStateUtil;
import com.chinapex.android.monitor.utils.SpUtils;

/**
 * @author : Seven Qiu
 * @date : 2019/1/18
 */
public class WriteConfigToSp implements Runnable {

    private static final String TAG = WriteConfigToSp.class.getSimpleName();

    public WriteConfigToSp() {
    }

    @Override
    public void run() {
        UploadConfigRequest dataBean = new UploadConfigRequest();
        dataBean.setVersion(PhoneStateUtil.getVersionName() + "#" + System.currentTimeMillis());

        UploadConfigRequest.Config config = new UploadConfigRequest.Config();
        config.setClick(MonitorCache.getInstance().getClickBeans());
        config.setPv(MonitorCache.getInstance().getPvBeans());
        config.setList(MonitorCache.getInstance().getListBeans());
        dataBean.setConfig(config);

        String dataBeanJsonString = GsonUtils.toJsonStr(dataBean);
        if (TextUtils.isEmpty(dataBeanJsonString)) {
            MLog.i(TAG, "run() ->  dataBeanJsonString is null or empty !");
            return;
        }

        MLog.i(TAG, "dataBeanJsonString() ->  " + dataBeanJsonString);
        SpUtils.putParam(MonitorCache.getInstance().getContext(), Constant.SP_KEY_EVENT_CONFIG, dataBeanJsonString);
    }

}
