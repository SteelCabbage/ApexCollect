package com.chinapex.android.datacollect.model.bean.request;

import java.util.List;

/**
 * @author SteelCabbage
 * @date 2018/11/21
 */
public class Test {

    /**
     * reportTime : 1542611717.178961
     * identity : {"uid":"","uuid":"b44ea697-24c6-4873-9e60-2bae86d2023a"}
     * eventData : [{"lable":"click","eventType":2,"value":{"timeStamp":"1542611704.378851"}}]
     */

    private String reportTime;
    private IdentityBean identity;
    private List<EventDataBean> eventData;

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public IdentityBean getIdentity() {
        return identity;
    }

    public void setIdentity(IdentityBean identity) {
        this.identity = identity;
    }

    public List<EventDataBean> getEventData() {
        return eventData;
    }

    public void setEventData(List<EventDataBean> eventData) {
        this.eventData = eventData;
    }

    public static class IdentityBean {
        /**
         * uid :
         * uuid : b44ea697-24c6-4873-9e60-2bae86d2023a
         */

        private String uid;
        private String uuid;

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
    }

    public static class EventDataBean {
        /**
         * lable : click
         * eventType : 2
         * value : {"timeStamp":"1542611704.378851"}
         */

        private String lable;
        private int eventType;
        private ValueBean value;

        public String getLable() {
            return lable;
        }

        public void setLable(String lable) {
            this.lable = lable;
        }

        public int getEventType() {
            return eventType;
        }

        public void setEventType(int eventType) {
            this.eventType = eventType;
        }

        public ValueBean getValue() {
            return value;
        }

        public void setValue(ValueBean value) {
            this.value = value;
        }

        public static class ValueBean {
            /**
             * timeStamp : 1542611704.378851
             */

            private String timeStamp;

            public String getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
            }
        }
    }
}
