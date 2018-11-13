package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.LoginResultBean;
import com.haokuo.happyclub.consts.SpConsts;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.LoginByTelParams;
import com.haokuo.happyclub.network.bean.LoginParams;
import com.haokuo.happyclub.network.bean.base.TelPhoneParams;
import com.haokuo.happyclub.util.MySpUtil;
import com.haokuo.happyclub.util.utilscode.RegexUtils;
import com.haokuo.happyclub.util.utilscode.SPUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.rey.material.widget.Button;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/7.
 */
public class LoginActivity extends BaseActivity {
    private static final int CODE_NUM = 4;
    private static final long TOTAL_TIME = 60 * 1000;
    private static final long ONCE_TIME = 1000;
    @BindView(R.id.btn_register)
    Button mBtnRegister;
    @BindView(R.id.et_tel)
    TextInputEditText mEtTel;
    @BindView(R.id.et_code)
    TextInputEditText mEtCode;
    @BindView(R.id.tv_get_code)
    TextView mTvGetCode;
    @BindView(R.id.ll_tel_login_container)
    LinearLayout mLlTelLoginContainer;
    @BindView(R.id.et_account)
    TextInputEditText mEtAccount;
    @BindView(R.id.et_password)
    TextInputEditText mEtPassword;
    @BindView(R.id.ll_password_login_container)
    LinearLayout mLlPasswordLoginContainer;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.tv_login_by_password)
    TextView mTvLoginByPassword;
    @BindView(R.id.tv_login_by_tel)
    TextView mTvLoginByTel;
    @BindView(R.id.tv_forget_password)
    TextView mTvForgetPassword;
    private boolean mIsTelLogin;
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
    private NetworkCallback mLoginCallback;

    private void resetGetVerifyCode() {
        canGetCode = true;
        mTvGetCode.setText("重新获取");
    }

    @Override
    protected int initContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        mIsTelLogin = true;
        applyUiByState();
        canGetCode = true;
        mEtCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(CODE_NUM)});
        mLoginCallback = new EntityCallback<LoginResultBean>() {
            @Override
            public void onFailure(Call call, String message) {
                loadFailed("登录失败，" + message);
            }

            @Override
            public void onSuccess(Call call, LoginResultBean result) {
                SPUtils spUtils = SPUtils.getInstance(SpConsts.FILE_PERSONAL_INFORMATION);
                spUtils.put(SpConsts.KEY_USER_ID, result.getUserId());
                spUtils.put(SpConsts.KEY_TOKEN, result.getToken());
                String tel = mIsTelLogin ? mEtTel.getEditableText().toString().trim() : mEtAccount.getEditableText().toString().trim();
                MySpUtil.getInstance().saveTel(tel);
                loadSuccess("登录成功", new LoadingDialog.OnFinishListener() {
                    @Override
                    public void onFinish() {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
        };
    }

    //        @Override
    //        public void onSuccess(Call call, String json) {
    //            loadSuccess("登录成功", new LoadingDialog.OnFinishListener() {
    //                @Override
    //                public void onFinish() {
    //                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
    //                    finish();
    //                }
    //            });
    //        }
    //
    //        @Override
    //        public void onFailure(Call call, String message) {
    //            loadFailed("登录失败，" + message);
    //        }
    //    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {// 注册成功
            showLoading("登录中...");
            String tel = data.getStringExtra(RegisterActivity.EXTRA_TEL);
            String password = data.getStringExtra(RegisterActivity.EXTRA_PASSWORD);
            LoginParams params = new LoginParams(tel, password);
            HttpHelper.getInstance().login(params, mLoginCallback);
        }
    }

    @OnClick({R.id.btn_register, R.id.tv_get_code, R.id.btn_login, R.id.tv_login_by_password, R.id.tv_login_by_tel, R.id.tv_forget_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 0);
                break;
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
                    HttpHelper.getInstance().getLoginVerifyCode(params, new NetworkCallback() {
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
            case R.id.btn_login: {
                if (mIsTelLogin) {
                    String tel = mEtTel.getEditableText().toString().trim();
                    String code = mEtCode.getEditableText().toString().trim();
                    if (!RegexUtils.isMobileSimple(tel)) {
                        ToastUtils.showShort("请输入正确的手机号码");
                        return;
                    }
                    if (code.length() != CODE_NUM) {
                        ToastUtils.showShort("验证码格式错误");
                        return;
                    }
                    LoginByTelParams params = new LoginByTelParams(tel, code);
                    showLoading("登录中...");
                    HttpHelper.getInstance().loginByTel(params, mLoginCallback);
                } else {
                    String account = mEtAccount.getEditableText().toString().trim();
                    String password = mEtPassword.getEditableText().toString().trim();
                    if (!RegexUtils.isSimplePassword(password)) {
                        ToastUtils.showShort("密码格式错误");
                        return;
                    }
                    showLoading("登录中...");
                    LoginParams params = new LoginParams(account, password);
                    HttpHelper.getInstance().login(params, mLoginCallback);
                }
            }
            break;
            case R.id.tv_login_by_password:
                mIsTelLogin = false;
                applyUiByState();
                break;
            case R.id.tv_login_by_tel:
                mIsTelLogin = true;
                applyUiByState();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
                break;
        }
    }

    private void applyUiByState() {
        mLlTelLoginContainer.setVisibility(mIsTelLogin ? View.VISIBLE : View.GONE);
        mTvLoginByPassword.setVisibility(mIsTelLogin ? View.VISIBLE : View.GONE);
        mLlPasswordLoginContainer.setVisibility(mIsTelLogin ? View.GONE : View.VISIBLE);
        mTvLoginByTel.setVisibility(mIsTelLogin ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }
}
