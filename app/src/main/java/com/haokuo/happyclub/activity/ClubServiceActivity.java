package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.ClubServiceModelAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.NewsTypeBean;
import com.haokuo.happyclub.bean.list.NewsTypeListBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/11/23.
 */
public class ClubServiceActivity extends BaseActivity {
    @BindView(R.id.rv_club_service)
    RecyclerView mRvClubService;
    @BindView(R.id.srl_club_service)
    SmartRefreshLayout mSrlClubService;
    private ClubServiceModelAdapter mClubServiceModelAdapter;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_club_service;
    }

    @Override
    protected void initData() {
        mRvClubService.setLayoutManager(new LinearLayoutManager(this));
        mRvClubService.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL));
        mClubServiceModelAdapter = new ClubServiceModelAdapter(R.layout.item_club_service_provider);
        mRvClubService.setAdapter(mClubServiceModelAdapter);
    }

    @Override
    protected void loadData() {
        mSrlClubService.autoRefresh();
    }

    @Override
    protected void initListener() {
        mClubServiceModelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsTypeBean item = mClubServiceModelAdapter.getItem(position);
                if (item != null) {
                    Intent intent = new Intent(ClubServiceActivity.this, ClubServiceProviderActivity.class);
                    intent.putExtra(ClubServiceProviderActivity.EXTRA_MODEL_ID, item.getId());
                    intent.putExtra(ClubServiceProviderActivity.EXTRA_NAME, item.getSortName());
                    startActivity(intent);
                }
            }
        });
        mSrlClubService.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                //加载标题
                HttpHelper.getInstance().getNewsSortList(new EntityCallback<NewsTypeListBean>() {
                    @Override
                    public void onFailure(Call call, String message) {
                        ToastUtils.showShort("加载内容失败");
                    }

                    @Override
                    public void onSuccess(Call call, NewsTypeListBean result) {
                        mClubServiceModelAdapter.setNewData(result.getData());
                        refreshLayout.finishRefresh();
                        refreshLayout.setNoMoreData(true);
                    }
                });
            }
        });
    }
}
