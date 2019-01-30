package com.chinapex.android.monitor.view;

/**
 * @author wyhusky
 * @date 2018/12/27
 */
public interface BaseView<T> {

    /**
     * bind a presenter to view
     *
     * @param presenter which is binding to the view
     */
    void setPresenter(T presenter);
}
