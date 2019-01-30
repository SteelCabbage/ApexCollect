package com.chinapex.android.monitor.view.charts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chinapex.android.monitor.R;
import com.chinapex.android.monitor.bean.LineChartPoint;
import com.chinapex.android.monitor.utils.DateUtils;
import com.chinapex.android.monitor.utils.MLog;

import java.util.List;

/**
 * @author wyhusky
 * @date 2019/1/8
 */
public class LineChartContainer extends FrameLayout {
    private static final String TAG = LineChartContainer.class.getSimpleName();

    private LineChart mLineChart;
    private TextView label;
    private List<LineChartPoint> mLineChartPointsInfo;
    private float mDownX, mDownY;
    private int mTouchSlop;

    public LineChartContainer(@NonNull Context context) {
        this(context, null);
    }

    public LineChartContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_linechart_container, this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mLineChart = findViewById(R.id.line_chart);
        label = findViewById(R.id.tv_label);
        mLineChartPointsInfo = mLineChart.getmPointsInfoList();
    }

    public void setData(int[] clickCount, String[] date) {
        mLineChart.setData(clickCount, date);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                if (mLineChartPointsInfo == null || mLineChartPointsInfo.size() == 0) {
                    return true;
                }
                if (Math.abs(x - mDownX) < mTouchSlop && Math.abs(y - mDownY) < mTouchSlop) {
                    for (LineChartPoint point : mLineChartPointsInfo) {
                        float dx = point.getX();
                        float dy = point.getY();
                        float radius = 30f;
                        boolean isInCircle = (mDownX < dx + radius && mDownX > dx - radius)
                                && (mDownY < dy + radius && mDownY > dy - radius);
                        if (isInCircle) {
                            MLog.d(TAG, "dx=" + dx + " dy=" + dy);
                            String date = DateUtils.date2TimeString(point.getDate());
                            String count = String.valueOf(point.getCount());
                            String labelText = date + "\n" + getResources().getString(R.string.click_count) + count;
                            label.setText(labelText);
                            label.setVisibility(VISIBLE);
                            if (dx < getWidth() / 2) {
                                label.setTranslationX(dx);
                                label.setTranslationY(dy);
                            } else {
                                label.setTranslationX(dx - label.getWidth());
                                label.setTranslationY(dy);
                            }
                            return true;
                        }
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }
}
