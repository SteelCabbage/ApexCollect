package com.chinapex.android.monitor.bean;

/**
 * @author : Seven
 * @date : 2019/1/8
 */
public class StatisticsBean {

    private String viewPathMD5;
    private String eventLabel;
    private int color;
    private int clickCount;
    private float clickProportion;

    public StatisticsBean() {
    }

    public StatisticsBean(String viewPathMD5, String eventLabel, int clickCount, float clickProportion, int color) {
        this.viewPathMD5 = viewPathMD5;
        this.eventLabel = eventLabel;
        this.clickCount = clickCount;
        this.clickProportion = clickProportion;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getEventLabel() {
        return eventLabel;
    }

    public void setEventLabel(String eventLabel) {
        this.eventLabel = eventLabel;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public float getClickProportion() {
        return clickProportion;
    }

    public void setClickProportion(float clickProportion) {
        this.clickProportion = clickProportion;
    }

    public String getViewPathMD5() {
        return viewPathMD5;
    }

    public void setViewPathMD5(String viewPathMD5) {
        this.viewPathMD5 = viewPathMD5;
    }
}
