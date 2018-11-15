package com.chinapex.analytics.sample.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chinapex.analytics.sample.utils.AppLog;
import com.chinapex.analytics.sample.R;
import com.chinapex.android.datacollect.testAop.CabbageButton;


/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
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

        Button bt2second = (Button) findViewById(R.id.bt_2second);
        bt2second.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_test:
                AppLog.i(TAG, "原有逻辑执行 ====== bt_test被点击了");
                break;
            case R.id.cb_jar:
                AppLog.i(TAG, "原有逻辑执行 ====== jar包中的button被点击了");
                break;
            case R.id.bt_2second:
                AppLog.i(TAG, "原有逻辑执行 ====== 跳转到第二个activity");
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
