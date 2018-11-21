package com.chinapex.android.datacollect.model.bean;

import java.util.List;

/**
 * @author SteelCabbage
 * @date 2018/11/19
 */
public class Identity {
    private String uid;
    private String uuid;
    private List<String> deviceIds;

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

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    @Override
    public String toString() {
        return "Identity{" +
                "uid='" + uid + '\'' +
                ", uuid='" + uuid + '\'' +
                ", deviceIds=" + deviceIds +
                '}';
    }
}
