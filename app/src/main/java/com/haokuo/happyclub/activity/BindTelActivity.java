package com.haokuo.happyclub.activity;

import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.TextView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.network.bean.base.TelPhoneParams;
import com.haokuo.happyclub.util.utilscode.RegexUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.rey.material.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zjf on 2018/9/11.
 */
public class BindTelActivity extends BaseActivity {
    private static final int CODE_NUM = 4;
    private static final long TOTAL_TIME = 60 * 1000;
    private static final long ONCE_TIME = 1000;
    @BindView(R.id.et_tel)
    TextInputEditText mEtTel;
    @BindView(R.id.et_code)
    TextInputEditText mEtCode;
    @BindView(R.id.tv_get_code)
    TextView mTvGetCode;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
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

    private void resetGetVerifyCode() {
        canGetCode = true;
        mTvGetCode.setText("重新获取");
    }

    @Override
    protected int initContentLayout() {
        return R.layout.activity_bind_tel;
    }

    @Override
    protected void initData() {
        canGetCode = true;
    }

    @OnClick({R.id.tv_get_code, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                if (canGetCode) {
                    String tel = mEtTel.getEditableText().toString().trim();
                    if (!RegexUtils.isMobileSimple(tel)) {
                        ToastUtils.showShort("请输入正确的手机号码");
                        return;
                    }
                    canGetCode = false;
                    mTvGetCode.setText("发送中");
                    TelPhoneParams params = new TelPhoneParams(tel);
//                    HttpHelper.getInstance().getResetVerifyCode(params, new NetworkCallback() {
//                        @Override
//                        public void onSuccess(Call call, String json) {
//                            countDownTimer.start();
//                        }
//
//                        @Override
//                        public void onFailure(Call call, String message) {
//                            ToastUtils.showShort("获取验证码失败，" + message);
//                            resetGetVerifyCode();
//                        }
//                    });
                }
                break;
            case R.id.btn_confirm:
                break;
        }
    }
}
