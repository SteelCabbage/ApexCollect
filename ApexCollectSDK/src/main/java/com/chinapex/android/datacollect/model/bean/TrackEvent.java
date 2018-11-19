package com.chinapex.android.datacollect.model.bean;

/**
 * @author SteelCabbage
 * @date 2018/11/16
 */
public class TrackEvent {

    /**
     * 可选参数, 0: delay (default), 1: instant
     */
    private final int mode;

    /**
     * 可选参数, 0: custom (default), 1: cold, 2: click, 4: pv ...
     */
    private final int eventType;

    /**
     * 可选参数, 别名
     */
    private final String label;

    /**
     * 可选参数, 具体值, JSON
     */
    private final String value;

    private TrackEvent(EventBuilder eventBuilder) {
        this.mode = eventBuilder.mode;
        this.eventType = eventBuilder.eventType;
        this.label = eventBuilder.label;
        this.value = eventBuilder.value;
    }

    public int getMode() {
        return mode;
    }

    public int getEventType() {
        return eventType;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public static class EventBuilder {
        private int mode;
        private int eventType;
        private String label;
        private String value;

        public EventBuilder() {

        }

        public EventBuilder setMode(int mode) {
            this.mode = mode;
            return this;
        }

        public EventBuilder setEventType(int eventType) {
            this.eventType = eventType;
            return this;
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

    @Override
    public String toString() {
        return "TrackEvent{" +
                "mode=" + mode +
                ", eventType=" + eventType +
                ", label='" + label + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
