package com.chinapex.analytics.sample.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinapex.analytics.sample.utils.AppLog;


/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public abstract class BaseFragmentV4 extends Fragment {

    private static final String TAG = BaseFragmentV4.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {

        return baseOnCreateView(inflater, container, savedInstanceState);
    }

    abstract View baseOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        baseOnViewCreated(view, savedInstanceState);
    }

    protected abstract void baseOnViewCreated(View view, Bundle savedInstanceState);


    @Override
    public void onResume() {
        AppLog.i(TAG, "FragmentV4 onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        AppLog.i(TAG, "FragmentV4 onPause");
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        AppLog.i(TAG, "FragmentV4 onHiddenChanged:" + hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        AppLog.i(TAG, "FragmentV4 setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }
}
