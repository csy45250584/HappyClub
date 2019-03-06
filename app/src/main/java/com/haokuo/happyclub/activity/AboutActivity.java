package com.haokuo.happyclub.activity;

import android.widget.TextView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.util.utilscode.AppUtils;

import butterknife.BindView;

/**
 * Created by zjf on 2018/9/12.
 */
public class AboutActivity extends BaseActivity {
    @BindView(R.id.tv_version_code)
    TextView mTvVersionCode;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData() {
        mTvVersionCode.setText("版本号："+AppUtils.getAppVersionName());
    }


}
