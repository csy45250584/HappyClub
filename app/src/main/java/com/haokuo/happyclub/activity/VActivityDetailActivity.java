package com.haokuo.happyclub.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.VolunteerActivityBean;
import com.haokuo.happyclub.bean.VolunteerActivityDetailBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.UrlConfig;
import com.haokuo.happyclub.network.bean.ActivityIdParams;
import com.haokuo.happyclub.util.ResUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.rey.material.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/11/21.
 */
public class VActivityDetailActivity extends BaseActivity {
    public static final String EXTRA_ACTIVITY_ID = "com.haokuo.happyclub.extra.EXTRA_ACTIVITY_ID";
    public static final String EXTRA_DISMISS_BUTTON = "com.haokuo.happyclub.extra.EXTRA_DISMISS_BUTTON";
    @BindView(R.id.iv_activity_pic)
    ImageView mIvActivityPic;
    @BindView(R.id.tv_activity_name)
    TextView mTvActivityName;
    @BindView(R.id.tv_activity_time)
    TextView mTvActivityTime;
    @BindView(R.id.tv_activity_count)
    TextView mTvActivityCount;
    @BindView(R.id.tv_activity_description)
    TextView mTvActivityDescription;
    @BindView(R.id.tv_activity_address)
    TextView mTvActivityAddress;
    @BindView(R.id.btn_sign_in)
    Button mBtnSignIn;
    private ActivityIdParams mParams;
    private VolunteerActivityDetailBean mResult;

    @Override
    protected int initContentLayout() {
        return R.layout.actitivty_v_activity_detail;
    }

    @Override
    protected void initData() {
        long activityId = getIntent().getLongExtra(EXTRA_ACTIVITY_ID, -1);
        boolean isDismissButton = getIntent().getBooleanExtra(EXTRA_DISMISS_BUTTON, false);
        if (isDismissButton) {
            mBtnSignIn.setVisibility(View.GONE);
        }
        mParams = new ActivityIdParams(activityId);
    }

    @Override
    protected void loadData() {
        showLoading("活动加载中...");
        HttpHelper.getInstance().getVolunteerActivityById(mParams, new EntityCallback<VolunteerActivityDetailBean>() {
            @Override
            public void onFailure(Call call, String message) {
                loadFailed("加载活动失败，" + message, true);
            }

            @Override
            public void onSuccess(Call call, VolunteerActivityDetailBean result) {
                loadClose();
                mResult = result;
                VolunteerActivityBean activity = mResult.getActivity();
                //加载数据
                Glide.with(VActivityDetailActivity.this).load(UrlConfig.buildImageUrl(activity.getImage())).into(mIvActivityPic);
                mTvActivityName.setText(activity.getActivityName());
                mTvActivityTime.setText(activity.getStartTime() + "至" + activity.getEndTime());
                setCountInfo(activity);
                mTvActivityDescription.setText(activity.getDescription());
                mTvActivityAddress.setText(activity.getAddress());
                if (result.isJoin()) {
                    mBtnSignIn.setText("取消预约");
                    mBtnSignIn.setBackgroundColor(ResUtils.getColor(R.color.colorPrimary));
                } else if (activity.getCount().equals(activity.getJoinCount())) {
                    mBtnSignIn.setText("预约已满");
                    mBtnSignIn.setBackgroundColor(ResUtils.getColor(R.color.colorDivider));
                } else {
                    mBtnSignIn.setText("立即预约");
                    mBtnSignIn.setBackgroundColor(ResUtils.getColor(R.color.colorPrimary));
                }
            }
        });
    }

    private void setCountInfo(VolunteerActivityBean activity) {
        String count = String.valueOf(activity.getCount());
        String joinCount = String.valueOf(activity.getJoinCount());
        SpannableString spannableString = new SpannableString(joinCount + "/" + count);
        spannableString.setSpan(new ForegroundColorSpan(ResUtils.getColor(R.color.colorPrimary)), 0, joinCount.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTvActivityCount.setText(spannableString);
    }

    @OnClick(R.id.btn_sign_in)
    public void onViewClicked() {
        final VolunteerActivityBean activity = mResult.getActivity();
        if (mResult.isJoin()) {//取消预约
            showCancelJoinDialog();
        } else if (activity.getCount().equals(activity.getJoinCount())) { //预约已满
            ToastUtils.showShort("预约人数已满，无法预约！");
        } else { //立即预约
            ActivityIdParams params = new ActivityIdParams(activity.getId());
            showLoading("预约中...");
            HttpHelper.getInstance().joinVolunteerActivity(params, new NetworkCallback() {
                @Override
                public void onSuccess(Call call, String json) {
                    loadSuccess("预约成功", false);
                    mResult.setJoin(true);
                    activity.setJoinCount(activity.getJoinCount() + 1);
                    setCountInfo(activity);
                    mBtnSignIn.setText("取消预约");
                    mBtnSignIn.setBackgroundColor(ResUtils.getColor(R.color.colorPrimary));
                    setResult(RESULT_OK);
                }

                @Override
                public void onFailure(Call call, String message) {
                    loadFailed("预约失败，" + message);
                }
            });
        }
    }

    private void showCancelJoinDialog() {
        new AlertDialog.Builder(this)
                .setMessage("是否取消预约该活动？")
                .setNegativeButton("否", null)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消预约
                        showLoading("取消预约中...");
                        HttpHelper.getInstance().cancelJoinVolunteerActivity(mParams, new NetworkCallback() {
                            @Override
                            public void onSuccess(Call call, String json) {
                                loadSuccess("取消成功", false);
                                mResult.setJoin(false);
                                VolunteerActivityBean activity = mResult.getActivity();
                                activity.setJoinCount(activity.getJoinCount() - 1);
                                setCountInfo(activity);
                                mBtnSignIn.setText("立即预约");
                                mBtnSignIn.setBackgroundColor(ResUtils.getColor(R.color.colorPrimary));
                                setResult(RESULT_OK);
                            }

                            @Override
                            public void onFailure(Call call, String message) {
                                loadFailed("取消失败，" + message);
                            }
                        });
                    }
                })
                .create()
                .show();
    }
}
