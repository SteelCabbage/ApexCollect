package com.chinapex.android.datacollect.changelistener;

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
    }

    @Override
    public void onDestroy() {
        if (null == mOnNetworkTypeChangeListeners) {
            ATLog.e(TAG, "onDestroy() -> mOnNetworkTypeChangeListeners is null!");
            return;
        }

        mOnNetworkTypeChangeListeners.clear();
        mOnNetworkTypeChangeListeners = null;
    }

    public void addOnNetworkTypeChangeListener(OnNetworkTypeChangeListener onNetworkTypeChangeListener) {
        if (null == mOnNetworkTypeChangeListeners || null == onNetworkTypeChangeListener) {
            ATLog.e(TAG, "1: mOnNetworkTypeChangeListeners or onNetworkTypeChangeListener is null!");
            return;
        }

        mOnNetworkTypeChangeListeners.add(onNetworkTypeChangeListener);
    }

    public void removeOnNetworkTypeChangeListener(OnNetworkTypeChangeListener onNetworkTypeChangeListener) {
        if (null == mOnNetworkTypeChangeListeners || null == onNetworkTypeChangeListener) {
            ATLog.e(TAG, "0: mOnNetworkTypeChangeListeners or onNetworkTypeChangeListener is null!");
            return;
        }

        mOnNetworkTypeChangeListeners.remove(onNetworkTypeChangeListener);
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

}
