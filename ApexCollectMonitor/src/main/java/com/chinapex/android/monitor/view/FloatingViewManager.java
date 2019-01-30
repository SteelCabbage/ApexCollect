package com.chinapex.android.monitor.view;

import android.content.Context;

import com.chinapex.android.monitor.global.MonitorCache;
import com.chinapex.android.monitor.utils.MLog;
import com.chinapex.android.monitor.view.event.DefineEventView;
import com.chinapex.android.monitor.view.menu.FloatingWindow;
import com.chinapex.android.monitor.view.statistics.StatisticsView;
import com.chinapex.android.monitor.view.upload.UploadView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wyhusky
 * @date 2018/12/27
 */
public class FloatingViewManager {

    private static final String TAG = FloatingViewManager.class.getSimpleName();

    private Map<Integer, IFloatingView> mFloatingViewMap;

    private FloatingViewManager() {
        mFloatingViewMap = new HashMap<>();
    }

    private static final class FloatingViewManagerHolder {
        private static final FloatingViewManager S_FLOATING_VIEW_MANAGER = new FloatingViewManager();
    }

    public static FloatingViewManager getInstance() {
        return FloatingViewManagerHolder.S_FLOATING_VIEW_MANAGER;
    }


    public void showFloatingView(int viewType) {
        IFloatingView iFloatingView;
        if (mFloatingViewMap.containsKey(viewType)) {
            iFloatingView = mFloatingViewMap.get(viewType);
        } else {
            iFloatingView = createView(viewType);
        }
        if (null == iFloatingView) {
            MLog.e(TAG, "showFloatingView()-> error:show floating view failed");
            return;
        }
        iFloatingView.show();
    }

    public void hideFloatingView(int viewType) {
        IFloatingView iFloatingView;
        if (mFloatingViewMap.containsKey(viewType)) {
            iFloatingView = mFloatingViewMap.get(viewType);
            if (null == iFloatingView) {
                MLog.e(TAG, "hideFloatingView()-> hide floating view failed");
                return;
            }
            iFloatingView.hide();
        }
    }

    public void dismissFloatingView(int viewType) {
        IFloatingView iFloatingView;
        if (mFloatingViewMap.containsKey(viewType)) {
            iFloatingView = mFloatingViewMap.get(viewType);
            if (null == iFloatingView) {
                MLog.e(TAG, "dismissFloatingView()-> dismiss floating view failed");
                return;
            }
            iFloatingView.dismiss();
            mFloatingViewMap.remove(viewType);
        }
    }

    public void hideAll() {
        for (IFloatingView iFloatingView : mFloatingViewMap.values()) {
            if (null == iFloatingView) {
                MLog.e(TAG, "hideAll()-> there is a null object in the map");
                continue;
            }
            iFloatingView.hide();
        }
    }

    public void showAll() {
        for (IFloatingView iFloatingView : mFloatingViewMap.values()) {
            if (null == iFloatingView) {
                MLog.e(TAG, "showAll()-> there is a null object in the map");
                continue;
            }
            if (iFloatingView.isHiding()) {
                iFloatingView.show();
            }
        }
    }

    public void dismissAll() {
        for (IFloatingView iFloatingView : mFloatingViewMap.values()) {
            if (null == iFloatingView) {
                MLog.e(TAG, "dismissAll()-> there is a null object in the map");
                continue;
            }
            iFloatingView.dismiss();
        }
        mFloatingViewMap.clear();
    }

    public IFloatingView getFloatingView(int viewType) {
        if (!mFloatingViewMap.containsKey(viewType)) {
            MLog.e(TAG, "getFloatingView()-> mFloatingViewMap do not have this view type");
            return null;
        }
        return mFloatingViewMap.get(viewType);
    }

    private IFloatingView createView(int viewType) {
        Context mContext = MonitorCache.getInstance().getContext();
        if (null == mContext) {
            MLog.e(TAG, "createView()-> error:context=null, create view failed");
            return null;
        }

        IFloatingView iFloatingView;
        switch (viewType) {
            case IFloatingView.WINDOW_FLOATING:
                iFloatingView = new FloatingWindow(mContext);
                break;
            case IFloatingView.WINDOW_DEFINE_PV_PAGE:
                iFloatingView = new DefineEventView(mContext, IFloatingView.WINDOW_DEFINE_PV_PAGE);
                break;
            case IFloatingView.WINDOW_DEFINE_CLICK_PAGE:
                iFloatingView = new DefineEventView(mContext, IFloatingView.WINDOW_DEFINE_CLICK_PAGE);
                break;
            case IFloatingView.WINDOW_DEFINE_LIST_PAGE:
                iFloatingView = new DefineEventView(mContext, IFloatingView.WINDOW_DEFINE_LIST_PAGE);
                break;
            case IFloatingView.WINDOW_CONTRAST_DATA:
                iFloatingView = new StatisticsView(mContext);
                break;
            case IFloatingView.WINDOW_UPLOAD:
                iFloatingView = new UploadView(mContext);
                break;
            default:
                iFloatingView = null;
                MLog.e(TAG, "createView()-> no this view type!");
                break;
        }
        if (null == iFloatingView) {
            MLog.e(TAG, "createView()-> create view failed");
            return null;
        }
        mFloatingViewMap.put(viewType, iFloatingView);
        return iFloatingView;
    }
}
