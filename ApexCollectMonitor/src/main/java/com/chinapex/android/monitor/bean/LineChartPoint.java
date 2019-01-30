package com.chinapex.android.monitor.bean;

import java.util.Date;

/**
 * @author wyhusky
 * @date 2019/1/8
 */
public class LineChartPoint {
    private float x;
    private float y;
    private Date date;
    private int count;

    public LineChartPoint() {

    }

    public LineChartPoint(float x, float y, Date date, int count) {
        this.x = x;
        this.y = y;
        this.date = date;
        this.count = count;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "[LineChartPoint]: x=" + x + " y=" + y + " date:"
                + date.toString() + " count=" + count;
    }
}
