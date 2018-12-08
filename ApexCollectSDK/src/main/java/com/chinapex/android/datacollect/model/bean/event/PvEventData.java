package com.chinapex.android.datacollect.model.bean.event;

import java.util.List;

/**
 * @author SteelCabbage
 * @date 2018/11/22
 */
public class PvEventData {
    private int eventType;
    private String label;
    private String userId;
    private String country;
    private String province;
    private String city;
    private long timeStamp;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ValueBean getValue() {
        return value;
    }

    public void setValue(ValueBean value) {
        this.value = value;
    }

    public static class ValueBean {

        /**
         * "reference": "ApexAssetMainController",      From
         * "pageTitle": "111111",
         * "pageClassName": "ApexAccountDetailController",
         * "durationTime": "1.45",       // 改为long 毫秒
         * "customPageUrl": "ddddd\/\/ddddd.ddddd",   // ios代码埋点
         * "customPageProperties": {                  // ios代码埋点
         *                           "a": "b",
         *                           "c": "d"
         *                         },
         * "customPageTitle": "ddddddd",              // ios代码埋点
         * "exposures": [
         *                     {
         *                         "viewPath": "UITableView(0)/UIView(0) => ApexAssetMainController(ETH)",
         *                         "pageClassName": "ApexAssetMainController",
         *                         "viewPathMD5": "798d070f2f9aba94",
         *                         "elements": {
         *                             "0:0": {
         *                                 "viewPath": "Cell[0:0]/UITableView(0)/UIView(0) => ApexAssetMainController(ETH)",
         *                                 "index": "0:0",
         *                                 "isShowing": true,
         *                                 "count": 1,
         *                                 "viewPathMD5": "f0b2a03bc1c662cb",
         *                                 "dataDict": {
         *                                     "image_url": "https://tracker.chinapex.com.cn/tool/static/icon84/84_ZRX.png",
         *                                     "symbol": "ZRX",
         *                                     "precision": "18",
         *                                     "type": "Erc20",
         *                                     "hex_hash": "0xe41d2489571d322189246dafa5ebde1f4699f498",
         *                                     "name": "ZRX"
         *                                 }
         *                             }
         *                         }
         *                     }
         *                 ],
         *
         */

        /**

         */

        private String reference;
        private String pageTitle;
        private String pageClassName;
        private long durationTime;

        // TODO: 2018/11/26 0026  列表曝光率
        /**
         * 列表曝光率
         */
        private List<Object> exposures;


        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getPageTitle() {
            return pageTitle;
        }

        public void setPageTitle(String pageTitle) {
            this.pageTitle = pageTitle;
        }

        public String getPageClassName() {
            return pageClassName;
        }

        public void setPageClassName(String pageClassName) {
            this.pageClassName = pageClassName;
        }

        public long getDurationTime() {
            return durationTime;
        }

        public void setDurationTime(long durationTime) {
            this.durationTime = durationTime;
        }

        public List<Object> getExposures() {
            return exposures;
        }

        public void setExposures(List<Object> exposures) {
            this.exposures = exposures;
        }
    }
}
