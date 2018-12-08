package com.chinapex.android.datacollect.executor.runnable;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.bean.event.PvEventData;
import com.chinapex.android.datacollect.model.db.DbConstant;
import com.chinapex.android.datacollect.model.db.DbDao;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.GsonUtils;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Seven
 * @date : 2018/11/28
 */
public class GenerateFragmentV4PvEventData implements Runnable {

    private static final String TAG = GenerateFragmentV4PvEventData.class.getSimpleName();

    private Fragment mFragment;
    private String mReference;
    private long mPvEndTime;

    public GenerateFragmentV4PvEventData(Fragment fragment, String reference, long pvEndTime) {
        mFragment = fragment;
        mReference = reference;
        mPvEndTime = pvEndTime;
    }

    @Override
    public void run() {
        if (null == mFragment) {
            ATLog.e(TAG, "GenerateFragmentV4PvEventData run() -> mFragment  is null!");
            return;
        }

        PvEventData.ValueBean valueBean = new PvEventData.ValueBean();
        valueBean.setReference(mReference);
        String pageClassName = mFragment.getClass().getName();
        valueBean.setPageClassName(pageClassName);

        ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
        if (null != pvDurationTimes && !TextUtils.isEmpty(pageClassName)) {
            Long pvStartTime = pvDurationTimes.get(pageClassName);
            if (null != pvStartTime) {
                valueBean.setDurationTime((mPvEndTime - pvStartTime));
                pvDurationTimes.remove(pageClassName);

                ATLog.v(TAG, "GenerateFragmentV4PvEventData run() -> mFragment: " + mFragment.getClass().getSimpleName() + " pvTime: " + (mPvEndTime - pvStartTime));
            }
        }

        // TODO: 2018/11/26 0026  列表曝光率
        valueBean.setExposures(new ArrayList<>());

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
            ATLog.e(TAG, "GenerateFragmentV4PvEventData run() -> dbDao is null!");
            return;
        }

        dbDao.insert(DbConstant.TABLE_DELAY_REPORT, trackEvent, System.currentTimeMillis());
    }
}
