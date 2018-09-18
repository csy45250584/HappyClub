package com.haokuo.happyclub.adapter;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.bean.list.ListResultBean;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.base.PageParams;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import okhttp3.Call;

/**
 * Created by zjf on 2018/9/14.
 */
public abstract class MyRefreshLoadMoreListener<T> implements OnRefreshLoadMoreListener {
    private PageParams mParams;
    private BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    protected NetworkCallback mLoadMoreCallback;
    protected NetworkCallback mRefreshCallback;

    public MyRefreshLoadMoreListener(final SmartRefreshLayout refreshLayout, PageParams params, BaseQuickAdapter<T, BaseViewHolder> adapter, final Class<? extends ListResultBean<T>> clz, final String failedText) {
        mParams = params;
        mAdapter = adapter;
        mLoadMoreCallback = new NetworkCallback() {
            @Override
            public void onSuccess(Call call, String json) {
                ListResultBean<T> result = JSON.parseObject(json, clz);
                List<T> data = result.getData();
                mAdapter.addData(data);
                if (mAdapter.getData().size() == result.getCount()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort(failedText + "，" + message);
                refreshLayout.finishLoadMore(false);
            }
        };
        mRefreshCallback = new NetworkCallback() {
            @Override
            public void onSuccess(Call call, String json) {
                ListResultBean<T> result = JSON.parseObject(json, clz);
                List<T> data = result.getData();
                mAdapter.setNewData(data);
                refreshLayout.finishRefresh();
                refreshLayout.setNoMoreData(mAdapter.getData().size() == result.getCount());
            }

            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort(failedText + "，" + message);
                refreshLayout.finishRefresh(false);
            }
        };
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mParams.increasePageIndex();
        loadMore();
    }

    protected abstract void loadMore();

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mParams.resetPageIndex();
        refresh();
    }

    protected abstract void refresh();
}
