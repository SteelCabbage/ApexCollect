package com.chinapex.android.datacollect.aop;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.chinapex.android.datacollect.R;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.utils.ATLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SteelCabbage
 * @date 2018/11/05
 */
public class AssembleXpath {

    private static final String TAG = AssembleXpath.class.getSimpleName();

    /**
     * @param context
     * @param childView
     * @return 长度至少为2, 0:viewPath, 1:activityName, 2+:fragmentName
     */
    public static ArrayList<String> getPathContainTag(Context context, View childView) {
        ArrayList<String> viewPath = new ArrayList<>();
        if (null == context || null == childView) {
            ATLog.e(TAG, "getPathContainTag() -> context or childView is null!");
            return viewPath;
        }

        StringBuilder builder = new StringBuilder();
        String viewType = childView.getClass().getSimpleName();
        View parentView = childView;
        int index;
        // 遍历view获取父view来进行拼接
        do {
            // 在父节点下，同类兄弟节点中的index
            index = getBrotherIndex(childView);
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
            String fragmentTag = getFragmentTag(childView);
            if (!TextUtils.isEmpty(fragmentTag)) {
                builder.insert(0, "##/");
                builder.insert(0, fragmentTag);
                builder.insert(0, "/##");
                viewPath.add(fragmentTag);
            } else {
                builder.insert(0, "/");
            }

            try {
                parentView = (ViewGroup) parentView.getParent();
            } catch (Exception e) {
                ATLog.e(TAG, "getPathContainTag() -> cast ViewGroup Exception:" + e.getMessage());
                break;
            }

            if (null == parentView) {
                ATLog.e(TAG, "getPathContainTag() -> parentView is null!");
                break;
            }

            viewType = parentView.getClass().getSimpleName();
            childView = parentView;
        } while (parentView.getParent() instanceof View);

        builder.insert(0, getResourceId(context, childView.getId()));
        builder.insert(0, viewType);

        StringBuilder contentViewBuilder = getContentViewBuilder(builder.toString());
        if (null == contentViewBuilder) {
            ATLog.e(TAG, "getPathContainTag() -> contentViewBuilder is null!");
            return viewPath;
        }

        contentViewBuilder.insert(0, "##");
        String activityName = getActivityName(childView);
        contentViewBuilder.insert(0, activityName);
        contentViewBuilder.insert(0, "/##");

        viewPath.add(0, activityName);
        viewPath.add(0, contentViewBuilder.toString());
        return viewPath;
    }

    private static StringBuilder getContentViewBuilder(String fullPath) {
        if (TextUtils.isEmpty(fullPath)) {
            ATLog.e(TAG, "getContentViewBuilder() -> fullPath is null or empty!");
            return null;
        }

        ATLog.d(TAG, "getContentViewBuilder() -> fullPath:" + fullPath);

        String[] split = fullPath.split(Constant.SEPARATOR_SYSTEM_PATH);
        if (split.length < Constant.VIEW_PATH_SIZE) {
            ATLog.e(TAG, "getContentViewBuilder() -> split length is illegal!");
            return new StringBuilder(fullPath);
        }

        String contentViewPath = split[1];
        if (TextUtils.isEmpty(contentViewPath)) {
            ATLog.e(TAG, "getContentViewBuilder() -> contentViewPath is null or empty!");
            return new StringBuilder(fullPath);
        }

        ATLog.d(TAG, "getContentViewBuilder() -> contentViewPath:" + contentViewPath);
        return new StringBuilder(contentViewPath);
    }

    private static String getFragmentTag(View childView) {
        if (null == childView) {
            return "";
        }

        Object tag = childView.getTag(R.id.apex_data_collect_fragment_name);
        if (null == tag) {
            return "";
        }

        return ((String) tag);
    }

    public static void setFragmentTag(String fragmentName, View rootView) {
        if (TextUtils.isEmpty(fragmentName) || null == rootView) {
            ATLog.e(TAG, "setFragmentTag() -> fragmentName or rootView is null or empty!");
            return;
        }

        rootView.setTag(R.id.apex_data_collect_fragment_name, fragmentName);
    }

    private static int getBrotherIndex(View view) {
        if (null == view) {
            ATLog.e(TAG, "getBrotherIndex() -> view is null!");
            return -1;
        }

        String viewType = view.getClass().getSimpleName();
        ViewGroup viewParent = null;
        try {
            viewParent = (ViewGroup) view.getParent();
        } catch (Exception e) {
            ATLog.e(TAG, "getBrotherIndex() -> view parent cast viewGroup is null!");
        }

        if (TextUtils.isEmpty(viewType) || null == viewParent) {
            ATLog.e(TAG, "getBrotherIndex() -> viewType or viewParent is null!");
            return -1;
        }

        ArrayList<Integer> brotherArray = new ArrayList<>();
        for (int i = 0; i < viewParent.getChildCount(); i++) {
            View childAt = viewParent.getChildAt(i);
            if (null == childAt) {
                ATLog.e(TAG, "getBrotherIndex() -> childAt is null!");
                continue;
            }

            String childAtViewType = childAt.getClass().getSimpleName();
            if (TextUtils.isEmpty(childAtViewType)) {
                ATLog.e(TAG, "getBrotherIndex() -> childAtViewType is null!");
                continue;
            }

            if (viewType.equals(childAtViewType)) {
                brotherArray.add(childAt.hashCode());
            }
        }

        int brotherIndex = brotherArray.indexOf(view.hashCode());
        brotherArray.clear();
        return brotherIndex;
    }

    public static String getMenuPath(Context context, MenuItem menuItem) {
        StringBuilder builder = new StringBuilder();
        SearchView childView = (SearchView) MenuItemCompat.getActionView(menuItem);
        String viewType = childView.getClass().getSimpleName();

        builder.insert(0, getResourceId(context, menuItem.getGroupId()));
        builder.insert(0, "]");
        builder.insert(0, getResourceId(context, menuItem.getItemId()));
        builder.insert(0, "[");
        builder.insert(0, viewType);

        return builder.toString();
    }

    private static String getResourceId(Context context, int viewId) {
        String resourceName = "";
        try {
            resourceName = context.getResources().getResourceEntryName(viewId);
            resourceName = "#" + resourceName;
        } catch (Exception e) {
//            ATLog.v(TAG, "getResourceId() -> Exception:" + e.getMessage());
        }
        return resourceName;
    }

    public static String getActivityName(View view) {
        if (null == view) {
            ATLog.e(TAG, "getActivityName() -> view is null!");
            return "";
        }

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

        return (runningTaskInfos.get(0).topActivity).getClassName();
    }

}
