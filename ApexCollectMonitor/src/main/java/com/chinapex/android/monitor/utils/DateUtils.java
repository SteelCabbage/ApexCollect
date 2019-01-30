package com.chinapex.android.monitor.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wyhusky
 * @date 2019/1/8
 */
public class DateUtils {

    private static final DateFormat DATE_FORMAT_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final DateFormat DATE_FORMAT_2 = new SimpleDateFormat("MM.dd");

    public static Date string2Date(String str) {
        Date date = new Date();
        try {
            date = DATE_FORMAT_1.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String date2MonthDayString(Date date) {
        return DATE_FORMAT_2.format(date);
    }

    public static String date2TimeString(Date date) {
        return DATE_FORMAT_1.format(date);
    }
}
