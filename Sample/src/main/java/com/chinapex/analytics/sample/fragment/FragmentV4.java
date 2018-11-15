package com.chinapex.analytics.sample.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinapex.analytics.sample.R;
import com.chinapex.android.datacollect.utils.ATLog;

/**
 * @author SteelCabbage
 * @date 2018/11/14
 */
public class FragmentV4 extends BaseFragmentV4 {

    private static final String TAG = FragmentV4.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_v4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        ATLog.i(TAG, "FragmentV4 onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        ATLog.i(TAG, "FragmentV4 onPause");
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        ATLog.i(TAG, "FragmentV4 onHiddenChanged:" + hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        ATLog.i(TAG, "FragmentV4 setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }
}
