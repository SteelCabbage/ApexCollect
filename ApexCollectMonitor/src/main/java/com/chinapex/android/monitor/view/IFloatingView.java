package com.chinapex.android.monitor.view;

/**
 * @author wyhusky
 * @date 2018/12/27
 */
public interface IFloatingView {

    int WINDOW_FLOATING = 0;
    int WINDOW_DEFINE_PV_PAGE = 1;
    int WINDOW_DEFINE_CLICK_PAGE = 2;
    int WINDOW_DEFINE_LIST_PAGE = 3;
    int WINDOW_CONTRAST_DATA = 4;
    int WINDOW_UPLOAD = 5;


    /**
     * show floating view
     */
    void show();

    /**
     * hide floating view
     */
    void hide();

    /**
     * remove floating view
     */
    void dismiss();

    /**
     * if the view is hiding
     * @return true if the view is hiding now;else false
     */
    boolean isHiding();
}
