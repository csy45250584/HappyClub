package com.haokuo.happyclub.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.activity.VActivityDetailActivity;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.adapter.VolunteerActivityAdapter;
import com.haokuo.happyclub.base.BaseLazyLoadFragment;
import com.haokuo.happyclub.bean.VolunteerActivityBean;
import com.haokuo.happyclub.bean.list.VolunteerActivityListBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.GetMyActivityParams;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * Created by zjf on 2018/9/28.
 */
public class MyActivityFragment extends BaseLazyLoadFragment {
    public static final String KEY_STATUS = "key_status";
    @BindView(R.id.rv_my_activity)
    RecyclerView mRvMyActivity;
    @BindView(R.id.srl_my_activity)
    SmartRefreshLayout mSrlMyActivity;
    private Integer mStatus;
    private GetMyActivityParams mParams;
    private VolunteerActivityAdapter mActivityAdapter;

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mStatus = (Integer) getArguments().getSerializable(KEY_STATUS);
        }
        mRvMyActivity.setLayoutManager(new LinearLayoutManager(mContext));
        mRvMyActivity.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL));
        mActivityAdapter = new VolunteerActivityAdapter(R.layout.item_volunteer_activity);
        mRvMyActivity.setAdapter(mActivityAdapter);
        mParams = new GetMyActivityParams(mStatus);
    }

    @Override
    protected void loadData() {
        mSrlMyActivity.autoRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            loadData();
        }
    }

    @Override
    protected void initListener() {
        mSrlMyActivity.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<VolunteerActivityBean>
                (mSrlMyActivity, mParams, mActivityAdapter, VolunteerActivityListBean.class, "获取我的活动失败") {
            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getMyActivityList(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getMyActivityList(mParams, mRefreshCallback);
            }
        });
        mActivityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VolunteerActivityBean item = mActivityAdapter.getItem(position);
                if (item != null) {
                    Intent intent = new Intent(mContext, VActivityDetailActivity.class);
                    intent.putExtra(VActivityDetailActivity.EXTRA_ACTIVITY_ID, item.getId());
                    if (mStatus != GetMyActivityParams.STATUS_JOIN) {
                        intent.putExtra(VActivityDetailActivity.EXTRA_DISMISS_BUTTON, true);
                    }
                    startActivityForResult(intent, 0);
                }
            }
        });
    }

    @Override
    protected int initContentLayout() {
        return R.layout.fragment_my_activity;
    }
}
