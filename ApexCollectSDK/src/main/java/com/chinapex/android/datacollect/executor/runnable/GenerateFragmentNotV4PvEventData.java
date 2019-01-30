package com.chinapex.android.datacollect.executor.runnable;

import android.text.TextUtils;

import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.bean.event.PvEventData;
import com.chinapex.android.datacollect.model.bean.response.UpdateConfigResponse;
import com.chinapex.android.datacollect.model.db.DbConstant;
import com.chinapex.android.datacollect.model.db.DbDao;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.ConfigUtils;
import com.chinapex.android.datacollect.utils.GsonUtils;
import com.chinapex.android.datacollect.utils.MD5Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Seven
 * @date : 2018/11/28
 */
public class GenerateFragmentNotV4PvEventData implements Runnable {

    private static final String TAG = GenerateFragmentNotV4PvEventData.class.getSimpleName();

    private String mPageClassName;
    private String mReference;
    private long mPvEndTime;

    public GenerateFragmentNotV4PvEventData(String pageClassName, String reference, long pvEndTime) {
        mPageClassName = pageClassName;
        mReference = reference;
        mPvEndTime = pvEndTime;
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mPageClassName)) {
            ATLog.e(TAG, "GenerateFragmentNotV4PvEventData run() -> mPageClassName  is null or empty!");
            return;
        }

        String pageClassNameMD5 = MD5Utils.getMD5(mPageClassName);
        UpdateConfigResponse.DataBean.Config.PvBean configPvBean = ConfigUtils.getConfigPvBean(mPageClassName);

        PvEventData.ValueBean valueBean = new PvEventData.ValueBean();

        if (null == configPvBean || TextUtils.isEmpty(configPvBean.getAlias())) {
            valueBean.setAlias(Constant.EVENT_LABEL_PV);
        } else {
            valueBean.setAlias(configPvBean.getAlias());
        }

        valueBean.setReference(null == mReference ? "" : mReference);
        valueBean.setPageClassName(mPageClassName);
        valueBean.setMd5(pageClassNameMD5);

        ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
        if (null != pvDurationTimes && !TextUtils.isEmpty(mPageClassName)) {
            Long pvStartTime = pvDurationTimes.get(mPageClassName);
            if (null != pvStartTime) {
                valueBean.setDurationTime((mPvEndTime - pvStartTime));
                pvDurationTimes.remove(mPageClassName);

                ATLog.v(TAG, "GenerateFragmentNotV4PvEventData run() -> mFragment: " + mPageClassName
                        + " pvTime: " + (mPvEndTime - pvStartTime));
            }
        }

        PvEventData pvEventData = new PvEventData();
        pvEventData.setEventType(Constant.EVENT_TYPE_PV);
        pvEventData.setLabel(Constant.EVENT_LABEL_PV);
        pvEventData.setUserId(ApexCache.getInstance().getUserId());
        pvEventData.setCountry(ApexCache.getInstance().getCountry());
        pvEventData.setProvince(ApexCache.getInstance().getProvince());
        pvEventData.setCity(ApexCache.getInstance().getCity());
        pvEventData.setTimeStamp(mPvEndTime);
        pvEventData.setValue(valueBean);

        TrackEvent trackEvent = new TrackEvent.EventBuilder()
                .setMode(Constant.MODE_DELAY)
                .setEventType(Constant.EVENT_TYPE_PV)
                .setLabel(Constant.EVENT_LABEL_PV)
                .setValue(GsonUtils.toJsonStr(pvEventData))
                .build();

        DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
        if (null == dbDao) {
            ATLog.e(TAG, "GenerateFragmentNotV4PvEventData run() -> dbDao is null!");
            return;
        }

        dbDao.insert(DbConstant.TABLE_DELAY_REPORT, trackEvent, System.currentTimeMillis());
    }

}
