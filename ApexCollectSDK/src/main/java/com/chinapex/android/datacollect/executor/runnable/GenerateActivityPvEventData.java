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
 * @author SteelCabbage
 * @date 2018/11/26
 */
public class GenerateActivityPvEventData implements Runnable {

    private static final String TAG = GenerateActivityPvEventData.class.getSimpleName();
    private String mPageClassName;
    private String mReference;
    private long mPvEndTime;

    public GenerateActivityPvEventData(String pageClassName, String reference, long pvEndTime) {
        mPageClassName = pageClassName;
        mReference = reference;
        mPvEndTime = pvEndTime;
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mPageClassName) || null == mReference) {
            ATLog.e(TAG, "GenerateActivityPvEventData run() -> mActivity or mReference is null!");
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

        valueBean.setReference(mReference);
        valueBean.setPageClassName(mPageClassName);
        valueBean.setMd5(MD5Utils.getMD5(pageClassNameMD5));

        ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
        if (null != pvDurationTimes && !TextUtils.isEmpty(mPageClassName)) {
            Long pvStartTime = pvDurationTimes.get(mPageClassName);
            if (null != pvStartTime) {
                valueBean.setDurationTime((mPvEndTime - pvStartTime));
                // 计算完毕后, 从缓存中移除
                pvDurationTimes.remove(mPageClassName);
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
            ATLog.e(TAG, "GenerateActivityPvEventData run() -> dbDao is null!");
            return;
        }

        dbDao.insert(DbConstant.TABLE_DELAY_REPORT, trackEvent, System.currentTimeMillis());
    }

}
