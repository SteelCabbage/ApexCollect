package com.chinapex.android.datacollect.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author SteelCabbage
 * @date 2018/11/14
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getSimpleName();
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "apex_data";


    DbHelper(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    private DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
//        SQLiteDatabase.loadLibs(context);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建延时上报的表
        db.execSQL(DbConstant.SQL_CREATE_DELAY_REPORT);
        // 创建即时上报错误时的表
        db.execSQL(DbConstant.SQL_CREATE_INSTANT_ERR);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
