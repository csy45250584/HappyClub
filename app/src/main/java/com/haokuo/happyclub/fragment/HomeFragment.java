package com.haokuo.happyclub.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.activity.CanteenActivity;
import com.haokuo.happyclub.activity.VolunteerOrderActivity;
import com.haokuo.happyclub.adapter.ActionAdapter;
import com.haokuo.happyclub.base.BaseLazyLoadFragment;
import com.haokuo.happyclub.bean.ActionBean;
import com.haokuo.happyclub.util.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by zjf on 2018/9/11.
 */
public class HomeFragment extends BaseLazyLoadFragment {
    @BindView(R.id.banner_home)
    Banner mBannerHome;
    @BindView(R.id.rv_action)
    RecyclerView mRvAction;
    @BindView(R.id.rv_volunteer_action)
    RecyclerView mRvVolunteerAction;
    private ActionAdapter mActionAdapter;
    private ActionAdapter mVolunteerActionAdapter;

    @Override
    protected int initContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        //banner设置
        mBannerHome.setImageLoader(new GlideImageLoader());
        mRvAction.setLayoutManager(new GridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false));
        mActionAdapter = new ActionAdapter(R.layout.item_action);
        mRvAction.setAdapter(mActionAdapter);
        mRvVolunteerAction.setLayoutManager(new GridLayoutManager(mContext, 5, LinearLayoutManager.VERTICAL, false));
        mVolunteerActionAdapter = new ActionAdapter(R.layout.item_volunteer_action);
        mRvVolunteerAction.setAdapter(mVolunteerActionAdapter);
        initActionAdapter();
        initVolunteerActionAdapter();
    }

    private void initVolunteerActionAdapter() {
        ArrayList<ActionBean> actionBeans = new ArrayList<>();
        actionBeans.add(new ActionBean("党员录入", R.drawable.zy1));
        actionBeans.add(new ActionBean("志愿工单", R.drawable.zy2, VolunteerOrderActivity.class));
        actionBeans.add(new ActionBean("我的服务", R.drawable.zy3));
        actionBeans.add(new ActionBean("服务评价", R.drawable.zy4));
        actionBeans.add(new ActionBean("我的积分", R.drawable.zy5));
        mVolunteerActionAdapter.setNewData(actionBeans);
    }

    private void initActionAdapter() {
        ArrayList<ActionBean> actionBeans = new ArrayList<>();
        actionBeans.add(new ActionBean("签到", R.drawable.q1));
        actionBeans.add(new ActionBean("会所服务", R.drawable.q2));
        actionBeans.add(new ActionBean("活动公开", R.drawable.q3));
        actionBeans.add(new ActionBean("幸福积分", R.drawable.q4));
        actionBeans.add(new ActionBean("幸福食堂", R.drawable.q5, CanteenActivity.class));
        actionBeans.add(new ActionBean("幸福养老", R.drawable.q6));
        actionBeans.add(new ActionBean("幸福教育", R.drawable.q7));
        actionBeans.add(new ActionBean("更多", R.drawable.q8));
        mActionAdapter.setNewData(actionBeans);
    }

    @Override
    protected void initListener() {
        mActionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActionBean item = mActionAdapter.getItem(position);
                if (item != null && item.getClz() != null) {
                    Intent intent = new Intent(mContext, item.getClz());
                    startActivity(intent);
                }
            }
        });
        mVolunteerActionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActionBean item = mVolunteerActionAdapter.getItem(position);
                if (item != null && item.getClz() != null) {
                    Intent intent = new Intent(mContext, item.getClz());
                    startActivity(intent);
                }
            }
        });
    }
}
