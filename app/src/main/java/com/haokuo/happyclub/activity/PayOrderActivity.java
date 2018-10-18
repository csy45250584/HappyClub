package com.haokuo.happyclub.activity;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.OrderDetailBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.UpdateOrderParams;
import com.haokuo.happyclub.util.utilscode.DecimalUtils;
import com.haokuo.midtitlebar.MidTitleBar;
import com.rey.material.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/15.
 */
public class PayOrderActivity extends BaseActivity {
    public static final String EXTRA_ORDER_BEAN = "com.haokuo.happyclub.extra.EXTRA_ORDER_BEAN";
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.tv_order_number)
    TextView mTvOrderNumber;
    @BindView(R.id.tv_order_price)
    TextView mTvOrderPrice;
    @BindView(R.id.tv_order_score)
    TextView mTvOrderScore;
    @BindView(R.id.rb_wechat_pay)
    RadioButton mRbWechatPay;
    @BindView(R.id.rb_alipay_pay)
    RadioButton mRbAlipayPay;
    @BindView(R.id.rb_union_pay)
    RadioButton mRbUnionPay;
    @BindView(R.id.rb_score_pay)
    RadioButton mRbScorePay;
    @BindView(R.id.btn_confirm_pay)
    Button mBtnConfirmPay;
    @BindView(R.id.rg_pay_type)
    RadioGroup mRgPayType;
    @BindView(R.id.tv_slash)
    TextView mTvSlash;
    private OrderDetailBean mOrderDetailBean;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_pay_order;
    }

    @Override
    protected void initData() {
        mOrderDetailBean = (OrderDetailBean) getIntent().getSerializableExtra(EXTRA_ORDER_BEAN);
        if (mOrderDetailBean.getOrderType() != OrderDetailBean.ORDER_TYPE_CANTEEN) {
            mRbWechatPay.setVisibility(View.GONE);
            mRbAlipayPay.setVisibility(View.GONE);
            mRbUnionPay.setVisibility(View.GONE);
            mTvOrderPrice.setVisibility(View.GONE);
            mTvSlash.setVisibility(View.GONE);
        }
        mTvOrderNumber.setText(String.format("订单编号：%s", mOrderDetailBean.getOrderNo()));
        mTvOrderPrice.setText(DecimalUtils.getMoneyString(mOrderDetailBean.getMoneySum()));
        mTvOrderScore.setText(String.valueOf(mOrderDetailBean.getIntegralSum()));
    }

    @Override
    protected void initListener() {
        mRgPayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_score_pay) {
                    mBtnConfirmPay.setText(String.format("确认支付%d积分", mOrderDetailBean.getIntegralSum()));
                } else {
                    mBtnConfirmPay.setText(String.format("确认支付%s", DecimalUtils.getMoneyString(mOrderDetailBean.getMoneySum())));
                }
            }
        });
    }

    @Override
    protected void loadData() {
        mRgPayType.check(R.id.rb_score_pay);
    }

    @OnClick(R.id.btn_confirm_pay)
    public void onViewClicked() {
        showLoading("订单支付中...");
        switch (mRgPayType.getCheckedRadioButtonId()) {
            case R.id.rb_score_pay:
                //支付订单
                UpdateOrderParams params = new UpdateOrderParams(mOrderDetailBean.getId(), OrderDetailBean.PAY_TYPE_SCORE, UpdateOrderParams.OPERATION_PAY);
                HttpHelper.getInstance().updateFoodOrder(params, new NetworkCallback() {
                    @Override
                    public void onSuccess(Call call, String json) {
                        setResult(RESULT_OK);
                        loadSuccess("支付成功");
                    }

                    @Override
                    public void onFailure(Call call, String message) {
                        loadFailed("支付失败，" + message);
                    }
                });
                break;
            default:
                loadFailed("暂未开放的支付方式");
        }
    }
}
