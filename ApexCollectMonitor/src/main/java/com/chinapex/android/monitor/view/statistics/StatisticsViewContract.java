package com.chinapex.android.monitor.view.statistics;

import com.chinapex.android.monitor.view.BasePresenter;
import com.chinapex.android.monitor.view.BaseView;

/**
 * @author wyhusky
 * @date 2019/1/17
 */
interface StatisticsViewContract {
    interface View extends BaseView {
        void updateData();
    }

    interface Presenter extends BasePresenter {
        void loadContrastData();
    }
}
