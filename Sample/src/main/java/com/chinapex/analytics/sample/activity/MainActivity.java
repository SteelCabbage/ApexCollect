package com.chinapex.analytics.sample.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.chinapex.analytics.sample.R;
import com.chinapex.analytics.sample.utils.AppLog;
import com.chinapex.android.datacollect.ApexAnalytics;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.testAop.CabbageButton;


/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        checkPermission();
    }

    private void initView() {
        Button btTest = (Button) findViewById(R.id.bt_test);
        btTest.setOnClickListener(this);

        CabbageButton cbJar = findViewById(R.id.cb_jar);
        cbJar.setOnClickListener(this);

        Button bt2fragment = (Button) findViewById(R.id.bt_2fragment);
        bt2fragment.setOnClickListener(this);

        Button bt2rvList = (Button) findViewById(R.id.bt_2rv_list);
        bt2rvList.setOnClickListener(this);

        Button btDelay = (Button) findViewById(R.id.bt_delay);
        btDelay.setOnClickListener(this);

        Button btInstant = (Button) findViewById(R.id.bt_instant);
        btInstant.setOnClickListener(this);

        Button btSignIn = (Button) findViewById(R.id.bt_signIn);
        btSignIn.setOnClickListener(this);

        Button btSignOut = (Button) findViewById(R.id.bt_signOut);
        btSignOut.setOnClickListener(this);

        Button btnClick = (Button) findViewById(R.id.bt_click_test);
        btnClick.setOnClickListener(this);
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
            case R.id.bt_2fragment:
                AppLog.i(TAG, "原有逻辑执行 ====== 跳转到FragmentActivity");
                Intent intent = new Intent(MainActivity.this, FragmentActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_2rv_list:
                AppLog.i(TAG, "原有逻辑执行 ====== 跳转到ListRvActivity");
                Intent intent2 = new Intent(MainActivity.this, ListRvActivity.class);
                startActivity(intent2);
                break;
            case R.id.bt_delay:
                ApexAnalytics.getInstance().track(new TrackEvent.EventBuilder()
                        .setLabel("延时上报的label")
                        .setValue("{\n" +
                                "                \"custom1\": \"111111\",\n" +
                                "                \"custom2\": \"222222\",\n" +
                                "                \"custom3\": \"333333\",\n" +
                                "                \"custom4\": \"444444\",\n" +
                                "                \"custom5\": \"555555\"\n" +
                                "            }")
                        .build());
                break;
            case R.id.bt_instant:
                ApexAnalytics.getInstance().track(new TrackEvent.EventBuilder()
                        // 0: delay (default), 1: instant
                        .setMode(1)
                        .setLabel("即时上报的label")
                        .setValue("{\n" +
                                "                \"custom1\": \"111111\",\n" +
                                "                \"custom2\": \"222222\",\n" +
                                "                \"custom3\": \"333333\",\n" +
                                "                \"custom4\": \"444444\",\n" +
                                "                \"custom5\": \"555555\"\n" +
                                "            }")
                        .build());
                break;
            case R.id.bt_signIn:
                ApexAnalytics.getInstance().signIn("userId登入啦");
                break;
            case R.id.bt_signOut:
                ApexAnalytics.getInstance().signOut();
                break;
            case R.id.bt_click_test:
                startActivity(new Intent(this, ClickTestActivity.class));
                break;
            default:
                break;
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
//                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE
                                /* , Manifest.permission.ACCESS_FINE_LOCATION
                                 , Manifest.permission.ACCESS_COARSE_LOCATION*/},
                        REQUEST_PERMISSION);
            }
        }
    }
}
