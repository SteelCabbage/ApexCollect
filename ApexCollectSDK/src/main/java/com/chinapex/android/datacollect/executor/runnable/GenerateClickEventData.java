package com.chinapex.android.datacollect.executor.runnable;

import android.text.TextUtils;
import android.view.View;

import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.bean.event.ClickEventData;
import com.chinapex.android.datacollect.model.db.DbConstant;
import com.chinapex.android.datacollect.model.db.DbDao;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.GsonUtils;
import com.chinapex.android.datacollect.utils.MD5Utils;

/**
 * @author SteelCabbage
 * @date 2018/11/25
 */
public class GenerateClickEventData implements Runnable {

    private static final String TAG = GenerateClickEventData.class.getSimpleName();
    private View mView;
    private String mViewPath;
    private String mPageClassName;

    public GenerateClickEventData(View view, String viewPath, String pageClassName) {
        mView = view;
        mViewPath = viewPath;
        mPageClassName = pageClassName;
    }

    @Override
    public void run() {
        if (null == mView || TextUtils.isEmpty(mPageClassName) || TextUtils.isEmpty(mViewPath)) {
            ATLog.e(TAG, "GenerateClickEventData run() -> mView or mPageClassName or mViewPath is null!");
            return;
        }

        ClickEventData.ValueBean valueBean = new ClickEventData.ValueBean();
        valueBean.setPageClassName(mPageClassName);
        valueBean.setTimeStamp(System.currentTimeMillis());
        valueBean.setViewPath(mViewPath);
        String viewPathMD5 = MD5Utils.getMD5(mViewPath);
        valueBean.setViewPathMD5(viewPathMD5);
//        valueBean.setContent();
//        valueBean.setFrame();
        valueBean.setAlpha(mView.getAlpha());
//        valueBean.setInvocation();

        ClickEventData clickEventData = new ClickEventData();
        clickEventData.setEventType(Constant.EVENT_TYPE_CLICK);
        clickEventData.setLabel(Constant.EVENT_LABEL_CLICK);
        clickEventData.setUserId(ApexCache.getInstance().getUserId());
        clickEventData.setCountry(ApexCache.getInstance().getCountry());
        clickEventData.setProvince(ApexCache.getInstance().getProvince());
        clickEventData.setCity(ApexCache.getInstance().getCity());
        clickEventData.setValue(valueBean);

        TrackEvent trackEvent = new TrackEvent.EventBuilder()
                .setMode(Constant.MODE_DELAY)
                .setEventType(Constant.EVENT_TYPE_CLICK)
                .setLabel(Constant.EVENT_LABEL_CLICK)
                .setValue(GsonUtils.toJsonStr(clickEventData))
                .build();

        DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
        if (null == dbDao) {
            ATLog.e(TAG, "track() -> dbDao is null!");
            return;
        }

        dbDao.insert(DbConstant.TABLE_DELAY_REPORT, trackEvent, System.currentTimeMillis());
    }
}
