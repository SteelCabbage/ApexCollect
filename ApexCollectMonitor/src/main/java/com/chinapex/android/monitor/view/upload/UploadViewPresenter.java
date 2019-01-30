package com.chinapex.android.monitor.view.upload;

import com.chinapex.android.monitor.executor.TaskController;
import com.chinapex.android.monitor.executor.runnable.WriteConfigToSp;

/**
 * @author wyhusky
 * @date 2019/1/17
 */
public class UploadViewPresenter implements UploadViewContract.Presenter {

    private UploadViewContract.View mUploadView;

    public UploadViewPresenter(UploadViewContract.View view) {
        this.mUploadView = view;
    }

    @Override
    public void init() {

    }

    @Override
    public void writeConfig() {
        TaskController.getInstance().submit(new WriteConfigToSp());
    }
}
