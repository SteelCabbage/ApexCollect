package com.chinapex.android.datacollect.broadcast;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.chinapex.android.datacollect.changelistener.AnalyticsListenerController;
import com.chinapex.android.datacollect.utils.ATLog;
import com.chinapex.android.datacollect.utils.PhoneStateUtils;

/**
 * @author SteelCabbage
 * @date 2018/12/03
 */
public class NetworkChangeReceiver extends BroadcastReceiver implements IRegister {

    private static final String TAG = NetworkChangeReceiver.class.getSimpleName();


    private NetworkChangeReceiver() {

    }

    private static class NetworkChangeReceiverHolder {
        private static final NetworkChangeReceiver NETWORK_CHANGE_RECEIVER = new NetworkChangeReceiver();
    }

    public static NetworkChangeReceiver getInstance() {
        return NetworkChangeReceiverHolder.NETWORK_CHANGE_RECEIVER;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String networkType = PhoneStateUtils.getNetworkType(context);
        AnalyticsListenerController.getInstance().notifyNetworkTypeChange(networkType);
    }

    @Override
    public void register(Context context) {
        if (null == context) {
            ATLog.e(TAG, "register() -> context is null!");
            return;
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(this, intentFilter, Manifest.permission.ACCESS_NETWORK_STATE, null);
    }

    @Override
    public void unRegister(Context context) {
        if (null == context) {
            ATLog.e(TAG, "unRegister() -> context is null!");
            return;
        }

        context.unregisterReceiver(this);
    }
}
