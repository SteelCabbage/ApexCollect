package com.chinapex.analytics.sample.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.chinapex.analytics.sample.R;
import com.chinapex.analytics.sample.utils.AppLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SteelCabbage
 * @date 2018/11/14
 */
public class FragmentNoV4 extends Fragment implements View.OnClickListener {
    private static final String TAG = FragmentNoV4.class.getSimpleName();
    private ListView mLvFrag;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_v4, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btFrag = view.findViewById(R.id.bt_frag);
        btFrag.setOnClickListener(this);

        Button btFrag2 = view.findViewById(R.id.bt_frag2);
        btFrag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLog.i(TAG, "FragmentNoV4 bt_frag2 匿名内部类点击");
            }
        });

        mLvFrag = view.findViewById(R.id.lv_frag);
        mLvFrag.setTag(R.id.apex_data_collect_list_data_path, "item");
        mLvFrag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppLog.w(TAG, "mListView 的匿名内部类");
            }
        });

        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list1.add("" + i);
        }

        mLvFrag.setAdapter(new ArrayAdapter<>(this.getActivity(), R.layout.list_view_item, R.id.list_view_item_text, list1));
    }

    @Override
    public void onResume() {
        AppLog.i(TAG, "Fragment onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        AppLog.i(TAG, "Fragment onPause");
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        AppLog.i(TAG, "Fragment onHiddenChanged:" + hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        AppLog.i(TAG, "Fragment setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_frag:
                AppLog.i(TAG, "FragmentNoV4 bt_frag clicked");
                AppLog.i(TAG, "FragmentNoV4 bt_frag getCanonicalName" + v.getContext().getClass().getCanonicalName());
                break;
            default:
                break;
        }
    }
}
