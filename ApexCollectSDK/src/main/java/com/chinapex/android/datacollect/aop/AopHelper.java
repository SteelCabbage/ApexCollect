package com.chinapex.android.datacollect.aop;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.chinapex.android.datacollect.R;
import com.chinapex.android.datacollect.changelistener.AnalyticsListenerController;
import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.runnable.GenerateClickEventData;
import com.chinapex.android.datacollect.executor.runnable.GenerateFragmentNotV4PvEventData;
import com.chinapex.android.datacollect.executor.runnable.GenerateFragmentV4PvEventData;
import com.chinapex.android.datacollect.executor.runnable.GenerateListClickEventData;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.ConfigUtils;
import com.chinapex.android.monitor.MonitorTools;
import com.chinapex.android.monitor.utils.MLog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author SteelCabbage
 * @date 2018/11/05
 */
public class AopHelper {
    private static final String TAG = AopHelper.class.getSimpleName();

    private static final int DEFINE_MODE = 0;
    private static final int CONTRAST_MODE = 1;

    private static boolean mIsMonitor = false;
    private static int mMonitorMode = 0;

    public static void setIsMonitor(boolean isMonitor, int monitorMode) {
        mIsMonitor = isMonitor;
        mMonitorMode = monitorMode;
    }

    public static void v4fragmentOnViewCreated(Fragment fragment, View rootView) {
        if (null == fragment || null == rootView) {
            ATLog.e(TAG, "v4fragmentOnViewCreated() -> v4 fragment or rootView is null!");
            return;
        }

        String fragmentName = fragment.getClass().getCanonicalName();
        AssembleXpath.setFragmentTag(fragmentName, rootView);
    }

    public static void fragmentOnViewCreated(android.app.Fragment fragment, View rootView) {
        if (null == fragment || null == rootView) {
            ATLog.e(TAG, "fragmentOnViewCreated() -> fragment or rootView is null!");
            return;
        }

        String fragmentName = fragment.getClass().getCanonicalName();
        AssembleXpath.setFragmentTag(fragmentName, rootView);
    }

    public static boolean onClick(View view) {
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList<String> viewPath = AssembleXpath.getPathContainTag(ApexCache.getInstance().getContext(), view);
        if (null == viewPath || viewPath.size() < Constant.VIEW_PATH_SIZE) {
            ATLog.e(TAG, "onClick() -> viewPath is illegal!");
            return mIsMonitor;
        }

        String pageClassName = ConfigUtils.getPageClassName(viewPath);
        if (TextUtils.isEmpty(pageClassName)) {
            ATLog.e(TAG, "onClick() -> pageClassName is null or empty!");
            return mIsMonitor;
        }

        if (mIsMonitor) {
            ATLog.d(TAG, "圈选模式开启，不走原有逻辑======记录并加入配置文件中，待后续上传");
            switch (mMonitorMode) {
                case DEFINE_MODE:
                    MonitorTools.showClickDefinePage(viewPath.get(0), pageClassName);
                    break;
                case CONTRAST_MODE:
                    MonitorTools.addContrastItem(viewPath.get(0));
                    break;
                default:
                    break;
            }
        } else {
            ATLog.i(TAG, "圈选模式关闭，原有逻辑执行======正常上传埋点事件");
            TaskController.getInstance().submit(new GenerateClickEventData(view, viewPath.get(0), pageClassName));
        }

        ATLog.e(TAG, "onClick() -> 插桩方法耗时======" + (System.currentTimeMillis() - currentTimeMillis));
        return mIsMonitor;
    }

    public static boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList<String> adapterViewPath = AssembleXpath.getPathContainTag(ApexCache.getInstance().getContext(), parent);
        if (null == adapterViewPath || adapterViewPath.size() < Constant.VIEW_PATH_SIZE) {
            ATLog.e(TAG, "onItemClick() -> adapterViewPath is illegal!");
            return mIsMonitor;
        }

        String listId = adapterViewPath.get(0);
        if (TextUtils.isEmpty(listId)) {
            ATLog.e(TAG, "onItemClick() -> listId is null or empty!");
            return mIsMonitor;
        }

        String dataPath = null;
        try {
            dataPath = (String) ((ViewGroup) view.getParent()).getTag(R.id.apex_data_collect_list_data_path);
        } catch (Exception e) {
            ATLog.e(TAG, "onItemClick() -> get dataPath Exception:" + e.getMessage());
        }

        Object itemAtPosition = parent.getItemAtPosition(position);
        String dataKey = ConfigUtils.getDataKey(itemAtPosition, dataPath);
        adapterViewPath.set(0, String.valueOf(listId + Constant.SEPARATOR_LIST_ITEM_GROUP + dataKey));

        String pageClassName = ConfigUtils.getPageClassName(adapterViewPath);
        if (TextUtils.isEmpty(pageClassName)) {
            ATLog.e(TAG, "onItemClick() -> pageClassName is null or empty!");
            return mIsMonitor;
        }

        if (mIsMonitor) {
            ATLog.d(TAG, "onItemClick() -> 圈选模式开启，不走原有逻辑======记录并加入配置文件中，待后续上传");
            switch (mMonitorMode) {
                case DEFINE_MODE:
                    MonitorTools.showListItemClickDefinePage(listId, pageClassName, adapterViewPath.get(0));
                    break;
                case CONTRAST_MODE:
                    MonitorTools.addContrastItem(listId);
                    break;
                default:
                    break;
            }
        } else {
            ATLog.i(TAG, "onItemClick() -> 圈选模式关闭，原有逻辑执行======正常上传埋点事件");
            TaskController.getInstance().submit(new GenerateListClickEventData(view, listId, adapterViewPath.get(0),
                    pageClassName));
        }

        ATLog.e(TAG, "onItemClick() -> 插桩方法耗时======" + (System.currentTimeMillis() - currentTimeMillis));
        return mIsMonitor;
    }

    public static boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList<String> adapterViewPath = AssembleXpath.getPathContainTag(ApexCache.getInstance().getContext(), parent);

        if (null == adapterViewPath || adapterViewPath.size() < Constant.VIEW_PATH_SIZE) {
            ATLog.e(TAG, "onGroupClick() -> adapterViewPath is illegal!");
            return mIsMonitor;
        }

        String listId = adapterViewPath.get(0);
        if (TextUtils.isEmpty(listId)) {
            ATLog.e(TAG, "onGroupClick() -> listId is null or empty!");
            return mIsMonitor;
        }

        String dataPath = null;
        try {
            dataPath = (String) ((ViewGroup) view.getParent()).getTag(R.id.apex_data_collect_expandable_list_group_data_path);
        } catch (Exception e) {
            ATLog.e(TAG, "onGroupClick() -> get dataPath Exception:" + e.getMessage());
        }

        Object itemAtPosition = parent.getItemAtPosition(groupPosition);
        String dataKey = ConfigUtils.getDataKey(itemAtPosition, dataPath);
        adapterViewPath.set(0, String.valueOf(listId + Constant.SEPARATOR_LIST_ITEM_GROUP + dataKey));

        String pageClassName = ConfigUtils.getPageClassName(adapterViewPath);
        if (TextUtils.isEmpty(pageClassName)) {
            ATLog.e(TAG, "onGroupClick() -> pageClassName is null or empty!");
            return mIsMonitor;
        }

        if (mIsMonitor) {
            ATLog.d(TAG, "onGroupClick() -> 圈选模式开启，不走原有逻辑======记录并加入配置文件中，待后续上传");
            switch (mMonitorMode) {
                case DEFINE_MODE:
                    MonitorTools.showListItemClickDefinePage(listId, pageClassName, adapterViewPath.get(0));
                    break;
                case CONTRAST_MODE:
                    MonitorTools.addContrastItem(listId);
                    break;
                default:
                    break;
            }
        } else {
            ATLog.i(TAG, "onGroupClick() -> 圈选模式关闭，原有逻辑执行======正常上传埋点事件");
            TaskController.getInstance().submit(new GenerateListClickEventData(view, listId, adapterViewPath.get(0),
                    pageClassName));
        }

        ATLog.e(TAG, "onGroupClick() -> 插桩方法耗时======" + (System.currentTimeMillis() - currentTimeMillis));
        return mIsMonitor;
    }

    public static boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList<String> groupAdapterViewPath = AssembleXpath.getPathContainTag(ApexCache.getInstance().getContext(), parent);

        if (null == groupAdapterViewPath || groupAdapterViewPath.size() < Constant.VIEW_PATH_SIZE) {
            ATLog.e(TAG, "onChildClick() -> groupAdapterViewPath is illegal!");
            return mIsMonitor;
        }

        String listId = groupAdapterViewPath.get(0);
        if (TextUtils.isEmpty(listId)) {
            ATLog.e(TAG, "onChildClick() -> listId is null or empty!");
            return mIsMonitor;
        }

        String groupDataPath = null;
        try {
            groupDataPath = (String) ((ViewGroup) view.getParent()).getTag(R.id.apex_data_collect_expandable_list_group_data_path);
        } catch (Exception e) {
            ATLog.e(TAG, "onChildClick() -> get groupDataPath Exception:" + e.getMessage());
        }
        Object itemAtPosition = parent.getItemAtPosition(groupPosition);
        String groupDataKey = ConfigUtils.getDataKey(itemAtPosition, groupDataPath);

        String childDataPath = null;
        try {
            childDataPath = (String) ((ViewGroup) view.getParent()).getTag(R.id.apex_data_collect_expandable_list_child_data_path);
        } catch (Exception e) {
            ATLog.e(TAG, "onChildClick() -> get groupDataPath Exception:" + e.getMessage());
        }

        ExpandableListAdapter adapter = parent.getExpandableListAdapter();
        if (null == adapter) {
            ATLog.e(TAG, "onChildClick() -> adapter is null !");
            return mIsMonitor;
        }
        Object child = adapter.getChild(groupPosition, childPosition);
        String childDataKey = ConfigUtils.getDataKey(child, childDataPath);

        listId = listId + Constant.SEPARATOR_LIST_ITEM_GROUP_ID + groupDataKey;
        groupAdapterViewPath.set(0, String.valueOf(listId + Constant.SEPARATOR_LIST_ITEM_GROUP + childDataKey));

        String pageClassName = ConfigUtils.getPageClassName(groupAdapterViewPath);
        if (TextUtils.isEmpty(pageClassName)) {
            ATLog.e(TAG, "onChildClick() -> pageClassName is null or empty!");
            return mIsMonitor;
        }

        if (mIsMonitor) {
            ATLog.d(TAG, "onChildClick() -> 圈选模式开启，不走原有逻辑======记录并加入配置文件中，待后续上传");
            switch (mMonitorMode) {
                case DEFINE_MODE:
                    MonitorTools.showListItemClickDefinePage(listId, pageClassName, groupAdapterViewPath.get(0));
                    break;
                case CONTRAST_MODE:
                    MonitorTools.addContrastItem(listId);
                    break;
                default:
                    break;
            }
        } else {
            ATLog.i(TAG, "onChildClick() -> 圈选模式关闭，原有逻辑执行======正常上传埋点事件");
            TaskController.getInstance().submit(new GenerateListClickEventData(view, listId, groupAdapterViewPath.get(0),
                    pageClassName));
        }

        ATLog.e(TAG, "onChildClick() -> 插桩方法耗时======" + (System.currentTimeMillis() - currentTimeMillis));
        return mIsMonitor;
    }

    public static boolean onOptionsItemSelected(AppCompatActivity activity, MenuItem menuItem) {
        ViewGroup viewById = activity.findViewById(android.R.id.content);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        if (null == searchView) {
            ATLog.e(TAG, "searchView is null! ");
            return mIsMonitor;
        }

        long currentTimeMillis = System.currentTimeMillis();
        String menuPath = AssembleXpath.getMenuPath(ApexCache.getInstance().getContext(), menuItem);
        ArrayList<String> contentViewPath = AssembleXpath.getPathContainTag(ApexCache.getInstance().getContext(),
                viewById.getChildAt(0));
        if (null == contentViewPath || contentViewPath.size() < Constant.VIEW_PATH_SIZE) {
            ATLog.e(TAG, "onOptionsItemSelected() -> contentViewPath is illegal!");
            return mIsMonitor;
        }

        String pageClassName = AssembleXpath.getActivityName(searchView);
        String viewPath = contentViewPath.get(0) + menuPath;
        contentViewPath.set(0, viewPath);

        ATLog.i(TAG, "onOptionsItemSelected() -> viewPath:" + viewPath);
        ATLog.i(TAG, "onOptionsItemSelected() -> pageClassName:" + pageClassName);

        if (mIsMonitor) {
            ATLog.d(TAG, "onOptionsItemSelected() -> 圈选模式开启，不走原有逻辑======记录并加入配置文件中，待后续上传");
            switch (mMonitorMode) {
                case DEFINE_MODE:
                    MonitorTools.showClickDefinePage(viewPath, pageClassName);
                    break;
                case CONTRAST_MODE:
                    MonitorTools.addContrastItem(viewPath);
                    break;
                default:
                    break;
            }
        } else {
            ATLog.i(TAG, "onOptionsItemSelected() -> 圈选模式关闭，原有逻辑执行======正常上传埋点事件");
            TaskController.getInstance().submit(new GenerateClickEventData(searchView, viewPath, pageClassName));
        }

        ATLog.e(TAG, "onOptionsItemSelected() -> 插桩方法耗时======" + (System.currentTimeMillis() - currentTimeMillis));
        return mIsMonitor;
    }

    private static void getAdapterData(View view) {
        int index = ((RecyclerView) view.getParent()).getChildAdapterPosition(view);

        RecyclerView.Adapter adapter = ((RecyclerView) view.getParent()).getAdapter();
        try {
            Field mDatas = adapter.getClass().getDeclaredField("mDatas");
            mDatas.setAccessible(true);
            List list = (List) mDatas.get(adapter);
            ATLog.i(TAG, "list -> index:" + index + "=======" + list.get(index));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static String getV4FragmentPageClassName(Fragment v4Fragment, String methodTag) {
        if (null == v4Fragment || TextUtils.isEmpty(methodTag)) {
            ATLog.e(TAG, "getV4FragmentPageClassName() -> fragment or methodTag is null or empty!");
            return null;
        }

        String v4FragmentName = v4Fragment.getClass().getSimpleName();
        View view = v4Fragment.getView();
        if (null == view) {
            ATLog.e(TAG, v4FragmentName + " " + methodTag + "() -> view is null!");
            return null;
        }

        ArrayList<String> viewPath = AssembleXpath.getPathContainTag(ApexCache.getInstance().getContext(), view);
        if (null == viewPath || viewPath.size() < Constant.VIEW_PATH_SIZE) {
            ATLog.e(TAG, v4FragmentName + " " + methodTag + "() -> viewPath is illegal!");
            return null;
        }

        return ConfigUtils.getPageClassName(viewPath);
    }

    private static String getNoV4FragmentPageClassName(android.app.Fragment noV4Fragment, String methodTag) {
        if (null == noV4Fragment || TextUtils.isEmpty(methodTag)) {
            ATLog.e(TAG, "getNoV4FragmentPageClassName() -> noV4Fragment or methodTag is null or empty!");
            return null;
        }

        String noV4FragmentName = noV4Fragment.getClass().getSimpleName();
        View view = noV4Fragment.getView();
        if (null == view) {
            ATLog.e(TAG, noV4FragmentName + " " + methodTag + "() -> view is null!");
            return null;
        }

        ArrayList<String> viewPath = AssembleXpath.getPathContainTag(ApexCache.getInstance().getContext(), view);
        if (null == viewPath || viewPath.size() < Constant.VIEW_PATH_SIZE) {
            ATLog.e(TAG, noV4FragmentName + " " + methodTag + "() -> viewPath is illegal!");
            return null;
        }

        return ConfigUtils.getPageClassName(viewPath);
    }

    public static void onFragmentResume(Fragment fragment) {
        long pvStartTime = System.currentTimeMillis();

        String v4FragmentPageClassName = getV4FragmentPageClassName(fragment, "onFragmentResume");
        if (TextUtils.isEmpty(v4FragmentPageClassName)) {
            ATLog.e(TAG, "onFragmentResume() -> v4FragmentPageClassName is null or empty!");
            return;
        }

        Map<Integer, Stack<Map<String, Boolean>>> tasks = ApexCache.getInstance().getTasks();
        Map<Integer, Integer> top = ApexCache.getInstance().getTop();
        if (null == tasks) {
            ATLog.e(TAG, "onFragmentResumeV4()-> tasks is null");
            return;
        }
        if (null == top) {
            ATLog.e(TAG, "onFragmentResumeV4()-> top is null");
            return;
        }
        int taskId = fragment.getActivity().getTaskId();
        if (!tasks.containsKey(taskId)) {
            ATLog.e(TAG, "onFragmentResumeV4()-> tasks do not contain taskid:" + taskId);
            return;
        }
        Stack<Map<String, Boolean>> task = tasks.get(taskId);
        if (task.isEmpty()) {
            ATLog.e(TAG, "onFragmentResumeV4()-> task (id=" + taskId + ") is empty");
            return;
        }

        // do not add duplicate fragment
        if (!top.containsKey(taskId)) {
            MLog.e(TAG, "onFragmentResumeV4()-> top does not contain top[" + taskId + "]");
            return;
        }
        int topActivity = top.get(taskId);
        boolean isDuplicate = false;
        for (int i = task.size() - 1; i > topActivity; i--) {
            Map<String, Boolean> map = task.get(i);
            if (null == map) {
                ATLog.e(TAG, "onFragmentResumeV4()-> map should not be null");
                return;
            }
            Set<String> keySet = map.keySet();
            if (keySet.size() != 1) {
                ATLog.e(TAG, "onFragmentResume(v4) key set size can only be 1");
                return;
            }
            String[] key = map.keySet().toArray(new String[1]);
            if (!map.get(key[0])) {
                if (v4FragmentPageClassName.equals(key[0])) {
                    isDuplicate = true;
                    break;
                }
            }
        }
        if (!isDuplicate) {
            Map<String, Boolean> map = new HashMap<>(1);
            map.put(v4FragmentPageClassName, false);
            task.push(map);
            ATLog.v(TAG, "onFragmentResume(v4) add stack " + v4FragmentPageClassName);
        }

        ATLog.v(TAG, "onFragmentResume(v4)  name: " + fragment.getClass().getSimpleName()
                + " isVisibleToUser: " + fragment.getUserVisibleHint()
                + " hidden: " + fragment.isHidden()
                + " resumeTime: " + pvStartTime);

        if (fragment.getUserVisibleHint() && !fragment.isHidden()) {
            ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
            if (null == pvDurationTimes) {
                ATLog.e(TAG, "onFragmentResume(v4) pvDurationTimes is null!");
                return;
            }

            pvDurationTimes.put(v4FragmentPageClassName, pvStartTime);
        }
    }

    public static void onFragmentPause(Fragment fragment) {
        long pvEndTime = System.currentTimeMillis();

        String v4FragmentPageClassName = getV4FragmentPageClassName(fragment, "onFragmentPause");
        if (TextUtils.isEmpty(v4FragmentPageClassName)) {
            ATLog.e(TAG, "onFragmentPause() -> v4FragmentPageClassName is null or empty!");
            return;
        }

        ATLog.v(TAG, "onFragmentPause(v4)  name: " + fragment.getClass().getSimpleName()
                + " isVisibleToUser: " + fragment.getUserVisibleHint()
                + " hidden: " + fragment.isHidden()
                + " pauseTime: " + pvEndTime);

        if (fragment.getUserVisibleHint() && !fragment.isHidden()) {
            FragmentActivity activity = fragment.getActivity();
            TaskController.getInstance().submit(new GenerateFragmentV4PvEventData(v4FragmentPageClassName,
                    null == activity ? "" : activity.getClass().getCanonicalName(), pvEndTime));
        }
    }

    public static void setFragmentUserVisibleHint(Fragment fragment, boolean isVisibleToUser) {
        long time = System.currentTimeMillis();

        String v4FragmentPageClassName = getV4FragmentPageClassName(fragment, "setFragmentUserVisibleHint");
        if (TextUtils.isEmpty(v4FragmentPageClassName)) {
            ATLog.e(TAG, "setFragmentUserVisibleHint() -> v4FragmentPageClassName is null or empty!");
            return;
        }

        ATLog.v(TAG, "setFragmentUserVisibleHint(v4)  name: " + fragment.getClass().getSimpleName()
                + " isVisibleToUser: " + isVisibleToUser
                + " hidden: " + fragment.isHidden()
                + " time: " + time);

        if (isVisibleToUser && !fragment.isHidden()) {
            ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
            if (null == pvDurationTimes) {
                ATLog.e(TAG, "setFragmentUserVisibleHint(v4) pvDurationTimes is null!");
                return;
            }

            pvDurationTimes.put(v4FragmentPageClassName, time);
            return;
        }

        FragmentActivity activity = fragment.getActivity();
        TaskController.getInstance().submit(new GenerateFragmentV4PvEventData(v4FragmentPageClassName,
                null == activity ? "" : activity.getClass().getCanonicalName(), time));
    }

    public static void onFragmentHiddenChanged(Fragment fragment, boolean hidden) {
        long time = System.currentTimeMillis();

        String v4FragmentPageClassName = getV4FragmentPageClassName(fragment, "onFragmentHiddenChanged");
        if (TextUtils.isEmpty(v4FragmentPageClassName)) {
            ATLog.e(TAG, "onFragmentHiddenChanged() -> v4FragmentPageClassName is null or empty!");
            return;
        }

        ATLog.v(TAG, "onFragmentHiddenChanged(v4) name: " + fragment.getClass().getSimpleName()
                + " isVisibleToUser: " + fragment.getUserVisibleHint()
                + " hidden: " + hidden
                + " time: " + time);

        if (!hidden && fragment.getUserVisibleHint()) {
            ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
            if (null == pvDurationTimes) {
                ATLog.e(TAG, "onFragmentHiddenChanged(v4) pvDurationTimes is null!");
                return;
            }

            pvDurationTimes.put(v4FragmentPageClassName, time);
            return;
        }

        FragmentActivity activity = fragment.getActivity();
        TaskController.getInstance().submit(new GenerateFragmentV4PvEventData(v4FragmentPageClassName,
                null == activity ? "" : activity.getClass().getCanonicalName(), time));
    }

    public static void onFragmentResume(android.app.Fragment fragment) {
        long pvStartTime = System.currentTimeMillis();

        String noV4FragmentPageClassName = getNoV4FragmentPageClassName(fragment, "onFragmentResume");
        if (TextUtils.isEmpty(noV4FragmentPageClassName)) {
            ATLog.e(TAG, "onFragmentResume() -> noV4FragmentPageClassName is null or empty!");
            return;
        }

        Map<Integer, Stack<Map<String, Boolean>>> tasks = ApexCache.getInstance().getTasks();
        Map<Integer, Integer> top = ApexCache.getInstance().getTop();
        if (null == tasks) {
            ATLog.e(TAG, "onFragmentResume()-> tasks is null");
            return;
        }
        if (null == top) {
            ATLog.e(TAG, "onFragmentResume()-> top is null");
            return;
        }
        int taskId = fragment.getActivity().getTaskId();
        if (!tasks.containsKey(taskId)) {
            ATLog.e(TAG, "onFragmentResume()-> tasks do not contain taskid:" + taskId);
            return;
        }
        Stack<Map<String, Boolean>> task = tasks.get(taskId);
        if (task.isEmpty()) {
            ATLog.e(TAG, "onFragmentResume()-> task (id=" + taskId + ") is empty");
            return;
        }

        // do not add duplicate fragment
        if (!top.containsKey(taskId)) {
            MLog.e(TAG, "onFragmentResume()-> top does not contain top[" + taskId + "]");
            return;
        }
        int topActivity = top.get(taskId);
        boolean isDuplicate = false;
        for (int i = task.size() - 1; i > topActivity; i--) {
            Map<String, Boolean> map = task.get(i);
            if (null == map) {
                ATLog.e(TAG, "onFragmentResume()-> map should not be null");
                return;
            }
            Set<String> keySet = map.keySet();
            if (keySet.size() != 1) {
                ATLog.e(TAG, "onFragmentResume() key set size can only be 1");
                return;
            }

            String[] key = map.keySet().toArray(new String[1]);
            if (!map.get(key[0])) {
                if (noV4FragmentPageClassName.equals(key[0])) {
                    isDuplicate = true;
                    break;
                }
            }
        }

        if (!isDuplicate) {
            Map<String, Boolean> map = new HashMap<>(1);
            map.put(noV4FragmentPageClassName, false);
            task.push(map);
            ATLog.v(TAG, "onFragmentResume add stack " + noV4FragmentPageClassName);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            ATLog.v(TAG, "onFragmentResume name: " + fragment.getClass().getSimpleName()
                    + " isVisibleToUser: " + fragment.getUserVisibleHint()
                    + " hidden: " + fragment.isHidden()
                    + " resumeTime: " + pvStartTime);

            if (fragment.getUserVisibleHint() && !fragment.isHidden()) {
                ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
                if (null == pvDurationTimes) {
                    ATLog.e(TAG, "onFragmentResume() pvDurationTimes is null!");
                    return;
                }

                pvDurationTimes.put(noV4FragmentPageClassName, pvStartTime);
                return;
            }
        }

        ATLog.v(TAG, "onFragmentResume name: " + fragment.getClass().getSimpleName()
                + " hidden: " + fragment.isHidden()
                + " resumeTime: " + pvStartTime);

        if (!fragment.isHidden()) {
            ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
            if (null == pvDurationTimes) {
                ATLog.e(TAG, "onFragmentResume() pvDurationTimes is null!");
                return;
            }

            pvDurationTimes.put(noV4FragmentPageClassName, pvStartTime);
        }
    }

    public static void onFragmentPause(android.app.Fragment fragment) {
        long pvEndTime = System.currentTimeMillis();

        String noV4FragmentPageClassName = getNoV4FragmentPageClassName(fragment, "onFragmentPause");
        if (TextUtils.isEmpty(noV4FragmentPageClassName)) {
            ATLog.e(TAG, "onFragmentPause() -> noV4FragmentPageClassName is null or empty!");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            ATLog.v(TAG, "onFragmentPause  name: " + fragment.getClass().getSimpleName()
                    + " isVisibleToUser: " + fragment.getUserVisibleHint()
                    + " hidden: " + fragment.isHidden()
                    + " pauseTime: " + pvEndTime);

            if (fragment.getUserVisibleHint() && !fragment.isHidden()) {
                Activity activity = fragment.getActivity();


                TaskController.getInstance().submit(new GenerateFragmentNotV4PvEventData(noV4FragmentPageClassName,
                        null == activity ? "" : activity.getClass().getCanonicalName(), pvEndTime));
            }
        }
    }

    public static void setFragmentUserVisibleHint(android.app.Fragment fragment, boolean isVisibleToUser) {
        long time = System.currentTimeMillis();

        String noV4FragmentPageClassName = getNoV4FragmentPageClassName(fragment, "setFragmentUserVisibleHint");
        if (TextUtils.isEmpty(noV4FragmentPageClassName)) {
            ATLog.e(TAG, "setFragmentUserVisibleHint() -> noV4FragmentPageClassName is null or empty!");
            return;
        }

        ATLog.v(TAG, "setFragmentUserVisibleHint name: " + fragment.getClass().getSimpleName()
                + " isVisibleToUser: " + isVisibleToUser
                + " hidden: " + fragment.isHidden()
                + " time: " + time);

        if (isVisibleToUser && !fragment.isHidden()) {
            ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
            if (null == pvDurationTimes) {
                ATLog.e(TAG, "setFragmentUserVisibleHint() pvDurationTimes is null!");
                return;
            }

            pvDurationTimes.put(noV4FragmentPageClassName, time);
            return;
        }

        Activity activity = fragment.getActivity();
        TaskController.getInstance().submit(new GenerateFragmentNotV4PvEventData(noV4FragmentPageClassName,
                null == activity ? "" : activity.getClass().getCanonicalName(), time));
    }

    public static void onFragmentHiddenChanged(android.app.Fragment fragment, boolean hidden) {
        long time = System.currentTimeMillis();

        String noV4FragmentPageClassName = getNoV4FragmentPageClassName(fragment, "onFragmentHiddenChanged");
        if (TextUtils.isEmpty(noV4FragmentPageClassName)) {
            ATLog.e(TAG, "onFragmentHiddenChanged() -> noV4FragmentPageClassName is null or empty!");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            ATLog.v(TAG, "onFragmentHiddenChanged name: " + fragment.getClass().getSimpleName()
                    + " isVisibleToUser: " + fragment.getUserVisibleHint()
                    + " hidden: " + hidden
                    + " time: " + time);

            if (!hidden && fragment.getUserVisibleHint()) {
                ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
                if (null == pvDurationTimes) {
                    ATLog.e(TAG, "onFragmentHiddenChanged() pvDurationTimes is null!");
                    return;
                }

                pvDurationTimes.put(noV4FragmentPageClassName, time);
                return;
            }

            Activity activity = fragment.getActivity();
            TaskController.getInstance().submit(new GenerateFragmentNotV4PvEventData(noV4FragmentPageClassName,
                    null == activity ? "" : activity.getClass().getCanonicalName(), time));
            return;
        }

        ATLog.v(TAG, "onFragmentHiddenChanged name: " + fragment.getClass().getSimpleName()
                + " hidden: " + hidden
                + " time: " + time);

        if (!hidden) {
            ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
            if (null == pvDurationTimes) {
                ATLog.e(TAG, "onFragmentHiddenChanged() pvDurationTimes is null!");
                return;
            }

            pvDurationTimes.put(noV4FragmentPageClassName, time);
            return;
        }

        Activity activity = fragment.getActivity();
        TaskController.getInstance().submit(new GenerateFragmentNotV4PvEventData(noV4FragmentPageClassName,
                null == activity ? "" : activity.getClass().getCanonicalName(), time));
    }

    public static void listOnScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        AnalyticsListenerController.getInstance().notifyListPvEventsOnListScroll(view);
    }

    public static void listOnScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                ATLog.w(TAG, view.getClass().getSimpleName() + " SCROLL_STATE_IDLE");
                AnalyticsListenerController.getInstance().notifyListPvEventsOnListIdle(view);
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                break;
            default:
                break;
        }
    }

}
