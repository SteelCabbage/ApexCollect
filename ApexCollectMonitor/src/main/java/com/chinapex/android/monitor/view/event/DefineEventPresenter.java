package com.chinapex.android.monitor.view.event;


import android.text.TextUtils;

import com.chinapex.android.monitor.R;
import com.chinapex.android.monitor.bean.UploadConfigRequest;
import com.chinapex.android.monitor.executor.TaskController;
import com.chinapex.android.monitor.executor.runnable.WriteClickToSp;
import com.chinapex.android.monitor.executor.runnable.WriteListToSp;
import com.chinapex.android.monitor.executor.runnable.WritePvToSp;
import com.chinapex.android.monitor.global.MonitorCache;
import com.chinapex.android.monitor.utils.MD5Utils;
import com.chinapex.android.monitor.utils.MLog;
import com.chinapex.android.monitor.view.FloatingViewManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author wyhusky
 * @date 2018/12/28
 */
public class DefineEventPresenter implements DefineEventContract.Presenter {

    private static final String TAG = DefineEventPresenter.class.getSimpleName();
    private DefineEventContract.View mDefineEventView;
    private UploadConfigRequest.Config.PvBean mPvBean;
    private UploadConfigRequest.Config.ClickBean mClickBean;
    private UploadConfigRequest.Config.ListBean mListBean;

    public DefineEventPresenter(DefineEventContract.View view) {
        this.mDefineEventView = view;
        mPvBean = new UploadConfigRequest.Config.PvBean();
        mClickBean = new UploadConfigRequest.Config.ClickBean();
        mListBean = new UploadConfigRequest.Config.ListBean();
    }

    @Override
    public void loadPvData() {
        Map<Integer, Stack<Map<String, Boolean>>> tasks = MonitorCache.getInstance().getTasks();
        Map<Integer, Integer> top = MonitorCache.getInstance().getTop();
        int[] foregroundTask = MonitorCache.getInstance().getForegroundTask();
        if (null == tasks) {
            MLog.e(TAG, "loadPvData()-> tasks is null");
            return;
        }
        if (null == top) {
            MLog.e(TAG, "loadPvData()-> top is null");
            return;
        }
        if (null == foregroundTask) {
            MLog.e(TAG, "loadPvData()-> foregroundTask is null");
            return;
        }
        int taskId = foregroundTask[0];
        if (!tasks.containsKey(taskId)) {
            MLog.e(TAG, "loadPvData()-> tasks do not contain task[" + taskId + "]");
            return;
        }
        Stack<Map<String, Boolean>> task = tasks.get(taskId);
        if (task.isEmpty()) {
            MLog.e(TAG, "loadPvData()-> task[" + taskId + "] is empty");
            return;
        }

        List<String> pages = new ArrayList<>();
        String reference = null;
        if (!top.containsKey(taskId)) {
            MLog.e(TAG, "loadPvData()-> top does not contain top[" + taskId + "]");
            return;
        }
        int topActivity = top.get(taskId);
        for (int i = task.size() - 1; i >= topActivity; i--) {
            Map<String, Boolean> map = task.get(i);
            if (null == map) {
                MLog.e(TAG, "loadPvData()-> map should not be null");
                return;
            }
            String[] key = map.keySet().toArray(new String[1]);
            if (key.length != 1) {
                MLog.e(TAG, "loadPvData()-> keyset size can only be 1");
                return;
            }
            pages.add(key[0]);
            if (map.get(key[0])) {
                reference = key[0];
            }
        }
        if (pages.isEmpty()) {
            MLog.e(TAG, "loadPvData()-> pages is empty!");
            return;
        }
        if (null == reference) {
            MLog.e(TAG, "loadPvData()-> reference is null!");
            return;
        }
        mDefineEventView.loadPagePath(pages);
        mDefineEventView.showReference(reference);
        requestPvData();
    }

    @Override
    public void updatePvData(String viewPath) {
        requestPvData();
    }

    @Override
    public void loadClickData(String viewPath, String pageClass) {
        if (null == viewPath) {
            MLog.e(TAG, "loadClickData()-> viewPath is null!");
            return;
        }
        if (null == pageClass) {
            MLog.e(TAG, "loadClickData()-> pageClass is null");
            return;
        }
        Map<String, UploadConfigRequest.Config.PvBean> map = MonitorCache.getInstance().getPvBeans();
        if (null == map) {
            MLog.e(TAG, "loadClickData()-> ");
            return;
        }
        String definedPage = null;
        for (UploadConfigRequest.Config.PvBean pvBean : map.values()) {
            if (null == pvBean) {
                MLog.e(TAG, "loadClickData()-> pvBean is null!");
                continue;
            }
            if (null == pvBean.getViewPath()) {
                MLog.e(TAG, "loadClickData()-> viewPath of pvBean is null!");
                continue;
            }
            if (pageClass.contains(pvBean.getViewPath())) {
                definedPage = pvBean.getViewPath();
                break;
            }
        }
        if (TextUtils.isEmpty(definedPage)) {
            MLog.e(TAG, "loadClickData()-> no defined page!");
            mDefineEventView.showToast(R.string.defined_page_hint);
            dismiss();
            return;
        }

        mClickBean.setDefinedPage(definedPage);
        mClickBean.setPageClassName(pageClass);
        mClickBean.setViewPath(viewPath);
        mDefineEventView.showViewPath(viewPath);
        mDefineEventView.showReference(definedPage);
    }

    @Override
    public void loadListItemClickData(String viewPath, String pageClass, String itemPath) {
        if (null == viewPath) {
            MLog.e(TAG, "loadListItemClickData()-> viewPath is null!");
            return;
        }
        if (null == pageClass) {
            MLog.e(TAG, "loadListItemClickData()-> pageClass is null");
            return;
        }
        if (null == itemPath) {
            MLog.e(TAG, "loadListItemClickData()-> itemPath is null");
            return;
        }
        Map<String, UploadConfigRequest.Config.PvBean> pvMap = MonitorCache.getInstance().getPvBeans();
        if (null == pvMap) {
            MLog.e(TAG, "loadListItemClickData()-> pvMap is null!");
            return;
        }
        String definedPage = null;
        for (UploadConfigRequest.Config.PvBean pvBean : pvMap.values()) {
            if (null == pvBean) {
                MLog.e(TAG, "loadListItemClickData()-> pvBean is null!");
                continue;
            }
            if (null == pvBean.getViewPath()) {
                MLog.e(TAG, "loadListItemClickData()-> viewPath of pvBean is null!");
                continue;
            }
            if (pageClass.contains(pvBean.getViewPath())) {
                definedPage = pvBean.getViewPath();
                break;
            }
        }
        if (TextUtils.isEmpty(definedPage)) {
            MLog.e(TAG, "loadClickData()-> no defined page!");
            mDefineEventView.showToast(R.string.defined_page_hint);
            dismiss();
            return;
        }

        mListBean.setDefinedPage(definedPage);
        mListBean.setPageClassName(pageClass);
        mListBean.setViewPath(viewPath);

        mDefineEventView.showViewPath(itemPath);
        mDefineEventView.showReference(definedPage);
    }

    @Override
    public void saveClickEvent(String alias) {
        mClickBean.setAlias(alias);
        if (TextUtils.isEmpty(mClickBean.getViewPath())) {
            MLog.e(TAG, "saveClickEvent()-> viewPath is empty, save failed");
            return;
        }
        if (TextUtils.isEmpty(mClickBean.getDefinedPage())) {
            MLog.e(TAG, "saveClickEvent()-> defined page is empty, save failed");
            return;
        }
        String mClickPathMD5 = MD5Utils.getMD5(mClickBean.getViewPath());
        MonitorCache.getInstance().putClickBeans(mClickPathMD5, mClickBean);
        TaskController.getInstance().submit(new WriteClickToSp());
        dismiss();
    }

    @Override
    public void saveListItemClickEvent(String alias, String itemPath) {
        String itemPathMD5 = MD5Utils.getMD5(itemPath);
        boolean isListExist = false;
        Map<String, UploadConfigRequest.Config.ListBean> listMap = MonitorCache.getInstance().getListBeans();
        if (null == listMap) {
            MLog.e(TAG, "saveListItemClickEvent()-> listMap is null");
            return;
        }
        for (UploadConfigRequest.Config.ListBean bean : listMap.values()) {
            if (mListBean.getViewPath().equals(bean.getViewPath())) {
                isListExist = true;
                break;
            }
        }

        if (isListExist) {
            String listBeanKey = MD5Utils.getMD5(mListBean.getViewPath());
            UploadConfigRequest.Config.ListBean bean = listMap.get(listBeanKey);
            Map<String, String> itemMap = bean.getItemAliases();
            if (null == itemMap) {
                itemMap = new HashMap<>();
                itemMap.put(itemPathMD5, alias);
            }
            itemMap.put(itemPathMD5, alias);
        } else {
            if (TextUtils.isEmpty(mListBean.getDefinedPage())) {
                MLog.e(TAG, "saveListItemClickEvent()-> defined page is empty, save failed");
                return;
            }
            if (TextUtils.isEmpty(mListBean.getViewPath())) {
                MLog.e(TAG, "saveListItemClickEvent()-> viewPath is empty, save failed");
            }
            Map<String, String> itemMap = new HashMap<>();
            itemMap.put(itemPathMD5, alias);
            mListBean.setItemAliases(itemMap);
            String viewPathMD5 = MD5Utils.getMD5(mListBean.getViewPath());
            MonitorCache.getInstance().putListBeans(viewPathMD5, mListBean);
        }

        TaskController.getInstance().submit(new WriteListToSp());
        dismiss();
    }

    @Override
    public void savePageViewEvent(String alias, String definedPage) {
        mPvBean.setAlias(alias);
        mPvBean.setViewPath(definedPage);

        if (TextUtils.isEmpty(mPvBean.getViewPath())) {
            MLog.e(TAG, "savePageViewEvent()-> viewPath is empty, save failed");
            return;
        }
        String mPvMD5 = MD5Utils.getMD5(mPvBean.getAlias());
        MonitorCache.getInstance().putPvBeans(mPvMD5, mPvBean);
        TaskController.getInstance().submit(new WritePvToSp());
        dismiss();
    }

    @Override
    public void dismiss() {
        mListBean = null;
        mClickBean = null;
        mPvBean = null;
        FloatingViewManager.getInstance().dismissFloatingView(mDefineEventView.getViewType());
    }

    @Override
    public void init() {

    }

    private void requestPvData() {

    }

    private void requestClickData() {

    }
}
