package com.chinapex.android.datacollect.model.bean.request;

import com.chinapex.android.datacollect.model.bean.Identity;

import java.util.List;

/**
 * @author SteelCabbage
 * @date 2018/11/19
 */
public class AnalyticsReport {
    private long reportTime;
    private Identity identity;
    private List<String> eventDatas;

    public long getReportTime() {
        return reportTime;
    }

    public void setReportTime(long reportTime) {
        this.reportTime = reportTime;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public List<String> getEventDatas() {
        return eventDatas;
    }

    public void setEventDatas(List<String> eventDatas) {
        this.eventDatas = eventDatas;
    }

    @Override
    public String toString() {
        return "AnalyticsReport{" +
                "reportTime=" + reportTime +
                ", identity=" + identity +
                ", eventDatas=" + eventDatas +
                '}';
    }
}
