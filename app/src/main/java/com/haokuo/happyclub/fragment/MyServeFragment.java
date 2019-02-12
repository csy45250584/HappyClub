package com.haokuo.happyclub.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.adapter.MyServeAdapter;
import com.haokuo.happyclub.base.BaseLazyLoadFragment;
import com.haokuo.happyclub.bean.RecourseBean;
import com.haokuo.happyclub.bean.list.RecourseListBean;
import com.haokuo.happyclub.eventbus.ServeChangeEvent;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.ChangeServeStatusParams;
import com.haokuo.happyclub.network.bean.GetAcceptedServeParams;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/28.
 */
public class MyServeFragment extends BaseLazyLoadFragment {
    public static final String KEY_STATUS = "key_status";
    @BindView(R.id.rv_my_serve)
    RecyclerView mRvMyServe;
    @BindView(R.id.srl_my_serve)
    SmartRefreshLayout mSrlMyServe;
    private Integer mStatus;
    private GetAcceptedServeParams mParams;
    private MyServeAdapter mMyServeAdapter;

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mStatus = (Integer) getArguments().getSerializable(KEY_STATUS);
        }
        mRvMyServe.setLayoutManager(new LinearLayoutManager(mContext));
        mRvMyServe.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL));
        mMyServeAdapter = new MyServeAdapter(R.layout.item_my_serve);
        mRvMyServe.setAdapter(mMyServeAdapter);
        mParams = new GetAcceptedServeParams(mStatus);
    }

    @Override
    protected boolean getRegisterEventBus() {
        return true;
    }

    @Subscribe
    public void onServeChangeEvent(ServeChangeEvent event) {
        if (!getUserVisibleHint() && (mStatus == GetAcceptedServeParams.STATUS_ALL || event.getPreStatus() == mStatus || event.getNextStatus() == mStatus)) {
            setLoadDataFinished(false);
        }
    }

    @Override
    protected void loadData() {
        mSrlMyServe.autoRefresh();
    }

    @Override
    protected void initListener() {
        mSrlMyServe.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<RecourseBean>
                (mSrlMyServe, mParams, mMyServeAdapter, RecourseListBean.class, "获取我的服务失败") {
            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getAcceptedServe(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getAcceptedServe(mParams, mRefreshCallback);
            }
        });
        mMyServeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final RecourseBean item = mMyServeAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.tv_delete_serve: {
                        mContext.showLoading("正在提交...");
                        final ChangeServeStatusParams params = new ChangeServeStatusParams(item.getId(), GetAcceptedServeParams.STATUS_ABANDONED);
                        HttpHelper.getInstance().changeServeStatus(params, new NetworkCallback() {
                            @Override
                            public void onSuccess(Call call, String json) {
                                mContext.loadSuccess("提交成功", false);
                                EventBus.getDefault().post(new ServeChangeEvent(item.getReviewState(), params.getStatus()));
                                mSrlMyServe.autoRefresh();
                            }

                            @Override
                            public void onFailure(Call call, String message) {
                                mContext.loadFailed("提交失败，" + message);
                            }
                        });
                    }
                    case R.id.tv_right_button: {
                        Integer reviewState = item.getReviewState();
                        if (reviewState != GetAcceptedServeParams.STATUS_RECEIVED && reviewState != GetAcceptedServeParams.STATUS_SERVING) {
                            return;
                        }
                        Integer status = -1;
                        if (reviewState == GetAcceptedServeParams.STATUS_RECEIVED) {
                            status = GetAcceptedServeParams.STATUS_SERVING;
                        } else if (reviewState == GetAcceptedServeParams.STATUS_SERVING) {
                            status = GetAcceptedServeParams.STATUS_SERVED;
                        }
                        mContext.showLoading("正在提交...");
                        final ChangeServeStatusParams params = new ChangeServeStatusParams(item.getId(), status);
                        HttpHelper.getInstance().changeServeStatus(params, new NetworkCallback() {
                            @Override
                            public void onSuccess(Call call, String json) {
                                mContext.loadSuccess("提交成功", false);
                                EventBus.getDefault().post(new ServeChangeEvent(item.getReviewState(), params.getStatus()));
                                mSrlMyServe.autoRefresh();
                            }

                            @Override
                            public void onFailure(Call call, String message) {
                                mContext.loadFailed("提交失败，" + message);
                            }
                        });
                    }
                    break;
                }
            }
        });
    }

    @Override
    protected int initContentLayout() {
        return R.layout.fragment_my_serve;
    }
}
