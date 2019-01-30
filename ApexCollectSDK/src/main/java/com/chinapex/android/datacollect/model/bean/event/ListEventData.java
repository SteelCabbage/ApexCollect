package com.chinapex.android.datacollect.model.bean.event;

import java.util.Map;

/**
 * @author SteelCabbage
 * @date 2019/01/07
 */
public class ListEventData {
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
         * pageClassName: ""
         * listIdMD5: ""
         * exposures: {}
         */

        private String pageClassName;
        private String listIdMD5;
        private Map<String, ListItem> exposures;

        public String getPageClassName() {
            return pageClassName;
        }

        public void setPageClassName(String pageClassName) {
            this.pageClassName = pageClassName;
        }

        public String getListIdMD5() {
            return listIdMD5;
        }

        public void setListIdMD5(String listIdMD5) {
            this.listIdMD5 = listIdMD5;
        }

        public Map<String, ListItem> getExposures() {
            return exposures;
        }

        public void setExposures(Map<String, ListItem> exposures) {
            this.exposures = exposures;
        }

        public static class ListItem {
            /**
             * index: String
             * alias: String
             * impressions: String
             * dataKey: 根据dataPath反射拿到的值
             */

            private String index;
            private String dataKey;
            private String alias;
            private String impressions;

            public String getIndex() {
                return index;
            }

            public void setIndex(String index) {
                this.index = index;
            }

            public String getDataKey() {
                return dataKey;
            }

            public void setDataKey(String dataKey) {
                this.dataKey = dataKey;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getImpressions() {
                return impressions;
            }

            public void setImpressions(String impressions) {
                this.impressions = impressions;
            }
        }
    }
}
