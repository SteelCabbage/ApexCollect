package com.chinapex.android.datacollect.model.bean.event;

/**
 * @author SteelCabbage
 * @date 2018/11/19
 */
public class ColdEventData implements IEventData {
    /**
     * uid (用户id)
     * uuid (AndroidId)
     * deviceIds (当为Android手机时，需用户授权获取设备id)
     * "appName" = "APEX Wallet";
     * "appVersion" = "1.1.1";
     * "screenInfo" = "[width:414.000000,height:896.000000, density:2]";//
     * "os" = "iOS or Android";//
     * "osVersion" = "12.0";
     * "brandName" = "HUAWEI or Apple"; //
     * "customVersion" = "EMUI 8.0"; // apple : 同OSVersion
     * "deviceName" = "iPhone 6 or Mate 20";//
     * "apiKey" = "xxxxxxxxxxxxxxxxx"
     * "eventType" = "cold initialize";
     */

    private String uid;
    private String uuid;
    private String imei;
    private String appName;
    private String appVersion;
    private String screenInfo;
    private String os;
    private String osVersion;
    private String brandName;
    private String customVersion;
    private String deviceName;
    private String apiKey;
    private String eventType;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "ColdEventData{" +
                "uid='" + uid + '\'' +
                ", uuid='" + uuid + '\'' +
                ", imei='" + imei + '\'' +
                ", appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", screenInfo='" + screenInfo + '\'' +
                ", os='" + os + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", brandName='" + brandName + '\'' +
                ", customVersion='" + customVersion + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
