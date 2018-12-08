package com.chinapex.android.datacollect.executor.runnable;

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
import java.util.Map;

/**
 * @author SteelCabbage
 * @date 2018/11/16
 */
public class InstantEvent implements Runnable, INetCallback {

    private static final String TAG = InstantEvent.class.getSimpleName();
    private TrackEvent mTrackEvent;
    private long mReportTime;

    public InstantEvent(TrackEvent trackEvent) {
        mTrackEvent = trackEvent;
        mReportTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        if (null == mTrackEvent) {
            ATLog.e(TAG, "InstantEvent run() -> mTrackEvent is null!");
            return;
        }

        ArrayList<Object> events = new ArrayList<>();
        switch (mTrackEvent.getEventType()) {
            case Constant.EVENT_TYPE_CUSTOM:
                CustomEventData customEventData = GsonUtils.json2Bean(mTrackEvent.getValue(), CustomEventData.class);
                if (null == customEventData) {
                    ATLog.e(TAG, "customEventData is null!");
                    return;
                }

                events.add(customEventData);
                break;
            case Constant.EVENT_TYPE_COLD:
                ColdEventData coldEventData = GsonUtils.json2Bean(mTrackEvent.getValue(), ColdEventData.class);
                if (null == coldEventData) {
                    ATLog.e(TAG, "coldEventData is null!");
                    return;
                }

                events.add(coldEventData);
                break;
            case Constant.EVENT_TYPE_CLICK:
                ClickEventData clickEventData = GsonUtils.json2Bean(mTrackEvent.getValue(), ClickEventData.class);
                if (null == clickEventData) {
                    ATLog.e(TAG, "clickEventData is null!");
                    return;
                }

                events.add(clickEventData);
                break;
            case Constant.EVENT_TYPE_PV:
                PvEventData pvEventData = GsonUtils.json2Bean(mTrackEvent.getValue(), PvEventData.class);
                if (null == pvEventData) {
                    ATLog.e(TAG, "pvEventData is null!");
                    return;
                }

                events.add(pvEventData);
                break;
            default:
                ATLog.e(TAG, "InstantEvent unknown eventType!");
                break;
        }

        AnalyticsReport.DataBean dataBean = new AnalyticsReport.DataBean();
        dataBean.setReportTime(mReportTime);
        dataBean.setLanguage(PhoneStateUtils.getLanguage());
        dataBean.setUuid(ApexCache.getInstance().getUuid());
        dataBean.setDeviceID(ApexCache.getInstance().getDeviceIds());
        dataBean.setEvents(events);

        AnalyticsReport analyticsReport = new AnalyticsReport();
        analyticsReport.setCompany(Constant.COMPANY);
        analyticsReport.setTerminal(Constant.TERMINAL);
        analyticsReport.setConfigVersion(ApexCache.getInstance().getConfigVersion());
        analyticsReport.setData(dataBean);

        String analyticsReportJson = GsonUtils.toJsonStr(analyticsReport);
        ATLog.i(TAG, "InstantEvent analyticsReportJson:" + analyticsReportJson);

        OkHttpClientManager.getInstance().postJson(ApexCache.getInstance().getUrlInstant(), analyticsReportJson, this);
    }

    @Override
    public void onSuccess(int statusCode, String msg, String result) {
        ATLog.i(TAG, "onSuccess() -> statusCode: " + statusCode + " ,msg: " + msg + " ," + "result:\n" + result);

    }

    @Override
    public void onFailed(int failedCode, String msg) {
        ATLog.e(TAG, "onFailed() -> failedCode: " + failedCode + " ,msg: " + msg);
        // 请求失败，存入数据库
        DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
        if (null == dbDao) {
            ATLog.e(TAG, "InstantEvent saveDb() -> dbDao is null!");
            return;
        }

        dbDao.insert(DbConstant.TABLE_INSTANT_ERR, mTrackEvent, mReportTime);
    }

}
