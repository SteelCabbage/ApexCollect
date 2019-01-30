package com.chinapex.android.datacollect.controller;

import android.text.TextUtils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chinapex.android.datacollect.ApexAnalytics;
import com.chinapex.android.datacollect.changelistener.AnalyticsListenerController;
import com.chinapex.android.datacollect.changelistener.OnNetworkTypeChangeListener;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.ApexLocation;
import com.chinapex.android.datacollect.utils.ATLog;

/**
 * @author SteelCabbage
 * @date 2018/11/23
 */
public class LocationController implements IController, OnNetworkTypeChangeListener {

    private static final String TAG = LocationController.class.getSimpleName();

    /**
     * 百度定位返回的成功码
     */
    private static final int BAIDU_LOCATION_GPS_SUCCESS_1 = 61;
    private static final int BAIDU_LOCATION_NETWORK_SUCCESS_2 = 161;

    private LocationController() {
        synchronized (LocationController.class) {
            if (locationClient == null) {
                locationClient = new LocationClient(ApexCache.getInstance().getContext());
            }
        }
    }

    @Override
    public void networkTypeChange(String networkType) {
        if (!networkType.equals(Constant.NETWORK_TYPE_UNKNOWN)) {
            requestLocation();
        }
    }

    private static class LocationControllerHolder {
        private static final LocationController LOCATION_CONTROLLER = new LocationController();
    }

    public static LocationController getInstance() {
        return LocationControllerHolder.LOCATION_CONTROLLER;
    }

    private LocationClient locationClient;
    private BDAbstractLocationListener bdLocationListener;

    @Override
    public void doInit() {
        bdLocationListener = new ApexLocationListener();
        register(bdLocationListener);
        LocationClientOption option = getDefaultOption();
        locationClient.setLocOption(option);
        start();

        AnalyticsListenerController.getInstance().addOnNetworkTypeChangeListener(this);
    }

    @Override
    public void onDestroy() {
        unregister(bdLocationListener);
        stop();
    }

    private boolean register(BDAbstractLocationListener bdLocationListener) {
        boolean isSuccess = false;
        if (bdLocationListener != null) {
            locationClient.registerLocationListener(bdLocationListener);
            ATLog.i(TAG, "register location listener success!");
            return isSuccess;
        }
        return isSuccess;
    }

    private boolean unregister(BDAbstractLocationListener bdLocationListener) {
        boolean isSuccess = false;
        if (bdLocationListener != null) {
            locationClient.unRegisterLocationListener(bdLocationListener);
            ATLog.i(TAG, "unregister location listener success!");
            return isSuccess;
        }
        return isSuccess;
    }

    public void start() {
        synchronized (LocationController.class) {
            if (locationClient != null && !locationClient.isStarted()) {
                locationClient.start();
                ATLog.i(TAG, "location client start...");
            }
        }
    }

    private void stop() {
        synchronized (LocationController.class) {
            if (locationClient != null && locationClient.isStarted()) {
                locationClient.stop();
                ATLog.i(TAG, "location client stop...");
            }
        }
    }

    private LocationClientOption getDefaultOption() {
        LocationClientOption mOption = new LocationClientOption();
        // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setCoorType("bd09ll");
        // 可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setScanSpan(1000 * 5 * 60);
        // 可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedAddress(true);
        // 可选，设置是否需要地址描述
        mOption.setIsNeedLocationDescribe(false);
        // 可选，设置是否需要设备方向结果
        mOption.setNeedDeviceDirect(false);
        // 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setLocationNotify(false);
        // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIgnoreKillProcess(false);
        // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationDescribe(false);
        // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.setIsNeedLocationPoiList(false);
        // 可选，默认false，设置是否收集CRASH信息，默认收集
        mOption.SetIgnoreCacheException(false);
        // 可选，默认false，设置是否开启Gps定位
        mOption.setOpenGps(true);
        // 可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        mOption.setIsNeedAltitude(false);
        return mOption;
    }

    private void requestLocation() {
        synchronized (LocationController.class) {
            if (null != locationClient) {
                locationClient.requestLocation();
            }
        }
    }

    public static class ApexLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (null == bdLocation) {
                ATLog.e(TAG, "bdlocation is null!");
                return;
            }

            int errorCode = bdLocation.getLocType();
            if (!(errorCode == BAIDU_LOCATION_GPS_SUCCESS_1 || errorCode == BAIDU_LOCATION_NETWORK_SUCCESS_2)) {
                ATLog.e(TAG, "get location failed---->error code:" + errorCode);
                return;
            }

            double latitude = bdLocation.getLatitude();
            double longitude = bdLocation.getLongitude();
            String country = bdLocation.getCountry();
            String province = bdLocation.getProvince();
            String city = bdLocation.getCity();
            String district = bdLocation.getDistrict();

            if (null == country || TextUtils.isEmpty(country)) {
                ATLog.e(TAG, "can not get country!");
                LocationController.getInstance().requestLocation();
                return;
            }

            if (null == province || TextUtils.isEmpty(province)) {
                ATLog.e(TAG, "can not get province!");
                LocationController.getInstance().requestLocation();
                return;
            }

            if (null == city || TextUtils.isEmpty(city)) {
                ATLog.e(TAG, "can not get country!");
                LocationController.getInstance().requestLocation();
                return;
            }

            ApexLocation apexLocation = new ApexLocation.LocationBuilder()
                    .setLatitude(latitude)
                    .setLongitude(longitude)
                    .setCountry(country)
                    .setProvince(province)
                    .setCity(city)
                    .setDistrict(district)
                    .build();
            ATLog.i(TAG, apexLocation.toString());

            ApexAnalytics.getInstance().setApexLocation(apexLocation);
        }
    }

}
