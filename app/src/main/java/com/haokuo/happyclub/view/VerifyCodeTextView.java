package com.haokuo.happyclub.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zjf on 2018/9/10.
 */
public class VerifyCodeTextView extends AppCompatTextView implements View.OnClickListener {
    private static final int CODE_NUM = 4;
    private static final long TOTAL_TIME = 60 * 1000;
    private static final long ONCE_TIME = 1000;
    private boolean canGetCode;

    public VerifyCodeTextView(Context context) {
        this(context, null);
    }

    public VerifyCodeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    private CountDownTimer countDownTimer = new CountDownTimer(TOTAL_TIME, ONCE_TIME) {
        @Override
        public void onTick(long millisUntilFinished) {
            String value = String.valueOf((int) (millisUntilFinished / 1000));
            setText(value + "秒后");
        }

        @Override
        public void onFinish() {
            resetGetVerifyCode();
        }
    };

    private void resetGetVerifyCode() {
        canGetCode = true;
        setText("重新获取");
    }

    private OnVerifyCodeListener mOnVerifyCodeListener;

    public void setOnVerifyCodeListener(OnVerifyCodeListener onVerifyCodeListener) {
        mOnVerifyCodeListener = onVerifyCodeListener;
    }

    public interface OnVerifyCodeListener {
        boolean onCheckRegex();

        void getCode();
    }

    @Override
    public void onClick(View v) {
        boolean pass = true;
        if (mOnVerifyCodeListener != null) {
            pass = mOnVerifyCodeListener.onCheckRegex();
        }
        if (pass) {
            canGetCode = false;
            setText("发送中");
            mOnVerifyCodeListener.getCode();
        }
    }
}
