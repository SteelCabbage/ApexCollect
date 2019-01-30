package com.chinapex.analytics.sample.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chinapex.analytics.sample.R;
import com.chinapex.analytics.sample.bean.TestTracker;
import com.chinapex.analytics.sample.utils.AppLog;
import com.chinapex.android.datacollect.ApexAnalytics;
import com.chinapex.android.datacollect.model.bean.ApexLocation;
import com.chinapex.android.datacollect.model.bean.TrackEvent;
import com.chinapex.android.datacollect.testAop.CabbageButton;
import com.chinapex.android.datacollect.utils.GsonUtils;

import java.util.Properties;


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
        Button btToTestActivity = (Button) findViewById(R.id.bt_2_test_activity);
        btToTestActivity.setOnClickListener(this);

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

        Button btSetLocaction = (Button) findViewById(R.id.bt_setLocaction);
        btSetLocaction.setOnClickListener(this);
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_2_test_activity:
                startActivity(new Intent(MainActivity.this, TestActivity.class));
                break;
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
                TestTracker testTracker = new TestTracker();
                testTracker.setDate("2018.12.12");
                testTracker.setName("张三");
                testTracker.setAge("18");
                testTracker.setWork("study");

                AppLog.d(TAG, "testTracker:" + GsonUtils.toJsonStr(testTracker));
                ApexAnalytics.getInstance().track(new TrackEvent.EventBuilder()
                        .setMode(0)
                        .setLabel("testTracker")
                        .setValue(GsonUtils.toJsonStr(testTracker))
                        .build());
                break;
            case R.id.bt_instant:
                Properties properties = new Properties();
                properties.put("lala1", "lala11");
                properties.put("lala2", "lala22");
                properties.put("lala3", "lala33");
                properties.put("lala4", "lala44");
                properties.put("lala5", "lala55");

                AppLog.d(TAG, "properties:" + GsonUtils.toJsonStr(properties));
                ApexAnalytics.getInstance().track(new TrackEvent.EventBuilder()
                        .setMode(1)
                        .setLabel("properties")
                        .setValue(GsonUtils.toJsonStr(properties))
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
            case R.id.bt_setLocaction:
                ApexAnalytics.getInstance().setApexLocation(new ApexLocation.LocationBuilder()
                        .setLongitude(11.22)
                        .setLatitude(55.66)
                        .setCountry(String.valueOf("中国" + System.currentTimeMillis()))
                        .setProvince(String.valueOf("上海" + System.currentTimeMillis()))
                        .setCity(String.valueOf("上海市" + System.currentTimeMillis()))
                        .setDistrict("浦东新区")
                        .build());
                break;
            default:
                break;
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE
                                , Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.ACCESS_COARSE_LOCATION
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            }
        }
    }
}
