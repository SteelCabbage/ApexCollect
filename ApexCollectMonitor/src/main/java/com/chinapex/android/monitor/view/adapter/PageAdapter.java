package com.chinapex.android.monitor.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinapex.android.monitor.R;
import com.chinapex.android.monitor.utils.MLog;

import java.util.ArrayList;
import java.util.List;


/**
 * @author wyhusky
 * @date 2019/1/28
 */
public class PageAdapter extends BaseAdapter {

    private static final String TAG = PageAdapter.class.getSimpleName();
    private static final String HASH_SEPARATOR = "##";
    private static final String DOT_SEPARATOP = "\\.";

    private List<String> pages;
    private List<String> simpleName;
    private Context mContext;

    public PageAdapter(Context context) {
        this.mContext = context;
        this.pages = new ArrayList<>();
        this.simpleName = new ArrayList<>();
    }

    public void addAll(List<String> pages) {
        this.pages.addAll(pages);
        for (String page : pages) {
            this.simpleName.add(getSimpleName(page));
        }
        notifyDataSetChanged();
    }

    public void clear() {
        this.pages.clear();
        this.simpleName.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public Object getItem(int position) {
        return pages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView textView;
        if (null == convertView) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_spinner_item, parent, false);
            textView = view.findViewById(R.id.spinner_text);
            textView.setText(simpleName.get(position));
            view.setTag(textView);
        } else {
            view = convertView;
            textView = (TextView) view.getTag();
            textView.setText(simpleName.get(position));
        }
        return view;
    }

    private String getSimpleName(String str) {
        if (TextUtils.isEmpty(str)) {
            MLog.e(TAG, "getSimpleName()-> str is null");
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String[] fragmentNames = str.split(HASH_SEPARATOR);
        if (fragmentNames.length == 0) {
            MLog.e(TAG, "getSimpleName()-> fragmentNames is empty");
            return "";
        }
        for (int i = 0; i < fragmentNames.length; i++) {
            String[] partNames = fragmentNames[i].split(DOT_SEPARATOP);
            if (partNames.length == 0) {
                MLog.e(TAG, "getSimpleName()-> partNames is empty");
                return "";
            }
            String simpleName = partNames[partNames.length - 1];
            if (i > 0) {
                sb.append(HASH_SEPARATOR);
            }
            sb.append(simpleName);
        }
        return sb.toString();
    }
}
