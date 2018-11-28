package com.chinapex.analytics.sample.fragment;

import android.support.v4.app.Fragment;

import com.chinapex.android.datacollect.utils.ATLog;


/**
 * @author SteelCabbage
 * @date 2018/10/17
 */
public class BaseFragmentV4 extends Fragment {

    private static final String TAG = BaseFragmentV4.class.getSimpleName();

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
