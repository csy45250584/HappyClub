package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.RecourseBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.TitleEditorView;
import com.haokuo.midtitlebar.MidTitleBar;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/14.
 */
public class NewRecourseActivity extends BaseActivity {
    public static final String EXTRA_RECOURSE_BEAN = "com.haokuo.happyclub.extra.EXTRA_RECOURSE_BEAN";
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.et_serve_name)
    TitleEditorView mEtServeName;
    @BindView(R.id.et_serve_address)
    TitleEditorView mEtServeAddress;
    @BindView(R.id.iv_location)
    ImageView mIvLocation;
    @BindView(R.id.et_serve_content)
    EditText mEtServeContent;
    @BindView(R.id.et_request)
    TitleEditorView mEtRequest;
    @BindView(R.id.et_begin_date)
    TitleEditorView mEtBeginDate;
    @BindView(R.id.et_end_date)
    TitleEditorView mEtEndDate;
    @BindView(R.id.et_serve_score)
    TitleEditorView mEtServeScore;
    private String mAddressLatitude;
    private String mAddressLongitude;
    private RecourseBean mRecourseBean;
    private NetworkCallback mCallback;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_new_recourse;
    }

    @Override
    protected void initData() {
        //积分只能填写数字
        mEtServeScore.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        mRecourseBean = (RecourseBean) getIntent().getSerializableExtra(EXTRA_RECOURSE_BEAN);
        if (mRecourseBean != null) {
            mEtServeName.setText(mRecourseBean.getServeName());
            mEtServeAddress.setText(mRecourseBean.getAddress());
            mEtServeContent.setText(mRecourseBean.getContent());
            mEtRequest.setText(mRecourseBean.getClaim());
            mEtBeginDate.setText(mRecourseBean.getStartTime());
            mEtEndDate.setText(mRecourseBean.getEndTime());
            mEtServeScore.setText(String.valueOf(mRecourseBean.getIntegral()));
            mAddressLatitude = mRecourseBean.getLatitude();
            mAddressLongitude = mRecourseBean.getLongitude();
        }
        mCallback = new NetworkCallback() {
            @Override
            public void onSuccess(Call call, String json) {
                setResult(RESULT_OK);
                loadSuccess("发布成功");
            }

            @Override
            public void onFailure(Call call, String message) {
                loadFailed("发布失败，" + message);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complete, menu);
        return true;
    }

    @Override
    protected void initListener() {
        mMidTitleBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.item_complete) {
                    //获取数据
                    String serveName = mEtServeName.getText();
                    String serveAddress = mEtServeAddress.getText();
                    String ServeContent = mEtServeContent.getEditableText().toString().trim();
                    String serveRequest = mEtRequest.getText();
                    String beginDate = mEtBeginDate.getText();
                    String endDate = mEtEndDate.getText();
                    String ServeScore = mEtServeScore.getText();
                    //检查数据
                    if (TextUtils.isEmpty(serveName) || TextUtils.isEmpty(serveAddress) || TextUtils.isEmpty(ServeContent) ||
                            TextUtils.isEmpty(serveRequest) || TextUtils.isEmpty(beginDate) || TextUtils.isEmpty(endDate) ||
                            TextUtils.isEmpty(ServeScore)) {
                        ToastUtils.showShort("请完善求助信息！");
                        return true;
                    }
                    //发送请求
                    RecourseBean recourseBean = new RecourseBean();
                    if (mRecourseBean != null) {
                        recourseBean = mRecourseBean;
                    }
                    recourseBean.setServeName(serveName);
                    recourseBean.setAddress(serveAddress);
                    recourseBean.setContent(ServeContent);
                    recourseBean.setClaim(serveRequest);
                    recourseBean.setStartTime(beginDate);
                    recourseBean.setEndTime(endDate);
                    recourseBean.setIntegral(Double.parseDouble(ServeScore));
                    recourseBean.setLatitude(mAddressLatitude);
                    recourseBean.setLongitude(mAddressLongitude);
                    showLoading("正在提交...");
                    if (mRecourseBean != null) {
                        HttpHelper.getInstance().republishRecourse(recourseBean, mCallback);
                    }
                    HttpHelper.getInstance().newRecourse(recourseBean, mCallback);
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            String address = data.getStringExtra(ChooseLocationActivity.EXTRA_ADDRESS);
            mAddressLatitude = String.valueOf(data.getDoubleExtra(ChooseLocationActivity.EXTRA_LATITUDE, 0));
            mAddressLongitude = String.valueOf(data.getDoubleExtra(ChooseLocationActivity.EXTRA_LONGITUDE, 0));
            mEtServeAddress.setText(address);
        }
    }

    @OnClick(R.id.iv_location)
    public void onViewClicked() {
        //点击开启地图选择点位
        startActivityForResult(new Intent(this, ChooseLocationActivity.class), 0);
    }
}
