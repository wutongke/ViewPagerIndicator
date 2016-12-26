package com.linked.erfli.viewpagerindicator;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        GuideIndicator indicator = (GuideIndicator) findViewById(R.id.indicator);
        ImageView imageView1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.image, null);
        imageView1.setImageResource(R.drawable.b1);
        ImageView imageView2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.image, null);
        imageView2.setImageResource(R.drawable.b2);
        ImageView imageView3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.image, null);
        imageView3.setImageResource(R.drawable.b3);
        ImageView imageView4 = (ImageView) LayoutInflater.from(this).inflate(R.layout.image, null);
        imageView4.setImageResource(R.drawable.b4);

        List<View> viewList = new ArrayList<>();
        viewList.add(imageView1);
        viewList.add(imageView2);
        viewList.add(imageView3);
        viewList.add(imageView4);
        ViewPagerAdapter adapter = new ViewPagerAdapter(viewList, this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        indicator.setViewPager(viewPager);
        indicator.startAnimation();
    }


    public class ViewPagerAdapter extends PagerAdapter {

        // 界面列表
        private List<View> views;
        private Activity activity;

        public ViewPagerAdapter(List<View> views, Activity activity) {
            this.views = views;
            this.activity = activity;
        }

        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        public View getItem(int pos) {
            return views.get(pos);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "ff";
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }
    }
}
