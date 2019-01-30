package com.chinapex.android.monitor.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinapex.android.monitor.R;
import com.chinapex.android.monitor.bean.StatisticsBean;
import com.chinapex.android.monitor.bean.UploadViewBean;
import com.chinapex.android.monitor.utils.MLog;

import java.util.List;

/**
 * @author : Seven
 * @date : 2019/1/8
 */
public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.UploadListAdapterHolder> implements View
        .OnClickListener {

    private static final String TAG = UploadListAdapter.class.getSimpleName();

    private List<UploadViewBean> mUploadViewBeans;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public UploadListAdapter(List<UploadViewBean> uploadViewBeans) {
        mUploadViewBeans = uploadViewBeans;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public UploadListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_event_list_item, parent, false);
        return new UploadListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadListAdapterHolder holder, int position) {
        if (null == mUploadViewBeans) {
            MLog.e(TAG, "onBindViewHolder() -> mUploadViewBeans is null !");
            return;
        }

        UploadViewBean uploadViewBean = mUploadViewBeans.get(position);

        if (null == uploadViewBean) {
            MLog.e(TAG, "onBindViewHolder() -> uploadViewBean is null !");
            return;
        }

        holder.eventSequence.setText(String.valueOf(position));
        holder.eventName.setText(uploadViewBean.getAlias());

        holder.btnDeleteItem.setTag(position);
        holder.btnDeleteItem.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return null == mUploadViewBeans ? 0 : mUploadViewBeans.size();
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
        if (tag != mUploadViewBeans.size()) {
            notifyItemRangeChanged(tag, mUploadViewBeans.size() - tag);
        }
    }

    class UploadListAdapterHolder extends RecyclerView.ViewHolder {

        TextView eventSequence;
        TextView eventName;
        TextView btnDeleteItem;

        public UploadListAdapterHolder(View itemView) {
            super(itemView);
            eventSequence = itemView.findViewById(R.id.tv_upload_sequence);
            eventName = itemView.findViewById(R.id.tv_upload_event_name);
            btnDeleteItem = itemView.findViewById(R.id.tv_delete_upload_item);
        }
    }
}
