package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.OrderFoodAdapter;
import com.haokuo.happyclub.adapter.TablewareAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.AddressResultBean;
import com.haokuo.happyclub.bean.CartFoodBean;
import com.haokuo.happyclub.bean.FoodOrderBean;
import com.haokuo.happyclub.bean.OrderFoodResultBean;
import com.haokuo.happyclub.eventbus.OrderFoodEvent;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.util.ResUtils;
import com.haokuo.happyclub.util.utilscode.TimeUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.Button;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/25.
 */
public class FoodOrderActivity extends BaseActivity {
    private static final String TAG = "FoodOrderActivity";
    private static final int REQUEST_CODE_NOTE = 1;
    private static final int REQUEST_CODE_ADDRESS = 2;
    public static final String EXTRA_CHOOSE_ADDRESS = "com.haokuo.happyclub.extra.EXTRA_CHOOSE_ADDRESS";
    @BindView(R.id.rv_food_list)
    RecyclerView mRvFoodList;
    @BindView(R.id.rb_take_out)
    RadioButton mRbTakeOut;
    @BindView(R.id.rb_eat_in)
    RadioButton mRbEatIn;
    @BindView(R.id.rg_delivery_method)
    RadioGroup mRgDeliveryMethod;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_tel)
    TextView mTvTel;
    @BindView(R.id.tv_amount_price)
    TextView mTvAmountPrice;
    @BindView(R.id.tv_amount_score)
    TextView mTvAmountScore;
    @BindView(R.id.tv_tableware)
    TextView mTvTableware;
    @BindView(R.id.fl_tableware)
    FrameLayout mFlTableware;
    @BindView(R.id.tv_note)
    TextView mTvNote;
    @BindView(R.id.fl_note)
    FrameLayout mFlNote;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_total_score)
    TextView mTvTotalScore;
    @BindView(R.id.btn_submit_order)
    Button mBtnSubmitOrder;
    @BindView(R.id.tv_select_address_tips)
    TextView mTvSelectAddressTips;
    @BindView(R.id.rl_selected_address)
    RelativeLayout mRlSelectedAddress;
    @BindView(R.id.rl_address_container)
    RelativeLayout mRlAddressContainer;
    @BindView(R.id.tv_select_time)
    TextView mTvSelectTime;
    @BindView(R.id.rl_time_container)
    RelativeLayout mRlTimeContainer;
    private OrderFoodAdapter mOrderFoodAdapter;
    private ArrayList<Integer> mTableWareList;
    private int mTableWareCount;
    private String mNote;
    private BigDecimal mTotalPriceDecimal;
    private AddressResultBean mAddressBean;
    private String mEatTime;
    private int mTotalScore;
    private ArrayList<FoodOrderBean.CartFoodBean> mCartFoodBeans;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_food_order;
    }

    @Override
    protected void initData() {
        //初始化餐具选择
        mTableWareList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            mTableWareList.add(i);
        }
        mTableWareList.add(100);
        mTableWareCount = 0;
        //从数据库中读取购物车信息
        List<CartFoodBean> cartFoodList = LitePal.findAll(CartFoodBean.class);
        mTotalPriceDecimal = BigDecimal.valueOf(0);
        mTotalScore = 0;
        mCartFoodBeans = new ArrayList<>();
        for (CartFoodBean cartFoodBean : cartFoodList) {
            mTotalPriceDecimal = mTotalPriceDecimal.add(new BigDecimal(cartFoodBean.getPrice()));
            mTotalScore += cartFoodBean.getScore();
            mCartFoodBeans.add(new FoodOrderBean.CartFoodBean(cartFoodBean.getFoodId(), cartFoodBean.getCount()));
        }
        DecimalFormat decimalFormat = new DecimalFormat("¥0.00");
        String totalPrice = decimalFormat.format(mTotalPriceDecimal);
        mTvAmountPrice.setText(totalPrice);
        mTvAmountScore.setText(String.valueOf(mTotalScore));
        //设置RecyclerView
        mRvFoodList.setLayoutManager(new LinearLayoutManager(this));
        mRvFoodList.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, ResUtils.getDimens(R.dimen.dp_1), R.color.colorDivider));
        mOrderFoodAdapter = new OrderFoodAdapter(R.layout.item_order_food);
        mRvFoodList.setAdapter(mOrderFoodAdapter);
        RecyclerView.ItemAnimator itemAnimator = mRvFoodList.getItemAnimator();
        if (itemAnimator != null) {
            itemAnimator.setChangeDuration(0);
        }
        mOrderFoodAdapter.setNewData(cartFoodList);

        //设置底部总价合计
        mTvTotalPrice.setText(totalPrice);
        mTvTotalScore.setText(String.valueOf(mTotalScore));
        //        SpannableString totalPriceString = new SpannableString("合计:" + totalPrice + "元");
        //        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
        //        totalPriceString.setSpan(colorSpan, 3, totalPriceString.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //        mTvTotalPrice.setText(totalPriceString);
    }

    @OnClick({R.id.fl_tableware, R.id.fl_note, R.id.btn_submit_order, R.id.rl_address_container, R.id.rl_time_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_tableware:
                showTablewareDialog();
                break;
            case R.id.fl_note: {
                Intent intent = new Intent(FoodOrderActivity.this, FoodOrderNoteActivity.class);
                if (!TextUtils.isEmpty(mNote)) {
                    intent.putExtra(FoodOrderNoteActivity.EXTRA_NOTE, mNote);
                }
                startActivityForResult(intent, REQUEST_CODE_NOTE);
            }
            break;
            case R.id.btn_submit_order: {
                //检查数据
                int checkedRadioButtonId = mRgDeliveryMethod.getCheckedRadioButtonId();
                if (checkedRadioButtonId == R.id.rb_take_out && mAddressBean == null) {
                    ToastUtils.showShort("请选择配送地址");
                    return;
                }

                if (checkedRadioButtonId == R.id.rb_eat_in && TextUtils.isEmpty(mEatTime)) {
                    ToastUtils.showShort("请选择用餐时间");
                    return;
                }
                //下单请求
                FoodOrderBean foodOrderBean = new FoodOrderBean();
                foodOrderBean.setMoneySum(mTotalPriceDecimal.doubleValue());
                foodOrderBean.setRemarks(mNote);
                foodOrderBean.setState(mTableWareCount);
                foodOrderBean.setIntegralSum(mTotalScore);
                if (mRgDeliveryMethod.getCheckedRadioButtonId() == R.id.rb_take_out) {
                    foodOrderBean.setAddressId(mAddressBean.getId());
                } else {
                    foodOrderBean.setScheduledTime(mTvSelectTime.getText().toString());
                }
                foodOrderBean.setOrderItems(mCartFoodBeans);
                showLoading("提交订单中...");

                HttpHelper.getInstance().insertFoodOrder(foodOrderBean, new EntityCallback<OrderFoodResultBean>() {
                    @Override
                    public void onFailure(Call call, String message) {
                        loadFailed("下单失败，" + message);
                    }

                    @Override
                    public void onSuccess(Call call, final OrderFoodResultBean result) {
                        EventBus.getDefault().post(new OrderFoodEvent());
                        loadSuccess("下单成功", new LoadingDialog.OnFinishListener() {
                            @Override
                            public void onFinish() {
                                Intent intent = new Intent(FoodOrderActivity.this, FoodOrderDetailActivity.class);
                                intent.putExtra(FoodOrderDetailActivity.EXTRA_AUTO_PAY, true);
                                intent.putExtra(FoodOrderDetailActivity.EXTRA_ORDER_ID, result.getOrderId());
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
            }
            break;
            case R.id.rl_address_container: {
                Intent intent = new Intent(FoodOrderActivity.this, DeliverAddressActivity.class);
                intent.putExtra(EXTRA_CHOOSE_ADDRESS, 1);
                startActivityForResult(intent, REQUEST_CODE_ADDRESS);
            }
            break;
            case R.id.rl_time_container: {
                //食堂吃饭时间选择
                showTimeDialog();
            }
            break;
        }
    }

    private void showTimeDialog() {
        TimePickerDialog.Builder builder = new TimePickerDialog.Builder() {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                TimePickerDialog dialog = (TimePickerDialog) fragment.getDialog();
                mEatTime = dialog.getFormattedTime(TimeUtils.DEFAULT_FORMAT);
                mTvSelectTime.setText(mEatTime);
                mTvSelectTime.setTextColor(ResUtils.getColor(R.color.colorWhiteText1));
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };
        builder.positiveAction("确定")
                .negativeAction("取消");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    @Override
    protected void initListener() {
        mRgDeliveryMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_take_out:
                        mRlTimeContainer.setVisibility(View.GONE);
                        mRlAddressContainer.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_eat_in:
                        mRlAddressContainer.setVisibility(View.GONE);
                        mRlTimeContainer.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE_NOTE: {
                    mNote = data.getStringExtra(FoodOrderNoteActivity.EXTRA_NOTE);
                    mTvNote.setText(mNote);
                    mTvNote.setTextColor(ResUtils.getColor(R.color.colorText2));
                }
                break;
                case REQUEST_CODE_ADDRESS: {
                    mAddressBean = (AddressResultBean) data.getSerializableExtra(DeliverAddressActivity.EXTRA_ADDRESS);
                    mTvSelectAddressTips.setVisibility(View.GONE);
                    mRlSelectedAddress.setVisibility(View.VISIBLE);
                    mTvAddress.setText(String.format("%s %s", mAddressBean.getArea(), mAddressBean.getAddress()));
                    mTvName.setText(mAddressBean.getName());
                    mTvTel.setText(mAddressBean.getTelphone());
                }
                break;
            }
        }
    }

    private void showTablewareDialog() {
        final BottomSheetDialog tablewareDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_select_tableware, null);
        //设置RecyclerView
        RecyclerView rvTableware = contentView.findViewById(R.id.rv_tableware);
        rvTableware.setLayoutManager(new LinearLayoutManager(this));
        final TablewareAdapter tablewareAdapter = new TablewareAdapter(R.layout.item_tableware, mTableWareCount);
        rvTableware.setAdapter(tablewareAdapter);
        tablewareAdapter.setNewData(mTableWareList);
        tablewareAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Integer item = tablewareAdapter.getItem(position);
                if (item != null) {
                    mTableWareCount = item;
                    mTvTableware.setTextColor(ResUtils.getColor(R.color.colorText2));
                    mTvTableware.setText(tablewareAdapter.getText(item));
                } else {
                    Log.i(TAG, "onItemClick: " + "餐具返回为空");
                }
                tablewareDialog.dismiss();
            }
        });
        //设置取消按钮
        Button btnTablewareCancel = contentView.findViewById(R.id.btn_tableware_cancel);
        btnTablewareCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tablewareDialog.dismiss();
            }
        });
        tablewareDialog.contentView(contentView);
        tablewareDialog.show();
    }
}
