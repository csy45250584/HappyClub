package com.haokuo.happyclub.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.ActivityAdapter;
import com.haokuo.happyclub.base.BaseLazyLoadFragment;
import com.haokuo.happyclub.bean.RecourseBean;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by zjf on 2018/9/11.
 */
public class ActivityFragment extends BaseLazyLoadFragment {
    @BindView(R.id.rv_activity)
    RecyclerView mRvActivity;
    @BindView(R.id.srl_activity)
    SmartRefreshLayout mSrlActivity;
    private ActivityAdapter mActivityAdapter;

    @Override
    protected void initData() {
        mRvActivity.setLayoutManager(new LinearLayoutManager(mContext));
        mRvActivity.addItemDecoration(new RecyclerViewDivider(mContext,LinearLayoutManager.HORIZONTAL));
        mActivityAdapter = new ActivityAdapter(R.layout.item_activity);
        mRvActivity.setAdapter(mActivityAdapter);
        ArrayList<RecourseBean> recourseBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            recourseBeans.add(new RecourseBean());
        }
        mActivityAdapter.setNewData(recourseBeans);
    }

    @Override
    protected void loadData() {
        mSrlActivity.autoRefresh();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected int initContentLayout() {
        return R.layout.fragment_activity;
    }
}
