package com.chinapex.android.datacollect.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            mDatabase = mDbHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            mDatabase.close();
        }
    }


}
