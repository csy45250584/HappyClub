package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.ClubServiceDetailBean;
import com.haokuo.happyclub.bean.FoodOrderBean;
import com.haokuo.happyclub.bean.OrderResultBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.UrlConfig;
import com.haokuo.happyclub.network.bean.base.IdParams;
import com.haokuo.happyclub.util.GlideImageLoader;
import com.haokuo.happyclub.util.utilscode.PhoneUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.midtitlebar.MidTitleBar;
import com.rey.material.widget.Button;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/25.
 */
public class ClubServiceDetailActivity extends BaseActivity {
    public static final String EXTRA_SERVICE_ID = "com.haokuo.happyclub.extra.EXTRA_SERVICE_ID";
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.banner_service)
    Banner mBannerService;
    @BindView(R.id.tv_business_name)
    TextView mTvBusinessName;
    @BindView(R.id.tv_business_introduction)
    TextView mTvBusinessIntroduction;
    @BindView(R.id.tv_provider_name)
    TextView mTvProviderName;
    @BindView(R.id.tv_provider_tel)
    TextView mTvProviderTel;
    @BindView(R.id.iv_service_pic)
    ImageView mIvServicePic;
    @BindView(R.id.tv_club_service_name)
    TextView mTvClubServiceName;
    @BindView(R.id.tv_service_score)
    TextView mTvServiceScore;
    @BindView(R.id.tv_club_service_description)
    TextView mTvClubServiceDescription;
    @BindView(R.id.btn_exchange_service)
    Button mBtnExchangeService;
    private long mServiceId;
    private ClubServiceDetailBean mClubServiceDetailBean;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_club_service_detail;
    }

    @Override
    protected void initData() {
        mServiceId = getIntent().getLongExtra(EXTRA_SERVICE_ID, -1);
        mBannerService.setImageLoader(new GlideImageLoader());
    }

    @Override
    protected void loadData() {
        IdParams idParams = new IdParams(mServiceId);
        HttpHelper.getInstance().getServiceById(idParams, new EntityCallback<ClubServiceDetailBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("获取会所服务信息失败，" + message);
            }

            @Override
            public void onSuccess(Call call, ClubServiceDetailBean result) {
                mClubServiceDetailBean = result;
                loadServiceData(mClubServiceDetailBean);
            }
        });
    }

    private void loadServiceData(ClubServiceDetailBean result) {
        ClubServiceDetailBean.BusinessBean business = result.getBusiness();
        ClubServiceDetailBean.ServiceBean service = result.getService();
        //banner
        String[] splits = business.getImage().split(",");
        ArrayList<String> bannerImages = new ArrayList<>();
        for (String split : splits) {
            bannerImages.add(UrlConfig.buildBaseImageUrl(split));
        }
        mBannerService.setImages(bannerImages);
        mBannerService.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mBannerService.start();
        mTvBusinessName.setText(business.getBusinessName());
        mTvBusinessIntroduction.setText(business.getIntroduction());
        mTvProviderName.setText(business.getManagerName());
        mTvProviderTel.setText(business.getManagerPhone());
        Glide.with(this).load(UrlConfig.buildImageUrl(service.getService_pictureurl())).into(mIvServicePic);
        mTvClubServiceName.setText(service.getService_name());
        mTvServiceScore.setText(String.valueOf(service.getService_integral()));
        mTvClubServiceDescription.setText(service.getDescription());
    }

    @OnClick({R.id.tv_provider_tel, R.id.btn_exchange_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_provider_tel:
                PhoneUtils.call(mTvProviderTel.getText().toString());
                break;
            case R.id.btn_exchange_service:
                showLoading("正在下单...");
                //下单请求
                FoodOrderBean foodOrderBean = new FoodOrderBean();
                foodOrderBean.setIntegralSum(mClubServiceDetailBean.getService().getService_integral());
                ArrayList<FoodOrderBean.CartFoodBean> cartFoodBeans = new ArrayList<>();
                cartFoodBeans.add(new FoodOrderBean.CartFoodBean(mClubServiceDetailBean.getService().getId(), 1));
                foodOrderBean.setOrderItems(cartFoodBeans);
                HttpHelper.getInstance().insertServiceOrder(foodOrderBean, new EntityCallback<OrderResultBean>() {
                    @Override
                    public void onFailure(Call call, String message) {
                        loadFailed("下单失败，" + message);
                    }

                    @Override
                    public void onSuccess(Call call, final OrderResultBean result) {
                        loadSuccess("下单成功", new LoadingDialog.OnFinishListener() {
                            @Override
                            public void onFinish() {
                                Intent intent = new Intent(ClubServiceDetailActivity.this, ClubServiceOrderDetailActivity.class);
                                intent.putExtra(ClubServiceOrderDetailActivity.EXTRA_ORDER_ID, result.getOrderId());
                                startActivity(intent);
                            }
                        });
                    }
                });
                break;
        }
    }

    //    @OnClick(R.id.btn_exchange_service)
    //    public void onViewClicked() {
    //        showLoading("正在下单...");
    //        //下单请求
    //        FoodOrderBean foodOrderBean = new FoodOrderBean();
    //        foodOrderBean.setIntegralSum(mClubServiceDetailBean.getService().getService_integral());
    //        ArrayList<FoodOrderBean.CartFoodBean> cartFoodBeans = new ArrayList<>();
    //        cartFoodBeans.add(new FoodOrderBean.CartFoodBean(mClubServiceDetailBean.getService().getId(), 1));
    //        foodOrderBean.setOrderItems(cartFoodBeans);
    //        HttpHelper.getInstance().insertServiceOrder(foodOrderBean, new EntityCallback<OrderResultBean>() {
    //            @Override
    //            public void onFailure(Call call, String message) {
    //                loadFailed("下单失败，" + message);
    //            }
    //
    //            @Override
    //            public void onSuccess(Call call, final OrderResultBean result) {
    //                loadSuccess("下单成功", new LoadingDialog.OnFinishListener() {
    //                    @Override
    //                    public void onFinish() {
    //                        Intent intent = new Intent(ClubServiceDetailActivity.this, ClubServiceOrderDetailActivity.class);
    //                        intent.putExtra(ClubServiceOrderDetailActivity.EXTRA_ORDER_ID, result.getOrderId());
    //                        startActivity(intent);
    //                    }
    //                });
    //            }
    //        });
    //    }
}
