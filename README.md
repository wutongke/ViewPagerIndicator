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
### other

add something interesting
[通过三次优化，我将gif加载优化了16.9%](https://mp.weixin.qq.com/s?__biz=MzA3NjA3NTI5Mg==&mid=2656329608&idx=1&sn=9b26e25828112101bd898a521920f998&chksm=84c627fbb3b1aeedc76d711df000a230f8122f920e7eaac8e4ac2beca0502219f2b53e45c6b4&mpshare=1&scene=1&srcid=12228I8QamiflHBgDhZNZawR&key=564c3e9811aee0ab6916fb9ca3f862a9d762bed23faa15e4e7e2fdd0171e8633f0873a18f6ab77c8c3e3af80b24fd5bfa47d68db2011c843b6b4c03a175a0847f483a1899bef9845e6a586fc543673a9&ascene=0&uin=MTYzMjY2MTE1&devicetype=iMac+MacBookPro10%2C1+OSX+OSX+10.12.2+build(16C67)&version=12010210&nettype=WIFI&fontScale=100&pass_ticket=J%2BySkFufHrkUQmSQ9ma5sHID2qlp0Gk1afWsVSyCjZU%3D)

![](https://github.com/wutongke/ViewPagerIndicator/blob/master/0.gif)

### Todo
目前bitmap内存复用还有些问题，待解决……
