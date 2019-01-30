package com.chinapex.android.monitor.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.chinapex.android.monitor.global.Constant;
import com.chinapex.android.monitor.global.MonitorCache;

/**
 * @author : Seven
 * @date : 2019/1/25
 */
public class PhoneStateUtil {

    private static final String TAG = PhoneStateUtil.class.getSimpleName();

    /**
     * getVersionName (appVersion)
     *
     * @return VersionName
     */
    public static String getVersionName() {
        if (null == MonitorCache.getInstance().getContext()) {
            MLog.e(TAG, "getVersionName() -> context is null!");
            return Constant.DEFAULT_APP_VERSION;
        }

        PackageManager pm = MonitorCache.getInstance().getContext().getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(MonitorCache.getInstance().getContext().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            MLog.e(TAG, "getVersionName() -> exception:" + e.getMessage());
        }
        return Constant.DEFAULT_APP_VERSION;
    }

}
