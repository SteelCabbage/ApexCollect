package com.chinapex.android.datacollect.executor.runnable;

import android.text.TextUtils;
import android.widget.AbsListView;

import com.chinapex.android.datacollect.R;
import com.chinapex.android.datacollect.aop.AssembleXpath;
import com.chinapex.android.datacollect.changelistener.AnalyticsListenerController;
import com.chinapex.android.datacollect.changelistener.OnListPvEventsChangeListener;
import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.event.ListEventData;
import com.chinapex.android.datacollect.model.bean.response.UpdateConfigResponse;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.ConfigUtils;
import com.chinapex.android.datacollect.utils.MD5Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SteelCabbage
 * @date 2019/01/04
 */
public class ListPvEventsListener implements Runnable, OnListPvEventsChangeListener {

    private static final String TAG = ListPvEventsListener.class.getSimpleName();
    private static final int MAX_LISTEN_ON_SCROLL = 2;

    private boolean isNeedParseConfig = true;
    private boolean isListenOnScroll = true;
    private int mCounter = 0;
    private Map<String, Map<String, ListEventData.ValueBean.ListItem>> mExposures;
    private Map<String, Map<String, Boolean>> mItemShowStates;
    private Map<String, UpdateConfigResponse.DataBean.Config.ListBean> mListConfigs;
    private String mPageName;

    public ListPvEventsListener(String pageName) {
        mPageName = pageName;
        mExposures = new HashMap<>();
        mItemShowStates = new HashMap<>();
        mListConfigs = new HashMap<>();
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mPageName)) {
            ATLog.e(TAG, "ListPvEventsListener run() -> mPageName is null!");
            return;
        }

        // 添加该页面的列表监听
        AnalyticsListenerController.getInstance().addOnListPvEventsChangeListener(this);
    }

    @Override
    public void onPageExit(String pageName) {
        // 解析该页面的配置文件
        parseConfig();

        if (!mPageName.equals(pageName)) {
            ATLog.e(TAG, "onPageExit() -> not this page:" + mPageName + " exit!");
            return;
        }

        // 移除该页面的列表监听
        AnalyticsListenerController.getInstance().removeOnListPvEventsChangeListener(this);

        // 生成该页面的列表曝光事件
        generateListEvents();
    }

    private void generateListEvents() {
        if (null == mListConfigs || mListConfigs.isEmpty()) {
            ATLog.e(TAG, "onPageExit() -> mListConfigs is null or empty!");
            return;
        }

        for (Map.Entry<String, UpdateConfigResponse.DataBean.Config.ListBean> listBeanEntry : mListConfigs.entrySet()) {
            if (null == listBeanEntry) {
                ATLog.e(TAG, "onPageExit() -> listBeanEntry is null!");
                continue;
            }

            String listIdMD5 = listBeanEntry.getKey();
            if (TextUtils.isEmpty(listIdMD5)) {
                ATLog.e(TAG, "onPageExit() -> listIdMD5 is null or empty!");
                continue;
            }

            ATLog.i(TAG, "onPageExit() -> listIdMD5:" + listIdMD5);
            Map<String, ListEventData.ValueBean.ListItem> exposure = mExposures.get(listIdMD5);
            if (null == exposure || exposure.isEmpty()) {
                ATLog.e(TAG, "onPageExit() -> exposure is null!");
                continue;
            }

            UpdateConfigResponse.DataBean.Config.ListBean listBean = listBeanEntry.getValue();
            if (null == listBean) {
                ATLog.e(TAG, "onPageExit() -> listBean is null!");
                continue;
            }

            Map<String, String> itemAliases = listBean.getItemAliases();
            if (null == itemAliases || itemAliases.isEmpty()) {
                ATLog.e(TAG, "onPageExit() -> itemAliases is null or empty!");
                for (Map.Entry<String, ListEventData.ValueBean.ListItem> itemExposure1 : exposure.entrySet()) {
                    if (null == itemExposure1) {
                        ATLog.e(TAG, "onPageExit() -> itemExposure1 is null!");
                        continue;
                    }

                    ListEventData.ValueBean.ListItem listItem1 = itemExposure1.getValue();
                    if (null == listItem1) {
                        ATLog.e(TAG, "onPageExit() -> listItem1 is null!");
                        continue;
                    }

                    listItem1.setAlias("");
                }

                TaskController.getInstance().submit(new GenerateListEventData(mPageName, listIdMD5, exposure));
                continue;
            }

            for (Map.Entry<String, ListEventData.ValueBean.ListItem> itemExposure2 : exposure.entrySet()) {
                if (null == itemExposure2) {
                    ATLog.e(TAG, "onPageExit() -> itemExposure2 is null!");
                    continue;
                }

                String itemMD5 = itemExposure2.getKey();
                if (TextUtils.isEmpty(itemMD5)) {
                    ATLog.e(TAG, "onPageExit() -> itemMD5 is null or empty!");
                    continue;
                }

                ListEventData.ValueBean.ListItem listItem2 = itemExposure2.getValue();
                if (null == listItem2) {
                    ATLog.e(TAG, "onPageExit() -> listItem2 is null!");
                    continue;
                }

                String alias = itemAliases.get(itemMD5);
                if (TextUtils.isEmpty(alias)) {
                    ATLog.v(TAG, "onPageExit() -> this dataKey:" + itemMD5 + "'s alias is null or empty!");
                    listItem2.setAlias("");
                } else {
                    listItem2.setAlias(alias);
                }
            }

            TaskController.getInstance().submit(new GenerateListEventData(mPageName, listIdMD5, exposure));
        }
    }

    @Override
    public void onListIdle(AbsListView absListView) {
        // 解析该页面的配置文件
        parseConfig();

        if (isListenOnScroll) {
            ATLog.i(TAG, "ListPvEventsListener onListIdle() -> no need to listen onScroll!");
            isListenOnScroll = false;
        }

        calculateExposure(absListView);
    }

    @Override
    public void onListScroll(AbsListView absListView) {
        // 解析该页面的配置文件
        parseConfig();

        mCounter++;
        if (mCounter > MAX_LISTEN_ON_SCROLL) {
            ATLog.i(TAG, "ListPvEventsListener onListIdle() -> no need to listen onScroll!");
            isListenOnScroll = false;
            return;
        }

        calculateExposure(absListView);
    }

    private void calculateExposure(AbsListView absListView) {
        if (null == absListView) {
            ATLog.e(TAG, "ListPvEventsListener calculateExposure() -> absListView is null!");
            return;
        }

        ArrayList<String> viewPath = AssembleXpath.getPathContainTag(ApexCache.getInstance().getContext(), absListView);
        if (null == viewPath || viewPath.size() < Constant.VIEW_PATH_SIZE) {
            ATLog.e(TAG, "ListPvEventsListener calculateExposure() -> viewPath is illegal!");
            return;
        }

        String listId = viewPath.get(0);
        ATLog.i(TAG, "calculateExposure() -> listId:" + listId);
        String listIdMD5 = MD5Utils.getMD5(listId);
        if (TextUtils.isEmpty(listIdMD5)) {
            ATLog.e(TAG, "calculateExposure() -> listIdMD5 is null or empty!");
            return;
        }

        Map<String, ListEventData.ValueBean.ListItem> exposure = mExposures.get(listIdMD5);
        Map<String, Boolean> itemShowState = mItemShowStates.get(listIdMD5);
        if (null == exposure || null == itemShowState) {
            ATLog.e(TAG, "exposure or itemShowState is null!");
            return;
        }

        ArrayList<String> currentVisible = new ArrayList<>();

        int firstVisiblePosition = absListView.getFirstVisiblePosition();
        ATLog.i(TAG, "calculateExposure() -> firstVisiblePosition:" + firstVisiblePosition);
        int lastVisiblePosition = absListView.getLastVisiblePosition();
        ATLog.i(TAG, "calculateExposure() -> lastVisiblePosition:" + lastVisiblePosition);
        if (firstVisiblePosition < 0 || lastVisiblePosition < 0) {
            ATLog.e(TAG, "calculateExposure() -> no visible item!");
            return;
        }

        String dataPath = (String) absListView.getTag(R.id.apex_data_collect_list_data_path);
        ATLog.d(TAG, "calculateExposure() -> dataPath:" + dataPath);
        for (int i = firstVisiblePosition; i <= lastVisiblePosition; i++) {
            Object item = absListView.getAdapter().getItem(i);
            String dataKey = ConfigUtils.getDataKey(item, dataPath);
            if (TextUtils.isEmpty(dataKey)) {
                ATLog.e(TAG, "calculateExposure() -> dataKey is null or empty!");
                continue;
            }

            // 将本次可见项加入“当前可见列表”
            currentVisible.add(dataKey);

            // 计算item的MD5 (listId + dataKey)
            String itemMD5 = MD5Utils.getMD5(String.valueOf(listId + Constant.SEPARATOR_LIST_ITEM_GROUP + dataKey));
            if (TextUtils.isEmpty(itemMD5)) {
                ATLog.e(TAG, "calculateExposure() -> itemMD5 is null or empty!");
                continue;
            }

            // 获取每个条目“是否可见”的状态
            Boolean isShow = itemShowState.get(dataKey);
            if (null == isShow) {
                ATLog.v(TAG, "calculateExposure() -> dataKey:" + dataKey + " hasn't been exposed before!");
                itemShowState.put(dataKey, true);
                ListEventData.ValueBean.ListItem itemExposure = exposure.get(dataKey);
                if (null == itemExposure) {
                    ListEventData.ValueBean.ListItem listItem1 = new ListEventData.ValueBean.ListItem();
                    listItem1.setIndex(i + "");
                    listItem1.setDataKey(dataKey);
                    listItem1.setImpressions(1 + "");
                    exposure.put(itemMD5, listItem1);
                    continue;
                }

                String impressions = itemExposure.getImpressions();
                if (null == impressions) {
                    ATLog.e(TAG, "calculateExposure() -> 1: itemExposure's impressions is null!");
                    continue;
                }

                try {
                    Integer integer = Integer.valueOf(impressions);
                    itemExposure.setImpressions(String.valueOf(integer + 1));
                } catch (NumberFormatException e) {
                    ATLog.e(TAG, "calculateExposure() NumberFormatException:" + e.getMessage());
                }
                continue;
            }

            if (Boolean.TRUE.equals(isShow)) {
                ATLog.v(TAG, "calculateExposure() -> dataKey:" + dataKey + " no need to calculate pv");
                continue;
            }

            itemShowState.put(dataKey, true);
            ListEventData.ValueBean.ListItem itemExposure = exposure.get(dataKey);
            if (null == itemExposure) {
                ListEventData.ValueBean.ListItem listItem2 = new ListEventData.ValueBean.ListItem();
                listItem2.setIndex(i + "");
                listItem2.setDataKey(dataKey);
                listItem2.setImpressions(1 + "");
                exposure.put(itemMD5, listItem2);
            } else {
                String impressions = itemExposure.getImpressions();
                if (null == impressions) {
                    ATLog.e(TAG, "calculateExposure() -> 2: itemExposure's impressions is null!");
                    continue;
                }

                try {
                    Integer integer = Integer.valueOf(impressions);
                    itemExposure.setImpressions(String.valueOf(integer + 1));
                } catch (NumberFormatException e) {
                    ATLog.e(TAG, "calculateExposure() NumberFormatException:" + e.getMessage());
                }
            }

        }

        for (Map.Entry<String, Boolean> itemIsShow : itemShowState.entrySet()) {
            String dataKey = itemIsShow.getKey();
            if (TextUtils.isEmpty(dataKey)) {
                ATLog.e(TAG, "itemIsShow is null!");
                continue;
            }

            if (!currentVisible.contains(dataKey)) {
                itemShowState.put(dataKey, false);
            }
        }
    }

    private void parseConfig() {
        if (!isNeedParseConfig) {
            ATLog.w(TAG, "parseConfig() -> has been parsed the config!");
            return;
        }

        Map<String, UpdateConfigResponse.DataBean.Config.ListBean> configList = ApexCache.getInstance().getConfigList();
        if (null == configList || configList.isEmpty()) {
            ATLog.e(TAG, "parseConfig() -> configList is null or empty!");
            return;
        }

        for (Map.Entry<String, UpdateConfigResponse.DataBean.Config.ListBean> listBean : configList.entrySet()) {
            if (null == listBean) {
                ATLog.e(TAG, "parseConfig() -> listBean is null!");
                continue;
            }

            String listIdMD5 = listBean.getKey();
            if (TextUtils.isEmpty(listIdMD5)) {
                ATLog.e(TAG, "parseConfig() -> listIdMD5 is null or empty!");
                continue;
            }

            UpdateConfigResponse.DataBean.Config.ListBean listBeanValue = listBean.getValue();
            if (null == listBeanValue) {
                ATLog.e(TAG, "parseConfig() -> listBeanValue is null!");
                continue;
            }

            String definedPage = listBeanValue.getDefinedPage();
            if (!mPageName.equals(definedPage)) {
                ATLog.v(TAG, "parseConfig() -> this listIdMD5: " + listIdMD5 + " is not belong to the page:" + mPageName);
                continue;
            }

            mListConfigs.put(listIdMD5, listBeanValue);
            mExposures.put(listIdMD5, new HashMap<String, ListEventData.ValueBean.ListItem>());
            mItemShowStates.put(listIdMD5, new HashMap<String, Boolean>());
        }

        isNeedParseConfig = false;
    }

}
