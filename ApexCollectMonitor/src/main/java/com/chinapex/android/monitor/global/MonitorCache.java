package com.chinapex.android.monitor.global;

import android.content.Context;
import android.text.TextUtils;

import com.chinapex.android.monitor.bean.StatisticsBean;
import com.chinapex.android.monitor.bean.UploadConfigRequest;
import com.chinapex.android.monitor.callback.IMonitorCallback;
import com.chinapex.android.monitor.utils.GsonUtils;
import com.chinapex.android.monitor.utils.MD5Utils;
import com.chinapex.android.monitor.utils.MLog;
import com.chinapex.android.monitor.utils.SpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


/**
 * @author SteelCabbage
 * @date 2018/11/23
 */
public class MonitorCache {

    private static final String TAG = MonitorCache.class.getSimpleName();

    private Map<String, UploadConfigRequest.Config.ClickBean> mClickBeans = new HashMap<>();
    private Map<String, UploadConfigRequest.Config.PvBean> mPvBeans = new HashMap<>();
    private Map<String, UploadConfigRequest.Config.ListBean> mListBeans = new HashMap<>();

    private Set<String> mViewPathMD5List = new HashSet<>();

    private MonitorCache() {

    }

    private static class MonitorCacheHolder {
        private static final MonitorCache MONITOR_CACHE = new MonitorCache();
    }

    public static MonitorCache getInstance() {
        return MonitorCacheHolder.MONITOR_CACHE;
    }

    /**
     * app的context
     */
    private Context mContext;

    /**
     * 圈选模式开关的回调
     */
    private IMonitorCallback mIMonitorCallback;

    /**
     * 保存activity，fragment的栈
     */
    private Map<Integer, Stack<Map<String, Boolean>>> mTasks;

    /**
     * 指向栈顶activity的指针
     */
    private Map<Integer, Integer> mTop;

    /**
     * 指向前台任务栈
     */
    private int[] mForegroundTask;

    public Map<Integer, Stack<Map<String, Boolean>>> getTasks() {
        return mTasks;
    }

    public void setTasks(Map<Integer, Stack<Map<String, Boolean>>> mTasks) {
        this.mTasks = mTasks;
    }

    public Map<Integer, Integer> getTop() {
        return mTop;
    }

    public void setTop(Map<Integer, Integer> mTop) {
        this.mTop = mTop;
    }

    public int[] getForegroundTask() {
        return mForegroundTask;
    }

    public void setForegroundTask(int[] mForegroundTask) {
        this.mForegroundTask = mForegroundTask;
    }

    /**
     * Hostname verifier
     */
    private String hostnameVerifier = Constant.HOSTNAME_VERIFIER;

    public List<StatisticsBean> getStatisticsBeans() {
        return mStatisticsBeans;
    }

    public void setStatisticsBeans(List<StatisticsBean> statisticsBeans) {
        mStatisticsBeans = statisticsBeans;
    }

    public List<StatisticsBean> mStatisticsBeans = new ArrayList<>();

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public IMonitorCallback getIMonitorCallback() {
        return mIMonitorCallback;
    }

    public void setIMonitorCallback(IMonitorCallback iMonitorCallback) {
        mIMonitorCallback = iMonitorCallback;
    }

    public String getHostnameVerifier() {
        return hostnameVerifier;
    }

    public void setHostnameVerifier(String hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    public Map<String, UploadConfigRequest.Config.ClickBean> getClickBeans() {
        return mClickBeans;
    }

    public void setClickBeans(Map<String, UploadConfigRequest.Config.ClickBean> clickBeans) {
        mClickBeans = clickBeans;
    }

    public void putClickBeans(String clickMD5, UploadConfigRequest.Config.ClickBean clickBean) {
        mClickBeans.put(clickMD5, clickBean);
    }

    public Map<String, UploadConfigRequest.Config.PvBean> getPvBeans() {
        return mPvBeans;
    }

    public void setPvBeans(Map<String, UploadConfigRequest.Config.PvBean> pvBeans) {
        mPvBeans = pvBeans;
    }

    public void putPvBeans(String pvMD5, UploadConfigRequest.Config.PvBean pvBean) {
        mPvBeans.put(pvMD5, pvBean);
    }

    public Map<String, UploadConfigRequest.Config.ListBean> getListBeans() {
        return mListBeans;
    }

    public void setListBeans(Map<String, UploadConfigRequest.Config.ListBean> listBeans) {
        mListBeans = listBeans;
    }

    public void putListBeans(String listMD5, UploadConfigRequest.Config.ListBean listBean) {
        mListBeans.put(listMD5, listBean);
    }

    public void getExistData() {
        getExistPv();
        getExistClick();
        getExistList();
    }

    private void getExistList() {
        String listBeanStr = (String) SpUtils.getParam(MonitorCache.getInstance().getContext(), Constant.SP_KEY_EVENT_LIST,
                Constant.SP_VALUE_DEFAULT_EVENT);
        if (TextUtils.isEmpty(listBeanStr)) {
            MLog.i(TAG, "getExistList -> listBeanStr is null or empty !");
            return;
        }

        Map<String, UploadConfigRequest.Config.ListBean> listBeans = GsonUtils.json2ListMap(listBeanStr);
        if (null == listBeans || listBeans.isEmpty()) {
            MLog.i(TAG, "getExistList -> listBeans is null or empty !");
            return;
        }
        mListBeans.putAll(listBeans);
    }

    private void getExistClick() {
        String clickBeanStr = (String) SpUtils.getParam(MonitorCache.getInstance().getContext(), Constant.SP_KEY_EVENT_CLICK,
                Constant.SP_VALUE_DEFAULT_EVENT);
        if (TextUtils.isEmpty(clickBeanStr)) {
            MLog.i(TAG, "getExistClick -> clickBeanStr is null or empty !");
            return;
        }

        Map<String, UploadConfigRequest.Config.ClickBean> clickBeans = GsonUtils.json2ClickMap(clickBeanStr);
        if (null == clickBeans || clickBeans.isEmpty()) {
            MLog.i(TAG, "getExistClick -> clickBeans is null or empty !");
            return;
        }
        mClickBeans.putAll(clickBeans);
    }

    private void getExistPv() {
        String pvBeanStr = (String) SpUtils.getParam(MonitorCache.getInstance().getContext(), Constant.SP_KEY_EVENT_PV,
                Constant.SP_VALUE_DEFAULT_EVENT);
        MLog.i(TAG, "getExistPv -> pvBeanStr :" + pvBeanStr);
        if (TextUtils.isEmpty(pvBeanStr)) {
            MLog.i(TAG, "getExistPv -> pvBeanStr is null or empty !");
            return;
        }

        Map<String, UploadConfigRequest.Config.PvBean> pvBeans = GsonUtils.json2PvMap(pvBeanStr);
        if (null == pvBeans || pvBeans.isEmpty()) {
            MLog.i(TAG, "getExistPv -> pvBeans is null or empty !");
            return;
        }
        MLog.i(TAG, "getExistPv -> pvBeans " + GsonUtils.toJsonStr(pvBeans));

        mPvBeans.putAll(pvBeans);
    }

    public Set<String> getContrastItems() {
        return mViewPathMD5List;
    }

    public boolean addContrastItem(String viewPath) {
        String viewPathMD5 = MD5Utils.getMD5(viewPath);
        boolean add = mViewPathMD5List.add(viewPathMD5);
        MLog.v(TAG, "addContrastViewPath()-> add viewPath:" + viewPath + " " + add);
        return add;
    }

}