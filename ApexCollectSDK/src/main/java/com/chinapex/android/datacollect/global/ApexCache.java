package com.chinapex.android.datacollect.global;

import android.content.Context;

/**
 * @author SteelCabbage
 * @date 2018/11/12
 */
public class ApexCache {
    private static final String TAG = ApexCache.class.getSimpleName();
    private Context mContext;

    private ApexCache() {

    }

    private static class ApexCacheHolder {
        private static final ApexCache S_APEX_CACHE = new ApexCache();
    }

    public static ApexCache getInstance() {
        return ApexCacheHolder.S_APEX_CACHE;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }
}
