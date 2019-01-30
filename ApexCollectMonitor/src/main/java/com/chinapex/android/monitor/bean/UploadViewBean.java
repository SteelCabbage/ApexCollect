package com.chinapex.android.monitor.bean;

/**
 * @author SteelCabbage
 * @date 2019/01/22
 */
public class UploadViewBean {

    /**
     * 定义的事件类型
     */
    private int eventType;
    /**
     * MD5值
     */
    private String md5;
    /**
     * 别名, 即事件名称
     */
    private String alias;

    /**
     * 列表事件独有
     */
    private String listId;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }
}
