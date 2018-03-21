package com.mapleaf.centerviewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mapleaf on 2018/3/20.
 */

public class CustomPagerAdapter <T extends View> extends PagerAdapter {
    private List<T> mViewList;

    public CustomPagerAdapter() {
        mViewList = new ArrayList<>();
    }

    public void addAll(List<T> viewList){
        mViewList.addAll(viewList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position %= mViewList.size();
        if (position<0){
            position = mViewList.size()+position;
        }
        T view = mViewList.get(position);
        ViewParent vp =view.getParent();
        if (vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
