package com.chinapex.analytics.sample.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.chinapex.analytics.sample.R;
import com.chinapex.analytics.sample.adapter.ViewPagerAdapter;
import com.chinapex.analytics.sample.fragment.FragmentFactory;
import com.chinapex.analytics.sample.fragment.ViewPagerFragment1;
import com.chinapex.analytics.sample.fragment.ViewPagerFragment2;
import com.chinapex.android.datacollect.utils.ATLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public class FragmentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = FragmentActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);

        //test v4
        //initFragment();

        initFragmentNoV4();
        initView();
        initData();
    }

    private void initData() {
        mFragmentList.add(new ViewPagerFragment1());
        mFragmentList.add(new ViewPagerFragment2());
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList));
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.second_fl, FragmentFactory.getFragment("FragmentV4"), "FragmentV4");
        fragmentTransaction.commit();
    }

    private void initFragmentNoV4() {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.second_fl, FragmentFactory.getFragmentNoV4("FragmentNoV4"), "FragmentNoV4");
        fragmentTransaction.commit();
    }

    private void initView() {
        Button btShow1 = (Button) findViewById(R.id.bt_show1);
        Button btHide1 = (Button) findViewById(R.id.bt_hide1);
        mViewPager = findViewById(R.id.view_pager);

        btShow1.setOnClickListener(this);
        btHide1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_show1:
//                ATLog.i(TAG, "bt_show1 clicked=======showFragmentV4");
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                BaseFragmentV4 fragment = FragmentFactory.getFragment("FragmentV4");
//                if (!fragment.isAdded()) {
//                    fragmentTransaction.add(R.id.second_fl, fragment, "FragmentV4");
//                }
//                fragmentTransaction.show(fragment).commit();

                ATLog.i(TAG, "bt_show1 clicked=======showFragmentNoV4");
                android.app.FragmentTransaction ftNoV4 = getFragmentManager().beginTransaction();
                android.app.Fragment fragmentNoV4 = FragmentFactory.getFragmentNoV4("FragmentNoV4");
                if (!fragmentNoV4.isAdded()) {
                    ftNoV4.add(R.id.second_fl, fragmentNoV4, "FragmentNoV4");
                }
                ftNoV4.show(fragmentNoV4).commit();
                break;
            case R.id.bt_hide1:
//                ATLog.i(TAG, "bt_hide1 clicked=======hideFragmentV4");
//                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction2.hide(FragmentFactory.getFragment("FragmentV4"));
//                fragmentTransaction2.commit();

                ATLog.i(TAG, "bt_hide1 clicked=======hideFragmentNoV4");
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.hide(FragmentFactory.getFragmentNoV4("FragmentNoV4"));
                ft.commit();
                break;
            default:
                break;
        }
    }
}
