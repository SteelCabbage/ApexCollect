package com.chinapex.android.monitor.view.charts;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


import com.chinapex.android.monitor.utils.MLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wyhusky
 * @date 2018/12/29
 */
public class PieChart extends View {
    private static final String TAG = PieChart.class.getSimpleName();

    private static final int DEFAULT_RADIUS = 100;
    private static final int INIT_ANIMATION_DURATION = 1000;
    private static final int POP_ANIMATION_DURATION = 300;
    private static final float POP_PIE_TRANSLATION_LENGTH = 25;

    private static final int NO_ANIMATION_RUNNING = 0;
    private static final int POP_ANIMATION_RUNNING = 1 << 1;
    private static final int RESTORE_ANIMATION_RUNNING = 1 << 2;

    private int[] data;
    private int[] mColor;
    private int sum;
    private int total;

    private Paint mPaint;
    private RectF mRect;
    private List<ArcInfo> arcList;
    private float mAnimateAngle;
    private boolean mIsFirstTimeVisible;
    private int mAnimationFlag;

    public PieChart(Context context) {
        this(context, null);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mIsFirstTimeVisible = true;
        arcList = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
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
            setMeasuredDimension(2 * DEFAULT_RADIUS, 2 * DEFAULT_RADIUS);
        } else if (measureWidthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(2 * DEFAULT_RADIUS, measureHeightSize);
        } else if (measureHeightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(measureWidthSize, 2 * DEFAULT_RADIUS);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = w - getPaddingLeft() - getPaddingRight();
        int height = h - getPaddingTop() - getPaddingBottom();
        int radius = width > height ? height / 2 : width / 2;

        float centerX = w / 2f;
        float centerY = h / 2f;
        if (radius > DEFAULT_RADIUS) {
            mRect = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        } else {
            mRect = new RectF(centerX - DEFAULT_RADIUS, centerY - DEFAULT_RADIUS, centerX + DEFAULT_RADIUS, centerY + DEFAULT_RADIUS);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (arcList == null || arcList.size() == 0) {
            canvas.drawArc(mRect, 0f, 360f, true, mPaint);
            return;
        }

        for (int i = 0; i < arcList.size(); i++) {
            ArcInfo arcInfo = arcList.get(i);
            float startAngle = arcInfo.getStartAngle();
            float sweepAngle = arcInfo.getSweepAngle();
            int color = arcInfo.getColor();
            mPaint.setColor(color);
            float animateSweepAngle = Math.min(sweepAngle, mAnimateAngle - startAngle);
            if (animateSweepAngle > 0) {
                if (arcInfo.isPopPie) {
                    canvas.save();
                    float dx = arcInfo.getTranslationX();
                    float dy = arcInfo.getTranslationY();
                    canvas.translate(dx, dy);
                    canvas.drawArc(mRect, startAngle, animateSweepAngle, true, mPaint);
                    canvas.restore();
                } else {
                    canvas.drawArc(mRect, startAngle, animateSweepAngle, true, mPaint);
                }
            }
        }
    }

    public void setData(int[] data, int[] color) {
        this.data = data;
        for (Integer i : data) {
            sum += i;
        }
        this.mColor = color;
        calculateArcInfo();
        invalidate();
    }

    public void setData(int[] data, int[] color, int total) {
        this.data = data;
        this.total = total;
        for (Integer i : data) {
            sum += i;
        }
        this.mColor = color;
        calculateArcInfo();
        invalidate();
    }

    public void popChosenPie(int i) {
        if (mAnimationFlag != NO_ANIMATION_RUNNING) {
            MLog.d(TAG, "popChosenPie()-> animation is running, do no operation");
            return;
        }

        if (i < 0 || i > arcList.size() - 1) {
            MLog.e(TAG, "popChosenPie()-> illegal argument");
            return;
        }

        final ArcInfo popPie = arcList.get(i);
        ArcInfo prePopPie = null;
        for (ArcInfo arcInfo : arcList) {
            if (arcInfo.isPopPie()) {
                prePopPie = arcInfo;
            }
        }

        // 之前没有弹出的pie
        if (null == prePopPie) {
            popAnimator(popPie);
            return;
        }

        if (null == popPie) {
            MLog.d(TAG, "popChosenPie()-> pop pie info is abnormally null");
            return;
        }

        if (popPie.isPopPie()) {
            //选中的pie就是已弹出的pie
            restoreAnimator(popPie);
        } else {
            restoreAnimator(prePopPie);
            popAnimator(popPie);
        }
    }

    private void calculateArcInfo() {
        if (data == null || data.length == 0) {
            arcList.clear();
            ArcInfo arcInfo = new ArcInfo();
            arcInfo.setStartAngle(0);
            arcInfo.setSweepAngle(360);
            arcInfo.setCenterAngle(180);
            arcInfo.setColor(Color.LTGRAY);
            arcList.add(arcInfo);
            return;
        }

        if (arcList.size() > 0) {
            arcList.clear();
        }

        int startAngle = 0;
        int circulation = total > sum ? data.length + 1 : data.length;
        for (int i = 0; i < circulation; i++) {
            float sweepAngle;

            if (total > sum) {
                if (i == data.length) {
                    sweepAngle = 360 - startAngle;
                } else {
                    sweepAngle = (data[i] / (float) total) * 360;
                }
            } else {
                if (i == data.length - 1) {
                    sweepAngle = 360 - startAngle;
                } else {
                    sweepAngle = (data[i] / (float) sum) * 360;
                }
            }

            ArcInfo arcInfo = new ArcInfo();
            arcInfo.setColor(mColor[i]);
            arcInfo.setStartAngle(startAngle);
            arcInfo.setSweepAngle(sweepAngle);

            startAngle += sweepAngle;
            float centerAngle = startAngle - sweepAngle / 2;
            arcInfo.setCenterAngle(centerAngle);
            arcList.add(arcInfo);
        }
    }

    private void initAnimator() {
        ValueAnimator initAnimation = ValueAnimator.ofFloat(0, 360);
        initAnimation.setDuration(INIT_ANIMATION_DURATION);
        initAnimation.setInterpolator(new LinearInterpolator());
        initAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimateAngle = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        initAnimation.start();
        mIsFirstTimeVisible = false;
    }

    private void popAnimator(final ArcInfo popPie) {
        popPie.setPopPie(true);
        double radian = Math.toRadians(popPie.getCenterAngle());
        float dx = (float) (POP_PIE_TRANSLATION_LENGTH * Math.cos(radian));
        float dy = (float) (POP_PIE_TRANSLATION_LENGTH * Math.sin(radian));
        ValueAnimator xAnimation = ValueAnimator.ofFloat(0, dx);
        ValueAnimator yAnimation = ValueAnimator.ofFloat(0, dy);
        xAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                popPie.setTranslationX(value);
                invalidate();
            }
        });
        yAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                popPie.setTranslationY(value);
                invalidate();
            }
        });
        AnimatorSet popAnimatorSet = new AnimatorSet();
        popAnimatorSet.setInterpolator(new LinearInterpolator());
        popAnimatorSet.playTogether(xAnimation, yAnimation);
        popAnimatorSet.setDuration(POP_ANIMATION_DURATION);
        popAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimationFlag |= POP_ANIMATION_RUNNING;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationFlag &= ~POP_ANIMATION_RUNNING;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mAnimationFlag &= ~POP_ANIMATION_RUNNING;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        popAnimatorSet.start();
    }

    private void restoreAnimator(final ArcInfo restorePie) {
        ValueAnimator xAnimation = ValueAnimator.ofFloat(restorePie.getTranslationX(), 0);
        ValueAnimator yAnimation = ValueAnimator.ofFloat(restorePie.getTranslationY(), 0);
        xAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                restorePie.setTranslationX(value);
                invalidate();
            }
        });
        yAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                restorePie.setTranslationY(value);
                invalidate();
            }
        });
        AnimatorSet restoreAnimatorSet = new AnimatorSet();
        restoreAnimatorSet.setInterpolator(new LinearInterpolator());
        restoreAnimatorSet.playTogether(xAnimation, yAnimation);
        restoreAnimatorSet.setDuration(POP_ANIMATION_DURATION);
        restoreAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mAnimationFlag |= RESTORE_ANIMATION_RUNNING;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                restorePie.setPopPie(false);
                mAnimationFlag &= ~RESTORE_ANIMATION_RUNNING;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                mAnimationFlag &= ~RESTORE_ANIMATION_RUNNING;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        restoreAnimatorSet.start();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE && mIsFirstTimeVisible) {
            initAnimator();
        }
    }

    /**
     * save info of each Arc
     */
    class ArcInfo {

        private boolean isPopPie;

        private float translationX;

        private float translationY;

        private float startAngle;

        private float sweepAngle;

        private float centerAngle;

        private int color;

        public boolean isPopPie() {
            return isPopPie;
        }

        public void setPopPie(boolean popPie) {
            isPopPie = popPie;
        }

        public float getTranslationX() {
            return translationX;
        }

        public void setTranslationX(float translationX) {
            this.translationX = translationX;
        }

        public float getTranslationY() {
            return translationY;
        }

        public void setTranslationY(float translationY) {
            this.translationY = translationY;
        }

        float getStartAngle() {
            return startAngle;
        }

        void setStartAngle(float startAngle) {
            this.startAngle = startAngle;
        }

        float getSweepAngle() {
            return sweepAngle;
        }

        void setSweepAngle(float sweepAngle) {
            this.sweepAngle = sweepAngle;
        }

        float getCenterAngle() {
            return centerAngle;
        }

        void setCenterAngle(float centerAngle) {
            this.centerAngle = centerAngle;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return "startAngle" + startAngle + "\n" +
                    "sweepAngle" + sweepAngle + "\n" +
                    "centerAngle" + centerAngle + '\n' +
                    "color" + color;
        }
    }
}
