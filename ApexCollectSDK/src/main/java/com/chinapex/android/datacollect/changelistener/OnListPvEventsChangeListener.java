package com.chinapex.android.datacollect.changelistener;

import android.widget.AbsListView;

/**
 * @author SteelCabbage
 * @date 2019/01/11
 */
public interface OnListPvEventsChangeListener {

    /**
     * 页面不可见时
     *
     * @param pageName
     */
    void onPageExit(String pageName);

    /**
     * 当列表滑动停止时
     *
     * @param absListView 具体的列表项
     */
    void onListIdle(AbsListView absListView);

    /**
     * 当列表滑动时(结合idle状态，监听列表第一次可见时)
     *
     * @param absListView 具体的列表项
     */
    void onListScroll(AbsListView absListView);

}
