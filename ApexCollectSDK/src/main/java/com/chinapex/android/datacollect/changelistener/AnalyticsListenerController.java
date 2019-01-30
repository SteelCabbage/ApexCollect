package com.chinapex.android.datacollect.changelistener;

import android.widget.AbsListView;

import com.chinapex.android.datacollect.controller.IController;
import com.chinapex.android.datacollect.utils.ATLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SteelCabbage
 * @date 2018/12/03
 */
public class AnalyticsListenerController implements IController {

    private static final String TAG = AnalyticsListenerController.class.getSimpleName();

    private List<OnNetworkTypeChangeListener> mOnNetworkTypeChangeListeners;
    private List<OnListPvEventsChangeListener> mOnListPvEventsChangeListeners;

    private AnalyticsListenerController() {

    }

    private static class ChangeListenerControllerHolder {
        private static final AnalyticsListenerController ANALYTICS_LISTENER_CONTROLLER = new AnalyticsListenerController();
    }

    public static AnalyticsListenerController getInstance() {
        return ChangeListenerControllerHolder.ANALYTICS_LISTENER_CONTROLLER;
    }


    @Override
    public void doInit() {
        mOnNetworkTypeChangeListeners = new ArrayList<>();
        mOnListPvEventsChangeListeners = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        if (null == mOnNetworkTypeChangeListeners) {
            ATLog.e(TAG, "onDestroy() -> mOnNetworkTypeChangeListeners is null!");
            return;
        }

        if (null == mOnListPvEventsChangeListeners) {
            ATLog.e(TAG, "onDestroy() -> mOnListPvEventsChangeListeners is null!");
            return;
        }

        mOnNetworkTypeChangeListeners.clear();
        mOnNetworkTypeChangeListeners = null;

        mOnListPvEventsChangeListeners.clear();
        mOnListPvEventsChangeListeners = null;
    }

    public void addOnNetworkTypeChangeListener(OnNetworkTypeChangeListener onNetworkTypeChangeListener) {
        if (null == mOnNetworkTypeChangeListeners || null == onNetworkTypeChangeListener) {
            ATLog.e(TAG, "1: mOnNetworkTypeChangeListeners or onNetworkTypeChangeListener is null!");
            return;
        }

        mOnNetworkTypeChangeListeners.add(onNetworkTypeChangeListener);
    }

    public void addOnListPvEventsChangeListener(OnListPvEventsChangeListener onListPvEventsChangeListener) {
        if (null == mOnListPvEventsChangeListeners || null == onListPvEventsChangeListener) {
            ATLog.e(TAG, "1: mOnListPvEventsChangeListeners or onListPvEventsChangeListener is null!");
            return;
        }

        mOnListPvEventsChangeListeners.add(onListPvEventsChangeListener);
    }

    public void removeOnNetworkTypeChangeListener(OnNetworkTypeChangeListener onNetworkTypeChangeListener) {
        if (null == mOnNetworkTypeChangeListeners || null == onNetworkTypeChangeListener) {
            ATLog.e(TAG, "0: mOnNetworkTypeChangeListeners or onNetworkTypeChangeListener is null!");
            return;
        }

        mOnNetworkTypeChangeListeners.remove(onNetworkTypeChangeListener);
    }

    public void removeOnListPvEventsChangeListener(OnListPvEventsChangeListener onListPvEventsChangeListener) {
        if (null == mOnListPvEventsChangeListeners || null == onListPvEventsChangeListener) {
            ATLog.e(TAG, "0: mOnListPvEventsChangeListeners or onListPvEventsChangeListener is null!");
            return;
        }

        mOnListPvEventsChangeListeners.remove(onListPvEventsChangeListener);
    }

    public void notifyNetworkTypeChange(String networkType) {
        if (null == mOnNetworkTypeChangeListeners) {
            ATLog.e(TAG, "notifyNetworkTypeChange() -> mOnNetworkTypeChangeListeners is null!");
            return;
        }

        for (OnNetworkTypeChangeListener onNetworkTypeChangeListener : mOnNetworkTypeChangeListeners) {
            if (null == onNetworkTypeChangeListener) {
                ATLog.e(TAG, "notifyNetworkTypeChange() -> onNetworkTypeChangeListener is null!");
                continue;
            }

            onNetworkTypeChangeListener.networkTypeChange(networkType);
        }
    }

    public void notifyListPvEventsOnPageExit(String pageName) {
        if (null == mOnListPvEventsChangeListeners) {
            ATLog.e(TAG, "notifyListPvEventsOnPageExit() -> mOnListPvEventsChangeListeners is null!");
            return;
        }

        for (OnListPvEventsChangeListener onListPvEventsChangeListener : mOnListPvEventsChangeListeners) {
            if (null == onListPvEventsChangeListener) {
                ATLog.e(TAG, "notifyListPvEventsOnPageExit() -> onListPvEventsChangeListener is null!");
                continue;
            }

            onListPvEventsChangeListener.onPageExit(pageName);
        }
    }

    public void notifyListPvEventsOnListIdle(AbsListView absListView) {
        if (null == mOnListPvEventsChangeListeners) {
            ATLog.e(TAG, "notifyListPvEventsOnListIdle() -> mOnListPvEventsChangeListeners is null!");
            return;
        }

        for (OnListPvEventsChangeListener onListPvEventsChangeListener : mOnListPvEventsChangeListeners) {
            if (null == onListPvEventsChangeListener) {
                ATLog.e(TAG, "notifyListPvEventsChangeListener() -> onListPvEventsChangeListener is null!");
                continue;
            }

            onListPvEventsChangeListener.onListIdle(absListView);
        }
    }

    public void notifyListPvEventsOnListScroll(AbsListView absListView) {
        if (null == mOnListPvEventsChangeListeners) {
            ATLog.e(TAG, "notifyListPvEventsOnListScroll() -> mOnListPvEventsChangeListeners is null!");
            return;
        }

        for (OnListPvEventsChangeListener onListPvEventsChangeListener : mOnListPvEventsChangeListeners) {
            if (null == onListPvEventsChangeListener) {
                ATLog.e(TAG, "notifyListPvEventsChangeListener() -> onListPvEventsChangeListener is null!");
                continue;
            }

            onListPvEventsChangeListener.onListScroll(absListView);
        }
    }

}
