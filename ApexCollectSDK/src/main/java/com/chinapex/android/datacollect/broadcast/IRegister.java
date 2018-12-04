package com.chinapex.android.datacollect.broadcast;

import android.content.Context;

/**
 * @author SteelCabbage
 * @date 2018/12/03
 */
public interface IRegister {

    /**
     * 广播注册
     *
     * @param context application context
     */
    void register(Context context);

    /**
     * 广播反注册
     *
     * @param context application context
     */
    void unRegister(Context context);
}
