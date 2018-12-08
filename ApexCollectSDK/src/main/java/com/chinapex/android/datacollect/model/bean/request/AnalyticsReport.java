package com.chinapex.android.datacollect.model.bean.request;

import java.util.List;

/**
 * @author SteelCabbage
 * @date 2018/11/19
 */
public class AnalyticsReport {

    /**
     * company : apex
     * terminal : WEB/smallwec/iOS/android
     * data : {"reportTime":"","language":"cn","uuid":"androidId","deviceID":["GSM:abcdefghigklmn","CDMA:abcdefghigklmn",
     * "GSM:abcdefghigklmn"],"events":[]}
     */

    private String company;
    private String terminal;
    private String configVersion;
    private DataBean data;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(String configVersion) {
        this.configVersion = configVersion;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * reportTime :
         * language : cn
         * uuid : androidId
         * deviceID : ["GSM:abcdefghigklmn","CDMA:abcdefghigklmn","GSM:abcdefghigklmn"]
         * events : []
         */

        private long reportTime;
        private String language;
        private String uuid;
        private List<String> deviceID;
        private List<Object> events;

        public long getReportTime() {
            return reportTime;
        }

        public void setReportTime(long reportTime) {
            this.reportTime = reportTime;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public List<String> getDeviceID() {
            return deviceID;
        }

        public void setDeviceID(List<String> deviceID) {
            this.deviceID = deviceID;
        }

        public List<Object> getEvents() {
            return events;
        }

        public void setEvents(List<Object> events) {
            this.events = events;
        }
    }
}
