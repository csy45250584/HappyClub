package com.haokuo.happyclub.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.UserInfoBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.UrlConfig;
import com.haokuo.happyclub.network.bean.TransferPointsParams;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/11/14.
 */
public class TransferPointsActivity extends BaseActivity {
    public static final String EXTRA_USER = "com.haokuo.happyclub.extra.EXTRA_USER";
    @BindView(R.id.iv_avatar)
    CircleImageView mIvAvatar;
    @BindView(R.id.tv_target_name)
    TextView mTvTargetName;
    @BindView(R.id.et_points)
    EditText mEtPoints;
    private UserInfoBean mUserInfoBean;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_transfer_points;
    }

    @Override
    protected void initData() {
        mUserInfoBean = (UserInfoBean) getIntent().getSerializableExtra(EXTRA_USER);
        Glide.with(this).load(UrlConfig.buildImageUrl(mUserInfoBean.getHeadPhoto())).into(mIvAvatar);
        mTvTargetName.setText(mUserInfoBean.getRealname());
    }

    @OnClick({R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                int points = Integer.parseInt(mEtPoints.getEditableText().toString());
                TransferPointsParams params = new TransferPointsParams(mUserInfoBean.getId(), points);
                showLoading("转账中...");
                HttpHelper.getInstance().transferPoints(params, new NetworkCallback() {
                    @Override
                    public void onSuccess(Call call, String json) {
                        loadSuccess("转账成功");
                    }

                    @Override
                    public void onFailure(Call call, String message) {
                        loadFailed("转账失败，" + message);
                    }
                });
                break;
        }
    }
}
