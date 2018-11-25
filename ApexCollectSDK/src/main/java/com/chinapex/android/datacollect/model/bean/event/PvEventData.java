package com.chinapex.android.datacollect.model.bean.event;

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

    public ValueBean getValue() {
        return value;
    }

    public void setValue(ValueBean value) {
        this.value = value;
    }

    public static class ValueBean {

        /**
         * "timeStamp": 1542938209329,     毫秒
         * "reference": "ApexAssetMainController",      From
         * "pageTitle": "111111",
         * "pageClassName": "ApexAccountDetailController",
         * "durationTime": "1.45",
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

    }
}
