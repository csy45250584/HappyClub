package com.haokuo.happyclub.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.RepairBean;
import com.haokuo.happyclub.view.TitleTextView;
import com.haokuo.midtitlebar.MidTitleBar;

import butterknife.BindView;

/**
 * Created by zjf on 2018/10/8.
 */
public class RepairDetailActivity extends BaseActivity {
    public static final String EXTRA_BEAN = "com.haokuo.happyclub.extra.EXTRA_BEAN";
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.tv_repair_name)
    TitleTextView mTvRepairName;
    @BindView(R.id.tv_repair_tel)
    TitleTextView mTvRepairTel;
    @BindView(R.id.tv_repair_address)
    TitleTextView mTvRepairAddress;
    @BindView(R.id.tv_repair_type)
    TitleTextView mTvRepairType;
    @BindView(R.id.tv_repair_content)
    TextView mTvRepairContent;
    @BindView(R.id.iv_repair_image)
    ImageView mIvRepairImage;
    @BindView(R.id.tv_repair_time)
    TitleTextView mTvRepairTime;
    @BindView(R.id.tv_repair_state)
    TitleTextView mTvRepairState;
    @BindView(R.id.tv_repair_reply)
    TextView mTvRepairReply;
    @BindView(R.id.tv_reply_name)
    TitleTextView mTvReplyName;
    @BindView(R.id.tv_reply_time)
    TitleTextView mTvReplyTime;
    @BindView(R.id.ll_reply_container)
    LinearLayout mLlReplyContainer;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_repair_detail;
    }

    @Override
    protected void initData() {
        RepairBean repairBean = (RepairBean) getIntent().getSerializableExtra(EXTRA_BEAN);
        Integer state = repairBean.getState();
        switch (state) {
            case RepairBean.STATE_UNHANDLED:
                mLlReplyContainer.setVisibility(View.GONE);
                mTvRepairState.setText("待处理");
                mTvRepairState.setContentTextColor(0xFFFD5722);
                break;
            case RepairBean.STATE_HANDLING:
                mLlReplyContainer.setVisibility(View.GONE);
                mTvRepairState.setText("处理中");
                mTvRepairState.setContentTextColor(0xFF0FA01F);
                break;
            case RepairBean.STATE_HANDLED:
                mLlReplyContainer.setVisibility(View.VISIBLE);
                mTvRepairState.setText("已完成");
                mTvRepairState.setContentTextColor(0xFF32A5A6);
                mTvRepairReply.setText(repairBean.getReplyContent());
                mTvReplyName.setText(repairBean.getReplyUser());
                mTvReplyTime.setText(repairBean.getReplyTime());
                break;
        }
        mTvRepairName.setText(repairBean.getUserName());
        mTvRepairTel.setText(repairBean.getTelphone());
        mTvRepairAddress.setText(repairBean.getAddress());
        mTvRepairType.setText(repairBean.getRepairTypeName());
        mTvRepairContent.setText(repairBean.getReportContent());
        mTvRepairTime.setText(repairBean.getReportTime());
        Glide.with(this).load(repairBean.getPictureurl()).into(mIvRepairImage);
    }
}
