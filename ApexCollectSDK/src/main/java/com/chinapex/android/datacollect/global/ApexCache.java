package com.chinapex.android.datacollect.global;

import android.content.Context;
import android.text.TextUtils;

import com.chinapex.android.datacollect.aop.AopHelper;
import com.chinapex.android.datacollect.changelistener.AnalyticsListenerController;
import com.chinapex.android.datacollect.changelistener.OnNetworkTypeChangeListener;
import com.chinapex.android.datacollect.model.bean.response.UpdateConfigResponse;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.GsonUtils;
import com.chinapex.android.datacollect.utils.SpUtils;
import com.chinapex.android.monitor.callback.IMonitorCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SteelCabbage
 * @date 2018/11/23
 */
public class ApexCache implements OnNetworkTypeChangeListener, IMonitorCallback {

    private static final String TAG = ApexCache.class.getSimpleName();
    private ConcurrentHashMap<String, Long> mPvDurationTimes;
    private Map<Integer, Stack<Map<String, Boolean>>> mTasks;
    private Map<Integer, Integer> mTop;
    private int[] mForegroudTask;

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
    private String channelId = "channelId";
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
    /**
     * Hostname verifier
     */
    private String hostnameVerifier = Constant.HOSTNAME_VERIFIER;
    /**
     * 网络类型
     */
    private String mNetworkType;
    /**
     * 点击事件的配置
     */
    private Map<String, UpdateConfigResponse.DataBean.Config.ClickBean> mConfigClick;
    /**
     * 页面事件的配置
     */
    private Map<String, UpdateConfigResponse.DataBean.Config.PvBean> mConfigPv;
    /**
     * 列表事件的配置
     */
    private Map<String, UpdateConfigResponse.DataBean.Config.ListBean> mConfigList;


    private ApexCache() {

    }

    private static class ApexCacheHolder {
        private static final ApexCache S_APEX_CACHE = new ApexCache();
    }

    public static ApexCache getInstance() {
        return ApexCacheHolder.S_APEX_CACHE;
    }

    public void doInit() {
        // pv事件开始时间
        mPvDurationTimes = new ConcurrentHashMap<>();

        // pv栈
        mTasks = new HashMap<>();
        mTop = new HashMap<>();
        mForegroudTask = new int[1];

        // 网络状态改变的监听
        AnalyticsListenerController.getInstance().addOnNetworkTypeChangeListener(this);

        // 从本地加载配置文件
        mConfigVersion = (String) SpUtils.getParam(mContext, Constant.SP_KEY_CONFIG_VERSION, Constant.SP_KEY_CONFIG_VERSION);
        String configJson = (String) SpUtils.getParam(mContext, Constant.SP_KEY_CONFIG, Constant.SP_DEF_VAL_CONFIG);
        if (TextUtils.isEmpty(configJson)) {
            ATLog.e(TAG, "ApexCache doInit() -> configJson is null or empty!");
            return;
        }

        UpdateConfigResponse.DataBean.Config config = GsonUtils.json2Bean(configJson, UpdateConfigResponse.DataBean.Config.class);
        if (null == config) {
            ATLog.e(TAG, "ApexCache doInit() -> config is null!");
            return;
        }

        mConfigClick = config.getClick();
        mConfigPv = config.getPv();
        mConfigList = config.getList();
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

    public int[] getForegroudTask() {
        return mForegroudTask;
    }

    public Map<Integer, Stack<Map<String, Boolean>>> getTasks() {
        return mTasks;
    }

    public Map<Integer, Integer> getTop() {
        return mTop;
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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public String getHostnameVerifier() {
        return hostnameVerifier;
    }

    public void setHostnameVerifier(String hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    public String getNetworkType() {
        return mNetworkType;
    }

    public Map<String, UpdateConfigResponse.DataBean.Config.ClickBean> getConfigClick() {
        return mConfigClick;
    }

    public void setConfigClick(Map<String, UpdateConfigResponse.DataBean.Config.ClickBean> configClick) {
        mConfigClick = configClick;
    }

    public Map<String, UpdateConfigResponse.DataBean.Config.PvBean> getConfigPv() {
        return mConfigPv;
    }

    public void setConfigPv(Map<String, UpdateConfigResponse.DataBean.Config.PvBean> configPv) {
        mConfigPv = configPv;
    }

    public Map<String, UpdateConfigResponse.DataBean.Config.ListBean> getConfigList() {
        return mConfigList;
    }

    public void setConfigList(Map<String, UpdateConfigResponse.DataBean.Config.ListBean> configList) {
        mConfigList = configList;
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

    @Override
    public void isMonitor(boolean isMonitor, int monitorMode) {
        ATLog.i(TAG, "isMonitor:" + isMonitor + "monitorMode:" + monitorMode);
        AopHelper.setIsMonitor(isMonitor, monitorMode);
    }
}
