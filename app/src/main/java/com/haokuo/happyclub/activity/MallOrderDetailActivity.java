package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
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
import com.haokuo.happyclub.util.utilscode.TimeUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.midtitlebar.MidTitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/12.
 */
public class MallOrderDetailActivity extends BaseActivity {
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
    @BindView(R.id.tv_evaluate)
    TextView mTvEvaluate;
    private long mOrderId;
    private OrderDetailBean mOrderDetailBean;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_mall_order_detail;
    }

    @Override
    protected void initData() {
        //        mIsAutoPay = getIntent().getBooleanExtra(EXTRA_AUTO_PAY, false);
        mOrderId = getIntent().getLongExtra(EXTRA_ORDER_ID, 0);
        //设置RecyclerView

    }

    @Override
    protected void loadData() {
        GetOrderDetailParams params = new GetOrderDetailParams(mOrderId, OrderDetailBean.ORDER_TYPE_MALL);
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
            default:
                mLlBtnContainer.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_cancel_order, R.id.tv_pay_order, R.id.tv_show_qrcode, R.id.tv_complete, R.id.tv_refund, R.id.tv_evaluate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel_order: {
                showLoading("正在取消订单...");
                UpdateOrderParams params = new UpdateOrderParams(mOrderId, -1, UpdateOrderParams.OPERATION_REFUND);
                HttpHelper.getInstance().updateFoodOrder(params, new NetworkCallback() {
                    @Override
                    public void onSuccess(Call call, String json) {
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
                Intent intent = new Intent(MallOrderDetailActivity.this, PayOrderActivity.class);
                intent.putExtra(PayOrderActivity.EXTRA_ORDER_BEAN, mOrderDetailBean);
                startActivityForResult(intent, 0);
            }
            break;
            case R.id.tv_show_qrcode: {
                Intent intent = new Intent(MallOrderDetailActivity.this, OrderQrcodeActivity.class);
                intent.putExtra(OrderQrcodeActivity.EXTRA_ORDER_ID, mOrderDetailBean.getId());
                startActivityForResult(intent, 0);
            }
            break;
            case R.id.tv_complete: { //已服务点击完成订单
                showLoading("提交请求中...");
                UpdateOrderParams params = new UpdateOrderParams(mOrderId, null, UpdateOrderParams.OPERATION_COMPLETE);
                HttpHelper.getInstance().updateFoodOrder(params, new NetworkCallback() {
                    @Override
                    public void onSuccess(Call call, String json) {
                        loadSuccess("提交成功");
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
                showLoading("提交请求中...");
                UpdateOrderParams params = new UpdateOrderParams(mOrderId, null, UpdateOrderParams.OPERATION_REFUND);
                HttpHelper.getInstance().updateFoodOrder(params, new NetworkCallback() {
                    @Override
                    public void onSuccess(Call call, String json) {
                        loadSuccess("提交成功");
                        loadData();
                    }

                    @Override
                    public void onFailure(Call call, String message) {
                        loadFailed("提交失败，" + message);
                    }
                });
                break;
            case R.id.tv_evaluate:
                Intent intent = new Intent(MallOrderDetailActivity.this, EvaluateOrderActivity.class);
                intent.putExtra(EvaluateOrderActivity.EXTRA_ORDER_BEAN, mOrderDetailBean);
                startActivityForResult(intent, REQUEST_CODE_EVALUATE);
                break;
        }
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
                    finish();
                    break;
                case REQUEST_CODE_QRCODE:
                    loadData();
                    break;
            }

            //            loadData();
        }
    }
}
