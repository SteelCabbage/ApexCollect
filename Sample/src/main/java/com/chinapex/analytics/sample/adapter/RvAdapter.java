package com.chinapex.analytics.sample.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinapex.analytics.sample.R;
import com.chinapex.analytics.sample.bean.Student;
import com.chinapex.analytics.sample.utils.AppLog;
import com.chinapex.android.datacollect.utils.ATLog;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvHolder> implements View.OnClickListener, View
        .OnLongClickListener {

    private static final String TAG = RvAdapter.class.getSimpleName();

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private List<Student> mDatas;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public RvAdapter(List<Student> datas) {
        mDatas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public void onClick(View v) {
        if (null == mOnItemClickListener) {
            ATLog.e(TAG, "mOnItemClickListener is null!");
            return;
        }
        mOnItemClickListener.onItemClick((Integer) v.getTag());
    }

    @Override
    public boolean onLongClick(View v) {
        if (null == mOnItemLongClickListener) {
            ATLog.e(TAG, "mOnItemLongClickListener is null!");
            return false;
        }
        mOnItemLongClickListener.onItemLongClick((Integer) v.getTag());
        return true;
    }

    @NonNull
    @Override
    public RvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        RvHolder holder = new RvHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RvHolder holder, int position) {
        Student student = mDatas.get(position);
        if (null == student) {
            AppLog.e(TAG, "student is null!");
            return;
        }

        holder.name.setText(student.getName());
        holder.age.setText(String.valueOf(student.getAge()));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return null == mDatas ? 0 : mDatas.size();
    }

    class RvHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;

        RvHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_rv_list_name);
            age = itemView.findViewById(R.id.tv_rv_list_age);
        }
    }
}
