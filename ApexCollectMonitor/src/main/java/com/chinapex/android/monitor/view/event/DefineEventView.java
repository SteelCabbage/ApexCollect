package com.chinapex.android.monitor.view.event;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chinapex.android.monitor.R;
import com.chinapex.android.monitor.utils.MLog;
import com.chinapex.android.monitor.view.IFloatingView;
import com.chinapex.android.monitor.view.adapter.PageAdapter;
import com.chinapex.android.monitor.view.charts.LineChartContainer;

import java.util.List;


/**
 * @author wyhusky
 * @date 2018/12/27
 */
public class DefineEventView extends LinearLayout implements IFloatingView, DefineEventContract.View, View.OnClickListener {

    private static final String TAG = DefineEventView.class.getSimpleName();
    private WindowManager mWindowManager;
    private DefineEventContract.Presenter mPresenter;
    private LineChartContainer mLineChartContainer;
    private EditText mEditEventName, mEditViewPath, mEditDataPath;
    private Spinner mSpinnerPagePath;
    private PageAdapter mPageAdapter;
    private TextView mTvPathLabel;
    private int mViewType;


    public DefineEventView(Context context, int viewType) {
        super(context);
        this.mViewType = viewType;
        inflate(context, R.layout.layout_window_define_page, this);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mPageAdapter = new PageAdapter(context);
        setPresenter(new DefineEventPresenter(this));
        initView();

    }

    public void loadClickData(String viewPath, String pageClass) {
        mPresenter.loadClickData(viewPath, pageClass);
    }

    public void loadListItemClickData(String viewPath, String pageClass, String itemPath) {
        mPresenter.loadListItemClickData(viewPath, pageClass, itemPath);
    }

    @Override
    public void showViewPath(String path) {
        if (null == mEditViewPath) {
            MLog.e(TAG, "showViewPath()-> mEditViewPath is null");
            return;
        }
        mEditViewPath.setText(path);
    }

    @Override
    public void loadPagePath(List<String> pages) {
        mPageAdapter.clear();
        mPageAdapter.addAll(pages);
    }

    @Override
    public void showReference(String reference) {
        if (null == mEditDataPath) {
            MLog.e(TAG, "showViewPath()-> mEditViewPath is null");
            return;
        }
        mEditDataPath.setText(reference);
    }

    @Override
    public void showLineChart(int[] clickCounts, String[] date) {
        if (null == mLineChartContainer) {
            MLog.e(TAG, "showLineChart()-> mLineChart is null");
            return;
        }
        mLineChartContainer.setData(clickCounts, date);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_save_define_event) {
            save();
        } else if (viewId == R.id.btn_cancel_define_event) {
            mPresenter.dismiss();
        }
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

    @Override
    public void setPresenter(Object presenter) {
        this.mPresenter = (DefineEventContract.Presenter) presenter;
    }

    @Override
    public int getViewType() {
        return mViewType;
    }

    @Override
    public void showToast(int id) {
        Toast.makeText(this.getContext(), getResources().getText(id).toString(), Toast.LENGTH_LONG).show();
    }

    private void initView() {
        mLineChartContainer = findViewById(R.id.line_chart_container);
        findViewById(R.id.btn_save_define_event).setOnClickListener(this);
        findViewById(R.id.btn_cancel_define_event).setOnClickListener(this);

        mEditEventName = findViewById(R.id.et_event_name);
        mEditViewPath = findViewById(R.id.et_view_path);
        mSpinnerPagePath = findViewById(R.id.spinner_page_path);
        mEditDataPath = findViewById(R.id.et_data_path);
        mTvPathLabel = findViewById(R.id.tv_path_label);
        mSpinnerPagePath.setAdapter(mPageAdapter);
        mSpinnerPagePath.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String viewPath = (String) mPageAdapter.getItem(position);
                mPresenter.updatePvData(viewPath);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setEditTextEnable(mEditViewPath, false);
        final String elementPath = getResources().getString(R.string.defined_page);
        switch (mViewType) {
            case IFloatingView.WINDOW_DEFINE_CLICK_PAGE:
                mTvPathLabel.setText(elementPath);
                mEditViewPath.setVisibility(VISIBLE);
                setEditTextEnable(mEditDataPath, false);
                break;
            case IFloatingView.WINDOW_DEFINE_PV_PAGE:
                String reference = getResources().getString(R.string.reference);
                mTvPathLabel.setText(reference);
                mSpinnerPagePath.setVisibility(VISIBLE);
                setEditTextEnable(mEditDataPath, false);
                loadPvData();
                break;
            case IFloatingView.WINDOW_DEFINE_LIST_PAGE:
                mTvPathLabel.setText(elementPath);
                mEditViewPath.setVisibility(VISIBLE);
                setEditTextEnable(mEditDataPath, false);
                break;
            default:
                MLog.e(TAG, "initView()-> no this view type, show pv define page");
                String label = getResources().getString(R.string.reference);
                mTvPathLabel.setText(label);
                mSpinnerPagePath.setVisibility(VISIBLE);
                setEditTextEnable(mEditDataPath, false);
                loadPvData();
                break;
        }
    }

    private WindowManager.LayoutParams getDefaultParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.MATCH_PARENT;
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

    /**
     * 设置EditText是否可以编辑
     *
     * @param et   EditText控件
     * @param mode true可以编辑，false不可编辑
     */
    private void setEditTextEnable(EditText et, boolean mode) {
        if (null == et) {
            return;
        }
        et.setFocusable(mode);
        et.setFocusableInTouchMode(mode);
        et.setLongClickable(mode);
        et.setInputType(mode ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_NULL);
    }

    private void save() {
        String alias = mEditEventName.getText().toString();
        if (TextUtils.isEmpty(alias)) {
            MLog.e(TAG, "alias is empty");
            Toast.makeText(this.getContext(), getResources().getText(R.string.alias_leak_hint).toString(), Toast.LENGTH_LONG).show();
            return;
        }
        switch (getViewType()) {
            case IFloatingView.WINDOW_DEFINE_CLICK_PAGE:
                mPresenter.saveClickEvent(alias);
                break;
            case IFloatingView.WINDOW_DEFINE_LIST_PAGE:
                String itemPath = mEditViewPath.getText().toString();
                if (TextUtils.isEmpty(itemPath)) {
                    MLog.e(TAG, "itemPath is empty");
                    Toast.makeText(this.getContext(), getResources().getText(R.string.alias_leak_hint).toString(), Toast.LENGTH_LONG).show();
                    return;
                }
                mPresenter.saveListItemClickEvent(alias, itemPath);
                break;
            case IFloatingView.WINDOW_DEFINE_PV_PAGE:
                String definedPage = mSpinnerPagePath.getSelectedItem().toString();
                if (TextUtils.isEmpty(definedPage)) {
                    MLog.e(TAG, "defined page is empty");
                    Toast.makeText(this.getContext(), getResources().getText(R.string.defined_page_hint).toString(), Toast.LENGTH_LONG).show();
                    return;
                }
                mPresenter.savePageViewEvent(alias, definedPage);
                break;
            default:
                MLog.e(TAG, "save event:no this view type");
                break;
        }
    }

    private void loadPvData() {
        mPresenter.loadPvData();
    }
}
