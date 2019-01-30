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
         * "alias":"事件别名"
         * "reference": "ApexAssetMainController",      From
         * "pageClassName": "ApexAccountDetailController",
         * "md5":"",
         * "durationTime": "1.45",       // 改为long 毫秒
         */

        private String alias;
        private String reference;
        private String pageClassName;
        /**
         * MD5 ( pageClassName )
         */
        private String md5;
        private long durationTime;

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
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

    }
}
