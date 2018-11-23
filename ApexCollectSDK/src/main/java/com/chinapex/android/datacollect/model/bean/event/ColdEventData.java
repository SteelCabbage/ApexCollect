package com.chinapex.android.datacollect.model.bean.event;

/**
 * @author SteelCabbage
 * @date 2018/11/19
 */
public class ColdEventData {

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
         * "apiKey": "apikey",
         * "customVersion": "12.1",
         * "os": "iOS",
         * "osVersion": "12.1",
         * "deviceModel": "iPhone 5",
         * "manufacturer": "Apple",
         * "brandName": "Apple, Honor",
         * "appName": "APEX Wallet",
         * "appVersion": "1.1.1",
         * "screenWidth": "",
         * "screenHeight": "",
         * "screenDensity": ""
         */

        private String apiKey;
        private String customVersion;
        private String os;
        private String osVersion;
        private String deviceModel;
        private String manufacturer;
        private String brandName;
        private String appName;
        private String appVersion;
        private String screenWidth;
        private String screenHeight;
        private String screenDensity;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getCustomVersion() {
            return customVersion;
        }

        public void setCustomVersion(String customVersion) {
            this.customVersion = customVersion;
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

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
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

        public String getScreenWidth() {
            return screenWidth;
        }

        public void setScreenWidth(String screenWidth) {
            this.screenWidth = screenWidth;
        }

        public String getScreenHeight() {
            return screenHeight;
        }

        public void setScreenHeight(String screenHeight) {
            this.screenHeight = screenHeight;
        }

        public String getScreenDensity() {
            return screenDensity;
        }

        public void setScreenDensity(String screenDensity) {
            this.screenDensity = screenDensity;
        }
    }
}
