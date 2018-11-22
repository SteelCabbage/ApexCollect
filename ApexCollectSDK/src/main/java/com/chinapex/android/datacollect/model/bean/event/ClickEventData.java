package com.chinapex.android.datacollect.model.bean.event;

import android.widget.Button;

/**
 * @author SteelCabbage
 * @date 2018/11/22
 */
public class ClickEventData {
    private int eventType;
    private String label;
    private ValueBean value;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ValueBean getValue() {
        return value;
    }

    public void setValue(ValueBean value) {
        this.value = value;
    }

    public static class ValueBean {

        /**
         * "alpha": 1,
         *                 "viewPathMD5": "cf429db7f07064a8",
         *                 "frame": "{\"x\":0.000000,\"y\":0.010000,\"width\":384.000000,\"height\":90.000000}",
         *                 "ViewPath": "ApexAssetMainViewCell(0:0)/UITableView(0)/UIView(0)/UIView(0) =>
         *                 ApexAssetMainController(ETH)",
         *                 "pageTitle": "ETH",
         *                 "content":"button上的字",
         *                 "timeStamp": "1542611704.378851",
         *                 "uid": "b44ea697-24c6-4873-9e60-2bae86d2023a"
         */


    }
}
