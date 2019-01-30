package com.chinapex.android.monitor.view.menu;

import com.chinapex.android.monitor.view.BasePresenter;
import com.chinapex.android.monitor.view.BaseView;

/**
 * @author wyhusky
 * @date 2018/12/27
 */
public interface FloatingWindowContract {
    interface View extends BaseView {
        /**
         * switch floating dot to menu, or switch menu to dot
         */
        void transformMenu();

        /**
         * close Monitor mode if opened
         */
        void closeMonitorModeIfOpened();

        /**
         * close Contrast mode if opened
         */
        void closeContrastModeIfOpened();

        /**
         * whether to show chart option in the menu
         *
         * @param shouldShow true to show, false to hide
         */
        void showChartOption(boolean shouldShow);

        /**
         * set label describes the number of chosen event
         *
         * @param count the number of chosen event
         */
        void setLabel(int count);
    }

    interface Presenter extends BasePresenter {
        /**
         * switch monitor mode
         *
         * @param isChecked true to open, false to close
         */
        void switchMonitorMode(boolean isChecked);

        /**
         * switch contrast mode
         *
         * @param isChecked true to open, false to close
         */
        void switchContrastMode(boolean isChecked);

        /**
         * open event define page
         */
        void openDefinePage();

        /**
         * open contrast data page
         */
        void openContrastDataPage();

        /**
         * open upload event page
         */
        void openUploadEventPage();

        /**
         * close menu
         */
        void close();
    }
}
