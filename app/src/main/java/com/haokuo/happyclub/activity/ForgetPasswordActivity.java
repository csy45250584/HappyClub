package com.haokuo.happyclub.activity;

import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.CheckIsNextParams;
import com.haokuo.happyclub.network.bean.ResetPasswordParams;
import com.haokuo.happyclub.network.bean.base.TelPhoneParams;
import com.haokuo.happyclub.util.utilscode.RegexUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.rey.material.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/10.
 */
public class ForgetPasswordActivity extends BaseActivity {
    private static final int CODE_NUM = 4;
    private static final long TOTAL_TIME = 60 * 1000;
    private static final long ONCE_TIME = 1000;
    @BindView(R.id.et_tel)
    TextInputEditText mEtTel;
    @BindView(R.id.et_code)
    TextInputEditText mEtCode;
    @BindView(R.id.tv_get_code)
    TextView mTvGetCode;
    @BindView(R.id.btn_next_step)
    Button mBtnNextStep;
    @BindView(R.id.ll_step1_container)
    LinearLayout mLlStep1Container;
    @BindView(R.id.et_password)
    TextInputEditText mEtPassword;
    @BindView(R.id.et_confirm_password)
    TextInputEditText mEtConfirmPassword;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.ll_step2_container)
    LinearLayout mLlStep2Container;
    private boolean canGetCode;
    private CountDownTimer countDownTimer = new CountDownTimer(TOTAL_TIME, ONCE_TIME) {
        @Override
        public void onTick(long millisUntilFinished) {
            String value = String.valueOf((int) (millisUntilFinished / 1000));
            mTvGetCode.setText(value + "秒后");
        }

        @Override
        public void onFinish() {
            resetGetVerifyCode();
        }
    };
    private String mTel;
    private String mCode;

    private void resetGetVerifyCode() {
        canGetCode = true;
        mTvGetCode.setText("重新获取");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    protected int initContentLayout() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initData() {
        canGetCode = true;
        mEtCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(CODE_NUM)});
        mLlStep1Container.setVisibility(View.VISIBLE);
        mLlStep2Container.setVisibility(View.GONE);
    }

    @OnClick({R.id.tv_get_code, R.id.btn_next_step, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code: {
                if (canGetCode) {
                    String tel = mEtTel.getEditableText().toString().trim();
                    if (!RegexUtils.isMobileSimple(tel)) {
                        ToastUtils.showShort("请输入正确的手机号码");
                        return;
                    }
                    canGetCode = false;
                    mTvGetCode.setText("发送中");
                    TelPhoneParams params = new TelPhoneParams(tel);
                    HttpHelper.getInstance().getResetVerifyCode(params, new NetworkCallback() {
                        @Override
                        public void onSuccess(Call call, String json) {
                            countDownTimer.start();
                        }

                        @Override
                        public void onFailure(Call call, String message) {
                            ToastUtils.showShort("获取验证码失败，" + message);
                            resetGetVerifyCode();
                        }
                    });
                }
            }
            break;
            case R.id.btn_next_step: {
                mTel = mEtTel.getEditableText().toString().trim();
                mCode = mEtCode.getEditableText().toString().trim();
                if (!RegexUtils.isMobileSimple(mTel)) {
                    ToastUtils.showShort("请输入正确的手机号码");
                    return;
                }
                if (mCode.length() != CODE_NUM) {
                    ToastUtils.showShort("验证码格式错误");
                    return;
                }
                showLoading("验证中...");
                CheckIsNextParams params = new CheckIsNextParams(mTel, mCode);
                HttpHelper.getInstance().checkIsNext(params, new NetworkCallback() {
                    @Override
                    public void onSuccess(Call call, String json) {
                        mLlStep1Container.setVisibility(View.GONE);
                        mLlStep2Container.setVisibility(View.VISIBLE);
                        loadClose();
                    }

                    @Override
                    public void onFailure(Call call, String message) {
                        loadFailed("验证失败，" + message);
                    }
                });
            }
            break;
            case R.id.btn_confirm: {
                String password = mEtPassword.getEditableText().toString().trim();
                String confirmPassword = mEtConfirmPassword.getEditableText().toString().trim();
                if (!RegexUtils.isSimplePassword(password)) {
                    ToastUtils.showShort("密码格式错误");
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    ToastUtils.showShort("两次密码不同，请重新输入");
                    return;
                }
                showLoading("提交中...");
                ResetPasswordParams params = new ResetPasswordParams(mTel, mCode, password, confirmPassword);
                HttpHelper.getInstance().resetPassword(params, new NetworkCallback() {
                    @Override
                    public void onSuccess(Call call, String json) {
                        loadSuccess("修改成功");
                    }

                    @Override
                    public void onFailure(Call call, String message) {
                        loadFailed("修改失败，" + message);
                    }
                });
            }
            break;
        }
    }
}
