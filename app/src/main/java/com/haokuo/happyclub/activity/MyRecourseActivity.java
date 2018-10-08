package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.adapter.RecourseAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.RecourseBean;
import com.haokuo.happyclub.bean.list.RecourseListBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.ChangeServeStatusParams;
import com.haokuo.happyclub.network.bean.GetRecourseListParams;
import com.haokuo.happyclub.network.bean.base.IdParams;
import com.haokuo.happyclub.util.MySpUtil;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.rey.material.widget.Button;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/14.
 */
public class MyRecourseActivity extends BaseActivity {
    @BindView(R.id.rv_my_recourse)
    RecyclerView mRvMyRecourse;
    @BindView(R.id.srl_my_recourse)
    SmartRefreshLayout mSrlMyRecourse;
    @BindView(R.id.btn_new_recourse)
    Button mBtnNewRecourse;
    private RecourseAdapter mRecourseAdapter;
    private GetRecourseListParams mParams;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_my_recourse;
    }

    @Override
    protected void loadData() {
        mSrlMyRecourse.autoRefresh();
    }

    @Override
    protected void initData() {
        mRvMyRecourse.setLayoutManager(new LinearLayoutManager(this));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvMyRecourse.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDefBg,
                0, 0));
        mRecourseAdapter = new RecourseAdapter(R.layout.item_my_recourse);
        mRvMyRecourse.setAdapter(mRecourseAdapter);
        mParams = new GetRecourseListParams(MySpUtil.getInstance().geTel());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mSrlMyRecourse.autoRefresh();
        }
    }

    @Override
    protected void initListener() {
        mRecourseAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                RecourseBean item = mRecourseAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.tv_delete_recourse: {
                        IdParams params = new IdParams(item.getId());
                        showLoading("删除中...");
                        HttpHelper.getInstance().deleteRecourse(params, new NetworkCallback() {
                            @Override
                            public void onSuccess(Call call, String json) {
                                loadSuccess("删除成功", false);
                                mRecourseAdapter.remove(position);
                            }

                            @Override
                            public void onFailure(Call call, String message) {
                                loadFailed("删除失败，" + message);
                            }
                        });
                    }
                    break;
                    case R.id.tv_right_button: {
                        Integer reviewState = item.getReviewState();
                        if (reviewState == 2 || reviewState == 88) {//重新发布
                            Intent intent = new Intent(MyRecourseActivity.this, NewRecourseActivity.class);
                            intent.putExtra(NewRecourseActivity.EXTRA_RECOURSE_BEAN, item);
                            startActivityForResult(intent, 0);
                        } else if (reviewState == 33) { //完成
                            showLoading("提交中...");
                            ChangeServeStatusParams params = new ChangeServeStatusParams(item.getId(), 33);
                            HttpHelper.getInstance().changeServeStatus(params, new NetworkCallback() {
                                @Override
                                public void onSuccess(Call call, String json) {
                                    loadSuccess("提交成功", false);
                                }

                                @Override
                                public void onFailure(Call call, String message) {
                                    loadFailed("提交完成，" + message);
                                }
                            });
                        } else if (reviewState == 44) { //评价
                            //                            showLoading("提交中...");
                            //                            EvaluationBean evaluationBean = new EvaluationBean();
                            //                            HttpHelper.getInstance().evaluateRecourse(evaluationBean, new NetworkCallback() {
                            //                                @Override
                            //                                public void onSuccess(Call call, String json) {
                            //                                    loadSuccess("提交成功",false);
                            //                                }
                            //
                            //                                @Override
                            //                                public void onFailure(Call call, String message) {
                            //                                    loadFailed("提交完成，" + message);
                            //                                }
                            //                            });
                        }
                    }
                    break;
                }
            }
        });
        mSrlMyRecourse.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<RecourseBean>
                (mSrlMyRecourse, mParams, mRecourseAdapter, RecourseListBean.class, "获取我的求助失败") {
            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getRecourseList(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getRecourseList(mParams, mRefreshCallback);
            }
        });
    }

    @OnClick(R.id.btn_new_recourse)
    public void onViewClicked() {
        //新建求助
        startActivityForResult(new Intent(MyRecourseActivity.this, NewRecourseActivity.class), 0);
    }
}
