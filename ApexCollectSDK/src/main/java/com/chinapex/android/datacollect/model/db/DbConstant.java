package com.chinapex.android.datacollect.model.db;

/**
 * @author SteelCabbage
 * @date 2018/11/19
 */
public class DbConstant {

    public static final String TABLE_DELAY_REPORT = "delay_report";
    public static final String TABLE_INSTANT_ERR = "instant_err";

    public static final String FIELD_ID = "_id";
    public static final String FIELD_MODE = "mode";
    public static final String FIELD_EVENT_TYPE = "event_type";
    public static final String FIELD_LABEL = "label";
    public static final String FIELD_TIME = "time";
    public static final String FIELD_VALUE = "value";

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

}
