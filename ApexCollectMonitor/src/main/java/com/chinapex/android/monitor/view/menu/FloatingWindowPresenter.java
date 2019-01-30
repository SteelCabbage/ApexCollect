package com.chinapex.android.monitor.view.menu;

import com.chinapex.android.monitor.callback.IMonitorCallback;
import com.chinapex.android.monitor.global.MonitorCache;
import com.chinapex.android.monitor.utils.MLog;
import com.chinapex.android.monitor.view.FloatingViewManager;
import com.chinapex.android.monitor.view.IFloatingView;

/**
 * @author wyhusky
 * @date 2018/12/27
 */
public class FloatingWindowPresenter implements FloatingWindowContract.Presenter {

    private static final String TAG = FloatingWindowPresenter.class.getSimpleName();
    private final FloatingWindowContract.View menuView;

    public FloatingWindowPresenter(FloatingWindowContract.View view) {
        this.menuView = view;
    }

    @Override
    public void switchMonitorMode(boolean isChecked) {
        if (isChecked) {
            menuView.closeContrastModeIfOpened();
        }

        IMonitorCallback iMonitorCallback = MonitorCache.getInstance().getIMonitorCallback();
        if (null == iMonitorCallback) {
            MLog.e(TAG, "switchMonitor() -> iMonitorCallback is null!");
            return;
        }

        if (isChecked) {
            MLog.d(TAG, "switchMonitor on");
        } else {
            MLog.d(TAG, "switchMonitor off");
        }

        iMonitorCallback.isMonitor(isChecked, 0);
    }

    @Override
    public void switchContrastMode(boolean isChecked) {
        if (isChecked) {
            menuView.closeMonitorModeIfOpened();
        }
        menuView.showChartOption(isChecked);

        IMonitorCallback iMonitorCallback = MonitorCache.getInstance().getIMonitorCallback();
        if (null == iMonitorCallback) {
            MLog.e(TAG, "switchHeatMap() -> iMonitorCallback is null!");
            return;
        }

        if (isChecked) {
            MLog.d(TAG, "switchHeatMap on");
        } else {
            MLog.d(TAG, "switchHeatMap off");
        }
        iMonitorCallback.isMonitor(isChecked, 1);
    }

    @Override
    public void openDefinePage() {
        FloatingViewManager.getInstance().showFloatingView(IFloatingView.WINDOW_DEFINE_PV_PAGE);
    }

    @Override
    public void openContrastDataPage() {
        FloatingViewManager.getInstance().showFloatingView(IFloatingView.WINDOW_CONTRAST_DATA);
    }

    @Override
    public void openUploadEventPage() {
        FloatingViewManager.getInstance().showFloatingView(IFloatingView.WINDOW_UPLOAD);
    }

    @Override
    public void close() {
        menuView.transformMenu();
    }

    @Override
    public void init() {

    }
}
