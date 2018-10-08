package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.adapter.SuggestAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.SuggestBean;
import com.haokuo.happyclub.bean.list.SuggestListBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.base.PageParams;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.rey.material.widget.Button;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zjf on 2018/10/8.
 */
public class SuggestListActivity extends BaseActivity {

    @BindView(R.id.rv_my_suggest)
    RecyclerView mRvMySuggest;
    @BindView(R.id.srl_my_suggest)
    SmartRefreshLayout mSrlMySuggest;
    @BindView(R.id.btn_new_suggest)
    Button mBtnNewSuggest;
    private PageParams mParams;
    private SuggestAdapter mSuggestAdapter;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_suggest_list;
    }

    @Override
    protected void initData() {
        mRvMySuggest.setLayoutManager(new LinearLayoutManager(this));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvMySuggest.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDivider,
                0, 0));
        mSuggestAdapter = new SuggestAdapter(R.layout.item_my_suggest);
        mRvMySuggest.setAdapter(mSuggestAdapter);
        mParams = new PageParams();
    }

    @Override
    protected void loadData() {
        mSrlMySuggest.autoRefresh();
    }

    @Override
    protected void initListener() {
        mSuggestAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SuggestListActivity.this, SuggestDetailActivity.class);
                intent.putExtra(SuggestDetailActivity.EXTRA_BEAN, mSuggestAdapter.getItem(position));
                startActivity(intent);
            }
        });
        mSrlMySuggest.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<SuggestBean>
                (mSrlMySuggest, mParams, mSuggestAdapter, SuggestListBean.class, "获取投诉列表失败") {
            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getSuggestList(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getSuggestList(mParams, mRefreshCallback);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mSrlMySuggest.autoRefresh();
        }
    }

    @OnClick(R.id.btn_new_suggest)
    public void onViewClicked() {
        startActivityForResult(new Intent(this, ReportSuggestActivity.class), 0);
    }


}
