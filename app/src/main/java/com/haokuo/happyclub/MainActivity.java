package com.haokuo.happyclub;

import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.UserInfoBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.EntityCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_hello)
    TextView mTvHello;

    @OnClick(R.id.tv_hello)
    public void onViewClicked() {
        HttpHelper.getInstance().getUserInfo(new EntityCallback<UserInfoBean>() {
            @Override
            public void onFailure(Call call, String message) {

            }

            @Override
            public void onSuccess(Call call, UserInfoBean result) {
                Log.v("MY_CUSTOM_TAG", "MainActivity onSuccess()-->" + JSON.toJSONString(result));
            }
        });
    }

    @Override
    protected int initContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }
}
