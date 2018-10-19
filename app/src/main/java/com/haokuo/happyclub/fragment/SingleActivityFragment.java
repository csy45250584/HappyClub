package com.haokuo.happyclub.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.activity.NewsDetailActivity;
import com.haokuo.happyclub.adapter.ActivityAdapter;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.base.BaseFragment;
import com.haokuo.happyclub.bean.NewsBean;
import com.haokuo.happyclub.bean.list.NewsListBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.GetNewsListParams;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * Created by zjf on 2018/10/18.
 */
public class SingleActivityFragment extends BaseFragment {
    public static final String KEY_SORT_ID = "key_sort_id";
    @BindView(R.id.rv_activity)
    RecyclerView mRvActivity;
    @BindView(R.id.srl_activity)
    SmartRefreshLayout mSrlActivity;
    private long mSortId;
    private ActivityAdapter mActivityAdapter;
    private GetNewsListParams mParams;
    private String mNewsBaseUrl;

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mSortId = getArguments().getLong(KEY_SORT_ID);
        }
        mRvActivity.setLayoutManager(new LinearLayoutManager(mContext));
        mRvActivity.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL));
        mActivityAdapter = new ActivityAdapter(R.layout.item_activity);
        mRvActivity.setAdapter(mActivityAdapter);
        mParams = new GetNewsListParams(null, GetNewsListParams.STATUS_ACTIVITY, mSortId);
    }

    @Override
    protected void loadData() {
        mSrlActivity.autoRefresh();
    }

    @Override
    protected void initListener() {
        mActivityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsBean item = mActivityAdapter.getItem(position);
                if (item != null) {
                    String url = mNewsBaseUrl + "?id=" + item.getId();
                    Intent intent = new Intent(mContext, NewsDetailActivity.class);
                    intent.putExtra(NewsDetailActivity.EXTRA_NEWS_URL, url);
                    startActivity(intent);
                }
            }
        });
        mSrlActivity.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<NewsBean>
                (mSrlActivity, mParams, mActivityAdapter, NewsListBean.class, "获取活动列表失败") {
            @Override
            public void onSuccessResult(String json) {
                if (mNewsBaseUrl == null) {
                    NewsListBean newsListBean = JSON.parseObject(json, NewsListBean.class);
                    mNewsBaseUrl = newsListBean.getUrl();
                }
            }

            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getNewsList(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getNewsList(mParams, mRefreshCallback);
            }
        });
    }

    @Override
    protected int initContentLayout() {
        return R.layout.fragment_single_activity;
    }
}
