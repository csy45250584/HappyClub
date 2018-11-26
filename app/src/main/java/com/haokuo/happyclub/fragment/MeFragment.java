package com.haokuo.happyclub.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.activity.MyActivityActivity;
import com.haokuo.happyclub.activity.MyCourseActivity;
import com.haokuo.happyclub.activity.MyRecourseActivity;
import com.haokuo.happyclub.activity.MyScoreActivity;
import com.haokuo.happyclub.activity.MyServeActivity;
import com.haokuo.happyclub.activity.PersonalInfoActivity;
import com.haokuo.happyclub.activity.SafetySettingActivity;
import com.haokuo.happyclub.activity.SystemSettingActivity;
import com.haokuo.happyclub.base.BaseLazyLoadFragment;
import com.haokuo.happyclub.bean.UserInfoBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.util.MySpUtil;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.SettingItemView;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/11.
 */
public class MeFragment extends BaseLazyLoadFragment {
    @BindView(R.id.iv_user_avatar)
    CircleImageView mIvUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_recommend)
    TextView mTvRecommend;
    @BindView(R.id.ll_collection)
    LinearLayout mLlCollection;
    @BindView(R.id.ll_evaluation)
    LinearLayout mLlEvaluation;
    @BindView(R.id.ll_coupon)
    LinearLayout mLlCoupon;
    @BindView(R.id.siv_points)
    SettingItemView mSivPoints;
    @BindView(R.id.siv_service)
    SettingItemView mSivService;
    @BindView(R.id.siv_help)
    SettingItemView mSivHelp;
    @BindView(R.id.siv_safety_setting)
    SettingItemView mSivSafetySetting;
    @BindView(R.id.siv_system_setting)
    SettingItemView mSivSystemSetting;
    @BindView(R.id.siv_my_info)
    SettingItemView mSivMyInfo;
    @BindView(R.id.siv_apply_volunteer)
    SettingItemView mSivApplyVolunteer;

    @Override
    protected int initContentLayout() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initData() {
        HttpHelper.getInstance().getUserInfo(new EntityCallback<UserInfoBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("获取用户信息失败," + message);
            }

            @Override
            public void onSuccess(Call call, UserInfoBean result) {
                Glide.with(MeFragment.this).load(result.getHeadPhoto()).into(mIvUserAvatar);
                mTvUserName.setText(result.getUserName());
                MySpUtil.getInstance().saveUserInfo(result);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        UserInfoBean userInfo = MySpUtil.getInstance().getUserInfo();
        Glide.with(MeFragment.this).load(userInfo.getHeadPhoto()).into(mIvUserAvatar);
        mTvUserName.setText(userInfo.getUserName());
    }

    @OnClick({R.id.siv_course, R.id.siv_activity, R.id.siv_my_info, R.id.siv_apply_volunteer, R.id.tv_recommend, R.id.ll_collection, R.id.ll_evaluation, R.id.ll_coupon, R.id.siv_points, R.id.siv_service, R.id.siv_help, R.id.siv_safety_setting, R.id.siv_system_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_recommend:
                break;
            case R.id.siv_activity:
                startActivity(new Intent(mContext, MyActivityActivity.class));
                break;
            case R.id.siv_course:
                startActivity(new Intent(mContext, MyCourseActivity.class));
                break;
            case R.id.ll_collection:
                break;
            case R.id.ll_evaluation:
                break;
            case R.id.ll_coupon:
                break;
            case R.id.siv_points:
                startActivity(new Intent(mContext, MyScoreActivity.class));
                break;
            case R.id.siv_service:
                startActivity(new Intent(mContext, MyServeActivity.class));
                break;
            case R.id.siv_help:
                startActivity(new Intent(mContext, MyRecourseActivity.class));
                break;

            case R.id.siv_safety_setting:
                startActivity(new Intent(mContext, SafetySettingActivity.class));
                break;
            case R.id.siv_system_setting:
                startActivity(new Intent(mContext, SystemSettingActivity.class));
                break;
            case R.id.siv_my_info:
                startActivity(new Intent(mContext, PersonalInfoActivity.class));
                break;
            case R.id.siv_apply_volunteer: {
                int volunteerStatus = MySpUtil.getInstance().getVolunteerStatus();
                if (volunteerStatus == UserInfoBean.VOLUNTEER_STATUS_NONE || volunteerStatus == UserInfoBean.VOLUNTEER_STATUS_REJECTED) {
                    showApply2BeVolunteer();
                } else if (volunteerStatus == UserInfoBean.VOLUNTEER_STATUS_UNCHECKED) {
                    ToastUtils.showShort("您的志愿者已等待审核,无须再次提交!");
                } else if (volunteerStatus == UserInfoBean.VOLUNTEER_STATUS_AGREED) {
                    ToastUtils.showShort("您已是志愿者,无须申请!");
                }
            }
            break;
        }
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
                                mContext.loadSuccess("申请成功,等待审核", false);
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
