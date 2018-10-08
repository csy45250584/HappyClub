package com.haokuo.happyclub.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.RepairBean;
import com.haokuo.happyclub.bean.SuggestBean;
import com.haokuo.happyclub.view.TitleTextView;

import butterknife.BindView;

/**
 * Created by zjf on 2018/10/8.
 */
public class SuggestDetailActivity extends BaseActivity {
    public static final String EXTRA_BEAN = "com.haokuo.happyclub.extra.EXTRA_BEAN";
    @BindView(R.id.tv_suggest_name)
    TitleTextView mTvSuggestName;
    @BindView(R.id.tv_suggest_tel)
    TitleTextView mTvSuggestTel;
    @BindView(R.id.tv_suggest_content)
    TextView mTvSuggestContent;
    @BindView(R.id.iv_suggest_image)
    ImageView mIvSuggestImage;
    @BindView(R.id.tv_suggest_time)
    TitleTextView mTvSuggestTime;
    @BindView(R.id.tv_suggest_state)
    TitleTextView mTvSuggestState;
    @BindView(R.id.tv_suggest_reply)
    TextView mTvSuggestReply;
    @BindView(R.id.tv_reply_name)
    TitleTextView mTvReplyName;
    @BindView(R.id.tv_reply_time)
    TitleTextView mTvReplyTime;
    @BindView(R.id.ll_reply_container)
    LinearLayout mLlReplyContainer;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_suggest_detail;
    }

    @Override
    protected void initData() {
        SuggestBean suggestBean = (SuggestBean) getIntent().getSerializableExtra(EXTRA_BEAN);
        Integer state = suggestBean.getState();
        switch (state) {
            case RepairBean.STATE_UNHANDLED:
                mLlReplyContainer.setVisibility(View.GONE);
                mTvSuggestState.setText("未处理");
                mTvSuggestState.setContentTextColor(0xFFFD5722);
                break;
            case RepairBean.STATE_HANDLED:
                mLlReplyContainer.setVisibility(View.VISIBLE);
                mTvSuggestState.setText("已处理");
                mTvSuggestState.setContentTextColor(0xFF32A5A6);
                mTvSuggestReply.setText(suggestBean.getReplyContent());
                mTvReplyName.setText(suggestBean.getReplyUser());
                mTvReplyTime.setText(suggestBean.getReplyTime());
                break;
        }
        mTvSuggestName.setText(suggestBean.getUserName());
        mTvSuggestTel.setText(suggestBean.getTelphone());
        mTvSuggestContent.setText(suggestBean.getReportContent());
        mTvSuggestTime.setText(suggestBean.getReportTime());
        Glide.with(this).load(suggestBean.getPictureurl()).into(mIvSuggestImage);
    }

}
