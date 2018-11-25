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
         * "pageClassName": "所属的activity名字，或者当前栈顶的activity名字",
         * "timeStamp": 1542938208826,
         * "viewPath": "_UIButtonBarButton(9223372036854775807)&_invoke:forEvent: => ApexAssetMainController(NEO)",
         * "viewPathMD5": "51870df90a2633ba",
         * "content": "button上的字",
         * "frame": "{\"x\":0.000000,\"y\":0.000000,\"width\":43.000000,\"height\":44.000000}",
         * "alpha": 1,
         * "invocation": "_invoke:forEvent:"
         */

        private String pageClassName;
        private long timeStamp;
        private String viewPath;
        private String viewPathMD5;
        private String content;
        private String frame;
        private float alpha;
        private String invocation;

        public String getPageClassName() {
            return pageClassName;
        }

        public void setPageClassName(String pageClassName) {
            this.pageClassName = pageClassName;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getViewPath() {
            return viewPath;
        }

        public void setViewPath(String viewPath) {
            this.viewPath = viewPath;
        }

        public String getViewPathMD5() {
            return viewPathMD5;
        }

        public void setViewPathMD5(String viewPathMD5) {
            this.viewPathMD5 = viewPathMD5;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFrame() {
            return frame;
        }

        public void setFrame(String frame) {
            this.frame = frame;
        }

        public float getAlpha() {
            return alpha;
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        public String getInvocation() {
            return invocation;
        }

        public void setInvocation(String invocation) {
            this.invocation = invocation;
        }
    }
}
