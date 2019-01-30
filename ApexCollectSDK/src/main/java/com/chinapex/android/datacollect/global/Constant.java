package com.chinapex.android.datacollect.global;

/**
 * @author SteelCabbage
 * @date 2018/11/16
 */
public class Constant {

    /**
     * viewPath size: 长度至少为2, 0:viewPath, 1:activityName, 2+:fragmentName
     */
    public static final int VIEW_PATH_SIZE = 2;
    public static final String SEPARATOR_SYSTEM_PATH = "#content";

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
    public static final String CONTROLLER_ANALYTICS_LISTENER = "AnalyticsListenerController";
    public static final String CONTROLLER_BROADCAST = "BroadcastController";
    public static final String CONTROLLER_PHONE_STATE = "PhoneStateController";
    public static final String CONTROLLER_LOCATION = "LocationController";

    /**
     * 百度地图
     */
    public static final String PROCESS_NAME_BAIDU_LOCATION = ":remote";

    /**
     * 配置文件
     */
    public static final long POLLING_TIME_UPDATE_CONFIG = 1000 * 30;

    /**
     * Settings 上报条数, 时间
     */
    public static final int REPORT_MAX_NUM_DEFAULT = 30;
    public static final int REPORT_MIN_NUM = 5;
    public static final long CHECK_INSTANT_ERR_INTERVAL_DEFAULT = 1000 * 60 * 2;
    public static final long CHECK_INSTANT_ERR_INTERVAL_MIN = 1000 * 60;
    public static final long DELAY_REPORT_INTERVAL_DEFAULT = 1000 * 60 * 5;
    public static final long DELAY_REPORT_INTERVAL_MIN = 1000 * 60 * 2;

    /**
     * coldEventData delay report time ( 定位，用户授权需耗时且不定 )
     */
    public static final long DELAY_REPORT_COLD_EVENT = 1000 * 15;

    /**
     * SP
     */
    public static final String SP_KEY_RESET_ID_TIME = "_resetIdTime";
    public static final long SP_DEF_VAL_RESET_ID_TIME = 0;
    public static final String SP_KEY_CONFIG_VERSION = "_configVersion";
    public static final String SP_DEF_VAL_CONFIG_VERSION = "defConfigVersion";
    public static final String SP_KEY_CONFIG = "_config";
    public static final String SP_DEF_VAL_CONFIG = "";


    /**
     * Net
     */
    public static final String NETWORK_TYPE_UNKNOWN = "unknown";
    public static final String NETWORK_TYPE_WIFI = "WIFI";
    public static final String NETWORK_TYPE_MOBILE = "MOBILE";
    public static final String NETWORK_TYPE_2G = "2G";
    public static final String NETWORK_TYPE_3G = "3G";
    public static final String NETWORK_TYPE_4G = "4G";
    public static final String SERVER_ROOT = "http://139.219.5.206:8083/";
    public static final String HOSTNAME_VERIFIER = "139.219.5.206";
    public static final long CONNECT_TIMEOUT = 5;
    public static final long READ_TIMEOUT = 5;
    public static final long WRITE_TIMEOUT = 5;
    public static final int NET_ERROR = -1;
    public static final int NET_SUCCESS = 1;
    public static final int NET_BODY_NULL = 0;
    public static final String HEADER_KEY = "";
    public static final String HEADER_VALUE = "";
    public static final String URL_DELAY_REPORT = SERVER_ROOT + "eventReport/Android";
    public static final String URL_INSTANT_REPORT = SERVER_ROOT + "eventReport/Android";


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
    public static final int EVENT_TYPE_LIST = 1 << 3;


    /**
     * 事件label
     */
    public static final String EVENT_LABEL_CUSTOM = "custom";
    public static final String EVENT_LABEL_COLD = "cold";
    public static final String EVENT_LABEL_CLICK = "click";
    public static final String EVENT_LABEL_PV = "pv";
    public static final String EVENT_LABEL_LIST_CLICK = "list_click";
    public static final String EVENT_LABEL_LIST = "list";


    /**
     * 上报json格式 company, terminal
     */
    public static final String COMPANY = "apex";
    public static final String TERMINAL = "android";

    /**
     * regex
     */
    public static final String REGEX_LIST_VIEW = "^ListView\\[\\d+\\]$";
    public static final String REGEX_GRID_VIEW = "^GridView\\[\\d+\\]$";
    public static final String SEPARATOR_DATA_PATH = "#";
    public static final String SEPARATOR_PAGE_NAME = "##";
    public static final String SEPARATOR_LIST_ITEM_GROUP = "###";
    public static final String SEPARATOR_LIST_ITEM_GROUP_ID = "####";

    /**
     * 列表item的dataPath标识符
     */
    public static final String DATA_PATH_TAG = "item";

}
