package com.chinapex.android.datacollect.executor.runnable;

import android.text.TextUtils;

import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.executor.callback.IMultiEventCallback;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.db.DbDao;
import com.chinapex.android.datacollect.utils.ATLog;

import java.util.TreeMap;

/**
 * @author SteelCabbage
 * @date 2018/11/20
 */
public class DelayEvent implements Runnable, IMultiEventCallback {

    private static final String TAG = DelayEvent.class.getSimpleName();
    private String mTableName;

    public DelayEvent(String tableName) {
        mTableName = tableName;
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mTableName)) {
            ATLog.e(TAG, "run() -> mTableName is null or empty!");
            return;
        }

        DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
        if (null == dbDao) {
            ATLog.e(TAG, "run() -> dbDao is null!");
            return;
        }

        // 处理数据库id，避免索引溢出
        dbDao.avoidIdUnlimitedGrowth(mTableName);

        // 把数据库所有的事件, 按上报的最大数量, 拆分发送, 网络请求成功继续，失败则终止
        getTrackEventTreeMap(0);
    }

    private void getTrackEventTreeMap(int offset) {
        DbDao dbDao = DbDao.getInstance(ApexCache.getInstance().getContext());
        if (null == dbDao) {
            ATLog.e(TAG, "getTrackEventTreeMap() -> dbDao is null!");
            return;
        }

        TreeMap<Long, TrackEvent> trackEventTreeMap = dbDao.queryOffset(
                mTableName, offset, ApexCache.getInstance().getReportMaxNum());
        if (null == trackEventTreeMap || trackEventTreeMap.isEmpty()) {
            ATLog.w(TAG, mTableName + " DelayEvent trackEventTreeMap is null or empty!");
            return;
        }

        TaskController.getInstance().submit(new MultiEvent(mTableName, trackEventTreeMap, this));
    }

    @Override
    public void continueSend(int offset) {
        ATLog.v(TAG, mTableName + " continueSend() -> offset:" + offset);
        getTrackEventTreeMap(offset);
    }

    @Override
    public void stopSend(String errMsg) {
        ATLog.e(TAG, mTableName + " stopSend() -> errMsg:" + errMsg);
    }
}
