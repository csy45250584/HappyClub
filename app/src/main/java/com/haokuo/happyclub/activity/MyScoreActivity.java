package com.haokuo.happyclub.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.adapter.MyWalletDetailAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.MyWalletBean;
import com.haokuo.happyclub.bean.MyWalletDetailBean;
import com.haokuo.happyclub.bean.TransferPointsBean;
import com.haokuo.happyclub.bean.UserInfoBean;
import com.haokuo.happyclub.bean.list.MyWalletDetailListBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.GetPointsTransferFlagParams;
import com.haokuo.happyclub.network.bean.base.PageParams;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.haokuo.midtitlebar.MidTitleBar;
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
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    private PageParams mParams;
    private MyWalletDetailAdapter mMyWalletDetailAdapter;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_my_score;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_score, menu);
        return true;
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
        mMidTitleBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.item_transfer) { //转账
                    showTransferPointsDialog();
                }
                return true;
            }
        });
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

    private void showTransferPointsDialog() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_edittext, null);
        final EditText editText = inflate.findViewById(R.id.edittext);
        editText.setHint("请输入转账账号");
        new AlertDialog.Builder(this)
                .setTitle("积分转账")
                .setView(inflate)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tel = editText.getEditableText().toString();
                        GetPointsTransferFlagParams params = new GetPointsTransferFlagParams(tel);
                        HttpHelper.getInstance().getPointsTransferFlag(params, new EntityCallback<TransferPointsBean>() {
                            @Override
                            public void onFailure(Call call, String message) {
                                ToastUtils.showShort("获取对方账号信息失败，" + message);
                            }

                            @Override
                            public void onSuccess(Call call, TransferPointsBean result) {
                                UserInfoBean user = result.getUser();
                                Intent intent = new Intent(MyScoreActivity.this, TransferPointsActivity.class);
                                intent.putExtra(TransferPointsActivity.EXTRA_USER, user);
                                startActivity(intent);
                            }
                        });
                    }
                })

                .create()
                .show();
    }
}
