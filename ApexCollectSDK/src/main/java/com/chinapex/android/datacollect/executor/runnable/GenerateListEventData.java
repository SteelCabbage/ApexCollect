package com.chinapex.android.datacollect.executor.runnable;

import android.text.TextUtils;

import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.bean.event.ListEventData;
import com.chinapex.android.datacollect.model.db.DbConstant;
import com.chinapex.android.datacollect.model.db.DbDao;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.GsonUtils;

import java.util.Map;

/**
 * @author SteelCabbage
 * @date 2019/01/07
 */
public class GenerateListEventData implements Runnable {

    private static final String TAG = GenerateListEventData.class.getSimpleName();
    private String mPageName;
    private String mListIdMD5;
    private Map<String, ListEventData.ValueBean.ListItem> mExposures;

    public GenerateListEventData(String pageName, String listIdMD5, Map<String, ListEventData.ValueBean.ListItem> exposures) {
        mPageName = pageName;
        mListIdMD5 = listIdMD5;
        mExposures = exposures;
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mPageName) || TextUtils.isEmpty(mListIdMD5) || null == mExposures || mExposures.isEmpty()) {
            ATLog.e(TAG, "mPageName or mListIdMD5 or mExposures is null or empty!");
            return;
        }

        ListEventData.ValueBean valueBean = new ListEventData.ValueBean();
        valueBean.setPageClassName(mPageName);
        valueBean.setListIdMD5(mListIdMD5);
        valueBean.setExposures(mExposures);

        ListEventData listEventData = new ListEventData();
        listEventData.setEventType(Constant.EVENT_TYPE_LIST);
        listEventData.setLabel(Constant.EVENT_LABEL_LIST);
        listEventData.setUserId(ApexCache.getInstance().getUserId());
        listEventData.setCountry(ApexCache.getInstance().getCountry());
        listEventData.setProvince(ApexCache.getInstance().getProvince());
        listEventData.setCity(ApexCache.getInstance().getCity());
        listEventData.setTimeStamp(System.currentTimeMillis());
        listEventData.setValue(valueBean);

        TrackEvent trackEvent = new TrackEvent.EventBuilder()
                .setMode(Constant.MODE_DELAY)
                .setEventType(Constant.EVENT_TYPE_LIST)
                .setLabel(Constant.EVENT_LABEL_LIST)
                .setValue(GsonUtils.toJsonStr(listEventData))
                .build();

        DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
        if (null == dbDao) {
            ATLog.e(TAG, "GenerateListEventData run() -> dbDao is null!");
            return;
        }

        dbDao.insert(DbConstant.TABLE_DELAY_REPORT, trackEvent, System.currentTimeMillis());
    }
}
