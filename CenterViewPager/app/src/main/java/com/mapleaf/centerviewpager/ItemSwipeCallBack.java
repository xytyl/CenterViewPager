package com.mapleaf.centerviewpager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class ItemSwipeCallBack extends ItemTouchHelper.SimpleCallback {
    private CustomRecyclerViewAdapter mAdapter;
    private ViewPager mViewPager;

    public ItemSwipeCallBack(CustomRecyclerViewAdapter adapter, ViewPager viewPager) {
        super(0, ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        mAdapter = adapter;
        mViewPager = viewPager;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(direction== ItemTouchHelper.UP){
            //上滑将该页插入最后
            mAdapter.moveItemToBottom(viewHolder.getAdapterPosition());
        }else if(direction== ItemTouchHelper.DOWN){
            viewHolder.itemView.animate()
                    .translationYBy(1000)
                    .scaleX(0)
                    .scaleY(0)
                    .rotation(720)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (dY < 0) {
            viewHolder.itemView.setTranslationY(dY);
        } else if (dY < viewHolder.itemView.getHeight()) {
            viewHolder.itemView.setTranslationY(dY);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setScaleX(1);
        viewHolder.itemView.setScaleY(1);
        viewHolder.itemView.setRotation(0);
    }
}
