package com.chinapex.android.datacollect.executor.runnable;

import android.text.TextUtils;

import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.bean.request.AnalyticsReport;
import com.chinapex.android.datacollect.model.db.DbConstant;
import com.chinapex.android.datacollect.model.db.DbDao;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.GsonUtils;
import com.chinapex.android.datacollect.utils.net.INetCallback;
import com.chinapex.android.datacollect.utils.net.OkHttpClientManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author SteelCabbage
 * @date 2018/11/20
 */
public class DelayEvent implements Runnable, INetCallback {

    private static final String TAG = DelayEvent.class.getSimpleName();
    private String mTableName;
    private TreeMap<Long, TrackEvent> mTrackEventTreeMap;

    public DelayEvent(String tableName) {
        mTableName = tableName;
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mTableName)) {
            ATLog.e(TAG, "run() -> mTableName is null or empty!");
            return;
        }

        DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
        if (null == dbDao) {
            ATLog.e(TAG, "run() -> dbDao is null!");
            return;
        }

        mTrackEventTreeMap = dbDao.query(mTableName);
        if (null == mTrackEventTreeMap) {
            ATLog.e(TAG, "run() -> trackEventTreeMap is null!");
            return;
        }

        dealDbId();

        if (mTrackEventTreeMap.isEmpty()) {
            ATLog.w(TAG, "run() -> trackEventTreeMap is empty!");
            return;
        }

        AnalyticsReport analyticsReport = new AnalyticsReport();
        analyticsReport.setReportTime(System.currentTimeMillis());
        analyticsReport.setIdentity(ApexCache.getInstance().getIdentity());
        ArrayList<String> eventDatas = new ArrayList<>();

        for (Map.Entry<Long, TrackEvent> entry : mTrackEventTreeMap.entrySet()) {
            if (null == entry) {
                ATLog.e(TAG, "run() -> entry is null");
                continue;
            }

            TrackEvent trackEvent = entry.getValue();
            if (null == trackEvent) {
                ATLog.e(TAG, "run() -> trackEvent is null");
                continue;
            }

            eventDatas.add(trackEvent.getValue());
        }

        analyticsReport.setEventDatas(eventDatas);
        String analyticsReportJson = GsonUtils.toJsonStr(analyticsReport);
        ATLog.i(TAG, "analyticsReportJson:" + analyticsReportJson);

        String url;
        switch (mTableName) {
            case DbConstant.TABLE_DELAY_REPORT:
                url = ApexCache.getInstance().getUrlDelay();
                break;
            case DbConstant.TABLE_INSTANT_ERR:
                url = ApexCache.getInstance().getUrlInstant();
                break;
            default:
                url = null;
                break;
        }

        OkHttpClientManager.getInstance().postJson(url, analyticsReportJson, this);
    }

    private void dealDbId() {
        DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
        if (null == dbDao) {
            ATLog.e(TAG, "CheckInstantErr run() -> dbDao is null!");
            return;
        }

        dbDao.avoidIdUnlimitedGrowth(mTableName);
    }

    @Override
    public void onSuccess(int statusCode, String msg, String result) {
        ATLog.i(TAG, "onSuccess() -> request again ok! need to delete from db");
        if (null == mTrackEventTreeMap || mTrackEventTreeMap.isEmpty()) {
            ATLog.e(TAG, "onSuccess() -> mTrackEventTreeMap is null or empty!");
            return;
        }

        for (Map.Entry<Long, TrackEvent> entry : mTrackEventTreeMap.entrySet()) {
            if (null == entry) {
                ATLog.e(TAG, "onSuccess() -> entry is null or empty!");
                continue;
            }

            delSuccessRequest(entry.getKey());
        }
    }

    @Override
    public void onFailed(int failedCode, String msg) {
        ATLog.e(TAG, "onFailed() -> request again, still failed!");
    }

    private void delSuccessRequest(long time) {
        DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
        if (null == dbDao) {
            ATLog.e(TAG, "delSuccessRequest() -> analyticsDbDao is null!");
            return;
        }

        dbDao.deleteByTime(mTableName, time);
    }
}
