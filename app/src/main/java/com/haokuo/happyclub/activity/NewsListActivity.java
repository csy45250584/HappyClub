package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.ActivityAdapter;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.NewsBean;
import com.haokuo.happyclub.bean.list.NewsListBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.GetNewsListParams;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.haokuo.midtitlebar.MidTitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * Created by zjf on 2018/10/19.
 */
public class NewsListActivity extends BaseActivity {
    @BindView(R.id.rv_news)
    RecyclerView mRvNews;
    @BindView(R.id.srl_news)
    SmartRefreshLayout mSrlNews;
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    private GetNewsListParams mParams;
    private ActivityAdapter mActivityAdapter;
    private String mNewsBaseUrl;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_news_list;
    }

    @Override
    protected void initData() {
        mMidTitleBar.setMidTitle("新闻头条");
        mRvNews.setLayoutManager(new LinearLayoutManager(this));
        mRvNews.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL));
        mActivityAdapter = new ActivityAdapter(R.layout.item_activity);
        mRvNews.setAdapter(mActivityAdapter);
        mParams = new GetNewsListParams(null, GetNewsListParams.STATUS_NEWS, null);
    }

    @Override
    protected void loadData() {
        mSrlNews.autoRefresh();
    }

    @Override
    protected void initListener() {
        mActivityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsBean item = mActivityAdapter.getItem(position);
                if (item != null) {
                    String url = mNewsBaseUrl + "?id=" + item.getId();
                    Intent intent = new Intent(NewsListActivity.this, NewsDetailActivity.class);
                    intent.putExtra(NewsDetailActivity.EXTRA_NEWS_URL, url);
                    intent.putExtra(NewsDetailActivity.EXTRA_TITLE, "头条新闻");
                    startActivity(intent);
                }
            }
        });
        mSrlNews.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<NewsBean>
                (mSrlNews, mParams, mActivityAdapter, NewsListBean.class, "获取新闻列表失败") {
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
}
