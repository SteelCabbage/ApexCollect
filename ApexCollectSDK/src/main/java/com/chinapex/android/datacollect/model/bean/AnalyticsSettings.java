package com.chinapex.android.datacollect.model.bean;

import android.content.Context;

/**
 * @author SteelCabbage
 * @date 2018/12/04
 */
public class AnalyticsSettings {
    /**
     * 必要参数, 必须为Application的Context
     */
    private final Context applicationContext;

    /**
     * 可选参数, 若无，则默认androidId
     */
    private final String uuid;

    /**
     * 可选参数, 安装渠道id
     */
    private final String channelId;

    /**
     * 可选参数, 日志输出等级，默认为WARN
     */
    private final int logLevel;

    /**
     * 可选参数, Default: 30 (默认30条)
     */
    private final int reportMaxNum;

    /**
     * 可选参数, 延时上报的时间间隔, Default: 1000 * 60 * 5 (默认5分钟)
     */
    private final long delayReportInterval;

    /**
     * 可选参数, 检查即时上报是否存在异常的时间间隔, 默认2分钟
     */
    private final long checkInstantErrInterval;

    /**
     * 可选参数, 延时上报的url,默认为测试url
     */
    private final String urlDelay;

    /**
     * 可选参数, 即时上报的url，默认为测试url
     */
    private final String urlInstant;

    /**
     * 可选参数, 域名过滤, 自定义url的域名必须与hostnameVerifier一致
     */
    private final String hostnameVerifier;


    private AnalyticsSettings(SettingsBuilder settingsBuilder) {
        this.applicationContext = settingsBuilder.applicationContext;
        this.uuid = settingsBuilder.uuid;
        this.channelId = settingsBuilder.channelId;
        this.logLevel = settingsBuilder.logLevel;
        this.reportMaxNum = settingsBuilder.reportMaxNum;
        this.delayReportInterval = settingsBuilder.delayReportInterval;
        this.checkInstantErrInterval = settingsBuilder.checkInstantErrInterval;
        this.urlDelay = settingsBuilder.urlDelay;
        this.urlInstant = settingsBuilder.urlInstant;
        this.hostnameVerifier = settingsBuilder.hostnameVerifier;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public String getUuid() {
        return uuid;
    }

    public String getChannelId() {
        return channelId;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public int getReportMaxNum() {
        return reportMaxNum;
    }

    public long getDelayReportInterval() {
        return delayReportInterval;
    }

    public long getCheckInstantErrInterval() {
        return checkInstantErrInterval;
    }

    public String getUrlDelay() {
        return urlDelay;
    }

    public String getUrlInstant() {
        return urlInstant;
    }

    public String getHostnameVerifier() {
        return hostnameVerifier;
    }

    public static class SettingsBuilder {
        private final Context applicationContext;
        private String uuid;
        private String channelId;
        private int logLevel;
        private int reportMaxNum;
        private long delayReportInterval;
        private long checkInstantErrInterval;
        private String urlDelay;
        private String urlInstant;
        private String hostnameVerifier;

        public SettingsBuilder(Context applicationContext) {
            this.applicationContext = applicationContext;
        }

        public SettingsBuilder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public SettingsBuilder setChannelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public SettingsBuilder setLogLevel(int logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public SettingsBuilder setReportMaxNum(int reportMaxNum) {
            this.reportMaxNum = reportMaxNum;
            return this;
        }

        public SettingsBuilder setDelayReportInterval(long delayReportInterval) {
            this.delayReportInterval = delayReportInterval;
            return this;
        }

        public SettingsBuilder setCheckInstantErrInterval(long checkInstantErrInterval) {
            this.checkInstantErrInterval = checkInstantErrInterval;
            return this;
        }

        public SettingsBuilder setUrlDelay(String urlDelay) {
            this.urlDelay = urlDelay;
            return this;
        }

        public SettingsBuilder setUrlInstant(String urlInstant) {
            this.urlInstant = urlInstant;
            return this;
        }

        public SettingsBuilder setHostnameVerifier(String hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        public AnalyticsSettings build() {
            return new AnalyticsSettings(this);
        }
    }

    @Override
    public String toString() {
        return "AnalyticsSettings{" +
                "applicationContext=" + applicationContext +
                ", uuid='" + uuid + '\'' +
                ", channelId='" + channelId + '\'' +
                ", logLevel=" + logLevel +
                ", reportMaxNum=" + reportMaxNum +
                ", delayReportInterval=" + delayReportInterval +
                ", checkInstantErrInterval=" + checkInstantErrInterval +
                ", urlDelay='" + urlDelay + '\'' +
                ", urlInstant='" + urlInstant + '\'' +
                ", hostnameVerifier='" + hostnameVerifier + '\'' +
                '}';
    }
}
