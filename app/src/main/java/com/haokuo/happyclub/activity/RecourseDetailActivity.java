package com.haokuo.happyclub.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.RecourseBean;
import com.haokuo.happyclub.bean.list.RecourseListBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.base.IdParams;
import com.haokuo.happyclub.util.utilscode.TimeUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;

import butterknife.BindView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/11.
 */
public class RecourseDetailActivity extends BaseActivity {
    public static final String EXTRA_RECOURSE_ID = "com.haokuo.happyclub.extra.EXTRA_RECOURSE_ID";
    @BindView(R.id.tv_recourse_address)
    TextView mTvRecourseAddress;
    @BindView(R.id.tv_recourse_content)
    TextView mTvRecourseContent;
    @BindView(R.id.tv_recourse_request)
    TextView mTvRecourseRequest;
    @BindView(R.id.tv_recourse_time)
    TextView mTvRecourseTime;
    @BindView(R.id.tv_recourse_score)
    TextView mTvRecourseScore;
    @BindView(R.id.rb_recourse_score)
    MaterialRatingBar mRbRecourseScore;
    @BindView(R.id.tv_evaluate_score)
    TextView mTvEvaluateScore;
    @BindView(R.id.tv_recourse_evaluate)
    TextView mTvRecourseEvaluate;
    @BindView(R.id.ll_evaluate_container)
    LinearLayout mLlEvaluateContainer;
    @BindView(R.id.iv_give_up)
    ImageView mIvGiveUp;
    @BindView(R.id.iv_complete)
    ImageView mIvComplete;
    private long mRecourseId;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_recourse_detail;
    }

    @Override
    protected void initData() {
        mRecourseId = getIntent().getLongExtra(EXTRA_RECOURSE_ID, -1);
    }

    @Override
    protected void loadData() {
        IdParams params = new IdParams(mRecourseId);
        HttpHelper.getInstance().getRecourseDetail(params, new EntityCallback<RecourseListBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("获取求助详情失败," + message);
            }

            @Override
            public void onSuccess(Call call, RecourseListBean result) {
                applyInfo2Ui(result.getData().get(0));
            }
        });
    }

    private void applyInfo2Ui(RecourseBean recourseBean) {
        Integer state = recourseBean.getReviewState();
        mIvGiveUp.setVisibility(state == 88 ? View.VISIBLE : View.GONE);
        mIvComplete.setVisibility(state == 44 || state == 55 ? View.VISIBLE : View.GONE);
        mLlEvaluateContainer.setVisibility(state == 55 ? View.VISIBLE : View.GONE);
        mTvRecourseAddress.setText(recourseBean.getAddress());
        mTvRecourseContent.setText(recourseBean.getContent());
        mTvRecourseRequest.setText(recourseBean.getClaim());
        String startTime = TimeUtils.sqlTime2String(recourseBean.getStartTime(), TimeUtils.CUSTOM_FORMAT);
        String endTime = TimeUtils.sqlTime2String(recourseBean.getEndTime(), TimeUtils.CUSTOM_FORMAT);
        mTvRecourseTime.setText(String.format("%s~%s", startTime, endTime));
        mTvRecourseScore.setText(String.valueOf(recourseBean.getIntegral()));
        if (state == 55) { //已评价
            mRbRecourseScore.setRating(recourseBean.getStar());
            mTvEvaluateScore.setText(String.format("%d分", recourseBean.getStar()));
            mTvRecourseEvaluate.setText(recourseBean.getEvaluation());
        }
    }
}
