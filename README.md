# ViewPagerIndicator
Simple auto viewPagerIndicator


![](https://github.com/wutongke/ViewPagerIndicator/blob/master/movie-iloveimg-compressed.gif)

### start

- xml
```
<com.linked.erfli.viewpagerindicator.GuideIndicator
            android:id="@+id/indicator"
            android:layout_width="wrap_content"
            android:layout_height="50dp"
            android:layout_alignParentBottom="true"
            android:layout_centerHorizontal="true"
            android:layout_marginBottom="50dp"
            viewpager:stay_time="3" />
```           
- java 
```
   indicator.setViewPager(viewPager);
   indicator.startAnimation();
```
