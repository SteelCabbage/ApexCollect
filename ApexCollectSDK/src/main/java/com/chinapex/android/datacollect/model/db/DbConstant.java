package com.chinapex.android.datacollect.model.db;

/**
 * @author SteelCabbage
 * @date 2018/11/19
 */
public class DbConstant {

    /**
     * db表
     */
    public static final String TABLE_DELAY_REPORT = "delay_report";
    public static final String TABLE_INSTANT_ERR = "instant_err";


    /**
     * db字段
     */
    public static final String FIELD_ID = "_id";
    public static final String FIELD_MODE = "mode";
    public static final String FIELD_EVENT_TYPE = "event_type";
    public static final String FIELD_LABEL = "label";
    public static final String FIELD_TIME = "time";
    public static final String FIELD_VALUE = "value";


    /**
     * SQL语句
     */
    public static final String SQL_CREATE_DELAY_REPORT = "create table " + TABLE_DELAY_REPORT
            + " (" + FIELD_ID + " integer primary key autoincrement, "
            + FIELD_MODE + " integer, "
            + FIELD_EVENT_TYPE + " integer, "
            + FIELD_LABEL + " text, "
            + FIELD_TIME + " integer, "
            + FIELD_VALUE + " text)";

    public static final String SQL_CREATE_INSTANT_ERR = "create table " + TABLE_INSTANT_ERR
            + " (" + FIELD_ID + " integer primary key autoincrement, "
            + FIELD_MODE + " integer, "
            + FIELD_EVENT_TYPE + " integer, "
            + FIELD_LABEL + " text, "
            + FIELD_TIME + " integer, "
            + FIELD_VALUE + " text)";

    /**
     * 防止数据库db无限制增长
     */
    public static final long RESET_ID_INTERVAL = 1000 * 60 * 60 * 24 * 3;
    public static final String SQL_WIPE_DATA = "delete from ";
    public static final String SQL_RESET_ID = "UPDATE sqlite_sequence SET seq = 0 WHERE name = ";
    public static final int MAX_ID_DEF = -1;
    public static final int MAX_ID_TABLE = 1000;

}
