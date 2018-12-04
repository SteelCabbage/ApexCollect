![apex log](apex tech logo.jpg)

# ApexDataCollect-Android



## 集成步骤

1.  **在Project的build.gradle下，添加AnalyticsAop的插件：**

   ```java
   buildscript {
       
       repositories {
           google()
           jcenter()
       }
       
       dependencies {
           classpath 'com.android.tools.build:gradle:3.2.1'
           classpath 'com.chinapex.analytics.aop:AnalyticsAop:1.0.3'
       }
   }
   ```



2.  **在app的build.gradle下，添加：**

   ```java
   apply plugin: 'aop'
       
   dependencies {
       // app 自身的各种依赖   
       ...
   
       // 依赖ApexCollectSDK
       implementation 'com.chinapex.android.datacollect:ApexCollectSDK:1.0.3'
   
       // sdk为了获取RecyclerView的列表点击事件需要依赖
       implementation 'com.android.support:recyclerview-v7:28.0.0'
       implementation 'com.android.support:design:28.0.0'
           
       // sdk后续为了完善追踪事件，可能还会增加其它依赖，待补充
       ...
   }
   ```



3.   **混淆规则**

   ```java
   ==========================ApexCollectSDK start==============================
   # sdk
   -keep class com.chinapex.android.datacollect.**{*;}
   
   # okhttp
   -dontwarn javax.annotation.**
   -keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
   -dontwarn org.codehaus.mojo.animal_sniffer.*
   -dontwarn okhttp3.internal.platform.ConscryptPlatform
   
   # sqlcipher
   -keep  class net.sqlcipher.** {*;}
   -keep  class net.sqlcipher.database.** {*;}
   
   # Gson
   -keepattributes Signature
   -keepattributes *Annotation*
   -dontwarn sun.misc.**
   -keep class * implements com.google.gson.TypeAdapterFactory
   -keep class * implements com.google.gson.JsonSerializer
   -keep class * implements com.google.gson.JsonDeserializer
   ==========================ApexCollectSDK end==============================
   ```



4. **在app的application中，初始化：**

```java
ApexAnalytics.getInstance().init(
                new AnalyticsSettings.SettingsBuilder(applicationContex)
                        .setLogLevel(ATLog.VERBOSE)
                        .setDelayReportInterval(1000 * 60 * 2)
                        .setCheckInstantErrInterval(1000 * 60)
                        .setReportMaxNum(5)
                        .build());
```





## API



### 自定义代码埋点：void track(TrackEvent trackEvent)

```java
ApexAnalytics.getInstance().track(new TrackEvent.EventBuilder()
                        .setMode(1)  // 0: delay延时上报(default), 1: instant即时上报
                        .setLabel("即时上报的label")
                        .setValue({
                                "custom1": "111111",
                                "custom2": "222222",
                                "custom3": "333333",
                                "custom4": "444444",
                                "custom5": "555555"})
                        .build());
```



### App用户登入：void signIn(String userId)

```java
ApexAnalytics.getInstance().signIn("testUserId");
```



### App用户登出：void signOut()

```java
ApexAnalytics.getInstance().signOut();
```





## SDK现有的事件类型



- **自定义事件（代码埋点）**

```java
{
    "company": "apex",
    "data": {
        "deviceID": [
            "GSM:863127030601832",
            "CDMA:a0000065beca82",
            "GSM:863127030162348"
        ],
        "events": [
            {
                "eventType": 0,
                "label": "即时上报的label",
                "userId": "testUserId",
                "value": {
                    "custom1": "111111",
                    "custom2": "222222",
                    "custom3": "333333",
                    "custom4": "444444",
                    "custom5": "555555"
                }
            }
        ],
        "language": "zh_CN",
        "reportTime": 1543945844275,
        "uuid": "7cca7ec9fb8b4050"
    },
    "terminal": "android"
}
```



- **冷启动事件**

```java
{
    "company": "apex",
    "data": {
        "deviceID": [
            "GSM:863127030601832",
            "CDMA:a0000065beca82",
            "GSM:863127030162348"
        ],
        "events": [
            {
                "eventType": 1,
                "label": "cold",
                "userId": "",
                "value": {
                    "apiKey": "",
                    "appName": "Sample",
                    "appVersion": "1.0",
                    "brandName": "honor",
                    "customVersion": "",
                    "deviceModel": "FRD-AL10",
                    "manufacturer": "HUAWEI",
                    "os": "Linux",
                    "osVersion": "8.0.0",
                    "screenDensity": "3.0",
                    "screenHeight": "1792",
                    "screenWidth": "1080",
                    "timeStamp": 1543945912005
                }
            }
        ],
        "language": "zh_CN",
        "reportTime": 1543945912037,
        "uuid": "7cca7ec9fb8b4050"
    },
    "terminal": "android"
}
```



- **点击事件**

```java
{
    "company": "apex",
    "data": {
        "deviceID": [
            "GSM:863127030601832",
            "CDMA:a0000065beca82",
            "GSM:863127030162348"
        ],
        "events": [
            {
                "eventType": 2,
                "label": "click",
                "userId": "testUserId",
                "value": {
                    "alpha": 1,
                    "content": "登入",
                    "frame": "(408,1284)",
                    "pageClassName": "com.chinapex.analytics.sample.activity.MainActivity",
                    "timeStamp": 1543946314011,
                    "viewPath": "DecorView/LinearLayout[0]/FrameLayout[1]/ActionBarOverlayLayout[0]#decor_content_parent/ContentFrameLayout[0]#content/RelativeLayout[0]/AppCompatButton[7]#bt_signIn",
                    "viewPathMD5": "6c7c227b31ec63875a1683b0b7ddc262"
                }
            }
        ],
        "language": "zh_CN",
        "reportTime": 1543946366558,
        "uuid": "7cca7ec9fb8b4050"
    },
    "terminal": "android"
}
```



- **PV事件**

```java
{
    "company": "apex",
    "data": {
        "deviceID": [
            "GSM:863127030601832",
            "CDMA:a0000065beca82",
            "GSM:863127030162348"
        ],
        "events": [
            {
                "eventType": 4,
                "label": "pv",
                "userId": "",
                "value": {
                    "durationTime": 127758,
                    "exposures": [],
                    "pageClassName": "com.chinapex.analytics.sample.activity.ClickTestActivity",
                    "reference": "com.chinapex.analytics.sample.activity.MainActivity",
                    "timeStamp": 1543942700620
                }
            }
        ],
        "language": "zh_CN",
        "reportTime": 1543946006499,
        "uuid": "7cca7ec9fb8b4050"
    },
    "terminal": "android"
}
```





## todo：

1. CQ目前使用的控件类型（Fragment，dialog，list，menu等）
2. CQ目前的编译版本是多少，27？28？or？，已知：向下兼容最低14
3. 列表类型，若获取子条目详细内容，需配置传进adapter的data集合，SDK通过反射获取
4. 测试方式（推荐TestIn，百度），请CQ分别提供集成sdk和未集成sdk的包，进行云测，测试包仅wifi访问，云测是否受影响？



