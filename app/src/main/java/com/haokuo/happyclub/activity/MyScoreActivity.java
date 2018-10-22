package com.haokuo.happyclub.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.adapter.MyWalletDetailAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.MyWalletBean;
import com.haokuo.happyclub.bean.MyWalletDetailBean;
import com.haokuo.happyclub.bean.list.MyWalletDetailListBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.base.PageParams;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/19.
 */
public class MyScoreActivity extends BaseActivity {
    @BindView(R.id.tv_my_score)
    TextView mTvMyScore;
    @BindView(R.id.rv_score_detail)
    RecyclerView mRvScoreDetail;
    @BindView(R.id.srl_score_detail)
    SmartRefreshLayout mSrlScoreDetail;
    private PageParams mParams;
    private MyWalletDetailAdapter mMyWalletDetailAdapter;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_my_score;
    }

    @Override
    protected void initData() {
        mRvScoreDetail.setLayoutManager(new LinearLayoutManager(this));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvScoreDetail.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDefBg,
                0, 0));
        mMyWalletDetailAdapter = new MyWalletDetailAdapter(R.layout.item_my_wallet_detail);
        mRvScoreDetail.setAdapter(mMyWalletDetailAdapter);
        mParams = new PageParams();
    }

    @Override
    protected void loadData() {
        HttpHelper.getInstance().getMyWallet(new EntityCallback<MyWalletBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("加载我的积分余额失败，" + message);
            }

            @Override
            public void onSuccess(Call call, MyWalletBean result) {
                mTvMyScore.setText(String.valueOf(result.getData()));
            }
        });
        mSrlScoreDetail.autoRefresh();
    }

    @Override
    protected void initListener() {
        mSrlScoreDetail.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<MyWalletDetailBean>
                (mSrlScoreDetail, mParams, mMyWalletDetailAdapter, MyWalletDetailListBean.class, "加载我的积分消费详情失败") {
            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getMyWalletDetail(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getMyWalletDetail(mParams, mRefreshCallback);
            }
        });
    }
}
