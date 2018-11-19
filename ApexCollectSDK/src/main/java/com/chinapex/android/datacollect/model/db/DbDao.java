package com.chinapex.android.datacollect.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.utils.ATLog;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author SteelCabbage
 * @date 2018/11/14
 */

public class DbDao {

    private static final String TAG = DbDao.class.getSimpleName();

//    private static final String DB_PWD = "";

    private static DbDao sDbDao;

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private DbHelper mDbHelper;

    private SQLiteDatabase mDatabase;

    private DbDao(Context context) {
        mDbHelper = new DbHelper(context);
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

    public synchronized void insert(String tableName, TrackEvent trackEvent) {
        if (TextUtils.isEmpty(tableName) || null == trackEvent) {
            ATLog.e(TAG, "insert() -> tableName or trackEvent is null!");
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstant.FIELD_MODE, trackEvent.getMode());
        contentValues.put(DbConstant.FIELD_EVENT_TYPE, trackEvent.getEventType());
        contentValues.put(DbConstant.FIELD_LABEL, trackEvent.getLabel());
        contentValues.put(DbConstant.FIELD_TIME, System.currentTimeMillis());
        contentValues.put(DbConstant.FIELD_VALUE, trackEvent.getValue());

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.insertOrThrow(tableName, null, contentValues);
            db.setTransactionSuccessful();
            ATLog.i(TAG, "insert into " + tableName + " ->" + trackEvent);
        } catch (SQLException e) {
            ATLog.e(TAG, "insert into " + tableName + " exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
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


}
