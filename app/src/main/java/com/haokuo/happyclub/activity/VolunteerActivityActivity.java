package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.adapter.VolunteerActivityAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.VolunteerActivityBean;
import com.haokuo.happyclub.bean.list.VolunteerActivityListBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.GetVolunteerActivityParams;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * Created by zjf on 2018/11/20.
 */
public class VolunteerActivityActivity extends BaseActivity {
    @BindView(R.id.rv_volunteer_activity)
    RecyclerView mRvVolunteerActivity;
    @BindView(R.id.srl_volunteer_activity)
    SmartRefreshLayout mSrlVolunteerActivity;
    private VolunteerActivityAdapter mVolunteerActivityAdapter;
    private GetVolunteerActivityParams mParams;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_volunteer_activity;
    }

    @Override
    protected void initData() {
        //设置RecyclerView
        mRvVolunteerActivity.setLayoutManager(new LinearLayoutManager(this));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvVolunteerActivity.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDivider));
        mVolunteerActivityAdapter = new VolunteerActivityAdapter(R.layout.item_volunteer_activity);
        mRvVolunteerActivity.setAdapter(mVolunteerActivityAdapter);
        mParams = new GetVolunteerActivityParams(null);
    }

    @Override
    protected void loadData() {
        mSrlVolunteerActivity.autoRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadData();
        }
    }

    @Override
    protected void initListener() {
        mVolunteerActivityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VolunteerActivityBean item = mVolunteerActivityAdapter.getItem(position);
                if (item != null) {
                    Intent intent = new Intent(VolunteerActivityActivity.this, VActivityDetailActivity.class);
                    intent.putExtra(VActivityDetailActivity.EXTRA_ACTIVITY_ID, item.getId());
                    startActivityForResult(intent, 0);
                }
            }
        });
        mSrlVolunteerActivity.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<VolunteerActivityBean>
                (mSrlVolunteerActivity, mParams, mVolunteerActivityAdapter, VolunteerActivityListBean.class, "获取志愿者活动列表失败") {

            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getVolunteerActivityList(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getVolunteerActivityList(mParams, mRefreshCallback);
            }
        });
    }
}
