package com.haokuo.happyclub.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.MainFragmentPagerAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.fragment.HomeFragment;
import com.haokuo.happyclub.fragment.MeFragment;
import com.haokuo.midtitlebar.MidTitleBar;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    private static final int DEFAULT_TAB_POSITION = 0;
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        initBottomNaviBar();
        ArrayList<Fragment> fragments = new ArrayList<>();
        //        fragments.add(new IMFragment());
        fragments.add(new HomeFragment());
        fragments.add(new MeFragment());
        fragments.add(new MeFragment());
        fragments.add(new MeFragment());
        fragments.add(new MeFragment());
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragments.size()); //设置缓存fragment数量，防止fragment生命周期多次调用
        mViewPager.setCurrentItem(DEFAULT_TAB_POSITION); //设置初始化的位置
    }

    private void initBottomNaviBar() {
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar //值得一提，模式跟背景的设置都要在添加tab前面，不然不会有效果。
                .setActiveColor(R.color.colorPrimary)//选中颜色 图标和文字
                .setInActiveColor(R.color.colorText3)//默认未选择颜色
                .setBarBackgroundColor(R.color.colorWhite);//默认背景色
        mBottomNavigationBar
                //                .addItem(new BottomNavigationItem(R.drawable.news, "消息"))
                .addItem(new BottomNavigationItem(R.drawable.s1a, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.s2a, "活动"))
                .addItem(new BottomNavigationItem(R.drawable.s3a, "附近"))
                .addItem(new BottomNavigationItem(R.drawable.s4a, "订单"))
                .addItem(new BottomNavigationItem(R.drawable.s5a, "我的"))
                .setFirstSelectedPosition(DEFAULT_TAB_POSITION)//设置默认选择的按钮
                .initialise();//所有的设置需在调用该方法前完成
    }

    @Override
    protected void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationBar.selectTab(position);
                switch (position) {
                    case 0:
                        mMidTitleBar.setMidTitle("首页");
                        break;
                    case 1:
                        mMidTitleBar.setMidTitle("活动");
                        break;
                    case 2:
                        mMidTitleBar.setMidTitle("附近");
                        break;
                    case 3:
                        mMidTitleBar.setMidTitle("订单");
                        break;
                    case 4:
                        mMidTitleBar.setMidTitle("我的");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
    }
}
