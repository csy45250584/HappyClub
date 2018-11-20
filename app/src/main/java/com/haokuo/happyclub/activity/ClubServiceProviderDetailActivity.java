package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.ClubServiceAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.ClubServiceBean;
import com.haokuo.happyclub.bean.ClubServiceProviderDetailBean;
import com.haokuo.happyclub.bean.FoodOrderBean;
import com.haokuo.happyclub.bean.OrderResultBean;
import com.haokuo.happyclub.bean.data.ServiceProviderDetailResultBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.UrlConfig;
import com.haokuo.happyclub.network.bean.GetServiceProviderDetailParams;
import com.haokuo.happyclub.util.GlideImageLoader;
import com.haokuo.happyclub.util.utilscode.PhoneUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.haokuo.midtitlebar.MidTitleBar;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/25.
 */
public class ClubServiceProviderDetailActivity extends BaseActivity {
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
    @BindView(R.id.rv_club_service_list)
    RecyclerView mRvClubServiceList;

    private long mProviderId;
    private ClubServiceProviderDetailBean mClubServiceDetailBean;
    private ClubServiceAdapter mClubServiceAdapter;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_club_service_provider_detail;
    }

    @Override
    protected void initData() {
        mProviderId = getIntent().getLongExtra(EXTRA_SERVICE_ID, -1);
        mBannerService.setImageLoader(new GlideImageLoader());
        mRvClubServiceList.setLayoutManager(new LinearLayoutManager(this));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvClubServiceList.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDivider,
                0, 0));
        mClubServiceAdapter = new ClubServiceAdapter(R.layout.item_club_service);
        mRvClubServiceList.setAdapter(mClubServiceAdapter);
    }

    @Override
    protected void loadData() {
        GetServiceProviderDetailParams params = new GetServiceProviderDetailParams(mProviderId);
        HttpHelper.getInstance().getServiceProviderDetail(params, new EntityCallback<ServiceProviderDetailResultBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("获取会所服务信息失败，" + message);
            }

            @Override
            public void onSuccess(Call call, ServiceProviderDetailResultBean result) {
                mClubServiceDetailBean = result.getData();
                loadServiceData(mClubServiceDetailBean);
            }
        });
    }

    private void loadServiceData(ClubServiceProviderDetailBean result) {
        ClubServiceProviderDetailBean.BusinessBean business = result.getBusiness();
        List<ClubServiceBean> serviceList = result.getServiceList();
        //banner
        String[] splits = business.getImage().split(",");
        ArrayList<String> bannerImages = new ArrayList<>();
        for (String split : splits) {
            bannerImages.add(UrlConfig.buildBaseImageUrl(split));
        }
        mBannerService.setImages(bannerImages);
        mBannerService.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mBannerService.start();
        //服务商信息
        mTvBusinessName.setText(business.getBusinessName());
        mTvBusinessIntroduction.setText(business.getIntroduction());
        mTvProviderName.setText(business.getManagerName());
        mTvProviderTel.setText(business.getManagerPhone());
        //设置服务列表
        mClubServiceAdapter.setNewData(serviceList);

        //服务内容
        //        Glide.with(this).load(UrlConfig.buildImageUrl(service.getService_pictureurl())).into(mIvServicePic);
        //        mTvClubServiceName.setText(service.getService_name());
        //        mTvServiceScore.setText(String.valueOf(service.getService_integral()));
        //        mTvClubServiceDescription.setText(service.getDescription());
    }

    @Override
    protected void initListener() {
        mClubServiceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ClubServiceBean item = mClubServiceAdapter.getItem(position);
                if (view.getId() == R.id.tv_exchange && item != null) {
                    showLoading("正在下单...");
                    //下单请求
                    FoodOrderBean foodOrderBean = new FoodOrderBean();
                    foodOrderBean.setIntegralSum(item.getService_integral());
                    ArrayList<FoodOrderBean.CartFoodBean> cartFoodBeans = new ArrayList<>();
                    cartFoodBeans.add(new FoodOrderBean.CartFoodBean(item.getId(), 1));
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
                                    Intent intent = new Intent(ClubServiceProviderDetailActivity.this, ClubServiceOrderDetailActivity.class);
                                    intent.putExtra(ClubServiceOrderDetailActivity.EXTRA_ORDER_ID, result.getOrderId());
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @OnClick({R.id.tv_provider_tel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_provider_tel:
                PhoneUtils.call(mTvProviderTel.getText().toString());
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
