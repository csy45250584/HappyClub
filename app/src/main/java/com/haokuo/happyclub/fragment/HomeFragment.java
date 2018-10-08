package com.haokuo.happyclub.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.activity.CanteenActivity;
import com.haokuo.happyclub.activity.RepairListActivity;
import com.haokuo.happyclub.activity.SuggestListActivity;
import com.haokuo.happyclub.activity.VolunteerOrderActivity;
import com.haokuo.happyclub.adapter.ActionAdapter;
import com.haokuo.happyclub.base.BaseLazyLoadFragment;
import com.haokuo.happyclub.bean.ActionBean;
import com.haokuo.happyclub.bean.UserInfoBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.util.GlideImageLoader;
import com.haokuo.happyclub.util.MySpUtil;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/11.
 */
public class HomeFragment extends BaseLazyLoadFragment {
    @BindView(R.id.banner_home)
    Banner mBannerHome;
    @BindView(R.id.rv_action)
    RecyclerView mRvAction;
    @BindView(R.id.rv_volunteer_action)
    RecyclerView mRvVolunteerAction;
    private ActionAdapter mActionAdapter;
    private ActionAdapter mVolunteerActionAdapter;

    @Override
    protected int initContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        //banner设置
        mBannerHome.setImageLoader(new GlideImageLoader());
        mRvAction.setLayoutManager(new GridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false));
        mActionAdapter = new ActionAdapter(R.layout.item_action);
        mRvAction.setAdapter(mActionAdapter);
        mRvVolunteerAction.setLayoutManager(new GridLayoutManager(mContext, 5, LinearLayoutManager.VERTICAL, false));
        mVolunteerActionAdapter = new ActionAdapter(R.layout.item_volunteer_action);
        mRvVolunteerAction.setAdapter(mVolunteerActionAdapter);
        initActionAdapter();
        initVolunteerActionAdapter();
    }

    private void initVolunteerActionAdapter() {
        ArrayList<ActionBean> actionBeans = new ArrayList<>();
        actionBeans.add(new ActionBean("志愿者申请", R.drawable.zy1));
        actionBeans.add(new ActionBean("志愿工单", R.drawable.zy2, VolunteerOrderActivity.class));
        actionBeans.add(new ActionBean("我的服务", R.drawable.zy3));
        actionBeans.add(new ActionBean("服务评价", R.drawable.zy4));
        actionBeans.add(new ActionBean("我的积分", R.drawable.zy5));
        mVolunteerActionAdapter.setNewData(actionBeans);
    }

    private void initActionAdapter() {
        ArrayList<ActionBean> actionBeans = new ArrayList<>();
        actionBeans.add(new ActionBean("签到", R.drawable.q1));
        actionBeans.add(new ActionBean("会所服务", R.drawable.q2));
        actionBeans.add(new ActionBean("活动公开", R.drawable.q3));
        actionBeans.add(new ActionBean("幸福积分", R.drawable.q4));
        actionBeans.add(new ActionBean("幸福食堂", R.drawable.q5, CanteenActivity.class));
        actionBeans.add(new ActionBean("物业报修", R.drawable.q6, RepairListActivity.class));
        actionBeans.add(new ActionBean("物业投诉", R.drawable.q7,SuggestListActivity.class));
        actionBeans.add(new ActionBean("更多", R.drawable.q8));
        mActionAdapter.setNewData(actionBeans);
    }

    @Override
    protected void initListener() {
        mActionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActionBean item = mActionAdapter.getItem(position);
                if (item != null && item.getClz() != null) {
                    Intent intent = new Intent(mContext, item.getClz());
                    startActivity(intent);
                }
            }
        });
        mVolunteerActionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActionBean item = mVolunteerActionAdapter.getItem(position);
                if (item != null) {
                    if (item.getClz() != null) {
                        Intent intent = new Intent(mContext, item.getClz());
                        startActivity(intent);
                    } else {
                        switch (item.getTitle()) {
                            case "志愿者申请":
                                int volunteerStatus = MySpUtil.getInstance().getVolunteerStatus();
                                if (volunteerStatus == UserInfoBean.VOLUNTEER_STATUS_NONE || volunteerStatus == UserInfoBean.VOLUNTEER_STATUS_REJECTED) {
                                    showApply2BeVolunteer();
                                } else if (volunteerStatus == UserInfoBean.VOLUNTEER_STATUS_UNCHECKED) {
                                    ToastUtils.showShort("您的志愿者已等待审核,无须再次提交!");
                                }else if (volunteerStatus == UserInfoBean.VOLUNTEER_STATUS_AGREED) {
                                    ToastUtils.showShort("您已是志愿者,无须申请!");
                                }
                                break;
                        }
                    }
                }
            }
        });
    }

    private void showApply2BeVolunteer() {
        new AlertDialog.Builder(mContext)
                .setTitle("申请成为志愿者")
                .setMessage("成为志愿者后能够提供服务以赚取积分,是否成为志愿者?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mContext.showLoading("正在提交...");
                        HttpHelper.getInstance().apply2beVolunteer(new NetworkCallback() {
                            @Override
                            public void onSuccess(Call call, String json) {
                                mContext.loadSuccess("申请成功,等待审核",false);
                            }

                            @Override
                            public void onFailure(Call call, String message) {
                                mContext.loadFailed("申请失败," + message);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }
}
