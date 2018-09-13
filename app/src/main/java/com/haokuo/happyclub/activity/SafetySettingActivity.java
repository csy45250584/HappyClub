package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.view.View;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.view.SettingItemView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zjf on 2018/9/11.
 */
public class SafetySettingActivity extends BaseActivity {
    @BindView(R.id.siv_change_password)
    SettingItemView mSivChangePassword;
    @BindView(R.id.siv_bind_tel)
    SettingItemView mSivBindTel;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_safety_setting;
    }

    @Override
    protected void initData() {

    }



    @OnClick({R.id.siv_change_password, R.id.siv_bind_tel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.siv_change_password:
                startActivity(new Intent(SafetySettingActivity.this,ChangePasswordActivity.class));
                break;
            case R.id.siv_bind_tel:
                startActivity(new Intent(SafetySettingActivity.this,BindTelActivity.class));

                break;
        }
    }
}
