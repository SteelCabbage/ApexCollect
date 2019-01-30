package com.chinapex.analytics.sample.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinapex.analytics.sample.R;

/**
 * @author : Seven
 * @date : 2018/11/28
 */
public class ViewPagerFragment1 extends BaseFragmentV4 {

    @Override
    View baseOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vp1, container, false);
    }

    @Override
    protected void baseOnViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
