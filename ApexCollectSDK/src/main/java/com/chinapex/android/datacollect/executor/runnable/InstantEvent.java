package com.chinapex.android.datacollect.executor.runnable;

import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.bean.request.AnalyticsReport;
import com.chinapex.android.datacollect.model.db.DbConstant;
import com.chinapex.android.datacollect.model.db.DbDao;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.GsonUtils;
import com.chinapex.android.datacollect.utils.net.INetCallback;
import com.chinapex.android.datacollect.utils.net.OkHttpClientManager;

import java.util.ArrayList;

/**
 * @author SteelCabbage
 * @date 2018/11/16
 */
public class InstantEvent implements Runnable, INetCallback {

    private static final String TAG = InstantEvent.class.getSimpleName();
    private TrackEvent mTrackEvent;

    public InstantEvent(TrackEvent trackEvent) {
        mTrackEvent = trackEvent;
    }

    @Override
    public void run() {
        if (null == mTrackEvent) {
            ATLog.e(TAG, "run() -> mTrackEvent is null!");
            return;
        }

        AnalyticsReport analyticsReport = new AnalyticsReport();
        analyticsReport.setReportTime(System.currentTimeMillis());
        analyticsReport.setIdentity(ApexCache.getInstance().getIdentity());
        ArrayList<String> eventDatas = new ArrayList<>();
        eventDatas.add(mTrackEvent.getValue());
        analyticsReport.setEventDatas(eventDatas);

        String analyticsReportJson = GsonUtils.toJsonStr(analyticsReport);
        ATLog.i(TAG, "analyticsReportJson:" + analyticsReportJson);
        OkHttpClientManager.getInstance().postJson(Constant.URL_EVENT, analyticsReportJson, this);
    }

    @Override
    public void onSuccess(int statusCode, String msg, String result) {
        ATLog.v(TAG, "onSuccess() -> statusCode: " + statusCode + " ,msg: " + msg + " ," + "result:\n" + result);

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

        dbDao.insert(DbConstant.TABLE_INSTANT_ERR, mTrackEvent);
    }

//    @Override
//    public void onSuccess(int statusCode, String msg, String result) {
//        CpLog.v(TAG, "event onSuccess() -> statusCode:" + statusCode + ",msg:" + msg + "," +
//                "result:" +
//                result);
//
//        if (TextUtils.isEmpty(result)) {
//            CpLog.e(TAG, "event onSuccess() -> result is null or empty!");
//            saveDb();
//            return;
//        }
//
//        ReportResponse reportResponse = GsonUtils.json2Bean(result, ReportResponse.class);
//        if (null == reportResponse) {
//            CpLog.e(TAG, "event onSuccess() -> reportResponse is null");
//            saveDb();
//            return;
//        }
//
//        if (NetConstant.RESPOSE_OK == reportResponse.getStatus() && NetConstant
//                .RESPONSE_DATA_REASON_SUCCESS.equals(reportResponse.getData())) {
//            CpLog.i(TAG, "event onSuccess() -> event report successful!");
//            return;
//        }
//
//        //虽有返回，但返回码非200，或内容非success，也需存入数据库
//        CpLog.e(TAG, "event onSuccess() -> server abnormal return！need to save db!");
//        saveDb();
//
//    }
//
//    @Override
//    public void onFailed(int failedCode, String msg) {
//        CpLog.e(TAG, "event onFailed() -> failedCode:" + failedCode + ",msg:" + msg);
//        //请求失败，存入数据库
//        saveDb();
//
//    }
//
//    private void saveDb() {
//        AnalyticsDbDao analyticsDbDao = AnalyticsDbDao.getInstance(mContext);
//        if (null == analyticsDbDao) {
//            CpLog.e(TAG, "event saveDb() -> analyticsDbDao is null!");
//            return;
//        }
//
//        analyticsDbDao.insert(DbConstant.TABLE_delay, mAnalyticsRequest);
//
//    }
//
//    private String assembleEventRequestJson() {
//        PhoneStateController phoneStateController = PhoneStateController.getInstance(mContext);
//        if (null == phoneStateController) {
//            return "";
//        }
//
//        Event event = new Event();
//        event.setTid(phoneStateController.getTid());
//        event.setUtc(mAnalyticsRequest.getTime() / 1000);
//        event.setIds(phoneStateController.getIds());
//        event.setContext(phoneStateController.getPhoneContext());
//        event.setType(DbConstant.API_EVENT);
//        event.setVersion(ControllerConstant.API_VERSION);
//        event.setSource(NetConstant.REQUEST_FIELD_SOURCE);
//        event.setEvent(mEvent);
//        event.setEventtype(mEventtype);
//        event.setProperties(mHashMap);
//        return GsonUtils.toJsonStr(event);
//    }

}
