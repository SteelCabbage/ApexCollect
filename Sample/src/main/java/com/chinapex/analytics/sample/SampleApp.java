package com.chinapex.analytics.sample;

import android.app.Application;

import com.chinapex.analytics.sample.utils.AppLog;
import com.chinapex.android.datacollect.ApexAnalytics;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
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

        // 初始化埋点sdk
        ApexAnalytics.getInstance().init(sSampleApp);
        // settings
        ApexAnalytics.getInstance().setLogLevel(ATLog.VERBOSE);
        ApexAnalytics.getInstance().setReportMaxNum(5);
        ApexAnalytics.getInstance().setDelayReportInterval(1000 * 60 * 2);
        ApexAnalytics.getInstance().setCheckInstantErrInterval(1000 * 60);
    }

    public static SampleApp getInstance() {
        return sSampleApp;
    }

}