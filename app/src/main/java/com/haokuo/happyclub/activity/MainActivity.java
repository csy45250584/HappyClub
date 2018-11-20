package com.haokuo.happyclub.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.MainFragmentPagerAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.eventbus.LogoutEvent;
import com.haokuo.happyclub.fragment.ActivityFragment;
import com.haokuo.happyclub.fragment.HomeFragment;
import com.haokuo.happyclub.fragment.MeFragment;
import com.haokuo.happyclub.fragment.OrderFragment;
import com.haokuo.midtitlebar.MidTitleBar;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    private static final int DEFAULT_TAB_POSITION = 0;
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    private OrderFragment mOrderFragment;
    private HomeFragment mHomeFragment;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        initBottomNaviBar();
        ArrayList<Fragment> fragments = new ArrayList<>();
        //        fragments.add(new IMFragment());
        mHomeFragment = new HomeFragment();
        fragments.add(mHomeFragment);
        fragments.add(new ActivityFragment());
//        fragments.add(new NearbyFragment());
        mOrderFragment = new OrderFragment();
        fragments.add(mOrderFragment);
        fragments.add(new MeFragment());
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragments.size()); //设置缓存fragment数量，防止fragment生命周期多次调用
        mViewPager.setCurrentItem(DEFAULT_TAB_POSITION); //设置初始化的位置
    }

    @Override
    protected boolean getRegisterEventBus() {
        return true;
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
//                .addItem(new BottomNavigationItem(R.drawable.s3a, "附近"))
                .addItem(new BottomNavigationItem(R.drawable.s4a, "订单"))
                .addItem(new BottomNavigationItem(R.drawable.s5a, "我的"))
                .setFirstSelectedPosition(DEFAULT_TAB_POSITION)//设置默认选择的按钮
                .initialise();//所有的设置需在调用该方法前完成
    }

    @Subscribe
    public void onLogoutEvent(LogoutEvent event) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            case R.id.item_edit_info:
//
//                break;
//        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mOrderFragment.getUserVisibleHint() && mOrderFragment.isMenuShowing()) {
            mOrderFragment.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void initListener() {
        mMidTitleBar.setOnMenuItemClickListener(this);
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
                        mMidTitleBar.getMenu().clear();
                        break;
//                    case 1:
//                        mMidTitleBar.setMidTitle("活动");
//                        mMidTitleBar.getMenu().clear();
//                        break;
                    case 1:
                        mMidTitleBar.setMidTitle("活动");
                        mMidTitleBar.getMenu().clear();
                        break;
                    case 2:
                        mMidTitleBar.setMidTitle("订单");
                        mMidTitleBar.getMenu().clear();
                        break;
                    case 3:
                        mMidTitleBar.setMidTitle("我的");
                        mMidTitleBar.getMenu().clear();
//                        mMidTitleBar.getMenu().add(0, R.id.item_edit_info, 0, null).setIcon(R.drawable.xx1).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
    @Override
    public void onStart() {
        super.onStart();
        if (mHomeFragment!=null&&mHomeFragment.getMarqueeViewNews()!=null) {
            mHomeFragment.getMarqueeViewNews().startFlipping();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mHomeFragment!=null&&mHomeFragment.getMarqueeViewNews()!=null) {
            mHomeFragment.getMarqueeViewNews().stopFlipping();
        }
    }
}
