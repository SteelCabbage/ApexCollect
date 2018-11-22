package com.chinapex.android.datacollect.model.bean.event;

/**
 * @author SteelCabbage
 * @date 2018/11/22
 */
public class PvEventData {
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
         * "uid": "b44ea697-24c6-4873-9e60-2bae86d2023a",
         * "timeStamp": "1542611704.881565",  毫秒
         * "pageClassName": "ApexAssetMainController",
         * "durationTime": "17.57"   秒
         * "reference": "ApexAssetMainController"       From
         */

    }
}
