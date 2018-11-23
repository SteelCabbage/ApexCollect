package com.chinapex.android.datacollect.global;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SteelCabbage
 * @date 2018/11/23
 */
public class ApexCache {
    private static final String TAG = ApexCache.class.getSimpleName();

    /**
     * app的context
     */
    private Context mContext;

    /**
     * userId
     */
    private String mUserId = "";

    /**
     * uuid (androidId)
     */
    private String mUuid = "";

    /**
     * deviceIds
     */
    private List<String> mDeviceIds = new ArrayList<>();

    /**
     * country
     */
    private String country;

    /**
     * province
     */
    private String province;

    /**
     * city
     */
    private String city;

    /**
     * 上报的最大条数
     */
    private int mReportMaxNum = Constant.REPORT_MAX_NUM_DEFAULT;

    /**
     * 延时上报的时间间隔
     */
    private long mDelayReportInterval = Constant.DELAY_REPORT_INTERVAL_DEFAULT;

    /**
     * 检查即时上报是否存在异常的时间间隔
     */
    private long mCheckInstantErrInterval = Constant.CHECK_INSTANT_ERR_INTERVAL_DEFAULT;

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

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getUuid() {
        return mUuid;
    }

    public void setUuid(String uuid) {
        mUuid = uuid;
    }

    public List<String> getDeviceIds() {
        return mDeviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        mDeviceIds = deviceIds;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
