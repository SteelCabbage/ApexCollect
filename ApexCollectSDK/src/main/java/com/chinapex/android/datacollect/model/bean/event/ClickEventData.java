package com.chinapex.android.datacollect.model.bean.event;

/**
 * @author SteelCabbage
 * @date 2018/11/22
 */
public class ClickEventData {
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
         * "viewPath": "_UIButtonBarButton(9223372036854775807)&_invoke:forEvent: => ApexAssetMainController(NEO)",
         * "md5": "事件唯一id",
         * "pageClassName": "所属的activity名字，或者当前栈顶的activity名字",
         * "definedPage": 被定义在哪个页面
         * "alias":"事件别名"
         * "preMD5":"若有大的改动, 与之前版本的同一事件关联"
         * "content": "button上的字",
         */

        private String viewPath;
        /**
         * 普通:
         * MD5 ( viewPath )
         *
         * 列表:
         * MD5 (listId + dataKey)
         */
        private String md5;
        private String pageClassName;
        private String definedPage;
        private String alias;
        private String preMD5;
        private String content;

        public String getViewPath() {
            return viewPath;
        }

        public void setViewPath(String viewPath) {
            this.viewPath = viewPath;
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

        public String getDefinedPage() {
            return definedPage;
        }

        public void setDefinedPage(String definedPage) {
            this.definedPage = definedPage;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getPreMD5() {
            return preMD5;
        }

        public void setPreMD5(String preMD5) {
            this.preMD5 = preMD5;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
