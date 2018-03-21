# 背景介绍
最近项目需求，做一个订制的ViewPager，要求在能够左右滑动的同时，ViewPager的item本身也是一个叠放的容器且可上下滑动实现业务要求，如上滑删除、下滑加入收藏等，于是写一篇博客记录一下控件的实现。

首先看一下效果图：

![](https://github.com/xytyl/CenterViewPager/blob/master/CenterPagerView.gif)


# 基本思路

* 外层为ViewPager，实现条目的左右滑动

* ViewPager的子Item为RecyclerView，自定义该RecyclerView的LayoutManager及ItemTouchHelper.SimpleCallback实现控件叠放及上下滑动

# 实现过程

首先是布局文件：

```java?linenums
<android.support.constraint.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:clipChildren="false"
    tools:context="com.mapleaf.centerviewpager.MainActivity">

    <android.support.v4.view.ViewPager
        android:id="@+id/viewpager"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:clipChildren="false"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>

</android.support.constraint.ConstraintLayout>
```

记住viewpager及父控件都要添加`android:clipChildren="false"`，这句代码的含义是子控件可以超过自己的位置显示出来。

接下来初始化ViewPager：

```java?linenums
    private void initViewPager() {
        mPagerAdapter = new CustomPagerAdapter<CustomRecyclerView<String>>();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        ViewGroup.LayoutParams layoutParams = mViewPager.getLayoutParams();
        layoutParams.width = ((Activity) mViewPager.getContext()).getWindowManager().getDefaultDisplay().getWidth() / 21 * 10;
        mTransformer = new ScaleTransformer();
        mViewPager.setPageTransformer(false, mTransformer);
    }
```

ScaleTransformer继承自ViewPager.PageTransformer，它的作用是实现了ViewPager切换时的动画效果，具体见代码。

然后来实现自定义的RecyclerView，其核心在于自定义LayoutManager实现层叠摆放；以及实现ItemTouchHelper.SimpleCallback来订制上下滑动的操作。

自定义的LayoutManager代码如下：

```java?linenums
public class SimilarItemLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0 || state.isPreLayout()) {
            return;
        }
        detachAndScrapAttachedViews(recycler);

        int visibleCount = 2;
        if (getItemCount() < visibleCount) {
            visibleCount = getItemCount();
        }

        for(int i=visibleCount;i>=0;i--){
            View view=recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
            layoutDecorated(view,
                    widthSpace / 2,
                    heightSpace / 2,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 2 + getDecoratedMeasuredHeight(view));
        }
    }
}
```

因为我们的RecyclerView是层叠摆放，因此只需要显示上面的三个view即可，最终将view摆放在RecyclerView的中间。

最后就是实现ItemTouchHelper.SimpleCallback

```java?linenums
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
}
```

因为在我的控件里只需要上下滑动，因此在构造函数中`super(0, ItemTouchHelper.UP | ItemTouchHelper.DOWN);`第一个参数0表示禁止拖动，第二个参数表示支持上下滑动。

在`onSwiped`中可以监测到上下滑动，实现自己的逻辑即可，我在上滑时将第一个元素删除并添加到最后，在下滑时做了动画并移动ViewPager到下一个条目。

在`onChildDraw`中可以自定义动画，想干嘛干嘛(～￣▽￣)～

那么，最后一步就是将上面两个自定义的东东和我们的RecyclerView关联起来即可：
```java?linenums
        SimilarItemLayoutManager layoutManager = new SimilarItemLayoutManager();
        setLayoutManager(layoutManager);
		
		        ItemSwipeCallBack callback=new ItemSwipeCallBack(adapter,viewPager);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(this);
```

附上博客：http://blog.csdn.net/bjyanxin/article/details/79631945
