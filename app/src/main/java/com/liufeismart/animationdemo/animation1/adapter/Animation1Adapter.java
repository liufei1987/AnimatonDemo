package com.liufeismart.animationdemo.animation1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liufeismart.animationdemo.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Animation1Adapter extends RecyclerView.Adapter<Animation1Adapter.Animation1ViewHolder> {

    private List<String> data;
    private OnItemClickCallback mOnItemClickCallback;

    public Animation1Adapter(List<String> data, OnItemClickCallback mOnItemClickCallback) {
        this.data = data;
        this.mOnItemClickCallback = mOnItemClickCallback;
    }

    @NonNull
    @Override
    public Animation1ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_animation1, null);
        return new Animation1ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull Animation1ViewHolder viewHolder, final int i) {
        viewHolder.tv_content.setText(data.get(i));
        viewHolder.tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickCallback.onClick(Animation1Adapter.this, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Animation1ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_content;

        public Animation1ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }

    public interface OnItemClickCallback{
        void onClick(RecyclerView.Adapter adapter, int index);
    }
}
