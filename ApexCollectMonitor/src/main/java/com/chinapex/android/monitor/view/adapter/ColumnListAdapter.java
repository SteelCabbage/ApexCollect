package com.chinapex.android.monitor.view.adapter;


import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinapex.android.monitor.R;
import com.chinapex.android.monitor.bean.StatisticsBean;
import com.chinapex.android.monitor.utils.DensityUtil;

import java.util.List;

/**
 * @author wyhusky
 * @date 2019/1/10
 */
public class ColumnListAdapter extends BaseAdapter {

    private static final String PERCENT_SYMBOL = "%";

    private List<StatisticsBean> mStatisticsBeans;
    private int mMaxCount;

    public ColumnListAdapter(List<StatisticsBean> mStatisticsBeans) {
        this.mStatisticsBeans = mStatisticsBeans;
        AdapterDataSetObserver observer = new AdapterDataSetObserver();
        registerDataSetObserver(observer);
    }

    @Override
    public int getCount() {
        return mStatisticsBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return mStatisticsBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        StatisticsBean bean = mStatisticsBeans.get(i);
        View view;
        ViewHolder viewHolder;
        if (null == convertView) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.count_rank_list_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.eventNameTv = view.findViewById(R.id.tv_event_name);
            viewHolder.percentTv = view.findViewById(R.id.tv_column_percent);
            viewHolder.columnView = view.findViewById(R.id.v_column);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        float maxWidth = DensityUtil.getScreenWidth(viewGroup.getContext()) - viewGroup.getContext().getResources().getDimension(R.dimen.column_margin_left)
                - viewGroup.getContext().getResources().getDimension(R.dimen.column_margin_right);
        int columnViewWidth = (int) (maxWidth * bean.getClickCount()/mMaxCount);
        viewHolder.eventNameTv.setText(bean.getEventLabel());
        viewHolder.percentTv.setText(String.valueOf(bean.getClickProportion()) + PERCENT_SYMBOL);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.columnView.getLayoutParams();
        layoutParams.width = columnViewWidth;
        viewHolder.columnView.setLayoutParams(layoutParams);
        viewHolder.columnView.setBackgroundColor(bean.getColor());
        viewHolder.columnView.invalidate();
        return view;
    }

    static class ViewHolder {
        TextView eventNameTv;
        TextView percentTv;
        View columnView;
    }

    class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            for (StatisticsBean bean : mStatisticsBeans) {
                if (bean.getClickCount() > mMaxCount) {
                    mMaxCount = bean.getClickCount();
                }
            }
        }

        @Override
        public void onInvalidated() {
            for (StatisticsBean bean : mStatisticsBeans) {
                if (bean.getClickCount() > mMaxCount) {
                    mMaxCount = bean.getClickCount();
                }
            }
        }
    }
}
