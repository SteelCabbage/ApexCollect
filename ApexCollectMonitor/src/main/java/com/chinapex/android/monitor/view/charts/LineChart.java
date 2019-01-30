package com.chinapex.android.monitor.view.charts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.chinapex.android.monitor.R;
import com.chinapex.android.monitor.bean.LineChartPoint;
import com.chinapex.android.monitor.utils.DateUtils;
import com.chinapex.android.monitor.utils.DensityUtil;
import com.chinapex.android.monitor.utils.MLog;

import java.util.ArrayList;
import java.util.List;


/**
 * @author wyhusky
 * @date 2019/1/2
 */
public class LineChart extends View {

    private static final String TAG = LineChart.class.getSimpleName();

    private static final int DEFAULT_HEIGHT = 250;
    private static final int DEFAULT_WIDTH = 250;
    private static final int MAX_DATA_LENGTH = 7;

    private String[] mDateArray;
    private int[] mClickCount;


    private int mWidth;
    private int mHeight;

    /**
     * left and right padding
     */
    private float basePadding;

    /**
     * the width between two items
     */
    private float itemWidth;

    /**
     * height of the highest point
     */
    private float maxLineHeight;

    /**
     * height of the lowest point
     */
    private float minLineHeight;

    /**
     * space between maxLineHeight and minLineHeight
     */
    private float mLineSpace;

    /**
     * y position of baseline
     */
    private float mBaseLinePosition;

    /**
     * max of clickCount
     */
    private int mMaxClickCount;

    /**
     * min of clickCount
     */
    private int mMinClickCount;

    /**
     * the range between mMaxClickCount and mMinClickCount
     */
    private int mClickRange;

    /**
     * LineChart attrs
     */
    private int mPointColor, mTextColor, mBaseLineColor, mBrokenLineColor, mDashLineColor;
    private float mPointRadius, mTextSize, mBaseLineWidth, mBrokenLineWidth, mDashLineWidth;


    private List<LineChartPoint> mPointsInfoList;

    private Path mPath;

    private Paint mPointPaint, mBaseLinePaint, mTextPaint, mBrokenLinePaint, mDashLinePaint;

    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.LineChart,
                0, 0);

        //define default attr values
        int defPointColor = Color.parseColor("#00FF00");
        float defPointRadius = DensityUtil.dip2px(context, 3);
        int defTextColor = Color.parseColor("#212121");
        float defTextSize = DensityUtil.sp2px(context, 12);
        int defBaseLineColor = Color.parseColor("#000000");
        float defBaseLineWidth = DensityUtil.dip2px(context, 3);
        int defBrokenLineColor = Color.LTGRAY;
        float defBrokenLineWidth = DensityUtil.dip2px(context, 1);
        int defDashLineColor = Color.parseColor("#000000");
        float defDashLineWidth = DensityUtil.dip2px(context, 1);

        mPointColor = a.getColor(R.styleable.LineChart_pointColor, defPointColor);
        mPointRadius = a.getDimension(R.styleable.LineChart_pointRadius, defPointRadius);
        mTextColor = a.getColor(R.styleable.LineChart_textColor, defTextColor);
        mTextSize = a.getDimension(R.styleable.LineChart_textSize, defTextSize);
        mBaseLineColor = a.getColor(R.styleable.LineChart_baseLineColor, defBaseLineColor);
        mBaseLineWidth = a.getDimension(R.styleable.LineChart_baseLineWidth, defBaseLineWidth);
        mBrokenLineColor = a.getColor(R.styleable.LineChart_brokenLineColor, defBrokenLineColor);
        mBrokenLineWidth = a.getDimension(R.styleable.LineChart_baseLineWidth, defBrokenLineWidth);
        mDashLineColor = a.getColor(R.styleable.LineChart_dashLineColor, defDashLineColor);
        mDashLineWidth = a.getDimension(R.styleable.LineChart_dashLineWidth, defDashLineWidth);

        a.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (measureWidthMode == MeasureSpec.AT_MOST
                && measureHeightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        } else if (measureWidthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, measureHeightSize);
        } else if (measureHeightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(measureWidthSize, DEFAULT_HEIGHT);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w - getPaddingLeft() - getPaddingRight();
        mHeight = h - getPaddingTop() - getPaddingBottom();

        basePadding = mWidth / 16f;
        itemWidth = (mWidth - 2 * basePadding) / 6f;

        minLineHeight = mHeight - mHeight * 3 / 5;
        maxLineHeight = mHeight - mHeight / 5;
        mLineSpace = maxLineHeight - minLineHeight;
        caculatePointsInfo();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDateText(canvas);
        drawBaseLine(canvas);
        drawPoints(canvas);
    }

    public void setData(int[] clickCount, String[] date) {
        if (null == clickCount || date == null) {
            MLog.e(TAG, "setData()-> params can not be null!");
            return;
        }

        if (clickCount.length == 0 || date.length == 0) {
            MLog.e(TAG, "setData()-> no data in the array!");
            return;
        }

        if (clickCount.length > MAX_DATA_LENGTH) {
            int[] tmp = clickCount;
            clickCount = new int[MAX_DATA_LENGTH];
            System.arraycopy(tmp, 0, clickCount, 0, MAX_DATA_LENGTH);
        }

        if (date.length > MAX_DATA_LENGTH) {
            String[] tmp = date;
            date = new String[MAX_DATA_LENGTH];
            System.arraycopy(tmp, 0, date, 0, MAX_DATA_LENGTH);
        }

        this.mClickCount = clickCount;
        this.mDateArray = date;

        int minTmp = clickCount[0], maxTmp = clickCount[0];
        for (Integer count : clickCount) {
            if (count < minTmp) {
                minTmp = count;
            }
            if (count > maxTmp) {
                maxTmp = count;
            }
        }
        mMaxClickCount = maxTmp;
        mMinClickCount = minTmp;
        mClickRange = mMaxClickCount - mMinClickCount;
        mClickRange = mClickRange == 0 ? 1 : mClickRange;

        invalidate();
    }

    public List<LineChartPoint> getmPointsInfoList() {
        return mPointsInfoList;
    }

    private void init() {
        mPath = new Path();
        mPointsInfoList = new ArrayList<>();

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setColor(mPointColor);

        mBaseLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBaseLinePaint.setStyle(Paint.Style.FILL);
        mBaseLinePaint.setColor(mBaseLineColor);
        mBaseLinePaint.setStrokeWidth(mBaseLineWidth);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mBrokenLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBrokenLinePaint.setStyle(Paint.Style.STROKE);
        mBrokenLinePaint.setColor(mBrokenLineColor);
        mBrokenLinePaint.setStrokeWidth(mBrokenLineWidth);

        mDashLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDashLinePaint.setStyle(Paint.Style.STROKE);
        mDashLinePaint.setStrokeWidth(mDashLineWidth);
        mDashLinePaint.setColor(mDashLineColor);
        mDashLinePaint.setPathEffect(new DashPathEffect(new float[]{5, 10}, 0));
    }

    private void caculatePointsInfo() {
        if (mPointsInfoList.isEmpty()) {
            MLog.e(TAG, "caculatePoints()-> points info list is empty!");
            return;
        }
        int dataLength = mClickCount.length > mDateArray.length ? mDateArray.length : mClickCount.length;
        float dx, dy;
        for (int i = 0; i < dataLength; i++) {
            dx = basePadding + itemWidth * i;
            dy = mHeight - ((mClickCount[i] - mMinClickCount) * mLineSpace / mClickRange + minLineHeight);
            LineChartPoint point = new LineChartPoint();
            point.setX(dx);
            point.setY(dy);
            point.setCount(mClickCount[i]);
            point.setDate(DateUtils.string2Date(mDateArray[i]));
            mPointsInfoList.add(i, point);
        }
    }

    private void drawBaseLine(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        mBaseLinePosition = mHeight - fontHeight * 3 / 2;

        int startx = (int) basePadding;
        int endx = (int) (mWidth - basePadding);
        int y = (int) mBaseLinePosition;
        canvas.drawLine(startx, y, endx, y, mBaseLinePaint);
    }

    private void drawDateText(Canvas canvas) {
        float dy = mHeight;
        for (int i = 0; i < mPointsInfoList.size(); i++) {
            LineChartPoint point = mPointsInfoList.get(i);
            canvas.drawText(DateUtils.date2MonthDayString(point.getDate()), point.getX(), dy, mTextPaint);
        }
    }

    private void drawPoints(Canvas canvas) {
        // draw brokenLine
        for (int i = 0; i < mPointsInfoList.size(); i++) {
            LineChartPoint point = mPointsInfoList.get(i);
            float dx = point.getX();
            float dy = point.getY();
            if (i == 0) {
                mPath.reset();
                mPath.moveTo(dx, dy);
            } else {
                mPath.lineTo(dx, dy);
            }
            canvas.drawPath(mPath, mBrokenLinePaint);
        }


        // draw dashline
        for (int i = 0; i < mPointsInfoList.size(); i++) {
            LineChartPoint point = mPointsInfoList.get(i);
            float dx = point.getX();
            float dy = point.getY();
            mPath.reset();
            mPath.moveTo(dx, dy);
            mPath.lineTo(dx, mBaseLinePosition);
            canvas.drawPath(mPath, mDashLinePaint);
            canvas.drawCircle(dx, dy, mPointRadius, mPointPaint);
        }
    }
}
