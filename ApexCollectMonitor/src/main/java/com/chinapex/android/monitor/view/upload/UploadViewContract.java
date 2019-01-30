package com.chinapex.android.monitor.view.upload;

import com.chinapex.android.monitor.view.BasePresenter;
import com.chinapex.android.monitor.view.BaseView;

/**
 * @author wyhusky
 * @date 2019/1/17
 */
public interface UploadViewContract {
    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {
        /**
         * 上传配置文件
         */
        void writeConfig();
    }
}
