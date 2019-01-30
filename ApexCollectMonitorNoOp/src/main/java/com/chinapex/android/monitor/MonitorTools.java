package com.chinapex.android.monitor;

import android.content.Context;

import com.chinapex.android.monitor.bean.ViewAttrs;
import com.chinapex.android.monitor.callback.IMonitorCallback;
import com.chinapex.android.monitor.utils.MLog;

import java.util.Map;
import java.util.Stack;

/**
 * @author SteelCabbage
 * @date 2018/12/14
 */
public class MonitorTools {

    private static final String TAG = MonitorTools.class.getSimpleName();

    /**
     * 开启悬浮窗
     *
     * @param context          Application的上下文
     * @param iMonitorCallback 圈选模式的回调
     * @param top              指向栈顶Activity顶指针
     */
    public static void showFloatingWindow(Context context, IMonitorCallback iMonitorCallback,
                                          Map<Integer, Stack<Map<String, Boolean>>> tasks,
                                          Map<Integer, Integer> top, int[] foregroundTask) {
        MLog.d(TAG, "showFloatingWindow: NO OP!");
    }

    /**
     * 关闭悬浮窗，比如回到桌面
     */
    public static void dismissFloatingWindow() {
        MLog.d(TAG, "dismissFloatingWindow: NO OP!!");
    }

    /**
     * 展示控制台, 热力图(type), 属性等
     *
     * @param type
     */
    public static void showPanelView(int type, ViewAttrs viewAttrs) {
        MLog.d(TAG, "showPanelView: NO OP!");
    }

    /**
     * 显示点击事件定义页面
     *
     * @param viewPath  控件路径
     * @param pageClass 页面路径
     */
    public static void showClickDefinePage(String viewPath, String pageClass) {
        MLog.d(TAG, "showClickDefinePage: NO OP!");
    }

    /**
     * 显示列表点击事件的定义页面
     *
     * @param viewPath  控件路径
     * @param pageClass 页面路径
     * @param itemPath  列表条目路径
     */
    public static void showListItemClickDefinePage(String viewPath, String pageClass, String itemPath) {
        MLog.d(TAG, "showListItemClickDefinePage: NO OP!");

    }

    /**
     * 选择点击项进行对比
     *
     * @param viewPath 点击项的view path
     */
    public static boolean addContrastItem(String viewPath) {
        MLog.d(TAG, "addContrastItem: NO OP!");
        return false;
    }
}
