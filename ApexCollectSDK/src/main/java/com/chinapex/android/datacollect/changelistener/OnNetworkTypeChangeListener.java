package com.chinapex.android.datacollect.changelistener;

/**
 * @author SteelCabbage
 * @date 2018/12/03
 */
public interface OnNetworkTypeChangeListener {

    /**
     * 网络状态变化时回调
     *
     * @param networkType
     */
    void networkTypeChange(String networkType);

}
