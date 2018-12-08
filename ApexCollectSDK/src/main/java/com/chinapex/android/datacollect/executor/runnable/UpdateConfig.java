package com.chinapex.android.datacollect.executor.runnable;

import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.request.UpdateConfigRequest;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.GsonUtils;
import com.chinapex.android.datacollect.utils.PhoneStateUtils;
import com.chinapex.android.datacollect.utils.SpUtils;
import com.chinapex.android.datacollect.utils.net.INetCallback;
import com.chinapex.android.datacollect.utils.net.OkHttpClientManager;

/**
 * @author SteelCabbage
 * @date 2018/12/06
 */
public class UpdateConfig implements Runnable, INetCallback {

    private static final String TAG = UpdateConfig.class.getSimpleName();

    @Override
    public void run() {

        // TODO: 2018/12/6 0006  配置文件更新
        UpdateConfigRequest updateConfigRequest = new UpdateConfigRequest();
        updateConfigRequest.setAppVersion(PhoneStateUtils.getVersionName(ApexCache.getInstance().getContext()));

        OkHttpClientManager.getInstance().postJson(Constant.URL_DELAY_REPORT, GsonUtils.toJsonStr(updateConfigRequest), this);
    }

    @Override
    public void onSuccess(int statusCode, String msg, String result) {
        ATLog.i(TAG, "UpdateConfig onSuccess() -> result:" + result);

        String configVersion = PhoneStateUtils.getVersionName(ApexCache.getInstance().getContext())
                + "#" + System.currentTimeMillis();
        ApexCache.getInstance().setConfigVersion(configVersion);
        SpUtils.putParam(ApexCache.getInstance().getContext(), Constant.SP_KEY_CONFIG_VERSION, configVersion);
    }

    @Override
    public void onFailed(int failedCode, String msg) {
        ATLog.e(TAG, "UpdateConfig onSuccess() -> msg:" + msg);
    }

}
