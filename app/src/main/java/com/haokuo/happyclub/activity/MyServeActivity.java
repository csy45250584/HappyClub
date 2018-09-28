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
        String[] titles = {"已接单", "服务中", "已服务", "已完成", "已放弃"};
        ArrayList<Fragment> fragments = new ArrayList<>();
                MyServeFragment conferenceFragment = new MyServeFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt(MyServeFragment.KEY_STATUS, GetAcceptedServeParams.STATUS_RECEIVED);
                conferenceFragment.setArguments(bundle1);
                fragments.add(conferenceFragment);
                MyServeFragment newsFragment = new MyServeFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putInt(MyServeFragment.KEY_STATUS, GetAcceptedServeParams.STATUS_SERVING);
                newsFragment.setArguments(bundle2);
                fragments.add(newsFragment);
                MyServeFragment noticeFragment = new MyServeFragment();
                Bundle bundle3 = new Bundle();
                bundle3.putInt(MyServeFragment.KEY_STATUS, GetAcceptedServeParams.STATUS_SERVED);
                noticeFragment.setArguments(bundle3);
                fragments.add(noticeFragment);
        mIndicatorMyServe.setViewPager(mVpMyServe, titles, this, fragments);
    }
}
