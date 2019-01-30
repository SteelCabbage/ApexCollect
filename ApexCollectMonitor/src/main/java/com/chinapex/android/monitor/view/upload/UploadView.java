package com.chinapex.android.monitor.view.upload;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.chinapex.android.monitor.R;
import com.chinapex.android.monitor.bean.StatisticsBean;
import com.chinapex.android.monitor.bean.UploadConfigRequest;
import com.chinapex.android.monitor.bean.UploadViewBean;
import com.chinapex.android.monitor.executor.TaskController;
import com.chinapex.android.monitor.executor.runnable.WriteClickToSp;
import com.chinapex.android.monitor.executor.runnable.WriteListToSp;
import com.chinapex.android.monitor.executor.runnable.WritePvToSp;
import com.chinapex.android.monitor.global.Constant;
import com.chinapex.android.monitor.global.MonitorCache;
import com.chinapex.android.monitor.utils.MLog;
import com.chinapex.android.monitor.view.FloatingViewManager;
import com.chinapex.android.monitor.view.IFloatingView;
import com.chinapex.android.monitor.view.adapter.UploadListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : Seven Qiu
 * @date : 2019/1/8
 */
public class UploadView extends FrameLayout implements IFloatingView, UploadViewContract.View, View.OnClickListener,
        UploadListAdapter.OnItemClickListener {

    private static final String TAG = UploadView.class.getSimpleName();
    private WindowManager mWindowManager;

    private RecyclerView mUploadRecyView;

    private UploadListAdapter mListAdapter;
    private Context mContext;
    private List<UploadViewBean> mUploadViewBeans;
    private Map<String, UploadConfigRequest.Config.ClickBean> mClickBeans;
    private Map<String, UploadConfigRequest.Config.PvBean> mPvBeans;
    private Map<String, UploadConfigRequest.Config.ListBean> mListBeans;
    private UploadViewContract.Presenter mUploadViewPresenter;

    public UploadView(@NonNull Context context) {
        super(context);
        mContext = context;
        inflate(context, R.layout.upload_view_layout, this);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setPresenter(new UploadViewPresenter(this));

        initView();
        initData();
    }

    private void initView() {
        mUploadRecyView = findViewById(R.id.upload_event_list);

        mUploadRecyView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mUploadViewBeans = getUploadViewBeans();
        mListAdapter = new UploadListAdapter(mUploadViewBeans);
        mUploadRecyView.setAdapter(mListAdapter);
        mListAdapter.setOnItemClickListener(this);

        findViewById(R.id.btn_upload).setOnClickListener(this);
        findViewById(R.id.btn_cancel_upload).setOnClickListener(this);

    }


    private void initData() {
        List<StatisticsBean> statisticsBeans = MonitorCache.getInstance().getStatisticsBeans();

        if (null == statisticsBeans) {
            MonitorCache.getInstance().setStatisticsBeans(new ArrayList<StatisticsBean>());
            statisticsBeans = MonitorCache.getInstance().getStatisticsBeans();
        }
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public void show() {
        this.setVisibility(VISIBLE);
        try {
            mWindowManager.addView(this, getDefaultParams());
        } catch (Exception e) {
            MLog.e(TAG, "show exception:" + e.getMessage());
        }
    }

    @Override
    public void hide() {
        this.setVisibility(INVISIBLE);
    }

    @Override
    public void dismiss() {
        try {
            mWindowManager.removeView(this);
        } catch (Exception e) {
            MLog.e(TAG, "show exception:" + e.getMessage());
        }
    }

    @Override
    public boolean isHiding() {
        return this.getVisibility() == INVISIBLE;
    }

    private WindowManager.LayoutParams getDefaultParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.CENTER;
        return params;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (R.id.btn_upload == viewId) {
            mUploadViewPresenter.writeConfig();
            FloatingViewManager.getInstance().dismissFloatingView(IFloatingView.WINDOW_UPLOAD);
        } else if (R.id.btn_cancel_upload == viewId) {
            FloatingViewManager.getInstance().dismissFloatingView(IFloatingView.WINDOW_UPLOAD);
        }
    }

    @Override
    public void onItemClick(int position) {
        if (null == mUploadViewBeans) {
            MLog.e(TAG, "onItemClick() -> mUploadViewBeans is null!");
            return;
        }

        UploadViewBean uploadViewBean = mUploadViewBeans.get(position);
        if (null == uploadViewBean) {
            MLog.e(TAG, "onItemClick() -> uploadViewBean is null!");
            return;
        }

        String md5 = uploadViewBean.getMd5();
        if (TextUtils.isEmpty(md5)) {
            MLog.e(TAG, "onItemClick() -> md5 is null or empty!");
            return;
        }

        int eventType = uploadViewBean.getEventType();
        switch (eventType) {
            case Constant.DEFINE_EVENT_TYPE_CLICK:
                if (null == mClickBeans) {
                    MLog.e(TAG, "onItemClick() -> mClickBeans is null!");
                    break;
                }

                mClickBeans.remove(md5);
                mUploadViewBeans.remove(position);
                TaskController.getInstance().submit(new WriteClickToSp());
                break;
            case Constant.DEFINE_EVENT_TYPE_PV:
                if (null == mPvBeans) {
                    MLog.e(TAG, "onItemClick() -> mPvBeans is null!");
                    break;
                }

                mPvBeans.remove(md5);
                mUploadViewBeans.remove(position);
                TaskController.getInstance().submit(new WritePvToSp());
                break;
            case Constant.DEFINE_EVENT_TYPE_LIST:
                if (null == mListBeans) {
                    MLog.e(TAG, "onItemClick() -> mListBeans is null!");
                    break;
                }

                String listId = uploadViewBean.getListId();
                if (TextUtils.isEmpty(listId)) {
                    MLog.e(TAG, "onItemClick() -> listId is null or empty!");
                    break;
                }

                UploadConfigRequest.Config.ListBean listBean = mListBeans.get(listId);
                if (null == listBean) {
                    MLog.e(TAG, "onItemClick() -> listBean is null!");
                    break;
                }

                Map<String, String> itemAliases = listBean.getItemAliases();
                if (null == itemAliases || itemAliases.isEmpty()) {
                    MLog.e(TAG, "onItemClick() -> itemAliases is null or empty!");
                    break;
                }

                itemAliases.remove(md5);
                mUploadViewBeans.remove(position);
                TaskController.getInstance().submit(new WriteListToSp());
                break;
            default:
                break;
        }


//        try {
//            statisticsBeans.remove(position);
//        } catch (UnsupportedOperationException e) {
//            MLog.e(TAG, "onItemClick() ->  UnsupportedOperationException: " + e.getMessage());
//        } catch (IndexOutOfBoundsException e) {
//            MLog.e(TAG, "onItemClick() ->  IndexOutOfBoundsException: " + e.getMessage());
//        }
    }

    @Override
    public void setPresenter(Object presenter) {
        mUploadViewPresenter = (UploadViewContract.Presenter) presenter;
    }

    private List<UploadViewBean> getUploadViewBeans() {
        List<UploadViewBean> uploadViewBeans = new ArrayList<>();
        mClickBeans = MonitorCache.getInstance().getClickBeans();
        mPvBeans = MonitorCache.getInstance().getPvBeans();
        mListBeans = MonitorCache.getInstance().getListBeans();

        if (null == mClickBeans || null == mPvBeans || null == mListBeans) {
            MLog.e(TAG, "getUploadViewBeans() -> clickBeans or pvBeans or listBeans is null!");
            return uploadViewBeans;
        }

        for (Map.Entry<String, UploadConfigRequest.Config.ClickBean> clickBeanEntry : mClickBeans.entrySet()) {
            if (null == clickBeanEntry) {
                MLog.e(TAG, "getUploadViewBeans() -> clickBeanEntry is null!");
                continue;
            }

            String md5 = clickBeanEntry.getKey();
            UploadConfigRequest.Config.ClickBean clickBean = clickBeanEntry.getValue();
            if (TextUtils.isEmpty(md5) || null == clickBean) {
                MLog.e(TAG, "getUploadViewBeans() -> md5 or clickBean is null or empty!");
                continue;
            }

            UploadViewBean uploadViewBean = new UploadViewBean();
            uploadViewBean.setEventType(Constant.DEFINE_EVENT_TYPE_CLICK);
            uploadViewBean.setMd5(md5);
            uploadViewBean.setAlias(clickBean.getAlias());

            uploadViewBeans.add(uploadViewBean);
        }

        for (Map.Entry<String, UploadConfigRequest.Config.PvBean> pvBeanEntry : mPvBeans.entrySet()) {
            if (null == pvBeanEntry) {
                MLog.e(TAG, "getUploadViewBeans() -> pvBeanEntry is null!");
                continue;
            }

            String md5 = pvBeanEntry.getKey();
            UploadConfigRequest.Config.PvBean pvBean = pvBeanEntry.getValue();
            if (TextUtils.isEmpty(md5) || null == pvBean) {
                MLog.e(TAG, "getUploadViewBeans() -> md5 or pvBean is null or empty!");
                continue;
            }

            UploadViewBean uploadViewBean = new UploadViewBean();
            uploadViewBean.setEventType(Constant.DEFINE_EVENT_TYPE_PV);
            uploadViewBean.setMd5(md5);
            uploadViewBean.setAlias(pvBean.getAlias());

            uploadViewBeans.add(uploadViewBean);
        }

        for (Map.Entry<String, UploadConfigRequest.Config.ListBean> listBeanEntry : mListBeans.entrySet()) {
            if (null == listBeanEntry) {
                MLog.e(TAG, "getUploadViewBeans() -> listBeanEntry is null!");
                continue;
            }

            String listId = listBeanEntry.getKey();
            UploadConfigRequest.Config.ListBean listBean = listBeanEntry.getValue();
            if (TextUtils.isEmpty(listId) || null == listBean) {
                MLog.e(TAG, "getUploadViewBeans() -> listId or listBean is null or empty!");
                continue;
            }

            Map<String, String> itemAliases = listBean.getItemAliases();
            if (null == itemAliases || itemAliases.isEmpty()) {
                MLog.e(TAG, "getUploadViewBeans() -> itemAliases is null or empty!");
                continue;
            }

            for (Map.Entry<String, String> itemAlias : itemAliases.entrySet()) {
                if (null == itemAlias) {
                    MLog.e(TAG, "getUploadViewBeans() -> itemAlias is null!");
                    continue;
                }

                String md5 = itemAlias.getKey();
                String alias = itemAlias.getValue();
                if (TextUtils.isEmpty(md5) || TextUtils.isEmpty(alias)) {
                    MLog.e(TAG, "getUploadViewBeans() -> md5 or alias is null!");
                    continue;
                }

                UploadViewBean uploadViewBean = new UploadViewBean();
                uploadViewBean.setEventType(Constant.DEFINE_EVENT_TYPE_LIST);
                uploadViewBean.setMd5(md5);
                uploadViewBean.setAlias(alias);
                uploadViewBean.setListId(listId);

                uploadViewBeans.add(uploadViewBean);
            }
        }

        return uploadViewBeans;
    }

}
