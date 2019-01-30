package com.chinapex.android.monitor.global;

/**
 * @author : Seven Qiu
 * @date : 2019/1/18
 */
public class Constant {

    /**
     * net
     */
    public static final String HOSTNAME_VERIFIER = "139.219.5.206";
    public static final long CONNECT_TIMEOUT = 5;
    public static final long READ_TIMEOUT = 5;
    public static final long WRITE_TIMEOUT = 5;
    public static final int NET_ERROR = -1;
    public static final int NET_SUCCESS = 1;
    public static final int NET_BODY_NULL = 0;
    public static final String HEADER_KEY = "";
    public static final String HEADER_VALUE = "";

    /**
     * sp
     */
    public static final String SP_KEY_EVENT_CONFIG = "event_config";
    public static final String SP_KEY_EVENT_PV = "event_pv";
    public static final String SP_KEY_EVENT_CLICK = "event_click";
    public static final String SP_KEY_EVENT_LIST = "event_list";
    public static final String SP_VALUE_DEFAULT_EVENT = "";

    /**
     * 定义的事件类型
     */
    public static final int DEFINE_EVENT_TYPE_CLICK = 1 << 1;
    public static final int DEFINE_EVENT_TYPE_PV = 1 << 2;
    public static final int DEFINE_EVENT_TYPE_LIST = 1 << 3;

    /**
     * default base info
     */
    public static final String DEFAULT_APP_VERSION = "defaultAppVersion";
}
