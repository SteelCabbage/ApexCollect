package com.chinapex.android.datacollect.global;

import android.content.Context;

import com.chinapex.android.datacollect.model.bean.Identity;
import com.chinapex.android.datacollect.model.db.DbConstant;

/**
 * @author SteelCabbage
 * @date 2018/11/12
 */
public class ApexCache {
    private static final String TAG = ApexCache.class.getSimpleName();

    /**
     * app的context
     */
    private Context mContext;

    /**
     * 身份识别
     */
    private Identity mIdentity;

    /**
     * 上报的最大条数
     */
    private int mReportMaxNum = DbConstant.REPORT_MAX_NUM;

    /**
     * 延时上报的时间间隔
     */
    private long mDelayReportInterval = Constant.DELAY_REPORT_INTERVAL;

    /**
     * 检查即时上报是否存在异常的时间间隔
     */
    private long mCheckInstantErrInterval = Constant.CHECK_INSTANT_ERR_INTERVAL;

    /**
     * 延时上报的url
     */
    private String mUrlDelay = Constant.URL_DELAY_REPORT;

    /**
     * 即时上报的url
     */
    private String mUrlInstant = Constant.URL_INSTANT_REPORT;

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

    public Identity getIdentity() {
        return mIdentity;
    }

    public void setIdentity(Identity identity) {
        mIdentity = identity;
    }

    public int getReportMaxNum() {
        return mReportMaxNum;
    }

    public void setReportMaxNum(int reportMaxNum) {
        mReportMaxNum = reportMaxNum;
    }

    public long getDelayReportInterval() {
        return mDelayReportInterval;
    }

    public void setDelayReportInterval(long delayReportInterval) {
        mDelayReportInterval = delayReportInterval;
    }

    public long getCheckInstantErrInterval() {
        return mCheckInstantErrInterval;
    }

    public void setCheckInstantErrInterval(long checkInstantErrInterval) {
        mCheckInstantErrInterval = checkInstantErrInterval;
    }

    public String getUrlDelay() {
        return mUrlDelay;
    }

    public void setUrlDelay(String urlDelay) {
        mUrlDelay = urlDelay;
    }

    public String getUrlInstant() {
        return mUrlInstant;
    }

    public void setUrlInstant(String urlInstant) {
        mUrlInstant = urlInstant;
    }
}
