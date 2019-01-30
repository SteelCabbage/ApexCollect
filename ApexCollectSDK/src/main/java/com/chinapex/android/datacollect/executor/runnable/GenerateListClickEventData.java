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

import java.util.Map;

/**
 * @author SteelCabbage
 * @date 2018/11/25
 */
public class GenerateListClickEventData implements Runnable {

    private static final String TAG = GenerateListClickEventData.class.getSimpleName();
    private View mView;
    private String mListId;
    private String mViewPath;
    private String mPageClassName;

    public GenerateListClickEventData(View view, String listId, String viewPath, String pageClassName) {
        mView = view;
        mListId = listId;
        mViewPath = viewPath;
        mPageClassName = pageClassName;
    }

    @Override
    public void run() {
        if (null == mView || TextUtils.isEmpty(mListId) || TextUtils.isEmpty(mViewPath) || TextUtils.isEmpty(mPageClassName)) {
            ATLog.e(TAG, "GenerateClickEventData run() -> mView or mListId or mViewPath or mPageClassName is null or empty!");
            return;
        }

        String listIdMD5 = MD5Utils.getMD5(mListId);
        String viewPathMD5 = MD5Utils.getMD5(mViewPath);

        if (TextUtils.isEmpty(listIdMD5) || TextUtils.isEmpty(viewPathMD5)) {
            ATLog.e(TAG, "GenerateClickEventData run() -> listIdMD5 or viewPathMD5 is null or empty!");
            return;
        }

        ATLog.d(TAG, "listIdMD5:" + listIdMD5);
        ATLog.d(TAG, "viewPathMD5:" + viewPathMD5);

        UpdateConfigResponse.DataBean.Config.ListBean configListBean = ConfigUtils.getConfigListBean(listIdMD5);
        Map<String, String> itemAliases = getItemAliases(configListBean);

        ClickEventData.ValueBean valueBean = new ClickEventData.ValueBean();
        valueBean.setViewPath(mViewPath);
        valueBean.setMd5(viewPathMD5);
        valueBean.setPageClassName(mPageClassName);

        if (null == configListBean || TextUtils.isEmpty(configListBean.getDefinedPage())) {
            valueBean.setDefinedPage(mPageClassName);
        } else {
            valueBean.setDefinedPage(configListBean.getDefinedPage());
        }

        if (null == itemAliases || TextUtils.isEmpty(itemAliases.get(viewPathMD5))) {
            valueBean.setAlias(Constant.EVENT_LABEL_LIST_CLICK);
        } else {
            valueBean.setAlias(itemAliases.get(viewPathMD5));
        }

        // TODO: 2019/1/24 0024 列表条目的preMD5到底是否有必要?
        valueBean.setPreMD5("");

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

    private Map<String, String> getItemAliases(UpdateConfigResponse.DataBean.Config.ListBean listBean) {
        if (null == listBean) {
            ATLog.e(TAG, "GenerateListClickEventData getItemAliases() -> listBean is null!");
            return null;
        }

        return listBean.getItemAliases();
    }
}
