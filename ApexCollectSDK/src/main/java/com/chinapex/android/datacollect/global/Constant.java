package com.chinapex.android.datacollect.global;

/**
 * @author SteelCabbage
 * @date 2018/11/16
 */
public class Constant {

    /**
     * default base info
     */
    public static final String DEFAULT_ANDROID_ID = "defaultAndroidId";
    public static final String DEFAULT_APP_NAME = "defaultAppName";
    public static final String DEFAULT_APP_VERSION = "defaultAppVersion";

    /**
     * Controller_Constant
     */
    public static final String CONTROLLER_TASK = "TaskController";
    public static final String CONTROLLER_PHONE_STATE = "PhoneStateController";

    /**
     * 时间
     */
    public static final long CHECK_INSTANT_ERR_INTERVAL = 1000 * 60 * 2;
    public static final long RESET_ID_INTERVAL = 1000 * 60 * 60 * 24 * 3;
    public static final long DELAY_REPORT_INTERVAL = 1000 * 60 * 60;

    /**
     * SP
     */
    public static final String SP_KEY_RESET_ID_TIME = "_resetIdTime";
    public static final long SP_DEF_VAL_RESET_ID_TIME = 0;


    /**
     * Net
     */
//    public static final String SERVER_ROOT = "http://172.16.13.129:8080/";
    public static final String SERVER_ROOT = "https://www.baidu.com";
    //    public static final String HOSTNAME_VERIFIER = "172.16.13.129";
    public static final String HOSTNAME_VERIFIER = "www.baidu.com";
    public static final long CONNECT_TIMEOUT = 5;
    public static final long READ_TIMEOUT = 5;
    public static final long WRITE_TIMEOUT = 5;
    public static final int NET_ERROR = -1;
    public static final int NET_SUCCESS = 1;
    public static final int NET_BODY_NULL = 0;
    public static final String HEADER_KEY = "";
    public static final String HEADER_VALUE = "";
    //    public static final String URL_DELAY_REPORT = SERVER_ROOT + "eventReport/Android";
    public static final String URL_DELAY_REPORT = SERVER_ROOT + "";
    //    public static final String URL_INSTANT_REPORT = SERVER_ROOT + "eventReport/Android";
    public static final String URL_INSTANT_REPORT = SERVER_ROOT + "";


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


    /**
     * 事件label
     */
    public static final String EVENT_LABEL_COLD = "cold";
    public static final String EVENT_LABEL_CLICK = "click";
    public static final String EVENT_LABEL_PV = "pv";

}
