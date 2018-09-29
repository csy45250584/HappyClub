package com.haokuo.happyclub.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by zjf on 2018-08-14.
 */

public abstract class BaseLazyLoadFragment extends BaseFragment {
    /**
     * 是否已经初始化结束
     */
    private boolean isPrepare;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLazyLoad(true);
        isPrepare = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 创建时要判断是否已经显示给用户，加载数据
        onVisibleToUser();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 显示状态发生变化
        onVisibleToUser();
    }

    protected void resetLoadData() {
        loadDataFinished = false;
        onVisibleToUser();
    }

    protected void setLoadDataFinished(boolean loadDataFinished) {
        this.loadDataFinished = loadDataFinished;
    }

    /**
     * 判断是否需要加载数据
     */
    private void onVisibleToUser() {
        // 如果已经初始化完成，并且显示给用户
        if (isPrepare && getUserVisibleHint() && !loadDataFinished) {
            loadData();
            loadDataFinished = true;
        }
    }
}
