package com.chinapex.android.datacollect.model.bean.event;

/**
 * @author SteelCabbage
 * @date 2018/11/19
 */
public class ColdEventData {

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
         * uid (用户id)
         * "appName" = "APEX Wallet";
         * "appVersion" = "1.1.1";
         * "screenInfo" = "[width:414.000000,height:896.000000, density:2]";//
         * "os" = "iOS or Android";//
         * "osVersion" = "12.0";
         * "brandName" = "HUAWEI or Apple"; //
         * "customVersion" = "EMUI 8.0"; // apple : 同OSVersion
         * "manufacturer" = "iPhone 6 or Mate 20";
         * "deviceModel" = "型号",
         * "apiKey" = "xxxxxxxxxxxxxxxxx"
         */
        private String uid;
        private String appName;
        private String appVersion;
        private String screenInfo;
        private String os;
        private String osVersion;
        private String brandName;
        private String customVersion;
        private String manufacturer;
        private String deviceModel;
        private String apiKey;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getScreenInfo() {
            return screenInfo;
        }

        public void setScreenInfo(String screenInfo) {
            this.screenInfo = screenInfo;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getCustomVersion() {
            return customVersion;
        }

        public void setCustomVersion(String customVersion) {
            this.customVersion = customVersion;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

    }
}
