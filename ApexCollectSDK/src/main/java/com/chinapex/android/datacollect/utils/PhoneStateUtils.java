package com.chinapex.android.datacollect.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.chinapex.android.datacollect.global.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author SteelCabbage
 * @date 2018/11/20
 */

public class PhoneStateUtils {

    private static final String TAG = PhoneStateUtils.class.getSimpleName();

    /**
     * getAndroidId
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

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (null == windowManager) {
            ATLog.e(TAG, "getDisplayMetrics() -> windowManager is null!");
            return displayMetrics;
        }

        Display display = windowManager.getDefaultDisplay();
        if (null == display) {
            ATLog.e(TAG, "getDisplayMetrics() -> display is null!");
            return displayMetrics;
        }

        display.getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * getDeviceIds (用户授权的情况下，获取)
     *
     * @param context application context
     * @return deviceId 的数组
     */
    public static List<String> getDeviceIds(Context context) {
        if (null == context) {
            ATLog.e(TAG, "getImei() -> context is null!");
            return null;
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null == telephonyManager) {
            ATLog.e(TAG, "getImei() -> telephonyManager is null!");
            return null;
        }

        ArrayList<String> deviceIds = new ArrayList<>();

        // < 6.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                String deviceId = telephonyManager.getDeviceId();
                deviceIds.add(String.valueOf("IMEI:" + deviceId));
                return deviceIds;
            } catch (Exception e) {
                ATLog.e(TAG, "getImei() -> osVersion < 6.0 exception:" + e.getMessage());
                return null;
            }
        }

        // [6.0 , 8.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O
                && PackageManager.PERMISSION_GRANTED == context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)) {
            try {
                String deviceId = telephonyManager.getDeviceId();
                deviceIds.add(String.valueOf("IMEI:" + deviceId));
                return deviceIds;
            } catch (Exception e) {
                ATLog.e(TAG, "getImei() -> osVersion [6.0 , 8.0) exception:" + e.getMessage());
                return null;
            }
        }

        // >= 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && PackageManager.PERMISSION_GRANTED == context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)) {
            try {
                int phoneCount = telephonyManager.getPhoneCount();

                for (int i = 0; i < phoneCount; i++) {
                    String gsm = telephonyManager.getImei(i);
                    String cdma = telephonyManager.getMeid(i);

                    if (!TextUtils.isEmpty(gsm)) {
                        deviceIds.add("GSM:" + gsm);
                    }

                    if (!TextUtils.isEmpty(cdma)) {
                        deviceIds.add("CDMA:" + cdma);
                    }
                }

                return deviceIds;
            } catch (Exception e) {
                ATLog.e(TAG, "getImei() -> osVersion [6.0 , 8.0) exception:" + e.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * 获取系统语言
     *
     * @return 语言
     */
    public static String getLanguage() {
        return Locale.getDefault().toString();
    }

    /**
     * 获取网络类型
     *
     * @param context context
     * @return 网络类型
     */
    public static String getNetworkType(Context context) {
        if (null == context) {
            ATLog.e(TAG, "getNetworkType() -> context is null!");
            return Constant.NETWORK_TYPE_UNKNOWN;
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null == telephonyManager) {
            ATLog.e(TAG, "getNetworkType() -> telephonyManager is null!");
            return Constant.NETWORK_TYPE_UNKNOWN;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            ATLog.e(TAG, "getNetworkType() -> connectivityManager is null!");
            return Constant.NETWORK_TYPE_UNKNOWN;
        }

        try {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (null == activeNetworkInfo) {
                ATLog.e(TAG, "getNetworkType() -> activeNetworkInfo is null!");
                return Constant.NETWORK_TYPE_UNKNOWN;
            }

            if (!activeNetworkInfo.isConnected()) {
                ATLog.e(TAG, "getNetworkType() -> activeNetworkInfo is not connected!");
                return Constant.NETWORK_TYPE_UNKNOWN;
            }

            String typeName = activeNetworkInfo.getTypeName();
            if (TextUtils.isEmpty(typeName)) {
                ATLog.e(TAG, "getNetworkType() -> typeName is null!");
                return Constant.NETWORK_TYPE_UNKNOWN;
            }

            switch (typeName) {
                case Constant.NETWORK_TYPE_WIFI:
                    return typeName;
                case Constant.NETWORK_TYPE_MOBILE:
                    int networkType = telephonyManager.getNetworkType();
                    switch (networkType) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_GSM:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return Constant.NETWORK_TYPE_2G;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                        case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                            return Constant.NETWORK_TYPE_3G;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                        case TelephonyManager.NETWORK_TYPE_IWLAN:
                            return Constant.NETWORK_TYPE_4G;
                        default:
                            return Constant.NETWORK_TYPE_UNKNOWN;
                    }
                default:
                    return Constant.NETWORK_TYPE_UNKNOWN;
            }
        } catch (Exception e) {
            ATLog.e(TAG, "getNetworkType() -> exception:" + e.getMessage());
            return Constant.NETWORK_TYPE_UNKNOWN;
        }
    }

}
