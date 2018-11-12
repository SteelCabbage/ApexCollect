package com.chinapex.analytics.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chinapex.android.datacollect.testAop.CabbageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        Button btTest = (Button) findViewById(R.id.bt_test);
        btTest.setOnClickListener(this);

        CabbageButton cbJar = findViewById(R.id.cb_jar);
        cbJar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_test:
                AtLog.i(TAG, "原有逻辑执行 ====== bt_test被点击了");
                break;
            case R.id.cb_jar:
                AtLog.i(TAG, "原有逻辑执行 ====== jar包中的button被点击了");
                break;
            default:
                break;
        }
    }
}
