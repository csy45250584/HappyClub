package com.haokuo.happyclub.activity;

import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.UpdatePasswordParams;
import com.haokuo.happyclub.util.MySpUtil;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.midtitlebar.MidTitleBar;
import com.rey.material.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/31.
 */
public class ResetPasswordActivity extends BaseActivity {
    @BindView(R.id.et_old_password)
    TextInputEditText mEtOldPassword;
    @BindView(R.id.et_password)
    TextInputEditText mEtPassword;
    @BindView(R.id.et_confirm_password)
    TextInputEditText mEtConfirmPassword;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                //检查数据
                String oldPassword = mEtOldPassword.getEditableText().toString().trim();
                String password = mEtPassword.getEditableText().toString().trim();
                String confirmPassword = mEtConfirmPassword.getEditableText().toString().trim();
                if (!password.equals(confirmPassword)) {
                    ToastUtils.showShort("两次密码不一致，请重新输入");
                }
                UpdatePasswordParams params = new UpdatePasswordParams(MySpUtil.getInstance().geTel(), oldPassword, password);
                showLoading("正在修改...");
                HttpHelper.getInstance().updatePassword(params, new NetworkCallback() {
                    @Override
                    public void onSuccess(Call call, String json) {
                        loadSuccess("修改成功");
                    }

                    @Override
                    public void onFailure(Call call, String message) {
                        loadFailed("修改失败，" + message);
                    }
                });
                break;
        }
    }
}
