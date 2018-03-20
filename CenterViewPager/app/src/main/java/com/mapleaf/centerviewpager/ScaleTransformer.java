package com.mapleaf.centerviewpager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by mapleaf on 2018/3/13.
 */

public class ScaleTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.7f;

    @Override
    public void transformPage(View page, float position) {
        if (position < -1 || position > 1) {
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
        } else {
            if (position < 0) {
                float scale = 1 + 0.3f * position;
                page.setScaleX(scale);
                page.setScaleY(scale);
            } else {
                float scale = 1 - 0.3f * position;
                page.setScaleX(scale);
                page.setScaleY(scale);
            }
        }
    }
}
