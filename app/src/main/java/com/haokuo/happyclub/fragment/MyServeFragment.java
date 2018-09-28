package com.haokuo.happyclub.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zjf on 2018/9/28.
 */
public class MyServeFragment extends BaseFragment {
    public static final String KEY_STATUS = "key_status";
    @BindView(R.id.rv_my_serve)
    RecyclerView mRvMyServe;
    @BindView(R.id.srl_my_serve)
    SmartRefreshLayout mSrlMyServe;
    Unbinder unbinder;

    @Override
    protected void initData() {

    }

    @Override
    protected int initContentLayout() {
        return R.layout.fragment_my_serve;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
