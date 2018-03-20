package com.mapleaf.centerviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    CustomPagerAdapter mPagerAdapter;
    ScaleTransformer mTransformer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViewPager();
        loadData();
    }

    private void initViewPager() {
        mPagerAdapter = new CustomPagerAdapter<CustomRecyclerView<String>>();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        ViewGroup.LayoutParams layoutParams = mViewPager.getLayoutParams();
        layoutParams.width = ((Activity) mViewPager.getContext()).getWindowManager().getDefaultDisplay().getWidth() / 21 * 10;
        mTransformer = new ScaleTransformer();
        mViewPager.setPageTransformer(false, mTransformer);
    }

    private void loadData() {
        List<CustomRecyclerView> recyclerViewList = new ArrayList<>();
        List<String> list = getData();
        for(int i=0;i<list.size();i++) {
            CustomRecyclerView recyclerView = new CustomRecyclerView(this, mViewPager, list.get(i));
            recyclerViewList.add(recyclerView);
        }

        mPagerAdapter.addAll(recyclerViewList);
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        list.add("http://img1tuzi.b0.upaiyun.com/upload/origin/7/7494ab340ce37d721313bb40719cf8e6.jpg");
        list.add("http://img1tuzi.b0.upaiyun.com/upload/origin/9/143874059932292.png");
        list.add("http://rrcover.b0.upaiyun.com/cover/52d/841f1ec7440b23e05aa50a3d4938b52d.jpg");
        list.add("http://rrcover.b0.upaiyun.com/702/e4be4d99ff861760a32255e269f77702.jpg");
        list.add("http://rrcover.b0.upaiyun.com/cover/43d/9984bdd3ec0a61561ba1f4303c65343d.jpg");
        return list;
    }
}
