package com.chinapex.android.datacollect.executor.runnable;

import android.text.TextUtils;

import com.chinapex.android.datacollect.executor.TaskController;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.model.db.DbDao;
import com.chinapex.android.datacollect.utils.ATLog;

import java.util.TreeMap;

/**
 * @author SteelCabbage
 * @date 2018/11/20
 */
public class DelayEvent implements Runnable {

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

        // 把数据库所有的事件，按上报的最大数量，拆分发送
        TreeMap<Long, TrackEvent> trackEventTreeMap;
        int offset = 0;
        while (true) {
            trackEventTreeMap = dbDao.queryOffset(mTableName, offset, ApexCache.getInstance().getReportMaxNum());
            if (null == trackEventTreeMap || trackEventTreeMap.isEmpty()) {
                ATLog.w(TAG, mTableName + " DelayEvent trackEventTreeMap is null or empty!");
                break;
            }

            TaskController.getInstance().submit(new MultiEvent(mTableName, trackEventTreeMap));
            offset = offset + trackEventTreeMap.size();
        }
    }

}
