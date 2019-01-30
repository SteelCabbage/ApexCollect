package com.chinapex.analytics.sample;

import android.app.Application;

import com.chinapex.analytics.sample.utils.AppLog;
import com.chinapex.android.datacollect.ApexAnalytics;
import com.chinapex.android.datacollect.model.bean.AnalyticsSettings;
import com.chinapex.android.datacollect.utils.ATLog;

/**
 * @author SteelCabbage
 * @date 2018/11/05
 */

public class SampleApp extends Application {

    private static final String TAG = SampleApp.class.getSimpleName();

    private static SampleApp sSampleApp;

    @Override
    public void onCreate() {
        super.onCreate();
        AppLog.i(TAG, "SampleApp start!");
        sSampleApp = this;

        long time = System.currentTimeMillis();

        // 初始化埋点sdk
        ApexAnalytics.getInstance().init(new AnalyticsSettings.SettingsBuilder(sSampleApp)
                .setUuid("testUuid")
                .setChannelId("豌豆荚")
                .setLogLevel(ATLog.VERBOSE)
                .setDelayReportInterval(1000 * 60 * 2)
                .setCheckInstantErrInterval(1000 * 60)
                .setReportMaxNum(5)
                .setUrlDelay("")
                .setUrlInstant("")
                .setHostnameVerifier("")
                .build());

        AppLog.d(TAG, "================init ApexAnalytics cost time================:" + (System.currentTimeMillis() - time));
    }

    public static SampleApp getInstance() {
        return sSampleApp;
    }

}