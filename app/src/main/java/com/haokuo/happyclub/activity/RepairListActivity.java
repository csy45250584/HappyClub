package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.adapter.RepairAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.RepairBean;
import com.haokuo.happyclub.bean.list.RepairListBean;
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
public class RepairListActivity extends BaseActivity {
    @BindView(R.id.rv_my_repair)
    RecyclerView mRvMyRepair;
    @BindView(R.id.srl_my_repair)
    SmartRefreshLayout mSrlMyRepair;
    @BindView(R.id.btn_new_repair)
    Button mBtnNewRepair;
    private RepairAdapter mRepairAdapter;
    private PageParams mParams;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_repair_list;
    }

    @Override
    protected void initData() {
        mRvMyRepair.setLayoutManager(new LinearLayoutManager(this));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvMyRepair.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDivider,
                0, 0));
        mRepairAdapter = new RepairAdapter(R.layout.item_my_repair);
        mRvMyRepair.setAdapter(mRepairAdapter);
        mParams = new PageParams();
    }

    @Override
    protected void loadData() {
        mSrlMyRepair.autoRefresh();
    }

    @Override
    protected void initListener() {
        mRepairAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(RepairListActivity.this, RepairDetailActivity.class);
                intent.putExtra(RepairDetailActivity.EXTRA_BEAN, mRepairAdapter.getItem(position));
                startActivity(intent);
            }
        });
        mSrlMyRepair.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<RepairBean>
                (mSrlMyRepair, mParams, mRepairAdapter, RepairListBean.class, "获取报修列表失败") {
            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getRepairList(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getRepairList(mParams, mRefreshCallback);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mSrlMyRepair.autoRefresh();
        }
    }

    @OnClick(R.id.btn_new_repair)
    public void onViewClicked() {
        startActivityForResult(new Intent(this, ReportRepairActivity.class), 0);
    }
}
