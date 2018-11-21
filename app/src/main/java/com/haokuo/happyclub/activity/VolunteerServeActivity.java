package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.ActionAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.ActionBean;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by zjf on 2018/10/23.
 */
public class VolunteerServeActivity extends BaseActivity {
    @BindView(R.id.rv_action)
    RecyclerView mRvAction;
    private ActionAdapter mActionAdapter;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_volunteer_serve;
    }

    @Override
    protected void initData() {
        mRvAction.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        mActionAdapter = new ActionAdapter(R.layout.item_action);
        mRvAction.setAdapter(mActionAdapter);
        initActionAdapter();
    }

    private void initActionAdapter() {
        ArrayList<ActionBean> actionBeans = new ArrayList<>();
        actionBeans.add(new ActionBean("志愿者工单", R.drawable.q4, VolunteerOrderActivity.class));
        actionBeans.add(new ActionBean("志愿者活动", R.drawable.tbb4, VolunteerActivityActivity.class));
        mActionAdapter.setNewData(actionBeans);
    }

    @Override
    protected void initListener() {
        mActionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActionBean item = mActionAdapter.getItem(position);
                if (item != null && item.getClz() != null) {
                    Intent intent = new Intent(VolunteerServeActivity.this, item.getClz());
                    startActivity(intent);
                }
            }
        });
    }
}
