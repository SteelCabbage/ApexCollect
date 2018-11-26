package com.chinapex.android.datacollect.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.SpUtils;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author SteelCabbage
 * @date 2018/11/14
 */

public class DbDao {

    private static final String TAG = DbDao.class.getSimpleName();
    //    private static final String DB_PWD = "";
    private static DbDao sDbDao;
    private DbHelper mDbHelper;
    private AtomicInteger mOpenCounter = new AtomicInteger();
    private SQLiteDatabase mDatabase;
    private ReentrantLock mReentrantLock;


    private DbDao(Context context) {
        mDbHelper = new DbHelper(context);
        mReentrantLock = new ReentrantLock();
    }

    public static DbDao getInstance(Context context) {
        if (null == context) {
            ATLog.e(TAG, "context is null!");
            return null;
        }

        if (null == sDbDao) {
            synchronized (DbDao.class) {
                if (null == sDbDao) {
                    sDbDao = new DbDao(context);
                }
            }
        }

        return sDbDao;
    }

    private synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            mDatabase = mDbHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    private synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            mDatabase.close();
        }
    }

    public synchronized void insert(String tableName, TrackEvent trackEvent, long time) {
        if (TextUtils.isEmpty(tableName) || null == trackEvent) {
            ATLog.e(TAG, "insert() -> tableName or trackEvent is null!");
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstant.FIELD_MODE, trackEvent.getMode());
        contentValues.put(DbConstant.FIELD_EVENT_TYPE, trackEvent.getEventType());
        contentValues.put(DbConstant.FIELD_LABEL, trackEvent.getLabel());
        contentValues.put(DbConstant.FIELD_TIME, time);
        contentValues.put(DbConstant.FIELD_VALUE, trackEvent.getValue());

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            long insertOrThrow = db.insertOrThrow(tableName, null, contentValues);
            db.setTransactionSuccessful();
            ATLog.i(TAG, "insert into " + tableName + " id = " + insertOrThrow + " ->" + trackEvent);
        } catch (SQLException e) {
            ATLog.e(TAG, "insert into " + tableName + " exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    private synchronized void insertTreeMap(String tableName, TreeMap<Long, TrackEvent> trackEventTreeMap) {
        if (null == trackEventTreeMap || trackEventTreeMap.isEmpty()) {
            ATLog.e(TAG, "insertTreeMap() -> trackEventTreeMap is null or empty!");
            return;
        }

        for (Map.Entry<Long, TrackEvent> entry : trackEventTreeMap.entrySet()) {
            if (null == entry) {
                ATLog.e(TAG, "insertTreeMap() -> entry is null!");
                continue;
            }

            insert(tableName, entry.getValue(), entry.getKey());
        }
    }

    public TreeMap<Long, TrackEvent> query(String tableName) {
        if (TextUtils.isEmpty(tableName)) {
            ATLog.e(TAG, "query() -> tableName is null or empty!");
            return null;
        }

        TreeMap<Long, TrackEvent> trackEventTreeMap = new TreeMap<>();

        SQLiteDatabase db = openDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(DbConstant.FIELD_ID);
                int modeIndex = cursor.getColumnIndex(DbConstant.FIELD_MODE);
                int eventTypeIndex = cursor.getColumnIndex(DbConstant.FIELD_EVENT_TYPE);
                int labelIndex = cursor.getColumnIndex(DbConstant.FIELD_LABEL);
                int timeIndex = cursor.getColumnIndex(DbConstant.FIELD_TIME);
                int valueIndex = cursor.getColumnIndex(DbConstant.FIELD_VALUE);

                int id = cursor.getInt(idIndex);
                int mode = cursor.getInt(modeIndex);
                int eventType = cursor.getInt(eventTypeIndex);
                String label = cursor.getString(labelIndex);
                long time = cursor.getLong(timeIndex);
                String value = cursor.getString(valueIndex);

                TrackEvent trackEvent = new TrackEvent.EventBuilder()
                        .setMode(mode)
                        .setEventType(eventType)
                        .setLabel(label)
                        .setValue(value)
                        .build();

                trackEventTreeMap.put(time, trackEvent);
            }
            cursor.close();
        }
        closeDatabase();
        return trackEventTreeMap;
    }

    public TreeMap<Long, TrackEvent> queryOffset(String tableName, int offset, int limit) {
        if (TextUtils.isEmpty(tableName)) {
            ATLog.e(TAG, "query() -> tableName is null or empty!");
            return null;
        }

        TreeMap<Long, TrackEvent> trackEventTreeMap = new TreeMap<>();

        SQLiteDatabase db = openDatabase();
        String sqlLimit = String.valueOf(offset + "," + limit);
        Cursor cursor = db.query(tableName, null, null, null, null, null, null, sqlLimit);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(DbConstant.FIELD_ID);
                int modeIndex = cursor.getColumnIndex(DbConstant.FIELD_MODE);
                int eventTypeIndex = cursor.getColumnIndex(DbConstant.FIELD_EVENT_TYPE);
                int labelIndex = cursor.getColumnIndex(DbConstant.FIELD_LABEL);
                int timeIndex = cursor.getColumnIndex(DbConstant.FIELD_TIME);
                int valueIndex = cursor.getColumnIndex(DbConstant.FIELD_VALUE);

                int id = cursor.getInt(idIndex);
                int mode = cursor.getInt(modeIndex);
                int eventType = cursor.getInt(eventTypeIndex);
                String label = cursor.getString(labelIndex);
                long time = cursor.getLong(timeIndex);
                String value = cursor.getString(valueIndex);

                TrackEvent trackEvent = new TrackEvent.EventBuilder()
                        .setMode(mode)
                        .setEventType(eventType)
                        .setLabel(label)
                        .setValue(value)
                        .build();

                trackEventTreeMap.put(time, trackEvent);
                ATLog.i(TAG, tableName + " queryOffset() -> id:" + id);
            }
            cursor.close();
        }
        closeDatabase();
        return trackEventTreeMap;
    }


    private static final String WHERE_CLAUSE_TIME_EQ = DbConstant.FIELD_TIME + " = ?";

    public void deleteByTime(String tableName, long time) {
        if (TextUtils.isEmpty(tableName)) {
            ATLog.e(TAG, "deleteByTime() -> tableName is null or empty!");
            return;
        }

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.delete(tableName, WHERE_CLAUSE_TIME_EQ, new String[]{time + ""});
            db.setTransactionSuccessful();
            ATLog.i(TAG, "deleteByTime from table " + tableName + " ->" + time);
        } catch (Exception e) {
            ATLog.e(TAG, "deleteByTime from table " + tableName + " exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    public void avoidIdUnlimitedGrowth(String tableName) {
        if (TextUtils.isEmpty(tableName)) {
            ATLog.e(TAG, "avoidIdUnlimitedGrowth() -> tableName is null or empty!");
            return;
        }

        int currentId = getMaxId(tableName);
        ATLog.d(TAG, "avoidIdUnlimitedGrowth() -> currentId is:" + currentId);

        Object timeObj = SpUtils.getParam(ApexCache.getInstance().getContext(),
                (tableName + Constant.SP_KEY_RESET_ID_TIME),
                Constant.SP_DEF_VAL_RESET_ID_TIME);
        Long lastResetTime = Long.valueOf(String.valueOf(timeObj));
        ATLog.d(TAG, "avoidIdUnlimitedGrowth() -> lastResetTime is:" + lastResetTime);

        if (currentId <= DbConstant.MAX_ID_TABLE
                && (System.currentTimeMillis() - lastResetTime) < DbConstant.RESET_ID_INTERVAL) {
            ATLog.d(TAG, "avoidIdUnlimitedGrowth() -> don't need to deal");
            return;
        }

        TreeMap<Long, TrackEvent> tp = query(tableName);
        if (null == tp) {
            ATLog.e(TAG, "avoidIdUnlimitedGrowth() -> tp is null!");
            return;
        }

        if (tp.isEmpty()) {
            ATLog.d(TAG, "avoidIdUnlimitedGrowth() -> tp is empty! only wipeDataAndResetId!");
            wipeDataAndResetId(tableName);
            return;
        }

        mReentrantLock.lock();
        try {
            wipeDataAndResetId(tableName);
            insertTreeMap(tableName, tp);
            ATLog.d(TAG, "avoidIdUnlimitedGrowth() -> successful!");
        } catch (Exception e) {
            ATLog.e(TAG, "avoidIdUnlimitedGrowth() -> exception!");
        } finally {
            mReentrantLock.unlock();
        }
    }

    private synchronized void wipeDataAndResetId(String tableName) {
        if (TextUtils.isEmpty(tableName)) {
            ATLog.e(TAG, "wipeDataAndResetId() -> tableName is null or empty!");
            return;
        }

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.execSQL(DbConstant.SQL_WIPE_DATA + tableName);
            db.execSQL(DbConstant.SQL_RESET_ID + "'" + tableName + "'");
            db.setTransactionSuccessful();
            SpUtils.putParam(ApexCache.getInstance().getContext(),
                    (tableName + Constant.SP_KEY_RESET_ID_TIME),
                    System.currentTimeMillis());
            ATLog.d(TAG, "wipeDataAndResetId() -> wipe and reset successful!");
        } catch (Exception e) {
            ATLog.e(TAG, "wipeDataAndResetId() -> exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    private static final String ORDER_DESC = " desc";
    private static final String LIMIT = "1";

    public int getMaxId(String tableName) {
        int maxId = DbConstant.MAX_ID_DEF;
        if (TextUtils.isEmpty(tableName)) {
            ATLog.e(TAG, "getMaxId() -> tableName is null or empty!");
            return maxId;
        }

        SQLiteDatabase db = openDatabase();

        Cursor cursor = db.query(DbConstant.TABLE_INSTANT_ERR,
                new String[]{DbConstant.FIELD_ID},
                null,
                null,
                null,
                null,
                DbConstant.FIELD_ID + ORDER_DESC,
                LIMIT);

        if (null != cursor) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(DbConstant.FIELD_ID);
                maxId = cursor.getInt(idIndex);
            }
            cursor.close();
        }

        closeDatabase();
        return maxId;
    }


}
