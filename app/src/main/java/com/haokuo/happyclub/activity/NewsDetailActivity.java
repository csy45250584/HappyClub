package com.haokuo.happyclub.activity;

import android.webkit.WebSettings;
import android.widget.FrameLayout;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.midtitlebar.MidTitleBar;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;

/**
 * Created by zjf on 2018/10/19.
 */
public class NewsDetailActivity extends BaseActivity {
    public static final String EXTRA_NEWS_URL = "com.haokuo.happyclub.extra.EXTRA_NEWS_URL";
    public static final String EXTRA_TITLE = "com.haokuo.happyclub.extra.EXTRA_TITLE";
    @BindView(R.id.fl_web_container)
    FrameLayout mFlWebContainer;
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    private AgentWeb mAgentWeb;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initData() {
        String url = getIntent().getStringExtra(EXTRA_NEWS_URL);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        mMidTitleBar.setMidTitle(title);
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
