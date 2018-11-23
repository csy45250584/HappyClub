package com.haokuo.happyclub.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.fragment.MyActivityFragment;
import com.haokuo.happyclub.network.bean.GetMyActivityParams;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by zjf on 2018/11/22.
 */
public class MyActivityActivity extends BaseActivity {

    @BindView(R.id.indicator_my_activity)
    SlidingTabLayout mIndicatorMyActivity;
    @BindView(R.id.vp_my_activity)
    ViewPager mVpMyActivity;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_my_activity;
    }

    @Override
    protected void initData() {
        String[] titles = {"全部", "已预约", "已签到", "已完成"};
        ArrayList<Fragment> fragments = new ArrayList<>();
        Integer[] statusList = {null, GetMyActivityParams.STATUS_JOIN, GetMyActivityParams.STATUS_SIGN_IN, GetMyActivityParams.STATUS_SIGN_OUT};
        for (Integer status : statusList) {
            MyActivityFragment myActivityFragment = new MyActivityFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(MyActivityFragment.KEY_STATUS, status);
            myActivityFragment.setArguments(bundle);
            fragments.add(myActivityFragment);
        }
        mIndicatorMyActivity.setViewPager(mVpMyActivity, titles, this, fragments);
    }
}
