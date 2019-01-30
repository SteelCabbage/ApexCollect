package com.chinapex.android.monitor.view.charts;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinapex.android.monitor.R;

/**
 * @author wyhusky
 * @date 2019/1/2
 */
public class PieForm extends LinearLayout {

    private ImageView iv;
    private TextView tvEventName, tvClickCount, tvClickPercent;
    public PieForm(Context context) {
        this(context, null);
    }

    public PieForm(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieForm(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_pieform, this);
        init();
    }

    private void init() {
        iv = findViewById(R.id.event_color_iv);
        tvEventName = findViewById(R.id.tv_event_name);
        tvClickCount = findViewById(R.id.tv_click_count);
        tvClickPercent = findViewById(R.id.tv_click_percent);
    }

    public void setEventColor(int color) {
        iv.setBackgroundColor(color);
    }

    public void setTvEventName(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        tvEventName.setText(name);
    }

    public void setTvClickCount(int count) {
        tvClickCount.setText(String.valueOf(count));
    }

    public void setTvClickPercent(float percent) {
        tvClickPercent.setText(String.valueOf(percent) + "%");
    }
}
