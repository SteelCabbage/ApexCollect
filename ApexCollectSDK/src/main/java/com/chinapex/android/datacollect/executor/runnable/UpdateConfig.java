package com.chinapex.android.datacollect.executor.runnable;

import android.text.TextUtils;

import com.chinapex.android.datacollect.executor.callback.IUpdateConfigCallback;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.request.UpdateConfigRequest;
import com.chinapex.android.datacollect.model.bean.response.TestJson;
import com.chinapex.android.datacollect.model.bean.response.UpdateConfigResponse;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.GsonUtils;
import com.chinapex.android.datacollect.utils.PhoneStateUtils;
import com.chinapex.android.datacollect.utils.SpUtils;
import com.chinapex.android.datacollect.utils.net.INetCallback;
import com.chinapex.android.datacollect.utils.net.OkHttpClientManager;

import java.util.Map;

/**
 * @author SteelCabbage
 * @date 2018/12/06
 */
public class UpdateConfig implements Runnable, INetCallback {

    private static final String TAG = UpdateConfig.class.getSimpleName();
    private IUpdateConfigCallback mIUpdateConfigCallback;

    public UpdateConfig(IUpdateConfigCallback iUpdateConfigCallback) {
        mIUpdateConfigCallback = iUpdateConfigCallback;
    }

    @Override
    public void run() {
        if (null == mIUpdateConfigCallback) {
            ATLog.e(TAG, "mIUpdateConfigCallback is null!");
            return;
        }

        // TODO: 2018/12/6 0006  配置文件更新
        UpdateConfigRequest updateConfigRequest = new UpdateConfigRequest();
        updateConfigRequest.setAppVersion(PhoneStateUtils.getVersionName(ApexCache.getInstance().getContext()));

        OkHttpClientManager.getInstance().postJson(Constant.URL_DELAY_REPORT, GsonUtils.toJsonStr(updateConfigRequest), this);
    }

    @Override
    public void onSuccess(int statusCode, String msg, String result) {
        ATLog.i(TAG, "UpdateConfig onSuccess() -> result:" + result);

        // testData
        result = TestJson.JSON;
        boolean isSuccess = parseAndSaveConfig(result);
        if (!isSuccess) {
            ATLog.w(TAG, "parse config exception, local config is still used");
        }

        mIUpdateConfigCallback.updateConfig(true);
    }

    @Override
    public void onFailed(int failedCode, String msg) {
        ATLog.e(TAG, "UpdateConfig onFailed() -> msg:" + msg);

        mIUpdateConfigCallback.updateConfig(false);
    }

    private boolean parseAndSaveConfig(String result) {
        if (TextUtils.isEmpty(result)) {
            ATLog.e(TAG, "parseAndSaveConfig() -> result is null!");
            return false;
        }

        UpdateConfigResponse updateConfigResponse = GsonUtils.json2Bean(result, UpdateConfigResponse.class);
        if (null == updateConfigResponse) {
            ATLog.e(TAG, "parseAndSaveConfig() -> updateConfigResponse is null!");
            return false;
        }

        UpdateConfigResponse.DataBean data = updateConfigResponse.getData();
        if (null == data) {
            ATLog.e(TAG, "parseAndSaveConfig() -> data is null! no need to update config!");
            return false;
        }

        String configVersion = data.getVersion();
        if (TextUtils.isEmpty(configVersion)) {
            ATLog.e(TAG, "parseAndSaveConfig() -> configVersion is null! no need to update config!");
            return false;
        }

        UpdateConfigResponse.DataBean.Config config = data.getConfig();
        if (null == config) {
            ATLog.e(TAG, "parseAndSaveConfig() -> config is null! no need to update config!");
            return false;
        }

        Map<String, UpdateConfigResponse.DataBean.Config.ClickBean> configClick = config.getClick();
        Map<String, UpdateConfigResponse.DataBean.Config.PvBean> configPv = config.getPv();
        Map<String, UpdateConfigResponse.DataBean.Config.ListBean> configList = config.getList();

        ApexCache.getInstance().setConfigVersion(configVersion);
        SpUtils.putParam(ApexCache.getInstance().getContext(), Constant.SP_KEY_CONFIG_VERSION, configVersion);

        ApexCache.getInstance().setConfigClick(configClick);
        ApexCache.getInstance().setConfigPv(configPv);
        ApexCache.getInstance().setConfigList(configList);

        String configJson = GsonUtils.toJsonStr(config);
        ATLog.v(TAG, "parseAndSaveConfig() -> configJson:" + configJson);
        SpUtils.putParam(ApexCache.getInstance().getContext(), Constant.SP_KEY_CONFIG, configJson);
        return true;
    }

}
