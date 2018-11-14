package com.chinapex.android.datacollect.aop;

import android.view.View;

import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.utils.ATLog;

/**
 * @author SteelCabbage
 * @date 2018/11/05
 */
public class AopHelper {
    private static final String TAG = AopHelper.class.getSimpleName();

    private static boolean isMonitor = false;

    public static boolean onClick(View view) {
        String path = AssembleXpath.getPath(ApexCache.getInstance().getContext(), view);
        String activityName = AssembleXpath.getActivityName(view);
        path = activityName + ":onClick:" + path;
        ATLog.i(TAG, "根据View生成xpath:" + path);

        if (isMonitor) {
            ATLog.d(TAG, "圈选模式开启，不走原有逻辑======记录并加入配置文件中，待后续上传");
        } else {
            ATLog.i(TAG, "圈选模式关闭，原有逻辑执行======正常上传埋点事件");
        }

        return isMonitor;
    }
}