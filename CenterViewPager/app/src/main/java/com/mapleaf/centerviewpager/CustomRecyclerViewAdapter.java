package com.mapleaf.centerviewpager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by mapleaf on 2018/3/12.
 */

public class CustomRecyclerViewAdapter<T> extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder>{

    private List<T> mDataList;
    private Context mContext;

    public CustomRecyclerViewAdapter(List<T> list) {
        mDataList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.recommend_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(mDataList.get(position)).transform(new GlideRoundTransform(mContext,9)).into(holder.mImageViewImg);
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageViewImg;

        ViewHolder(View itemView) {
            super(itemView);
            mImageViewImg = itemView.findViewById(R.id.iv_recommend_recyclerview_item);
        }
    }

    public void moveItemToBottom(int position) {
        T data = mDataList.remove(position);
        mDataList.add(mDataList.size() > 1 ? mDataList.size() - 1 : 0, data);
        notifyDataSetChanged();
    }
}
