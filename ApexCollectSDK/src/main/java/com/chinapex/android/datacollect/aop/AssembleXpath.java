package com.chinapex.android.datacollect.aop;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.chinapex.android.datacollect.utils.ATLog;

import java.util.List;

/**
 * @author SteelCabbage
 * @date 2018/11/05
 */
public class AssembleXpath {

    private static final String TAG = AssembleXpath.class.getSimpleName();

    public static String getPath(Context context, View childView) {
        StringBuilder builder = new StringBuilder();
        String viewType = childView.getClass().getSimpleName();
        View parentView = childView;
        int index;
        // 遍历view获取父view来进行拼接
        do {
            int id = childView.getId();
            index = ((ViewGroup) childView.getParent()).indexOfChild(childView);
            // 根据从属于不同的类进行index判断
            if (childView.getParent() instanceof RecyclerView) {
                index = ((RecyclerView) childView.getParent()).getChildAdapterPosition(childView);
            } else if (childView.getParent() instanceof AdapterView) {
                index = ((AdapterView) childView.getParent()).getPositionForView(childView);
            } else if (childView.getParent() instanceof ViewPager) {
                index = ((ViewPager) childView.getParent()).getCurrentItem();
            }

            builder.insert(0, getResourceId(context, childView.getId()));
            builder.insert(0, "]");
            builder.insert(0, index);
            builder.insert(0, "[");
            builder.insert(0, viewType);

            parentView = (ViewGroup) parentView.getParent();
            viewType = parentView.getClass().getSimpleName();
            childView = parentView;
            builder.insert(0, "/");
        } while (parentView.getParent() instanceof View);

        builder.insert(0, getResourceId(context, childView.getId()));
        builder.insert(0, viewType);
        return builder.toString();
    }

    private static String getResourceId(Context context, int viewId) {
        String resourceName = "";
        try {
            resourceName = context.getResources().getResourceEntryName(viewId);
            resourceName = "#" + resourceName;
        } catch (Exception e) {
            ATLog.e(TAG, "getResourceId() -> Exception:" + e.getMessage());
        }
        return resourceName;
    }

    public static String getActivityName(View view) {
        Context context = view.getContext();
        if (context instanceof Activity) {
            //context本身是Activity的实例
            return context.getClass().getCanonicalName();
        } else if (context instanceof ContextWrapper) {
            //Activity有可能被系统＂装饰＂，看看context.base是不是Activity
            Activity activity = getActivityFromContextWrapper(context);
            if (null == activity) {
                //如果从view.getContext()拿不到Activity的信息（比如view的context是Application）,则返回当前栈顶Activity的名字
                return getTopActivity(context);
            }

            return activity.getClass().getCanonicalName();
        }

        return "";
    }

    private static Activity getActivityFromContextWrapper(Context context) {
        context = ((ContextWrapper) context).getBaseContext();
        if (context instanceof Activity) {
            //context本身是Activity的实例
            return (Activity) context;
        } else {
            return null;
        }
    }

    private static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (null == runningTaskInfos) {
            ATLog.e(TAG, "getTopActivity() -> runningTaskInfos is null!");
            return "";
        }

        return (runningTaskInfos.get(0).topActivity).getShortClassName();
    }

    public static String getReference(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (null == runningTaskInfos) {
            ATLog.e(TAG, "getReference() -> runningTaskInfos is null!");
            return "";
        }

        return (runningTaskInfos.get(0).topActivity).getClassName();
    }
}
