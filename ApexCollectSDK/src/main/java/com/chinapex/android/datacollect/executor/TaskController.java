package com.chinapex.android.datacollect.executor;

import com.chinapex.android.datacollect.controller.IController;
import com.chinapex.android.datacollect.utils.ATLog;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author SteelCabbage
 * @date 2018/11/14
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
            ATLog.e(TAG, "onDestroy() -> mScheduledExecutorService is null!");
            return;
        }

        mScheduledExecutorService.shutdown();
        if (!mScheduledExecutorService.isShutdown()) {
            ATLog.e(TAG, "onDestroy() -> mScheduledExecutorService did not close!");
            return;
        }

        ATLog.i(TAG, "onDestroy() -> mScheduledExecutorService has been closed");
        mScheduledExecutorService = null;
    }

    public void submit(Runnable runnable) {
        if (null == mScheduledExecutorService || null == runnable) {
            ATLog.e(TAG, "submit() -> mScheduledExecutorService or runnable is null!");
            return;
        }

        mScheduledExecutorService.submit(runnable);
    }

    public ScheduledFuture schedule(Runnable runnable, long initialDelay, long period) {
        if (null == mScheduledExecutorService || null == runnable) {
            ATLog.e(TAG, "schedule() -> mScheduledExecutorService or runnable is null!");
            return null;
        }

        return mScheduledExecutorService.scheduleAtFixedRate(runnable, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture scheduleDelay(Runnable runnable, long delay) {
        if (null == mScheduledExecutorService || null == runnable) {
            ATLog.e(TAG, "scheduleDelay() -> mScheduledExecutorService or runnable is null!");
            return null;
        }

        return mScheduledExecutorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

}
