package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.OrderDetailFoodAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.AddressResultBean;
import com.haokuo.happyclub.bean.OrderDetailBean;
import com.haokuo.happyclub.bean.data.OrderDetailEntityBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.GetOrderDetailParams;
import com.haokuo.happyclub.network.bean.UpdateOrderParams;
import com.haokuo.happyclub.util.ResUtils;
import com.haokuo.happyclub.util.utilscode.DecimalUtils;
import com.haokuo.happyclub.util.utilscode.TimeUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.RecyclerViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/12.
 */
public class FoodOrderDetailActivity extends BaseActivity {
    public static final String EXTRA_AUTO_PAY = "com.haokuo.happyclub.extra.EXTRA_AUTO_PAY";
    public static final String EXTRA_ORDER_ID = "com.haokuo.happyclub.extra.EXTRA_ORDER_ID";
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_order_state)
    TextView mTvOrderState;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_tel)
    TextView mTvTel;
    @BindView(R.id.tv_cancel_order)
    TextView mTvCancelOrder;
    @BindView(R.id.tv_pay_order)
    TextView mTvPayOrder;
    @BindView(R.id.rv_food_list)
    RecyclerView mRvFoodList;
    @BindView(R.id.tv_amount_price)
    TextView mTvAmountPrice;
    @BindView(R.id.tv_amount_score)
    TextView mTvAmountScore;
    @BindView(R.id.tv_order_number)
    TextView mTvOrderNumber;
    @BindView(R.id.tv_order_time)
    TextView mTvOrderTime;
    @BindView(R.id.tv_order_note)
    TextView mTvOrderNote;
    @BindView(R.id.tv_delivery_type)
    TextView mTvDeliveryType;
    @BindView(R.id.tv_scheduled_time)
    TextView mTvScheduledTime;
    @BindView(R.id.ll_address_container)
    LinearLayout mLlAddressContainer;
    @BindView(R.id.ll_btn_container)
    LinearLayout mLlBtnContainer;
    private boolean mIsAutoPay;
    private long mOrderId;
    private OrderDetailBean mOrderDetailBean;
    private OrderDetailFoodAdapter mOrderFoodAdapter;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_food_order_detail;
    }

    @Override
    protected void initData() {
        mIsAutoPay = getIntent().getBooleanExtra(EXTRA_AUTO_PAY, false);
        mOrderId = getIntent().getLongExtra(EXTRA_ORDER_ID, 0);
        //设置RecyclerView
        mRvFoodList.setLayoutManager(new LinearLayoutManager(this));
        mRvFoodList.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, ResUtils.getDimens(R.dimen.dp_1), R.color.colorDivider));
        mOrderFoodAdapter = new OrderDetailFoodAdapter(R.layout.item_order_food);
        mRvFoodList.setAdapter(mOrderFoodAdapter);
        RecyclerView.ItemAnimator itemAnimator = mRvFoodList.getItemAnimator();
        if (itemAnimator != null) {
            itemAnimator.setChangeDuration(0);
        }
    }

    @Override
    protected void loadData() {
        GetOrderDetailParams params = new GetOrderDetailParams(mOrderId, OrderDetailBean.ORDER_TYPE_CANTEEN);
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
        AddressResultBean address = orderDetailBean.getAddress();
        mTvOrderState.setText(orderDetailBean.getStateString());
        if (address != null) {
            mTvDeliveryType.setText("外卖配送");
            mTvScheduledTime.setVisibility(View.GONE);
            mLlAddressContainer.setVisibility(View.VISIBLE);
            mTvAddress.setText(address.getArea() + " " + address.getAddress());
            mTvName.setText(address.getName());
            mTvTel.setText(address.getTelphone());
        } else {
            mTvDeliveryType.setText("食堂用餐");
            mTvScheduledTime.setVisibility(View.VISIBLE);
            mLlAddressContainer.setVisibility(View.GONE);
            mTvScheduledTime.setText(TimeUtils.sqlTime2String(orderDetailBean.getScheduledTime()));
        }
        mOrderFoodAdapter.setNewData(orderDetailBean.getOrderItems());
        mTvAmountScore.setText(String.valueOf(orderDetailBean.getIntegralSum()));
        mTvAmountPrice.setText(DecimalUtils.getMoneyString(orderDetailBean.getMoneySum()));
        mTvOrderNumber.setText(orderDetailBean.getOrderNo());
        mTvOrderTime.setText(TimeUtils.sqlTime2String(orderDetailBean.getCreateDate()));
        mTvOrderNote.setText(orderDetailBean.getRemarks());
        List<OrderDetailBean.OrderItem> orderItems = orderDetailBean.getOrderItems();
        mOrderFoodAdapter.setNewData(orderItems);
        if (orderDetailBean.getStatus() == OrderDetailBean.STATE_WAIT_FOR_HANDLE) {
            mLlBtnContainer.setVisibility(View.VISIBLE);
        } else {
            mLlBtnContainer.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_cancel_order, R.id.tv_pay_order})
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
            case R.id.tv_pay_order:
                Intent intent = new Intent(FoodOrderDetailActivity.this, PayOrderActivity.class);
                intent.putExtra(PayOrderActivity.EXTRA_ORDER_BEAN, mOrderDetailBean);
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
//            loadData();
        }
    }
}
