package com.chinapex.android.datacollect.global;

/**
 * @author SteelCabbage
 * @date 2018/11/16
 */
public class Constant {

    /**
     * Net
     */
    public static final String SERVER_ROOT = "https://www.baidu.com/";
    public static final String HOSTNAME_VERIFIER = "www.baidu.com";
    public static final long CONNECT_TIMEOUT = 5;
    public static final long READ_TIMEOUT = 5;
    public static final long WRITE_TIMEOUT = 5;
    public static final int NET_ERROR = -1;
    public static final int NET_SUCCESS = 1;
    public static final int NET_BODY_NULL = 0;
    public static final String HEADER_KEY = "";
    public static final String HEADER_VALUE = "";
    public static final String URL_EVENT = SERVER_ROOT + "";


    /**
     * 上报策略
     */
    public static final int MODE_DELAY = 0;
    public static final int MODE_INSTANT = 1;


    /**
     * 事件类型
     */
    public static final int EVENT_TYPE_CUSTOM = 0;
    public static final int EVENT_TYPE_COLD = 1;
    public static final int EVENT_TYPE_CLICK = 1 << 1;
    public static final int EVENT_TYPE_PV = 1 << 2;


}
