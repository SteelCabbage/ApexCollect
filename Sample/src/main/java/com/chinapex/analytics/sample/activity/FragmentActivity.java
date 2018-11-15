package com.chinapex.analytics.sample.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.chinapex.analytics.sample.R;
import com.chinapex.analytics.sample.fragment.BaseFragmentV4;
import com.chinapex.analytics.sample.fragment.FragmentFactory;
import com.chinapex.android.datacollect.utils.ATLog;

/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public class FragmentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = FragmentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initFragment();
        initView();
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.second_fl, FragmentFactory.getFragment("FragmentV4"), "FragmentV4");
        fragmentTransaction.commit();
    }

    private void initView() {
        Button btShow1 = (Button) findViewById(R.id.bt_show1);
        Button btHide1 = (Button) findViewById(R.id.bt_hide1);

        btShow1.setOnClickListener(this);
        btHide1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_show1:
                ATLog.i(TAG, "bt_show1 clicked=======showFragmentV4");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                BaseFragmentV4 fragment = FragmentFactory.getFragment("FragmentV4");
                if (!fragment.isAdded()) {
                    fragmentTransaction.add(R.id.second_fl, fragment, "FragmentV4");
                }
                fragmentTransaction.show(fragment).commit();
                break;
            case R.id.bt_hide1:
                ATLog.i(TAG, "bt_hide1 clicked=======hideFragmentV4");
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.hide(FragmentFactory.getFragment("FragmentV4"));
                fragmentTransaction2.commit();
                break;
            default:
                break;
        }
    }
}
