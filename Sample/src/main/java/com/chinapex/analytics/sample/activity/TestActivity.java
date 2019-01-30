package com.chinapex.analytics.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chinapex.analytics.sample.R;
import com.chinapex.analytics.sample.utils.AppLog;

/**
 * @author SteelCabbage
 * @date 2019/01/25
 */
public class TestActivity extends Activity implements View.OnClickListener {

    private static final String TAG = TestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();
    }

    private void initView() {
        Button button = (Button) findViewById(R.id.bt_test_activity);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_test_activity:
                AppLog.i(TAG, "bt_test_activity has been clicked!");
                break;
            default:
                break;
        }
    }
}
