package com.chinapex.android.datacollect.executor.runnable;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.bean.event.ClickEventData;
import com.chinapex.android.datacollect.model.bean.response.UpdateConfigResponse;
import com.chinapex.android.datacollect.model.db.DbConstant;
import com.chinapex.android.datacollect.model.db.DbDao;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.ConfigUtils;
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
        if (null == mView || TextUtils.isEmpty(mViewPath) || TextUtils.isEmpty(mPageClassName)) {
            ATLog.e(TAG, "GenerateClickEventData run() -> mView or mViewPath or mPageClassName is null or empty!");
            return;
        }

        String viewPathMD5 = MD5Utils.getMD5(mViewPath);
        ATLog.d(TAG, "viewPathMD5:" + viewPathMD5);
        if (TextUtils.isEmpty(viewPathMD5)) {
            ATLog.e(TAG, "GenerateClickEventData run() -> viewPathMD5 is null or empty!");
            return;
        }

        UpdateConfigResponse.DataBean.Config.ClickBean clickBean = ConfigUtils.getConfigClickBean(viewPathMD5);

        ClickEventData.ValueBean valueBean = new ClickEventData.ValueBean();
        valueBean.setViewPath(mViewPath);
        valueBean.setMd5(viewPathMD5);
        valueBean.setPageClassName(mPageClassName);

        if (null == clickBean) {
            valueBean.setDefinedPage(mPageClassName);
            valueBean.setAlias(Constant.EVENT_LABEL_CLICK);
            valueBean.setPreMD5("");
        } else {
            valueBean.setDefinedPage(TextUtils.isEmpty(clickBean.getDefinedPage()) ? mPageClassName : clickBean.getDefinedPage());
            valueBean.setAlias(TextUtils.isEmpty(clickBean.getAlias()) ? Constant.EVENT_LABEL_CLICK : clickBean.getAlias());
            valueBean.setPreMD5(TextUtils.isEmpty(clickBean.getPreMD5()) ? "" : clickBean.getPreMD5());
        }

        valueBean.setDefinedPage(null == clickBean ? mPageClassName : clickBean.getDefinedPage());
        valueBean.setAlias(null == clickBean ? Constant.EVENT_LABEL_CLICK : clickBean.getAlias());
        valueBean.setPreMD5(null == clickBean ? "" : clickBean.getPreMD5());

        // view的内容(TextView, Button ...)
        if (mView instanceof TextView) {
            CharSequence text = ((TextView) mView).getText();
            valueBean.setContent(String.valueOf(text));
        } else {
            valueBean.setContent("");
        }

        ClickEventData clickEventData = new ClickEventData();
        clickEventData.setEventType(Constant.EVENT_TYPE_CLICK);
        clickEventData.setLabel(Constant.EVENT_LABEL_CLICK);
        clickEventData.setUserId(ApexCache.getInstance().getUserId());
        clickEventData.setCountry(ApexCache.getInstance().getCountry());
        clickEventData.setProvince(ApexCache.getInstance().getProvince());
        clickEventData.setCity(ApexCache.getInstance().getCity());
        clickEventData.setTimeStamp(System.currentTimeMillis());
        clickEventData.setValue(valueBean);

        TrackEvent trackEvent = new TrackEvent.EventBuilder()
                .setMode(Constant.MODE_DELAY)
                .setEventType(Constant.EVENT_TYPE_CLICK)
                .setLabel(Constant.EVENT_LABEL_CLICK)
                .setValue(GsonUtils.toJsonStr(clickEventData))
                .build();

        DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
        if (null == dbDao) {
            ATLog.e(TAG, "GenerateClickEventData run() -> dbDao is null!");
            return;
        }

        dbDao.insert(DbConstant.TABLE_DELAY_REPORT, trackEvent, System.currentTimeMillis());
    }

}
