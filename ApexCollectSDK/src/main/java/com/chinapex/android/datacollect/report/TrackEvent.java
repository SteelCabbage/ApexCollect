package com.chinapex.android.datacollect.report;

/**
 * @author SteelCabbage
 * @date 2018/11/16
 */
public class TrackEvent {

    /**
     * 必传参数, 例如: category_click, category_page, category_order
     */
    private final String category;

    /**
     * 必传参数, 例如: action_signIn, action_signOut, action_price
     */
    private final String action;

    /**
     * 可选参数, 别名
     */
    private final String label;

    /**
     * 可选参数, 具体值, JSON
     */
    private final String value;

    private TrackEvent(EventBuilder eventBuilder) {
        this.category = eventBuilder.category;
        this.action = eventBuilder.action;
        this.label = eventBuilder.label;
        this.value = eventBuilder.value;
    }

    public String getCategory() {
        return category;
    }

    public String getAction() {
        return action;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public static class EventBuilder {
        private final String category;
        private final String action;
        private String label;
        private String value;

        public EventBuilder(String category, String action) {
            this.category = category;
            this.action = action;
        }

        public EventBuilder setLabel(String label) {
            this.label = label;
            return this;
        }

        public EventBuilder setValue(String value) {
            this.value = value;
            return this;
        }

        public TrackEvent build() {
            return new TrackEvent(this);
        }
    }

}
