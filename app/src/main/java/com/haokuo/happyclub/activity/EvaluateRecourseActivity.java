package com.haokuo.happyclub.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.EvaluationBean;
import com.haokuo.happyclub.bean.RecourseBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.util.utilscode.TimeUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.midtitlebar.MidTitleBar;

import butterknife.BindView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/10.
 */
public class EvaluateRecourseActivity extends BaseActivity {
    public static final String EXTRA_RECOURSE_BEAN = "com.haokuo.happyclub.extra.EXTRA_RECOURSE_BEAN";
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.tv_serve_name)
    TextView mTvServeName;
    @BindView(R.id.tv_serve_request)
    TextView mTvServeRequest;
    @BindView(R.id.tv_serve_time)
    TextView mTvServeTime;
    @BindView(R.id.tv_serve_address)
    TextView mTvServeAddress;
    @BindView(R.id.rb_recourse_score)
    MaterialRatingBar mRbRecourseScore;
    @BindView(R.id.tv_recourse_score)
    TextView mTvRecourseScore;
    @BindView(R.id.et_evaluation_content)
    EditText mEtEvaluationContent;
    private RecourseBean mRecourseBean;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_evaluate_recourse;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complete, menu);
        return true;
    }

    @Override
    protected void initData() {
        mRecourseBean = (RecourseBean) getIntent().getSerializableExtra(EXTRA_RECOURSE_BEAN);
        mTvServeName.setText(mRecourseBean.getServeName());
        mTvServeRequest.setText(mRecourseBean.getClaim());
        String startTime = TimeUtils.sqlTime2String(mRecourseBean.getStartTime(), TimeUtils.CUSTOM_FORMAT);
        String endTime = TimeUtils.sqlTime2String(mRecourseBean.getEndTime(), TimeUtils.CUSTOM_FORMAT);
        mTvServeTime.setText(String.format("%s~%s", startTime, endTime));
        mTvServeAddress.setText(mRecourseBean.getAddress());
    }

    @Override
    protected void initListener() {
        mRbRecourseScore.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                mTvRecourseScore.setText(String.format("%d分", (int) rating));
            }
        });
        mMidTitleBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.item_complete) {
                    //点击完成
                    String content = mEtEvaluationContent.getEditableText().toString().trim();
                    if (content.length() <= 10) {
                        ToastUtils.showShort("评价内容不少于10字!");
                        return true;
                    }
                    EvaluationBean evaluationBean = new EvaluationBean();
                    evaluationBean.setServeId(mRecourseBean.getId());
                    evaluationBean.setEvaluation(content);
                    evaluationBean.setStar((int) mRbRecourseScore.getRating());
                    showLoading("提交中...");
                    HttpHelper.getInstance().evaluateRecourse(evaluationBean, new NetworkCallback() {
                        @Override
                        public void onSuccess(Call call, String json) {
                            setResult(RESULT_OK);
                            loadSuccess("提交成功", true);
                        }

                        @Override
                        public void onFailure(Call call, String message) {
                            loadFailed("提交失败，" + message);
                        }
                    });
                }
                return true;
            }
        });
    }
}
