package com.haokuo.happyclub.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.activity.ClubServiceProviderDetailActivity;
import com.haokuo.happyclub.adapter.ClubServiceProviderAdapter;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.base.BaseLazyLoadFragment;
import com.haokuo.happyclub.bean.ClubServiceProviderBean;
import com.haokuo.happyclub.bean.list.ClubServiceProviderListBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.GetServiceProviderListParams;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * Created by zjf on 2018/11/13.
 */
public class ClubServiceProviderFragment extends BaseLazyLoadFragment {
    public static final String KEY_SORT_ID = "key_sort_id";
    @BindView(R.id.rv_club_service)
    RecyclerView mRvClubService;
    @BindView(R.id.srl_club_service)
    SmartRefreshLayout mSrlClubService;
    private long mSortId;
    private ClubServiceProviderAdapter mClubServiceProviderAdapter;
    private GetServiceProviderListParams mParams;

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mSortId = getArguments().getLong(KEY_SORT_ID);
        }
        mRvClubService.setLayoutManager(new LinearLayoutManager(mContext));
        mRvClubService.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL));
        mClubServiceProviderAdapter = new ClubServiceProviderAdapter(R.layout.item_club_service_provider);
        mRvClubService.setAdapter(mClubServiceProviderAdapter);
        mParams = new GetServiceProviderListParams(mSortId);
    }
    @Override
    protected void loadData() {
        mSrlClubService.autoRefresh();
    }
    @Override
    protected void initListener() {
        mClubServiceProviderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ClubServiceProviderBean item = mClubServiceProviderAdapter.getItem(position);
                if (item != null) {
                    Intent intent = new Intent(mContext, ClubServiceProviderDetailActivity.class);
                    intent.putExtra(ClubServiceProviderDetailActivity.EXTRA_SERVICE_ID, item.getId());
                    startActivity(intent);
                }
            }
        });
        mSrlClubService.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<ClubServiceProviderBean>
                (mSrlClubService, mParams, mClubServiceProviderAdapter, ClubServiceProviderListBean.class, "获取服务商列表失败") {

            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getServiceProviderList(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getServiceProviderList(mParams, mRefreshCallback);
            }
        });
    }
    @Override
    protected int initContentLayout() {
        return R.layout.fragment_club_service_provider;
    }
}
