package com.chinapex.android.datacollect.broadcast;

import com.chinapex.android.datacollect.controller.IController;
import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.utils.ATLog;

/**
 * @author SteelCabbage
 * @date 2018/12/03
 */
public class BroadcastController implements IController {

    private static final String TAG = BroadcastController.class.getSimpleName();

    private BroadcastController() {

    }

    private static class BaseBroadcastManagerHolder {
        private static final BroadcastController BASE_BROADCAST_MANAGER = new BroadcastController();
    }

    public static BroadcastController getInstance() {
        return BaseBroadcastManagerHolder.BASE_BROADCAST_MANAGER;
    }


    @Override
    public void doInit() {
        NetworkChangeReceiver networkChangeReceiver = NetworkChangeReceiver.getInstance();
        if (null == networkChangeReceiver) {
            ATLog.w(TAG, "doInit() -> networkChangeReceiver is null!");
            return;
        }

        networkChangeReceiver.register(ApexCache.getInstance().getContext());
    }

    @Override
    public void onDestroy() {
        NetworkChangeReceiver networkChangeReceiver = NetworkChangeReceiver.getInstance();
        if (null == networkChangeReceiver) {
            ATLog.w(TAG, "onDestroy() -> networkChangeReceiver is null!");
            return;
        }

        networkChangeReceiver.unRegister(ApexCache.getInstance().getContext());
    }

}
