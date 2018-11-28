package com.chinapex.android.datacollect.aop;

import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.runnable.GenerateClickEventData;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.utils.ATLog;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author SteelCabbage
 * @date 2018/11/05
 */
public class AopHelper {
    private static final String TAG = AopHelper.class.getSimpleName();

    private static boolean isMonitor = false;

    public static boolean onClick(View view) {
        long currentThreadTimeMillis = SystemClock.currentThreadTimeMillis();
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

        ATLog.e(TAG, "插桩方法耗时======" + (SystemClock.currentThreadTimeMillis() - currentThreadTimeMillis));
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
        ATLog.w(TAG, "onFragmentResume(v4):" + fragment.getClass().getSimpleName());
    }

    public static void onFragmentPause(Fragment fragment) {
        ATLog.w(TAG, "onFragmentPause(v4):" + fragment.getClass().getSimpleName());
    }

    public static void setFragmentUserVisibleHint(Fragment fragment, boolean isVisibleToUser) {
        ATLog.w(TAG, "setFragmentUserVisibleHint(v4)->" + isVisibleToUser + "->" + fragment.getClass().getSimpleName());
    }

    public static void onFragmentHiddenChanged(Fragment fragment, boolean hidden) {
        ATLog.w(TAG, "onFragmentHiddenChanged(v4)->" + hidden + "->" + fragment.getClass().getSimpleName());
    }

    public static void onFragmentResume(android.app.Fragment fragment) {
        ATLog.w(TAG, "onFragmentResume:" + fragment.getClass().getSimpleName());
    }

    public static void onFragmentPause(android.app.Fragment fragment) {
        ATLog.w(TAG, "onFragmentPause:" + fragment.getClass().getSimpleName());
    }

    public static void setFragmentUserVisibleHint(android.app.Fragment fragment, boolean isVisibleToUser) {
        ATLog.w(TAG, "setFragmentUserVisibleHint->" + isVisibleToUser + "->" + fragment.getClass().getSimpleName());
    }

    public static void onFragmentHiddenChanged(android.app.Fragment fragment, boolean hidden) {
        ATLog.w(TAG, "onFragmentHiddenChanged->" + hidden + "->" + fragment.getClass().getSimpleName());
    }

    //    public static void rvOnScrollStateChanged(RecyclerView recyclerView, int newState) {
//        ATLog.w(TAG, "rvOnScrollStateChanged->" + "newState:" + newState + recyclerView.getClass().getSimpleName());
//    }
}
