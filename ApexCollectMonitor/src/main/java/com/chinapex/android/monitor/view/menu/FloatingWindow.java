package com.chinapex.android.monitor.view.menu;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.chinapex.android.monitor.R;
import com.chinapex.android.monitor.utils.DensityUtil;
import com.chinapex.android.monitor.utils.MLog;
import com.chinapex.android.monitor.view.IFloatingView;


/**
 * @author SteelCabbage
 * @date 2018/12/17
 */
public class FloatingWindow extends FrameLayout implements IFloatingView, FloatingWindowContract.View,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = FloatingWindow.class.getSimpleName();
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private FloatingWindowContract.Presenter mPresenter;

    private ImageView mPointIv;
    private ViewGroup mMonitorMenu;
    private Switch switchMonitor, switchHeatMap;
    private TextView tvHeatMapCharts;
    private int mTouchSlop;
    private float downX, downY, lastX, lastY;


    public FloatingWindow(Context context) {
        super(context);
        inflate(context, R.layout.layout_window_floating, this);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        init();
        mTouchSlop = DensityUtil.getTouchSlop(context);
    }

    private void init() {
        mParams = getDefaultParams();

        setPresenter(new FloatingWindowPresenter(this));

        mPointIv = findViewById(R.id.iv_point);
        mPointIv.setOnClickListener(this);

        mMonitorMenu = (RelativeLayout) findViewById(R.id.monitor_menu);

        // 圈选开关
        switchMonitor = findViewById(R.id.switch_monitor);
        switchMonitor.setOnCheckedChangeListener(this);

        // 热图开关
        switchHeatMap = findViewById(R.id.switch_heat_map);
        switchHeatMap.setOnCheckedChangeListener(this);

        // 对比图表
        tvHeatMapCharts = findViewById(R.id.tv_heat_map_charts);
        tvHeatMapCharts.setOnClickListener(this);


        // 定义页面事件
        TextView tvDefinePageEvents = findViewById(R.id.tv_define_page_events);
        tvDefinePageEvents.setOnClickListener(this);

        // 完成圈选
        TextView tvDone = findViewById(R.id.tv_done_select_event);
        tvDone.setOnClickListener(this);

        // 关闭菜单
        TextView tvClose = findViewById(R.id.close_menu);
        tvClose.setOnClickListener(this);
    }

    @Override
    public void show() {
        this.setVisibility(VISIBLE);
        try {
            mWindowManager.addView(this, mParams);
        } catch (Exception e) {
            MLog.e(TAG, "show Exception:" + e.getMessage());
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
            MLog.e(TAG, "dismiss Exception:" + e.getMessage());
        }
    }

    @Override
    public boolean isHiding() {
        return this.getVisibility() == INVISIBLE;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                downX = ev.getRawX();
                downY = ev.getRawY();
                lastX = downX;
                lastY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                // let parent view to handle move event
                float moveX = ev.getRawX();
                float moveY = ev.getRawY();
                if (Math.abs(moveX - downX) > mTouchSlop || Math.abs(moveY - downY) > mTouchSlop) {
                    intercepted = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getRawX();
                downY = event.getRawY();
                lastX = downX;
                lastY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                mParams.x += event.getRawX() - lastX;
                mParams.y += event.getRawY() - lastY;
                mWindowManager.updateViewLayout(FloatingWindow.this, mParams);
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    private WindowManager.LayoutParams getDefaultParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 10;
        params.y = 10;
        return params;
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_point) {
            transformMenu();
        } else if (viewId == R.id.tv_define_page_events) {
            MLog.i(TAG, "tv_define_page_events is clicked!");
            mPresenter.openDefinePage();
        } else if (viewId == R.id.close_menu) {
            mPresenter.close();
        } else if (viewId == R.id.tv_heat_map_charts) {
            MLog.i(TAG, "tv heat map charts is clicked");
            mPresenter.openContrastDataPage();
        } else if (viewId == R.id.tv_done_select_event) {
            MLog.i(TAG, "tv done select event is clicked");
            mPresenter.openUploadEventPage();
        } else {
            MLog.e(TAG, "unknown view is clicked!");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int i = buttonView.getId();
        if (i == R.id.switch_monitor) {
            mPresenter.switchMonitorMode(isChecked);
        } else if (i == R.id.switch_heat_map) {
            mPresenter.switchContrastMode(isChecked);
        } else {
            MLog.e(TAG, "unknown switch!");
        }
    }

    @Override
    public void transformMenu() {
        if (mPointIv.getVisibility() == GONE) {
            mPointIv.setVisibility(VISIBLE);
            mMonitorMenu.setVisibility(GONE);
        } else if (mPointIv.getVisibility() == VISIBLE) {
            mPointIv.setVisibility(GONE);
            mMonitorMenu.setVisibility(VISIBLE);
        } else {
            MLog.e(TAG, "transformMenu()-> invalid visivility state for floating point");
        }
    }

    @Override
    public void closeMonitorModeIfOpened() {
        if (switchMonitor.isChecked()) {
            switchMonitor.setChecked(false);
        }
    }

    @Override
    public void closeContrastModeIfOpened() {
        if (switchHeatMap.isChecked()) {
            switchHeatMap.setChecked(false);
        }
    }

    @Override
    public void showChartOption(boolean shouldShow) {
        if (shouldShow) {
            tvHeatMapCharts.setVisibility(VISIBLE);
        } else {
            tvHeatMapCharts.setVisibility(GONE);
        }
    }

    @Override
    public void setLabel(int count) {

    }

    @Override
    public void setPresenter(Object presenter) {
        this.mPresenter = (FloatingWindowContract.Presenter) presenter;
    }
}
