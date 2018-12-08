package com.chinapex.android.datacollect.executor.runnable;

import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.bean.event.CustomEventData;
import com.chinapex.android.datacollect.model.db.DbConstant;
import com.chinapex.android.datacollect.model.db.DbDao;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.GsonUtils;

import java.util.Map;

/**
 * @author SteelCabbage
 * @date 2018/11/25
 */
public class GenerateCustomEventData implements Runnable {

    private static final String TAG = GenerateCustomEventData.class.getSimpleName();
    private TrackEvent mTrackEvent;

    public GenerateCustomEventData(TrackEvent trackEvent) {
        mTrackEvent = trackEvent;
    }

    @Override
    public void run() {
        if (null == mTrackEvent) {
            ATLog.e(TAG, "GenerateCustomEventData run() -> mTrackEvent is null!");
            return;
        }

        if (mTrackEvent.getEventType() != Constant.EVENT_TYPE_CUSTOM) {
            ATLog.e(TAG, "not custom event, no need to cast to the CustomEventData");
            return;
        }

        CustomEventData customEventData = new CustomEventData();
        customEventData.setEventType(Constant.EVENT_TYPE_CUSTOM);
        customEventData.setLabel(mTrackEvent.getLabel());
        customEventData.setUserId(ApexCache.getInstance().getUserId());
        customEventData.setCountry(ApexCache.getInstance().getCountry());
        customEventData.setProvince(ApexCache.getInstance().getProvince());
        customEventData.setCity(ApexCache.getInstance().getCity());
        customEventData.setTimeStamp(System.currentTimeMillis());

        Map<String, String> customEventDataValue = GsonUtils.json2StringMap(mTrackEvent.getValue());
        customEventData.setValue(customEventDataValue);

        TrackEvent trackEvent = new TrackEvent.EventBuilder()
                .setMode(mTrackEvent.getMode())
                .setEventType(mTrackEvent.getEventType())
                .setLabel(mTrackEvent.getLabel())
                .setValue(GsonUtils.toJsonStr(customEventData))
                .build();

        switch (mTrackEvent.getMode()) {
            case Constant.MODE_DELAY:
                DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
                if (null == dbDao) {
                    ATLog.e(TAG, "track() -> dbDao is null!");
                    return;
                }

                dbDao.insert(DbConstant.TABLE_DELAY_REPORT, trackEvent, System.currentTimeMillis());
                break;
            case Constant.MODE_INSTANT:
                TaskController.getInstance().submit(new InstantEvent(trackEvent));
                break;
            default:
                break;
        }

    }

}
