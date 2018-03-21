package com.mapleaf.centerviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mapleaf on 2018/3/20.
 */

public class CustomRecyclerView<T> extends RecyclerView {
    private List<T> mSimilarList;

    public CustomRecyclerView(Context context, ViewPager viewPager, T data) {
        super(context);

        SimilarItemLayoutManager layoutManager = new SimilarItemLayoutManager();
        setLayoutManager(layoutManager);

        mSimilarList = new ArrayList<>();
        mSimilarList.add(data);
        mSimilarList.add(data);

        mSimilarList.add(0, data);
        CustomRecyclerViewAdapter adapter = new CustomRecyclerViewAdapter(mSimilarList);
        setAdapter(adapter);
        ItemSwipeCallBack callback=new ItemSwipeCallBack(adapter,viewPager);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(this);
    }
}
