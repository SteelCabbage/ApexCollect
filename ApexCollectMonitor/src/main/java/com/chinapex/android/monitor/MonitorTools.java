package com.chinapex.android.monitor;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.chinapex.android.monitor.callback.IMonitorCallback;
import com.chinapex.android.monitor.executor.TaskController;
import com.chinapex.android.monitor.global.MonitorCache;
import com.chinapex.android.monitor.utils.MLog;
import com.chinapex.android.monitor.view.FloatingViewManager;
import com.chinapex.android.monitor.view.IFloatingView;
import com.chinapex.android.monitor.view.event.DefineEventView;

import java.util.Map;
import java.util.Stack;

/**
 * @author SteelCabbage
 * @date 2018/12/14
 */
public class MonitorTools {

    private static final String TAG = MonitorTools.class.getSimpleName();

    private MonitorTools() {

    }

    private static class MonitorToolsHolder {
        private static final MonitorTools MONITOR_TOOLS = new MonitorTools();
    }

    public static MonitorTools getInstance() {
        return MonitorToolsHolder.MONITOR_TOOLS;
    }

    public static boolean isInit = false;

    public static void init() {
        if (isInit) {
            MLog.i(TAG, "init() -> TaskController has already been initialized !");
            return;
        }
        TaskController.getInstance().doInit();
        MonitorCache.getInstance().getExistData();
        isInit = true;
    }

    /**
     * 开启悬浮窗
     *
     * @param context          Application的上下文
     * @param iMonitorCallback 圈选模式的回调
     * @param tasks Activity和Fragment的任务栈
     * @param top 指向栈顶Activity顶指针
     * @param foregroundTask 指向前台任务栈
     */
    public static void showFloatingWindow(Context context, IMonitorCallback iMonitorCallback,
                                          Map<Integer, Stack<Map<String, Boolean>>> tasks,
                                          Map<Integer, Integer> top, int[] foregroundTask) {
        MLog.d(TAG, "showFloatingWindow: DEBUG!");
        if (null == context || null == iMonitorCallback) {
            MLog.e(TAG, "context or iMonitorCallback is null!");
            return;
        }

        MonitorCache.getInstance().setContext(context);
        MonitorCache.getInstance().setIMonitorCallback(iMonitorCallback);
        MonitorCache.getInstance().setTasks(tasks);
        MonitorCache.getInstance().setTop(top);
        MonitorCache.getInstance().setForegroundTask(foregroundTask);

        getInstance().showWindow();
        init();
    }

    /**
     * 关闭悬浮窗，比如回到桌面
     */
    public static void dismissFloatingWindow() {
        MLog.d(TAG, "dismissFloatingWindow: DEBUG!");
        getInstance().dismissWindow();
    }

    /**
     * 显示点击事件定义页面
     *
     * @param viewPath  控件路径
     * @param pageClass 页面路径
     */
    public static void showClickDefinePage(String viewPath, String pageClass) {
        FloatingViewManager.getInstance().showFloatingView(IFloatingView.WINDOW_DEFINE_CLICK_PAGE);
        DefineEventView defineEventView = (DefineEventView) FloatingViewManager.getInstance()
                .getFloatingView(IFloatingView.WINDOW_DEFINE_CLICK_PAGE);
        if (null == defineEventView) {
            MLog.d(TAG, "showDefinePage()-> can not get DefineEventView");
            return;
        }
        defineEventView.loadClickData(viewPath, pageClass);
    }

    /**
     * 显示列表点击事件的定义页面
     *
     * @param viewPath  控件路径
     * @param pageClass 页面路径
     * @param itemPath  列表条目路径
     */
    public static void showListItemClickDefinePage(String viewPath, String pageClass, String itemPath) {
        FloatingViewManager.getInstance().showFloatingView(IFloatingView.WINDOW_DEFINE_LIST_PAGE);
        DefineEventView defineEventView = (DefineEventView) FloatingViewManager.getInstance()
                .getFloatingView(IFloatingView.WINDOW_DEFINE_LIST_PAGE);
        if (null == defineEventView) {
            MLog.e(TAG, "showListItemClickDefinePage()-> can not get DefineEventView");
            return;
        }
        defineEventView.loadListItemClickData(viewPath, pageClass, itemPath);
    }

    /**
     * 选择点击项进行对比
     *
     * @param viewPath 点击项的view path
     */
    public static boolean addContrastItem(String viewPath) {
        return MonitorCache.getInstance().addContrastItem(viewPath);
    }

    private boolean showWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(MonitorCache.getInstance().getContext())) {
                requestPermission(MonitorCache.getInstance().getContext());
                Toast.makeText(MonitorCache.getInstance().getContext(), "showWindow() -> unauthorized", Toast.LENGTH_LONG).show();
                MLog.w(TAG, "showWindow() -> unauthorized");
                return false;
            }
        }

        FloatingViewManager.getInstance().showFloatingView(IFloatingView.WINDOW_FLOATING);
        FloatingViewManager.getInstance().showAll();

        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission(Context context) {
        if (null == context) {
            MLog.e(TAG, "requestPermission() -> context is null!");
            return;
        }

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void dismissWindow() {
        FloatingViewManager.getInstance().hideAll();
    }
}
