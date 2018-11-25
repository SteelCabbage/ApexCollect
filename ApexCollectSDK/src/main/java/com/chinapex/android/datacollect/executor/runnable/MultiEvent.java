package com.chinapex.android.datacollect.executor.runnable;

import android.text.TextUtils;

import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.bean.event.ColdEventData;
import com.chinapex.android.datacollect.model.bean.event.CustomEventData;
import com.chinapex.android.datacollect.model.bean.request.AnalyticsReport;
import com.chinapex.android.datacollect.model.db.DbConstant;
import com.chinapex.android.datacollect.model.db.DbDao;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.GsonUtils;
import com.chinapex.android.datacollect.utils.PhoneStateUtils;
import com.chinapex.android.datacollect.utils.net.INetCallback;
import com.chinapex.android.datacollect.utils.net.OkHttpClientManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author SteelCabbage
 * @date 2018/11/22
 */
public class MultiEvent implements Runnable, INetCallback {

    private static final String TAG = MultiEvent.class.getSimpleName();
    private String mTableName;
    private TreeMap<Long, TrackEvent> mTrackEventTreeMap;

    public MultiEvent(String tableName, TreeMap<Long, TrackEvent> trackEventTreeMap) {
        mTableName = tableName;
        mTrackEventTreeMap = trackEventTreeMap;
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mTableName) || null == mTrackEventTreeMap || mTrackEventTreeMap.isEmpty()) {
            ATLog.e(TAG, "MultiEvent mTableName or mTrackEventTreeMap is null or empty!");
            return;
        }

        AnalyticsReport.DataBean dataBean = new AnalyticsReport.DataBean();
        dataBean.setReportTime(System.currentTimeMillis());
        dataBean.setLanguage(PhoneStateUtils.getLanguage());
        dataBean.setUuid(ApexCache.getInstance().getUuid());
        dataBean.setDeviceID(ApexCache.getInstance().getDeviceIds());
        dataBean.setEvents(getEvents());

        AnalyticsReport analyticsReport = new AnalyticsReport();
        analyticsReport.setCompany(Constant.COMPANY);
        analyticsReport.setTerminal(Constant.TERMINAL);
        analyticsReport.setData(dataBean);

        String analyticsReportJson = GsonUtils.toJsonStr(analyticsReport);
        ATLog.i(TAG, mTableName + "MultiEvent analyticsReportJson:" + analyticsReportJson);

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

    private List<Object> getEvents() {
        ArrayList<Object> eventDatas = new ArrayList<>();

        a:
        for (Map.Entry<Long, TrackEvent> entry : mTrackEventTreeMap.entrySet()) {
            if (null == entry) {
                ATLog.e(TAG, mTableName + "run() -> entry is null");
                continue;
            }

            TrackEvent trackEvent = entry.getValue();
            if (null == trackEvent) {
                ATLog.e(TAG, mTableName + "run() -> trackEvent is null");
                continue;
            }

            switch (trackEvent.getEventType()) {
                case Constant.EVENT_TYPE_CUSTOM:
                    CustomEventData customEventData = GsonUtils.json2Bean(trackEvent.getValue(), CustomEventData.class);
                    if (null == customEventData) {
                        ATLog.e(TAG, "customEventData is null!");
                        continue a;
                    }

                    eventDatas.add(customEventData);
                    break;
                case Constant.EVENT_TYPE_COLD:
                    ColdEventData coldEventData = GsonUtils.json2Bean(trackEvent.getValue(), ColdEventData.class);
                    if (null == coldEventData) {
                        ATLog.e(TAG, mTableName + "coldEventData is null!");
                        continue a;
                    }

                    eventDatas.add(coldEventData);
                    break;
                case Constant.EVENT_TYPE_CLICK:
                    break;
                case Constant.EVENT_TYPE_PV:
                    break;
                default:
                    break;
            }
        }

        return eventDatas;
    }

    @Override
    public void onSuccess(int statusCode, String msg, String result) {
        ATLog.i(TAG, mTableName + "onSuccess() -> request again ok! need to delete from db");
        if (null == mTrackEventTreeMap || mTrackEventTreeMap.isEmpty()) {
            ATLog.e(TAG, mTableName + "onSuccess() -> mTrackEventTreeMap is null or empty!");
            return;
        }

        for (Map.Entry<Long, TrackEvent> entry : mTrackEventTreeMap.entrySet()) {
            if (null == entry) {
                ATLog.e(TAG, mTableName + "onSuccess() -> entry is null or empty!");
                continue;
            }

            delSuccessRequest(entry.getKey());
        }
    }

    private void delSuccessRequest(long time) {
        DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
        if (null == dbDao) {
            ATLog.e(TAG, mTableName + "delSuccessRequest() -> analyticsDbDao is null!");
            return;
        }

        dbDao.deleteByTime(mTableName, time);
    }

    @Override
    public void onFailed(int failedCode, String msg) {
        ATLog.e(TAG, mTableName + "onFailed() -> request again, still failed!");
    }
}
