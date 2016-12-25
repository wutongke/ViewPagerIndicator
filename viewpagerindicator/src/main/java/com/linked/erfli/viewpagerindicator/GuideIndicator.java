package com.linked.erfli.viewpagerindicator;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linked.erfli.viewpagerindicator.library.R;

import java.util.ArrayList;
import java.util.List;

public class GuideIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {
    private Context mContext;
    private ViewPager mViewPager;
    private int mCurrentTab;

    private int mTabCount;
    private List<View> children = new ArrayList<>();
    private List<ObjectAnimator> animators = new ArrayList<>();

    private Handler timeHandler;
    private Runnable runnable;
    private int maxTime = 3;
    private int minTime = 0;
    private int mCurrentTime = maxTime;

    public GuideIndicator(Context context) {
        this(context, null, 0);
    }

    public GuideIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttributes(context, attrs);
        this.mContext = context;
        timeHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                mCurrentTime--;
                if (mCurrentTime == minTime - 1) {
                    mCurrentTime = minTime;
                    if (mViewPager != null && mCurrentTab == mViewPager.getCurrentItem() && mCurrentTab < mViewPager.getAdapter().getCount() - 1) {
                        mViewPager.setCurrentItem(mCurrentTab + 1, true);
                    } else if (mCurrentTab == mViewPager.getAdapter().getCount() - 1) {
                        resetAllIndicator();
                    }
                }
                updateTimeText();
                timeHandler.postDelayed(this, 1000);
            }
        };

    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.viewpagerindicator);
        maxTime = ta.getInteger(R.styleable.viewpagerindicator_stay_time, 3);
        ta.recycle();
    }

    public void startAnimation() {
        timeHandler.postDelayed(runnable, 1000);
    }

    public void setViewPager(ViewPager vp) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
        }

        this.mViewPager = vp;
        this.mTabCount = vp.getAdapter().getCount();
        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        this.mCurrentTime = maxTime;
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        removeAllViews();
        children.clear();
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            tabView = View.inflate(mContext, R.layout.layout_guide_indicator, null);
            children.add(tabView);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tabView.findViewById(R.id.animation_image), "rotation", 0f, 360f);
            objectAnimator.setDuration(800);
            objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            objectAnimator.setInterpolator(new LinearInterpolator());
            animators.add(objectAnimator);
            addTab(i, tabView);
        }
        updateAnimation();
    }

    private void addTab(final int position, View tabView) {
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = indexOfChild(v);
                if (position != -1) {
                    if (mViewPager.getCurrentItem() != position) {
                        mViewPager.setCurrentItem(position, true);
                    }
                }
            }
        });

        LayoutParams lp_tab = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp_tab = new LayoutParams(80, LayoutParams.MATCH_PARENT);

        addView(tabView, position, lp_tab);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.mCurrentTab = position;
        this.mCurrentTime = maxTime;
        updateAnimation();
    }

    private void updateAnimation() {
        int position = 0;
        for (View child : children) {
            if (position == mCurrentTab) {
                child.findViewById(R.id.animation_image).setVisibility(VISIBLE);
                child.findViewById(R.id.animation_text).setVisibility(VISIBLE);
                child.findViewById(R.id.default_image).setVisibility(GONE);
                ((TextView) child.findViewById(R.id.animation_text)).setText(String.valueOf(mCurrentTime));
                animators.get(position).start();
            } else {
                child.findViewById(R.id.animation_image).setVisibility(GONE);
                child.findViewById(R.id.animation_text).setVisibility(GONE);
                View defaultImage = child.findViewById(R.id.default_image);
                defaultImage.setVisibility(VISIBLE);
                if (position < mCurrentTab) {
                    defaultImage.setBackgroundResource(R.drawable.circle_green_background);
                } else {
                    defaultImage.setBackgroundResource(R.color.transparent);
                }
                animators.get(position).cancel();
            }
            position++;
        }
    }

    private void resetAllIndicator() {
        for (View child : children) {
            child.findViewById(R.id.animation_image).setVisibility(GONE);
            child.findViewById(R.id.animation_text).setVisibility(GONE);
            child.findViewById(R.id.default_image).setVisibility(VISIBLE);
            child.findViewById(R.id.default_image).setBackgroundResource(R.drawable.circle_green_background);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void updateTimeText() {
        if (children != null && children.get(mCurrentTab) != null) {
            ((TextView) children.get(mCurrentTab).findViewById(R.id.animation_text)).setText(String.valueOf(mCurrentTime));
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", mCurrentTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentTab = bundle.getInt("mCurrentTab");
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

}
