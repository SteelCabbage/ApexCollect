package com.chinapex.android.datacollect.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.chinapex.android.datacollect.global.Constant;

/**
 * @author SteelCabbage
 * @date 2018/11/20
 */

public class PhoneStateUtils {

    private static final String TAG = PhoneStateUtils.class.getSimpleName();

    /**
     * getAndroidId (uuid)
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        if (null == context) {
            ATLog.e(TAG, "getAndroidId() -> context is null!");
            return Constant.DEFAULT_ANDROID_ID;
        }

        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return TextUtils.isEmpty(androidId) ? Constant.DEFAULT_ANDROID_ID : androidId;
    }

    /**
     * getAppName
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        if (null == context) {
            ATLog.e(TAG, "getAppName() -> context is null!");
            return Constant.DEFAULT_APP_NAME;
        }

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            ATLog.e(TAG, "getAppName() -> exception:" + e.getMessage());
        }
        return Constant.DEFAULT_APP_NAME;
    }

    /**
     * getVersionName (appVersion)
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        if (null == context) {
            ATLog.e(TAG, "getVersionName() -> context is null!");
            return Constant.DEFAULT_APP_VERSION;
        }

        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            ATLog.e(TAG, "getVersionName() -> exception:" + e.getMessage());
        }
        return Constant.DEFAULT_APP_VERSION;
    }

    /**
     * getDisplayMetrics (screenInfo)
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (null == context) {
            ATLog.e(TAG, "getDisplayMetrics() -> context is null!");
            return displayMetrics;
        }

        WindowManager windowManager = (WindowManager) context.getSystemService(Context
                .WINDOW_SERVICE);
        if (null == windowManager) {
            ATLog.e(TAG, "getDisplayMetrics() -> windowManager is null!");
            return displayMetrics;
        }

        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * getDeviceIds (用户授权的情况下，获取)
     *
     * @param context
     * @return
     */
    public static String getDeviceIds(Context context) {
        if (null == context) {
            ATLog.e(TAG, "getImei() -> context is null!");
            return Constant.DEFAULT_IMEI;
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null == telephonyManager) {
            ATLog.e(TAG, "getImei() -> telephonyManager is null!");
            return Constant.DEFAULT_IMEI;
        }

        // < 6.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                return telephonyManager.getDeviceId();
            } catch (Exception e) {
                ATLog.e(TAG, "getImei() -> osVersion < 6.0 exception:" + e.getMessage());
                return Constant.DEFAULT_IMEI;
            }
        }

        // [6.0 , 8.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O
                && PackageManager.PERMISSION_GRANTED == context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)) {
            try {
                return telephonyManager.getDeviceId();
            } catch (Exception e) {
                ATLog.e(TAG, "getImei() -> osVersion [6.0 , 8.0) exception:" + e.getMessage());
                return Constant.DEFAULT_IMEI;
            }
        }

        // >= 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && PackageManager.PERMISSION_GRANTED == context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)) {
            try {
                int phoneCount = telephonyManager.getPhoneCount();
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (int i = 0; i < phoneCount; i++) {
                    String gsm = telephonyManager.getImei(i);
                    String cdma = telephonyManager.getMeid(i);

                    if (!TextUtils.isEmpty(gsm)) {
                        if (i == phoneCount - 1) {
                            sb.append(String.valueOf("gsm:" + gsm));
                        } else {
                            sb.append(String.valueOf("gsm:" + gsm + ","));
                        }
                    }

                    if (!TextUtils.isEmpty(cdma)) {
                        if (i == phoneCount - 1) {
                            sb.append(String.valueOf("cdma:" + cdma));
                        } else {
                            sb.append(String.valueOf("cdma:" + cdma + ","));
                        }
                    }
                }
                sb.append("]");
                return sb.toString();
            } catch (Exception e) {
                ATLog.e(TAG, "getImei() -> osVersion [6.0 , 8.0) exception:" + e.getMessage());
                return Constant.DEFAULT_IMEI;
            }
        }
        return Constant.DEFAULT_IMEI;
    }


}
