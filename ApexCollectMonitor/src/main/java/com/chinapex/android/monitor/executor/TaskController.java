package com.chinapex.android.monitor.executor;

import com.chinapex.android.monitor.utils.MLog;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author : Seven Qiu
 * @date : 2019/1/18
 */

public class TaskController implements IController {

    private static final String TAG = TaskController.class.getSimpleName();
    private ScheduledExecutorService mScheduledExecutorService;

    private TaskController() {

    }

    private static class TaskControllerHolder {
        private static final TaskController TASK_CONTROLLER = new TaskController();
    }

    public static TaskController getInstance() {
        return TaskControllerHolder.TASK_CONTROLLER;
    }

    @Override
    public void doInit() {
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void onDestroy() {
        if (null == mScheduledExecutorService) {
            MLog.e(TAG, "onDestroy() -> mScheduledExecutorService is null!");
            return;
        }

        mScheduledExecutorService.shutdown();
        if (!mScheduledExecutorService.isShutdown()) {
            MLog.e(TAG, "onDestroy() -> mScheduledExecutorService did not close!");
            return;
        }

        MLog.i(TAG, "onDestroy() -> mScheduledExecutorService has been closed");
        mScheduledExecutorService = null;
    }

    public void submit(Runnable runnable) {
        if (null == mScheduledExecutorService || null == runnable) {
            MLog.e(TAG, "submit() -> mScheduledExecutorService or runnable is null!");
            return;
        }

        mScheduledExecutorService.submit(runnable);
    }

    public ScheduledFuture schedule(Runnable runnable, long initialDelay, long period) {
        if (null == mScheduledExecutorService || null == runnable) {
            MLog.e(TAG, "schedule() -> mScheduledExecutorService or runnable is null!");
            return null;
        }

        return mScheduledExecutorService.scheduleAtFixedRate(runnable, initialDelay, period, TimeUnit.MILLISECONDS);
    }

}
