package com.haokuo.happyclub.activity;

import android.webkit.WebSettings;
import android.widget.FrameLayout;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;

/**
 * Created by zjf on 2018/10/19.
 */
public class NewsDetailActivity extends BaseActivity {
    public static final String EXTRA_NEWS_URL = "com.haokuo.happyclub.extra.EXTRA_NEWS_URL";
    @BindView(R.id.fl_web_container)
    FrameLayout mFlWebContainer;
    private AgentWeb mAgentWeb;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initData() {
        String url = getIntent().getStringExtra(EXTRA_NEWS_URL);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((FrameLayout) mFlWebContainer, new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);
        WebSettings webSettings = mAgentWeb.getAgentWebSettings().getWebSettings();
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
    }
}
