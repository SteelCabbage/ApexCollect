package com.chinapex.android.datacollect.model.bean.event;

/**
 * @author SteelCabbage
 * @date 2018/11/20
 */
public class EventData {
    private int eventType;
    private String label;
    private IEventData value;

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

    public IEventData getValue() {
        return value;
    }

    public void setValue(IEventData value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "EventData{" +
                "eventType=" + eventType +
                ", label='" + label + '\'' +
                ", value=" + value +
                '}';
    }
}
