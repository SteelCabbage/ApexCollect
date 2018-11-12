package com.chinapex.android.datacollect.aop;

import android.view.View;

/**
 * @author SteelCabbage
 * @date 2018/11/05
 */
public class AopHelper {
    private static final String TAG = AopHelper.class.getSimpleName();

    private static boolean isMonitor = false;

    public static boolean onClick(View view) {
//        String path = AssembleXpath.getPath(AopSDKApp.getInstance(), view);
//        String activityName = AssembleXpath.getActivityName(view);
//        path = activityName + ":onClick:" + path;
//        AtLog.i(TAG, path);
//        System.out.println("===================我是钢铁大白菜=============");
//        AtLog.i(TAG,"++++++++++++++++++我是钢铁大白菜+++++++++++++++++++");

        System.out.println("根据View生成xpath");
        if (isMonitor) {
            System.out.println("圈选模式开启，不走原有逻辑======记录并加入配置文件中，待后续上传");
        } else {
            System.out.println("圈选模式关闭，原有逻辑执行======正常上传埋点事件");
        }

        return isMonitor;
    }
}
