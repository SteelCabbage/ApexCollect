package com.chinapex.android.datacollect.aop;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.runnable.GenerateClickEventData;
import com.chinapex.android.datacollect.executor.runnable.GenerateFragmentNotV4PvEventData;
import com.chinapex.android.datacollect.executor.runnable.GenerateFragmentV4PvEventData;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.utils.ATLog;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SteelCabbage
 * @date 2018/11/05
 */
public class AopHelper {
    private static final String TAG = AopHelper.class.getSimpleName();

    private static boolean isMonitor = false;

    public static boolean onClick(View view) {
        long currentTimeMillis = System.currentTimeMillis();
        String viewPath = AssembleXpath.getPath(ApexCache.getInstance().getContext(), view);
        String pageClassName = AssembleXpath.getActivityName(view);
        ATLog.i(TAG, "viewPath:" + viewPath);
        ATLog.i(TAG, "pageClassName:" + pageClassName);

        // 后期根据配置文件，下发数据集合的名字，根据反射获取具体条目信息
//        if (view.getParent() instanceof RecyclerView) {
//            getAdapterData(view);
//        }

        if (isMonitor) {
            ATLog.d(TAG, "圈选模式开启，不走原有逻辑======记录并加入配置文件中，待后续上传");
        } else {
            ATLog.i(TAG, "圈选模式关闭，原有逻辑执行======正常上传埋点事件");
            TaskController.getInstance().submit(new GenerateClickEventData(view, viewPath, pageClassName));
        }

        ATLog.e(TAG, "插桩方法耗时======" + (System.currentTimeMillis() - currentTimeMillis));
        return isMonitor;
    }

    public static boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTimeMillis = System.currentTimeMillis();
        String viewPath = AssembleXpath.getPath(ApexCache.getInstance().getContext(), view);
        String pageClassName = AssembleXpath.getActivityName(view);
        ATLog.i(TAG, "onItemClick viewPath:" + viewPath);
        ATLog.i(TAG, "onItemClick pageClassName:" + pageClassName);

        // 后期根据配置文件，下发数据集合的名字，根据反射获取具体条目信息
//        if (view.getParent() instanceof RecyclerView) {
//            getAdapterData(view);
//        }

        if (isMonitor) {
            ATLog.d(TAG, "onItemClick 圈选模式开启，不走原有逻辑======记录并加入配置文件中，待后续上传");
        } else {
            ATLog.i(TAG, "onItemClick 圈选模式关闭，原有逻辑执行======正常上传埋点事件");
            TaskController.getInstance().submit(new GenerateClickEventData(view, viewPath, pageClassName));
        }

        ATLog.e(TAG, "onItemClick 插桩方法耗时======" + (System.currentTimeMillis() - currentTimeMillis));
        return isMonitor;
    }

    public static boolean onOptionsItemSelected(AppCompatActivity activity, MenuItem menuItem) {
        ViewGroup viewById = activity.findViewById(android.R.id.content);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        if (null == searchView) {
            ATLog.e(TAG, "searchView is null! ");
            return isMonitor;
        }

        long currentTimeMillis = System.currentTimeMillis();
        String menuPath = AssembleXpath.getMenuPath(ApexCache.getInstance().getContext(), menuItem);
        String contentViewPath = AssembleXpath.getPath(ApexCache.getInstance().getContext(), viewById.getChildAt(0));
        String pageClassName = AssembleXpath.getActivityName(searchView);
        String viewPath = contentViewPath + menuPath;

        ATLog.i(TAG, "onOptionsItemSelected viewPath:" + contentViewPath + menuPath);
        ATLog.i(TAG, "onOptionsItemSelected pageClassName:" + pageClassName);


        if (isMonitor) {
            ATLog.d(TAG, "onItemClick 圈选模式开启，不走原有逻辑======记录并加入配置文件中，待后续上传");
        } else {
            ATLog.i(TAG, "onItemClick 圈选模式关闭，原有逻辑执行======正常上传埋点事件");
            TaskController.getInstance().submit(new GenerateClickEventData(searchView, viewPath, pageClassName));
        }

        ATLog.e(TAG, "插桩方法耗时======" + (System.currentTimeMillis() - currentTimeMillis));
        return isMonitor;
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

//        Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
//        field.setAccessible(true);
//        field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);

    }

    public static void onFragmentResume(Fragment fragment) {
        long pvStartTime = System.currentTimeMillis();

        ATLog.v(TAG, "onFragmentResume(v4)  name: " + fragment.getClass().getSimpleName()
                + " isVisibleToUser: " + fragment.getUserVisibleHint() + " hidden: " + fragment.isHidden() + " resumeTime: " +
                pvStartTime);

        if (fragment.getUserVisibleHint() && !fragment.isHidden()) {
            ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
            if (null == pvDurationTimes) {
                ATLog.e(TAG, "onFragmentResume(v4) pvDurationTimes is null!");
                return;
            }

            pvDurationTimes.put(fragment.getClass().getName(), pvStartTime);
        }
    }

    public static void onFragmentPause(Fragment fragment) {
        long pvEndTime = System.currentTimeMillis();

        ATLog.v(TAG, "onFragmentPause(v4)  name: " + fragment.getClass().getSimpleName()
                + " isVisibleToUser: " + fragment.getUserVisibleHint() + " hidden: " + fragment.isHidden()
                + " pauseTime: " + pvEndTime);

        if (fragment.getUserVisibleHint() && !fragment.isHidden()) {
            FragmentActivity activity = fragment.getActivity();
            if (null == activity) {
                TaskController.getInstance().submit(new GenerateFragmentV4PvEventData(fragment, "", pvEndTime));
                return;
            }

            TaskController.getInstance().submit(new GenerateFragmentV4PvEventData(fragment, activity.getClass().getName(),
                    pvEndTime));
        }
    }

    public static void setFragmentUserVisibleHint(Fragment fragment, boolean isVisibleToUser) {
        long time = System.currentTimeMillis();

        ATLog.v(TAG, "setFragmentUserVisibleHint(v4)  name: " + fragment.getClass().getSimpleName()
                + " isVisibleToUser: " + isVisibleToUser + " hidden: " + fragment.isHidden() + " time: " + time);

        if (isVisibleToUser && !fragment.isHidden()) {
            ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
            if (null == pvDurationTimes) {
                ATLog.e(TAG, "setFragmentUserVisibleHint(v4) pvDurationTimes is null!");
                return;
            }

            pvDurationTimes.put(fragment.getClass().getName(), time);
            return;
        }

        FragmentActivity activity = fragment.getActivity();
        if (null == activity) {
            TaskController.getInstance().submit(new GenerateFragmentV4PvEventData(fragment, "", time));
            return;
        }

        TaskController.getInstance().submit(new GenerateFragmentV4PvEventData(fragment, activity.getClass().getName(), time));
    }

    public static void onFragmentHiddenChanged(Fragment fragment, boolean hidden) {
        long time = System.currentTimeMillis();

        ATLog.v(TAG, "onFragmentHiddenChanged(v4) name: " + fragment.getClass().getSimpleName()
                + " isVisibleToUser: " + fragment.getUserVisibleHint() + " hidden: " + hidden + " time: " + time);

        if (!hidden && fragment.getUserVisibleHint()) {
            ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
            if (null == pvDurationTimes) {
                ATLog.e(TAG, "onFragmentHiddenChanged(v4) pvDurationTimes is null!");
                return;
            }

            pvDurationTimes.put(fragment.getClass().getName(), time);
            return;
        }

        FragmentActivity activity = fragment.getActivity();
        if (null == activity) {
            TaskController.getInstance().submit(new GenerateFragmentV4PvEventData(fragment, "", time));
            return;
        }

        TaskController.getInstance().submit(new GenerateFragmentV4PvEventData(fragment, activity.getClass().getName(), time));
    }

    public static void onFragmentResume(android.app.Fragment fragment) {
        long pvStartTime = System.currentTimeMillis();
        String fragmentName = fragment.getClass().getName();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {

            ATLog.v(TAG, "onFragmentResume  name: " + fragment.getClass().getSimpleName()
                    + " isVisibleToUser: " + fragment.getUserVisibleHint() + " hidden: " + fragment.isHidden() + " resumeTime: " +
                    "" + pvStartTime);

            if (fragment.getUserVisibleHint() && !fragment.isHidden()) {
                ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
                if (null == pvDurationTimes) {
                    ATLog.e(TAG, "onFragmentResume() pvDurationTimes is null!");
                    return;
                }

                pvDurationTimes.put(fragmentName, pvStartTime);
                return;
            }
        }

        ATLog.v(TAG, "onFragmentResume  name: " + fragment.getClass().getSimpleName()
                + " hidden: " + fragment.isHidden() + " resumeTime: " + pvStartTime);

        if (!fragment.isHidden()) {
            ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
            if (null == pvDurationTimes) {
                ATLog.e(TAG, "onFragmentResume() pvDurationTimes is null!");
                return;
            }

            pvDurationTimes.put(fragmentName, pvStartTime);
        }
    }

    public static void onFragmentPause(android.app.Fragment fragment) {
        long pvEndTime = System.currentTimeMillis();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            ATLog.v(TAG, "onFragmentPause  name: " + fragment.getClass().getSimpleName()
                    + " isVisibleToUser: " + fragment.getUserVisibleHint() + " hidden: " + fragment.isHidden()
                    + " pauseTime: " + pvEndTime);

            if (fragment.getUserVisibleHint() && !fragment.isHidden()) {
                Activity activity = fragment.getActivity();
                if (null == activity) {
                    TaskController.getInstance().submit(new GenerateFragmentNotV4PvEventData(fragment, "", pvEndTime));
                    return;
                }

                TaskController.getInstance().submit(
                        new GenerateFragmentNotV4PvEventData(fragment, activity.getClass().getName(), pvEndTime));
            }
        }
    }

    public static void setFragmentUserVisibleHint(android.app.Fragment fragment, boolean isVisibleToUser) {
        long time = System.currentTimeMillis();

        ATLog.v(TAG, "setFragmentUserVisibleHint  name: " + fragment.getClass().getSimpleName()
                + " isVisibleToUser: " + isVisibleToUser + " hidden: " + fragment.isHidden() + " time: " + time);

        if (isVisibleToUser && !fragment.isHidden()) {
            ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
            if (null == pvDurationTimes) {
                ATLog.e(TAG, "setFragmentUserVisibleHint() pvDurationTimes is null!");
                return;
            }

            pvDurationTimes.put(fragment.getClass().getName(), time);
            return;
        }

        Activity activity = fragment.getActivity();
        if (null == activity) {
            TaskController.getInstance().submit(new GenerateFragmentNotV4PvEventData(fragment, "", time));
            return;
        }

        TaskController.getInstance().submit(new GenerateFragmentNotV4PvEventData(fragment, activity.getClass().getName(), time));
    }

    public static void onFragmentHiddenChanged(android.app.Fragment fragment, boolean hidden) {
        long time = System.currentTimeMillis();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            ATLog.v(TAG, "onFragmentHiddenChanged name: " + fragment.getClass().getSimpleName()
                    + " isVisibleToUser: " + fragment.getUserVisibleHint() + " hidden: " + hidden + " time: " + time);

            if (!hidden && fragment.getUserVisibleHint()) {
                ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
                if (null == pvDurationTimes) {
                    ATLog.e(TAG, "onFragmentHiddenChanged() pvDurationTimes is null!");
                    return;
                }

                pvDurationTimes.put(fragment.getClass().getName(), time);
                return;
            }

            Activity activity = fragment.getActivity();
            if (null == activity) {
                TaskController.getInstance().submit(new GenerateFragmentNotV4PvEventData(fragment, "", time));
                return;
            }

            TaskController.getInstance().submit(
                    new GenerateFragmentNotV4PvEventData(fragment, activity.getClass().getName(), time));
            return;
        }


        ATLog.v(TAG, "onFragmentHiddenChanged name: " + fragment.getClass().getSimpleName()
                + " hidden: " + hidden + " time: " + time);

        if (!hidden) {
            ConcurrentHashMap<String, Long> pvDurationTimes = ApexCache.getInstance().getPvDurationTimes();
            if (null == pvDurationTimes) {
                ATLog.e(TAG, "onFragmentHiddenChanged() pvDurationTimes is null!");
                return;
            }

            pvDurationTimes.put(fragment.getClass().getName(), time);
            return;
        }

        Activity activity = fragment.getActivity();
        if (null == activity) {
            TaskController.getInstance().submit(new GenerateFragmentNotV4PvEventData(fragment, "", time));
            return;
        }

        TaskController.getInstance().submit(new GenerateFragmentNotV4PvEventData(fragment, activity.getClass().getName(), time));
    }

//    public static void rvOnScrollStateChanged(RecyclerView recyclerView, int newState) {
//        ATLog.w(TAG, "rvOnScrollStateChanged->" + "newState:" + newState + recyclerView.getClass().getSimpleName());
//    }

}
