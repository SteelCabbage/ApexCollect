package com.chinapex.android.monitor.view.statistics;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinapex.android.monitor.R;
import com.chinapex.android.monitor.bean.StatisticsBean;
import com.chinapex.android.monitor.global.MonitorCache;
import com.chinapex.android.monitor.utils.MLog;
import com.chinapex.android.monitor.view.FloatingViewManager;
import com.chinapex.android.monitor.view.IFloatingView;
import com.chinapex.android.monitor.view.adapter.ColumnListAdapter;
import com.chinapex.android.monitor.view.adapter.StatisticsListAdapter;
import com.chinapex.android.monitor.view.charts.PieChart;
import com.chinapex.android.monitor.view.charts.PieForm;

import java.util.List;

/**
 * @author wyhusky
 * @date 2019/1/2
 */
public class StatisticsView extends FrameLayout implements StatisticsViewContract.View, IFloatingView, View.OnClickListener, StatisticsListAdapter.OnItemClickListener {

    private static final String TAG = StatisticsView.class.getSimpleName();
    private StatisticsViewContract.Presenter mPresenter;
    private WindowManager mWindowManager;
    private PieChart mPieChart;
    private PieForm mPieForm;
    private LinearLayout mChartContainer;
    private LinearLayout mListContainer;
    private RecyclerView mStatisticsRecyView;
    private ListView mColumnListView;

    private StatisticsListAdapter mListAdapter;
    private ColumnListAdapter mColumnListAdapter;
    private Button mBtnShowList;
    private Button mBtnShowChart;
    private Context mContext;

    public StatisticsView(@NonNull Context context) {
        super(context);
        mContext = context;
        inflate(context, R.layout.layout_window_contrast_data, this);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setPresenter(new StatisticsViewPresenter(this));
        initView();
    }

    private void initView() {
        mPieChart = findViewById(R.id.pie_chart);
        mPieForm = findViewById(R.id.pie_form);

        mColumnListView = findViewById(R.id.lv_count_rank);
        mStatisticsRecyView = findViewById(R.id.statistics_list);
        mListContainer = findViewById(R.id.statistics_list_container);
        mChartContainer = findViewById(R.id.statistics_chart_container);
        mBtnShowList = findViewById(R.id.btn_show_list);
        mBtnShowChart = findViewById(R.id.btn_show_chart);

        mStatisticsRecyView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mListAdapter = new StatisticsListAdapter(MonitorCache.getInstance().getStatisticsBeans());
        mStatisticsRecyView.setAdapter(mListAdapter);
        mListAdapter.setOnItemClickListener(this);

        mColumnListAdapter = new ColumnListAdapter(MonitorCache.getInstance().getStatisticsBeans());
        mColumnListView.setAdapter(mColumnListAdapter);
        mColumnListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPieChart.popChosenPie(i);
                mPieForm.setEventColor(MonitorCache.getInstance().getStatisticsBeans().get(i).getColor());
                mPieForm.setTvClickCount(MonitorCache.getInstance().getStatisticsBeans().get(i).getClickCount());
                mPieForm.setTvClickPercent(MonitorCache.getInstance().getStatisticsBeans().get(i).getClickProportion());
                mPieForm.setTvEventName(MonitorCache.getInstance().getStatisticsBeans().get(i).getEventLabel());
            }
        });

        mBtnShowChart.setOnClickListener(this);
        mBtnShowList.setOnClickListener(this);
        findViewById(R.id.btn_clear_list).setOnClickListener(this);
        findViewById(R.id.statistics_back).setOnClickListener(this);

        mBtnShowChart.setTextColor(Color.BLACK);
        mBtnShowList.setTextColor(getResources().getColor(R.color.c_548FA8));
        mChartContainer.setVisibility(View.GONE);
        mListContainer.setVisibility(View.VISIBLE);

        loadData();
    }

    private void loadData() {
        mPresenter.loadContrastData();
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

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.btn_clear_list) {
            List<StatisticsBean> statisticsBeans = MonitorCache.getInstance().getStatisticsBeans();
            if (null == statisticsBeans || statisticsBeans.isEmpty()) {
                MLog.e(TAG, "setPieData() -> StatisticsBeans is null or empty !");
                return;
            }
            statisticsBeans.clear();
            mListAdapter.notifyDataSetChanged();
            mColumnListAdapter.notifyDataSetChanged();
            mPieForm.setEventColor(Color.LTGRAY);
            mPieForm.setTvEventName("");
            mPieForm.setTvClickPercent(0);
            mPieForm.setTvClickCount(0);
            updatePieChart();
        } else if (viewId == R.id.btn_show_chart) {
            if (mChartContainer.getVisibility() == View.VISIBLE && mListContainer.getVisibility() == View.GONE) {
                MLog.i(TAG, "onClick() -> statistics chart is already showing  !!!");
                return;
            }
            mBtnShowChart.setTextColor(getResources().getColor(R.color.c_548FA8));
            mBtnShowList.setTextColor(Color.BLACK);
            mChartContainer.setVisibility(View.VISIBLE);
            mListContainer.setVisibility(View.GONE);
        } else if (viewId == R.id.btn_show_list) {
            if (mChartContainer.getVisibility() == View.GONE && mListContainer.getVisibility() == View.VISIBLE) {
                MLog.i(TAG, "onClick() -> statistics list is already showing  !!!");
                return;
            }
            mBtnShowChart.setTextColor(Color.BLACK);
            mBtnShowList.setTextColor(getResources().getColor(R.color.c_548FA8));
            mChartContainer.setVisibility(View.GONE);
            mListContainer.setVisibility(View.VISIBLE);
        } else if (viewId == R.id.statistics_back) {
            FloatingViewManager.getInstance().dismissFloatingView(IFloatingView.WINDOW_CONTRAST_DATA);
        }

    }

    @Override
    public void onItemClick(int position) {
        List<StatisticsBean> statisticsBeans = MonitorCache.getInstance().getStatisticsBeans();
        if (null == statisticsBeans || statisticsBeans.isEmpty()) {
            MLog.e(TAG, "onItemClick() ->  statisticsBeans is null or isEmpty !!!");
            return;
        }

        if (position < 0 || position > statisticsBeans.size() - 1) {
            MLog.e(TAG, "onItemClick()-> argument error, will throw IndexOutOfBoundsException!");
        }
        statisticsBeans.remove(position);
        updateData();
    }

    @Override
    public void updateData() {
        mListAdapter.notifyDataSetChanged();
        mColumnListAdapter.notifyDataSetChanged();
        updatePieChart();
    }

    private void updatePieChart() {
        List<StatisticsBean> statisticsBeans = MonitorCache.getInstance().getStatisticsBeans();
        if (null == statisticsBeans || statisticsBeans.isEmpty()) {
            MLog.e(TAG, "setPieData() -> statisticsBeans is null or empty !");
            return;
        }

        int[] data = new int[statisticsBeans.size()];
        int[] colorArray = new int[statisticsBeans.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = statisticsBeans.get(i).getClickCount();
            colorArray[i] = statisticsBeans.get(i).getColor();
        }
        mPieChart.setData(data, colorArray);
    }


    @Override
    public void setPresenter(Object presenter) {
        this.mPresenter = (StatisticsViewContract.Presenter)presenter;
    }
}
