package com.haokuo.happyclub.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haokuo.happyclub.util.SafeHandler;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zjf on 2018-07-09.
 */

public abstract class BaseFragment extends Fragment {

    /**
     * 是否启用懒加载，此属性仅对BaseLazyLoadFragment有效
     */
    private boolean isLazyLoad;
    protected boolean loadDataFinished;
    protected Unbinder unbinder;
    protected BaseActivity mContext;
    private Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            BaseFragment.this.handleMessage(msg);
            return true;
        }
    };

    /**
     * handler的信息处理，如果使用handler必须重写该方法。
     *
     * @param msg
     */
    protected void handleMessage(Message msg) {
        throw new RuntimeException("It is required to override handleMessage(Message msg) before using handler!");
    }

    /**
     * 防止内存泄露的handler，使用必须重写handleMessage方法。
     *
     * @param msg
     */
    protected SafeHandler mHandler = new SafeHandler<Fragment>(this, mCallback);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(initContentLayout(), null);
        unbinder = ButterKnife.bind(this, rootView);
        loadDataFinished = false;
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mContext = (BaseActivity) getActivity();
        initData();
        initListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isLazyLoad) {
            loadData();
        }
    }

    protected abstract int initContentLayout();

    protected abstract void initData();

    protected abstract void initListener();

    /**
     * 是否启用懒加载，此方法仅对BaseLazyLoadFragment有效
     */
    public void setLazyLoad(boolean lazyLoad) {
        isLazyLoad = lazyLoad;
    }

    /**
     * 懒加载数据
     */
    protected void loadData() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
