package com.chinapex.android.monitor.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chinapex.android.monitor.R;
import com.chinapex.android.monitor.bean.StatisticsBean;
import com.chinapex.android.monitor.utils.MLog;

import java.util.List;

/**
 * @author : Seven
 * @date : 2019/1/8
 */
public class StatisticsListAdapter extends RecyclerView.Adapter<StatisticsListAdapter.StatisticsListAdapterHolder> implements View.OnClickListener {

    private static final String TAG = StatisticsListAdapter.class.getSimpleName();

    private List<StatisticsBean> mStatisticsBeans;
    private OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public StatisticsListAdapter(List<StatisticsBean> statisticsBeans) {
        mStatisticsBeans = statisticsBeans;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public StatisticsListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_list_item, parent, false);
        StatisticsListAdapterHolder holder = new StatisticsListAdapterHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsListAdapterHolder holder, int position) {
        if (null == mStatisticsBeans) {
            MLog.e(TAG, "onBindViewHolder() -> mStatisticsBeans is null !!!");
            return;
        }

        StatisticsBean statisticsBean = mStatisticsBeans.get(position);

        if (null == statisticsBean) {
            MLog.e(TAG, "onBindViewHolder() -> statisticsBean is null !!!");
            return;
        }

        holder.eventSequence.setText(String.valueOf(position));
        holder.evenName.setText(statisticsBean.getEventLabel());
        holder.clickCount.setText(String.valueOf(statisticsBean.getClickCount()));
        String proPortion = String.valueOf(statisticsBean.getClickProportion()) + "%";
        holder.clickProportion.setText(proPortion);

        holder.ibDeleteItem.setTag(position);
        holder.ibDeleteItem.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return null == mStatisticsBeans ? 0 : mStatisticsBeans.size();
    }

    @Override
    public void onClick(View v) {
        if (null == mOnItemClickListener) {
            MLog.e(TAG, "mOnItemClickListener is null!");
            return;
        }

        int tag = (Integer) v.getTag();
        mOnItemClickListener.onItemClick(tag);
        notifyItemRemoved(tag);
        if (tag != mStatisticsBeans.size()) {
            notifyItemRangeChanged(tag, mStatisticsBeans.size() - tag);
        }
    }

    class StatisticsListAdapterHolder extends RecyclerView.ViewHolder {

        TextView eventSequence;
        TextView evenName;
        TextView clickCount;
        TextView clickProportion;
        ImageButton ibDeleteItem;

        public StatisticsListAdapterHolder(View itemView) {
            super(itemView);
            eventSequence = itemView.findViewById(R.id.tv_sequence);
            evenName = itemView.findViewById(R.id.tv_event_name);
            clickCount = itemView.findViewById(R.id.tv_item_click_cnt_value);
            clickProportion = itemView.findViewById(R.id.tv_item_click_proportion_value);
            ibDeleteItem = itemView.findViewById(R.id.ib_delete_item);
        }
    }
}
