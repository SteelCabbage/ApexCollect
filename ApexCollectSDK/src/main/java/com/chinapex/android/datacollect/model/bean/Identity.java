package com.chinapex.android.datacollect.model.bean;

/**
 * @author SteelCabbage
 * @date 2018/11/19
 */
public class Identity {
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

    @Override
    public String toString() {
        return "Identity{" +
                "uid='" + uid + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
