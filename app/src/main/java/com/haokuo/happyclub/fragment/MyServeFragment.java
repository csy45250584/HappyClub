package com.haokuo.happyclub.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseFragment;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * Created by zjf on 2018/9/28.
 */
public class MyServeFragment extends BaseFragment {
    public static final String KEY_STATUS = "key_status";
    @BindView(R.id.rv_my_serve)
    RecyclerView mRvMyServe;
    @BindView(R.id.srl_my_serve)
    SmartRefreshLayout mSrlMyServe;
    private int mStatus;

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mStatus = getArguments().getInt(KEY_STATUS);
        }
        mRvMyServe.setLayoutManager(new LinearLayoutManager(mContext));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvMyServe.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDivider,
                0, 0));
//        mRecourseAdapter = new MyServeAdapter(R.layout.item_my_recourse);
//        mRvMyServe.setAdapter(mRecourseAdapter);
//        mParams = new GetRecourseListParams(MySpUtil.getInstance().geTel());
    }

    @Override
    protected int initContentLayout() {
        return R.layout.fragment_my_serve;
    }



}
