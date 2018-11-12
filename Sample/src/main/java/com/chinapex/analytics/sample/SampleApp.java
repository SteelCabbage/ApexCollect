package com.chinapex.analytics.sample;

import android.app.Application;

/**
 * Created by SteelCabbage on 2018/5/21 0021.
 */

public class SampleApp extends Application {

    private static final String TAG = SampleApp.class.getSimpleName();

    private static SampleApp sSampleApp;

    @Override
    public void onCreate() {
        super.onCreate();
        AtLog.i(TAG, "SampleApp start!");
        sSampleApp = this;
    }

    public static SampleApp getInstance() {
        return sSampleApp;
    }

}