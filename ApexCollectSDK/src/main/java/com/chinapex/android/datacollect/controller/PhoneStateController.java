package com.chinapex.android.datacollect.controller;

import android.os.Build;
import android.util.DisplayMetrics;

import com.chinapex.analytics.BuildConfig;
import com.chinapex.analytics.changelistener.AnalyticsListenerController;
import com.chinapex.analytics.entity.general.Ids;
import com.chinapex.analytics.entity.general.PhoneContext;
import com.chinapex.analytics.utils.PhoneContextUtils;

/**
 * @author SteelCabbage
 * @date 2018/11/20
 */

public class PhoneStateController implements IController {

    private static final String TAG = PhoneStateController.class.getSimpleName();

    private PhoneStateController() {

    }

    private static class PhoneStateControllerHolder {
        private static final PhoneStateController PHONE_STATE_CONTROLLER = new PhoneStateController();
    }

    public static PhoneStateController getInstance() {
        return PhoneStateControllerHolder.PHONE_STATE_CONTROLLER;
    }

    @Override
    public void doInit() {


        AnalyticsListenerController.getInstance().addOnNetworkTypeChangeListener(this);

        mIds = new Ids();
        mIds.setAndroidid(PhoneContextUtils.getAndroidId(mContext));
        mIds.setImei(PhoneContextUtils.getImei(mContext));

        mPhoneContext = new PhoneContext();

        PhoneContext.App app = new PhoneContext.App();
        app.setName(PhoneContextUtils.getAppName(mContext));
        app.setVersion(PhoneContextUtils.getVersionName(mContext));
        app.setBuild(PhoneContextUtils.getVersionCode(mContext));
        mPhoneContext.setApp(app);

        PhoneContext.Device device = new PhoneContext.Device();
        device.setManufacturer(Build.MANUFACTURER);
        device.setModel(Build.MODEL);
        device.setName(Build.DEVICE);
        device.setType("android");
        device.setVersion(BuildConfig.VERSION_NAME);
        mPhoneContext.setDevice(device);

        mNetwork = new PhoneContext.Network();
        mNetwork.setBluetooth(PhoneContextUtils.getBluetooth(mContext));
        mNetwork.setCarrier(PhoneContextUtils.getCarrier(mContext));
        mNetwork.setCellular(PhoneContextUtils.getCellular(mContext));
        mNetwork.setWifi(PhoneContextUtils.getWifiBssid(mContext));
        mPhoneContext.setNetwork(mNetwork);

        PhoneContext.Os os = new PhoneContext.Os();
        os.setName(System.getProperty("os.name"));
        os.setVersion(Build.VERSION.RELEASE);
        mPhoneContext.setOs(os);

        PhoneContext.Screen screen = new PhoneContext.Screen();
        DisplayMetrics displayMetrics = PhoneContextUtils.getDisplayMetrics(mContext);
        screen.setDensity(displayMetrics.density);
        screen.setHeight(displayMetrics.heightPixels);
        screen.setWidth(displayMetrics.widthPixels);
        mPhoneContext.setScreen(screen);
    }

    @Override
    public void onDestroy() {

    }

}
