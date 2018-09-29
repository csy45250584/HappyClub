package com.haokuo.happyclub.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.fragment.MyServeFragment;
import com.haokuo.happyclub.network.bean.GetAcceptedServeParams;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by zjf on 2018/9/27.
 */
public class MyServeActivity extends BaseActivity {
    @BindView(R.id.indicator_my_serve)
    SlidingTabLayout mIndicatorMyServe;
    @BindView(R.id.vp_my_serve)
    ViewPager mVpMyServe;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_my_serve;
    }

    @Override
    protected void initData() {
        String[] titles = {"全部", "已接单", "服务中", "已服务", "已完成", "已放弃"};
        ArrayList<Fragment> fragments = new ArrayList<>();
        Integer[] statusList = {null, GetAcceptedServeParams.STATUS_RECEIVED, GetAcceptedServeParams.STATUS_SERVING, GetAcceptedServeParams.STATUS_SERVED,
                GetAcceptedServeParams.STATUS_COMPLETED, GetAcceptedServeParams.STATUS_ABANDONED};
        for (Integer status : statusList) {
            MyServeFragment myServeFragment = new MyServeFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(MyServeFragment.KEY_STATUS, status);
            myServeFragment.setArguments(bundle);
            fragments.add(myServeFragment);
        }
        mIndicatorMyServe.setViewPager(mVpMyServe, titles, this, fragments);
    }
}
