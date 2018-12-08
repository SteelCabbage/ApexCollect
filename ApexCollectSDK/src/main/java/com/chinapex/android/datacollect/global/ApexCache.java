package com.chinapex.android.datacollect.global;

import android.content.Context;
import android.text.TextUtils;

import com.chinapex.android.datacollect.changelistener.AnalyticsListenerController;
import com.chinapex.android.datacollect.changelistener.OnNetworkTypeChangeListener;
import com.chinapex.android.datacollect.utils.ATLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SteelCabbage
 * @date 2018/11/23
 */
public class ApexCache implements OnNetworkTypeChangeListener {

    private static final String TAG = ApexCache.class.getSimpleName();
    private ConcurrentHashMap<String, Long> mPvDurationTimes;
    private Stack<String> mReferences;

    /**
     * app的context
     */
    private Context mContext;
    /**
     * 配置文件的版本号, appVersion#时间戳
     */
    private String mConfigVersion;
    private String mUserId = "";
    /**
     * uuid (由使用者设置)
     */
    private String mUuid;
    private List<String> mDeviceIds = new ArrayList<>();
    private String country = "country";
    private String province = "province";
    private String city = "city";
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
    private String mNetworkType;


    private ApexCache() {

    }

    private static class ApexCacheHolder {
        private static final ApexCache S_APEX_CACHE = new ApexCache();
    }

    public static ApexCache getInstance() {
        return ApexCacheHolder.S_APEX_CACHE;
    }

    public void doInit() {
        mPvDurationTimes = new ConcurrentHashMap<>();
        mReferences = new Stack<>();
        AnalyticsListenerController.getInstance().addOnNetworkTypeChangeListener(this);
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public String getConfigVersion() {
        return mConfigVersion;
    }

    public void setConfigVersion(String configVersion) {
        mConfigVersion = configVersion;
    }

    public ConcurrentHashMap<String, Long> getPvDurationTimes() {
        return mPvDurationTimes;
    }

    public Stack<String> getReferences() {
        return mReferences;
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

    public String getNetworkType() {
        return mNetworkType;
    }

    @Override
    public void networkTypeChange(String networkType) {
        if (TextUtils.isEmpty(networkType)) {
            ATLog.e(TAG, "networkTypeChange() -> mNetworkType is null!");
            return;
        }

        mNetworkType = networkType;
        ATLog.d(TAG, "networkTypeChange():" + mNetworkType);
    }
}
