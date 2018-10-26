package com.haokuo.happyclub.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.OrderDetailBean;
import com.haokuo.happyclub.bean.data.OrderDetailEntityBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.UrlConfig;
import com.haokuo.happyclub.network.bean.GetOrderDetailParams;
import com.haokuo.happyclub.network.bean.UpdateOrderParams;
import com.haokuo.happyclub.network.bean.UpdateOrderWithReasonParams;
import com.haokuo.happyclub.util.utilscode.TimeUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.midtitlebar.MidTitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/12.
 */
public class ClubServiceOrderDetailActivity extends BaseActivity {
    public static final String EXTRA_AUTO_PAY = "com.haokuo.happyclub.extra.EXTRA_AUTO_PAY";
    public static final String EXTRA_ORDER_ID = "com.haokuo.happyclub.extra.EXTRA_ORDER_ID";
    public static final int REQUEST_CODE_EVALUATE = 1;
    public static final int REQUEST_CODE_PAY = 2;
    public static final int REQUEST_CODE_QRCODE = 3;
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.tv_order_state)
    TextView mTvOrderState;
    @BindView(R.id.iv_product_pic)
    ImageView mIvProductPic;
    @BindView(R.id.tv_product_name)
    TextView mTvProductName;
    @BindView(R.id.tv_product_score)
    TextView mTvProductScore;
    @BindView(R.id.tv_cancel_order)
    TextView mTvCancelOrder;
    @BindView(R.id.tv_pay_order)
    TextView mTvPayOrder;
    @BindView(R.id.tv_show_qrcode)
    TextView mTvShowQrcode;
    @BindView(R.id.ll_btn_container)
    LinearLayout mLlBtnContainer;
    @BindView(R.id.tv_order_number)
    TextView mTvOrderNumber;
    @BindView(R.id.tv_order_time)
    TextView mTvOrderTime;
    @BindView(R.id.tv_complete)
    TextView mTvComplete;
    @BindView(R.id.tv_refund)
    TextView mTvRefund;
    @BindView(R.id.tv_refund_appeal)
    TextView mTvRefundAppeal;
    @BindView(R.id.tv_evaluate)
    TextView mTvEvaluate;
    @BindView(R.id.rb_order_score)
    MaterialRatingBar mRbOrderScore;
    @BindView(R.id.tv_order_score)
    TextView mTvOrderScore;
    @BindView(R.id.tv_evaluation_content)
    TextView mTvEvaluationContent;
    @BindView(R.id.tv_reply_content)
    TextView mTvReplyContent;
    @BindView(R.id.ll_reply_container)
    LinearLayout mLlReplyContainer;
    @BindView(R.id.ll_evaluate_container)
    LinearLayout mLlEvaluateContainer;
    @BindView(R.id.iv_evaluate_image)
    ImageView mIvEvaluateImage;
    private long mOrderId;
    private OrderDetailBean mOrderDetailBean;
    private AlertDialog mRefundDialog;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_club_service_order_detail;
    }

    @Override
    protected void initData() {
        //        mIsAutoPay = getIntent().getBooleanExtra(EXTRA_AUTO_PAY, false);
        mOrderId = getIntent().getLongExtra(EXTRA_ORDER_ID, 0);
    }

    @Override
    protected void loadData() {
        GetOrderDetailParams params = new GetOrderDetailParams(mOrderId, OrderDetailBean.ORDER_TYPE_SERVICE);
        HttpHelper.getInstance().getOrderDetail(params, new EntityCallback<OrderDetailEntityBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("加载订单信息失败," + message);
            }

            @Override
            public void onSuccess(Call call, OrderDetailEntityBean result) {
                mOrderDetailBean = result.getData();
                loadOrderDetail(mOrderDetailBean);
            }
        });
    }

    private void loadOrderDetail(OrderDetailBean orderDetailBean) {
        mTvOrderState.setText(orderDetailBean.getStateString());
        mTvOrderNumber.setText(orderDetailBean.getOrderNo());
        mTvOrderTime.setText(TimeUtils.sqlTime2String(orderDetailBean.getCreateDate()));
        List<OrderDetailBean.OrderItem> orderItems = orderDetailBean.getOrderItems();
        if (orderItems.size() != 0) {
            OrderDetailBean.OrderItem orderItem = orderItems.get(0);
            Glide.with(this).load(UrlConfig.buildImageUrl(orderItem.getProPictureurl())).into(mIvProductPic);
            mTvProductName.setText(orderItem.getProName());
            mTvProductScore.setText(String.valueOf(orderItem.getProIntegral()));
        }
        mLlBtnContainer.setVisibility(View.GONE);
        mTvPayOrder.setVisibility(View.GONE);
        mTvCancelOrder.setVisibility(View.GONE);
        mTvShowQrcode.setVisibility(View.GONE);
        mTvRefund.setVisibility(View.GONE);
        mTvComplete.setVisibility(View.GONE);
        mTvEvaluate.setVisibility(View.GONE);
        mTvRefundAppeal.setVisibility(View.GONE);
        switch (orderDetailBean.getStatus()) {
            case OrderDetailBean.STATE_WAIT_FOR_HANDLE:
                mLlBtnContainer.setVisibility(View.VISIBLE);
                mTvPayOrder.setVisibility(View.VISIBLE);
                mTvCancelOrder.setVisibility(View.VISIBLE);
                break;
            case OrderDetailBean.STATE_PAYED:
                mLlBtnContainer.setVisibility(View.VISIBLE);
                mTvShowQrcode.setVisibility(View.VISIBLE);
                mTvRefund.setVisibility(View.VISIBLE);
                break;
            case OrderDetailBean.STATE_SERVED:
                mLlBtnContainer.setVisibility(View.VISIBLE);
                mTvRefund.setVisibility(View.VISIBLE);
                mTvComplete.setVisibility(View.VISIBLE);
                break;
            case OrderDetailBean.STATE_NO_EVALUATE:
                mLlBtnContainer.setVisibility(View.VISIBLE);
                mTvEvaluate.setVisibility(View.VISIBLE);
                break;
            case OrderDetailBean.STATE_REFUND_FAILED:
                mLlBtnContainer.setVisibility(View.VISIBLE);
                mTvRefundAppeal.setVisibility(View.VISIBLE);
                break;
            default:
                mLlBtnContainer.setVisibility(View.GONE);
        }
        //设置评论
        OrderDetailBean.EvaluationBean evaluation = mOrderDetailBean.getEvaluation();
        if (evaluation == null) {
            mLlEvaluateContainer.setVisibility(View.GONE);
        } else {
            mLlEvaluateContainer.setVisibility(View.VISIBLE);
            mTvEvaluationContent.setText(evaluation.getEvaluation());
            mRbOrderScore.setRating(evaluation.getStar());
            mTvOrderScore.setText(String.format("%d分", evaluation.getStar()));
            String imageUrl = evaluation.getImage();
            if (TextUtils.isEmpty(imageUrl)) {
                mIvEvaluateImage.setVisibility(View.GONE);
            } else {
                mIvEvaluateImage.setVisibility(View.VISIBLE);
                Glide.with(this).load(UrlConfig.buildImageUrl(imageUrl)).into(mIvEvaluateImage);
            }
            String replay = evaluation.getReplay();
            if (TextUtils.isEmpty(replay)) {
                mLlReplyContainer.setVisibility(View.GONE);
            } else {
                mLlReplyContainer.setVisibility(View.VISIBLE);
                mTvReplyContent.setText(replay);
            }
        }
    }

    @OnClick({R.id.tv_cancel_order, R.id.tv_pay_order, R.id.tv_show_qrcode, R.id.tv_complete, R.id.tv_refund, R.id.tv_evaluate, R.id.tv_refund_appeal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel_order: {
                showLoading("正在取消订单...");
                UpdateOrderParams params = new UpdateOrderParams(mOrderId, -1, UpdateOrderParams.OPERATION_CANCEL);
                HttpHelper.getInstance().updateFoodOrder(params, new NetworkCallback() {
                    @Override
                    public void onSuccess(Call call, String json) {
                        loadData();
                        loadSuccess("取消订单成功");
                    }

                    @Override
                    public void onFailure(Call call, String message) {
                        loadFailed("取消订单失败," + message);
                    }
                });
            }
            break;
            case R.id.tv_pay_order: {
                Intent intent = new Intent(ClubServiceOrderDetailActivity.this, PayOrderActivity.class);
                intent.putExtra(PayOrderActivity.EXTRA_ORDER_BEAN, mOrderDetailBean);
                startActivityForResult(intent, REQUEST_CODE_PAY);
            }
            break;
            case R.id.tv_show_qrcode: {
                Intent intent = new Intent(ClubServiceOrderDetailActivity.this, OrderQrcodeActivity.class);
                intent.putExtra(OrderQrcodeActivity.EXTRA_ORDER_ID, mOrderDetailBean.getId());
                startActivityForResult(intent, REQUEST_CODE_QRCODE);
            }
            break;
            case R.id.tv_complete: { //已服务点击完成订单
                showLoading("提交请求中...");
                UpdateOrderParams params = new UpdateOrderParams(mOrderId, null, UpdateOrderParams.OPERATION_COMPLETE);
                HttpHelper.getInstance().updateFoodOrder(params, new NetworkCallback() {
                    @Override
                    public void onSuccess(Call call, String json) {
                        loadSuccess("提交成功", false);
                        loadData();
                    }

                    @Override
                    public void onFailure(Call call, String message) {
                        loadFailed("提交失败，" + message);
                    }
                });
            }
            break;
            case R.id.tv_refund:
                showRefundDialog(false);
                break;
            case R.id.tv_refund_appeal:
                showRefundDialog(true);
                break;
            case R.id.tv_evaluate:
                Intent intent = new Intent(ClubServiceOrderDetailActivity.this, EvaluateOrderActivity.class);
                intent.putExtra(EvaluateOrderActivity.EXTRA_ORDER_BEAN, mOrderDetailBean);
                startActivityForResult(intent, REQUEST_CODE_EVALUATE);
                break;
        }
    }

    private void showRefundDialog(final boolean isAppeal) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_edittext, null);
        final EditText editText = inflate.findViewById(R.id.edittext);
        editText.setHint("请输入退款理由");
        mRefundDialog = new AlertDialog.Builder(this)
                .setTitle("申请退款")
                .setView(inflate)
                .setPositiveButton("提交", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRefundDialog.dismiss();
                        showLoading("提交请求中...");
                        String refundReason = editText.getEditableText().toString();
                        int opStatus;
                        if (isAppeal) {
                            opStatus = UpdateOrderWithReasonParams.OPERATION_REFUND;
                        } else {
                            opStatus = mOrderDetailBean.getStatus() == OrderDetailBean.STATE_PAYED ? UpdateOrderWithReasonParams.OPERATION_REFUND_PAYED : UpdateOrderWithReasonParams.OPERATION_REFUND_CLUB;
                        }
                        UpdateOrderWithReasonParams params = new UpdateOrderWithReasonParams(mOrderId, refundReason, opStatus);
                        HttpHelper.getInstance().updateOrderWithReason(params, new NetworkCallback() {
                            @Override
                            public void onSuccess(Call call, String json) {
                                loadSuccess("提交成功", false);
                                loadData();
                            }

                            @Override
                            public void onFailure(Call call, String message) {
                                loadFailed("提交失败，" + message);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        mRefundDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_EVALUATE:
                    loadData();
                    break;
                case REQUEST_CODE_PAY:
                    loadData();
                    break;
                case REQUEST_CODE_QRCODE:
                    loadData();
                    break;
            }
        }
    }
}
