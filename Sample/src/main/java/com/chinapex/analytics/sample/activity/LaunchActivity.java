package com.chinapex.analytics.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.chinapex.analytics.sample.R;

/**
 * @author wyhusky
 * @date 2019/1/25
 */
public class LaunchActivity extends Activity {

    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                LaunchActivity.this.startActivity(intent);
                LaunchActivity.this.finish();
            }
        }, 2000);
    }
}
