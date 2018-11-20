package com.haokuo.happyclub.activity;

import android.webkit.WebSettings;
import android.widget.FrameLayout;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;

/**
 * Created by zjf on 2018/11/20.
 */
public class NursingActivity extends BaseActivity {
    private static final String NURSING_URL = "http://www.longwill.com.cn/";
    @BindView(R.id.fl_web_container)
    FrameLayout mFlWebContainer;
    private AgentWeb mAgentWeb;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_nursing;
    }

    @Override
    protected void initData() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((FrameLayout) mFlWebContainer, new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(NURSING_URL);
        WebSettings webSettings = mAgentWeb.getAgentWebSettings().getWebSettings();
        //        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
    }
}
