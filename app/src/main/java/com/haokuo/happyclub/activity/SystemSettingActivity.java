package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.view.View;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.CartFoodBean;
import com.haokuo.happyclub.eventbus.LogoutEvent;
import com.haokuo.happyclub.util.MySpUtil;
import com.haokuo.happyclub.view.SettingItemView;
import com.rey.material.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zjf on 2018/9/11.
 */
public class SystemSettingActivity extends BaseActivity {

    @BindView(R.id.siv_clear_cache)
    SettingItemView mSivClearCache;
    @BindView(R.id.siv_about_us)
    SettingItemView mSivAboutUs;
    @BindView(R.id.btn_logout)
    Button mBtnLogout;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_system_setting;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.siv_clear_cache, R.id.siv_about_us, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.siv_clear_cache:
                break;
            case R.id.siv_about_us:
                startActivity(new Intent(SystemSettingActivity.this,AboutActivity.class));
                break;
            case R.id.btn_logout:
                clearAllData();
                startActivity(new Intent(SystemSettingActivity.this, LoginActivity.class));
                EventBus.getDefault().post(new LogoutEvent());
                finish();
                break;
        }
    }

    private void clearAllData() {
        MySpUtil.getInstance().logout();
        LitePal.deleteAll(CartFoodBean.class);
    }
}
