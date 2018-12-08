package com.chinapex.android.datacollect.executor.runnable;

import android.text.TextUtils;

import com.chinapex.android.datacollect.executor.callback.IMultiEventCallback;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.bean.event.ClickEventData;
import com.chinapex.android.datacollect.model.bean.event.ColdEventData;
import com.chinapex.android.datacollect.model.bean.event.CustomEventData;
import com.chinapex.android.datacollect.model.bean.event.PvEventData;
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
    private IMultiEventCallback mIMultiEventCallback;

    public MultiEvent(String tableName, TreeMap<Long, TrackEvent> trackEventTreeMap, IMultiEventCallback iMultiEventCallback) {
        mTableName = tableName;
        mTrackEventTreeMap = trackEventTreeMap;
        mIMultiEventCallback = iMultiEventCallback;
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mTableName)
                || null == mTrackEventTreeMap || mTrackEventTreeMap.isEmpty()
                || null == mIMultiEventCallback) {
            ATLog.e(TAG, "MultiEvent mTableName or mTrackEventTreeMap or mIMultiEventCallback is null or empty!");
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
        analyticsReport.setConfigVersion(ApexCache.getInstance().getConfigVersion());
        analyticsReport.setData(dataBean);

        String analyticsReportJson = GsonUtils.toJsonStr(analyticsReport);
        ATLog.i(TAG, mTableName + " MultiEvent analyticsReportJson:" + analyticsReportJson);

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
        ArrayList<Object> events = new ArrayList<>();

        a:
        for (Map.Entry<Long, TrackEvent> entry : mTrackEventTreeMap.entrySet()) {
            if (null == entry) {
                ATLog.e(TAG, mTableName + " run() -> entry is null");
                continue;
            }

            TrackEvent trackEvent = entry.getValue();
            if (null == trackEvent) {
                ATLog.e(TAG, mTableName + " run() -> trackEvent is null");
                continue;
            }

            switch (trackEvent.getEventType()) {
                case Constant.EVENT_TYPE_CUSTOM:
                    CustomEventData customEventData = GsonUtils.json2Bean(trackEvent.getValue(), CustomEventData.class);
                    if (null == customEventData) {
                        ATLog.e(TAG, mTableName + " customEventData is null!");
                        continue a;
                    }

                    events.add(customEventData);
                    break;
                case Constant.EVENT_TYPE_COLD:
                    ColdEventData coldEventData = GsonUtils.json2Bean(trackEvent.getValue(), ColdEventData.class);
                    if (null == coldEventData) {
                        ATLog.e(TAG, mTableName + " coldEventData is null!");
                        continue a;
                    }

                    events.add(coldEventData);
                    break;
                case Constant.EVENT_TYPE_CLICK:
                    ClickEventData clickEventData = GsonUtils.json2Bean(trackEvent.getValue(), ClickEventData.class);
                    if (null == clickEventData) {
                        ATLog.e(TAG, mTableName + " clickEventData is null!");
                        continue a;
                    }

                    events.add(clickEventData);
                    break;
                case Constant.EVENT_TYPE_PV:
                    PvEventData pvEventData = GsonUtils.json2Bean(trackEvent.getValue(),
                            PvEventData.class);
                    if (null == pvEventData) {
                        ATLog.e(TAG, mTableName + " pvEventData is null!");
                        continue a;
                    }

                    events.add(pvEventData);
                    break;
                default:
                    ATLog.e(TAG, " MultiEvent unknown eventType!");
                    break;
            }
        }

        return events;
    }

    @Override
    public void onSuccess(int statusCode, String msg, String result) {
        ATLog.i(TAG, mTableName + " MultiEvent onSuccess() -> request again ok! need to delete from db");
        if (null == mTrackEventTreeMap || mTrackEventTreeMap.isEmpty()) {
            ATLog.e(TAG, mTableName + " MultiEvent onSuccess() -> mTrackEventTreeMap is null or empty!");
            return;
        }

        for (Map.Entry<Long, TrackEvent> entry : mTrackEventTreeMap.entrySet()) {
            if (null == entry) {
                ATLog.e(TAG, mTableName + " MultiEvent onSuccess() -> entry is null or empty!");
                continue;
            }

            delSuccessRequest(entry.getKey());
        }

        if (null == mIMultiEventCallback) {
            ATLog.e(TAG, mTableName + " MultiEvent onSuccess() -> mIMultiEventCallback is null!");
            return;
        }

        // 如果result解析无异常且数据库中正常删除后, 再查询的offset应为0, 否则offset应为数据集合的长度, 保证后续数据正常发送
        mIMultiEventCallback.continueSend(0);
    }

    private void delSuccessRequest(long time) {
        DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
        if (null == dbDao) {
            ATLog.e(TAG, mTableName + " delSuccessRequest() -> analyticsDbDao is null!");
            return;
        }

        dbDao.deleteByTime(mTableName, time);
    }

    @Override
    public void onFailed(int failedCode, String msg) {
        ATLog.e(TAG, mTableName + " MultiEvent onFailed() -> request again, still failed!");

        if (null == mIMultiEventCallback) {
            ATLog.e(TAG, mTableName + " MultiEvent onFailed() -> mIMultiEventCallback is null!");
            return;
        }

        mIMultiEventCallback.stopSend(msg);
    }
}
