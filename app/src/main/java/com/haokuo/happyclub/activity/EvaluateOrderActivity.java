package com.haokuo.happyclub.activity;

import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseTakePhotoActivity;
import com.haokuo.happyclub.bean.OrderDetailBean;
import com.haokuo.happyclub.bean.UploadPicResultBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.EvaluateOrderParams;
import com.haokuo.happyclub.network.bean.UploadFileParams;
import com.haokuo.happyclub.util.DirUtil;
import com.haokuo.happyclub.util.MySpUtil;
import com.haokuo.happyclub.util.utilscode.TimeUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.midtitlebar.MidTitleBar;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/22.
 */
public class EvaluateOrderActivity extends BaseTakePhotoActivity {
    public static final String EXTRA_ORDER_BEAN = "com.haokuo.happyclub.extra.EXTRA_ORDER_BEAN";
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.tv_order_number)
    TextView mTvOrderNumber;
    @BindView(R.id.tv_order_time)
    TextView mTvOrderTime;
    @BindView(R.id.rb_order_score)
    MaterialRatingBar mRbOrderScore;
    @BindView(R.id.tv_order_score)
    TextView mTvOrderScore;
    @BindView(R.id.et_evaluation_content)
    EditText mEtEvaluationContent;
    @BindView(R.id.tv_order_item)
    TextView mTvOrderItem;
    @BindView(R.id.iv_evaluate_image)
    ImageView mIvEvaluateImage;
    @BindView(R.id.iv_delete_image)
    ImageView mIvDeleteImage;
    private OrderDetailBean mOrderDetailBean;
    private String mEvaluateImagePath;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_evaluate_order;
    }

    @Override
    protected void initData() {
        mOrderDetailBean = (OrderDetailBean) getIntent().getSerializableExtra(EXTRA_ORDER_BEAN);
        mTvOrderNumber.setText(mOrderDetailBean.getOrderNo());
        List<OrderDetailBean.OrderItem> orderItems = mOrderDetailBean.getOrderItems();
        if (orderItems.size() == 0) {
            mTvOrderItem.setText("无商品");
        } else if (orderItems.size() > 1) {
            mTvOrderItem.setText(orderItems.get(0).getProName() + "等" + orderItems.size() + "个商品");
        } else {
            mTvOrderItem.setText(orderItems.get(0).getProName());
        }
        mTvOrderTime.setText(TimeUtils.sqlTime2String(mOrderDetailBean.getCreateDate(), TimeUtils.DEFAULT_FORMAT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complete, menu);
        return true;
    }

    @Override
    protected void initListener() {
        mRbOrderScore.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                mTvOrderScore.setText(String.format("%d分", (int) rating));
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
                    EvaluateOrderParams params = new EvaluateOrderParams(mOrderDetailBean.getId(), content, mEvaluateImagePath, (int) mRbOrderScore.getRating(), EvaluateOrderParams.OPERATION_EVALUATE);
                    showLoading("提交中...");
                    HttpHelper.getInstance().evaluateOrder(params, new NetworkCallback() {
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

    @OnClick({R.id.iv_evaluate_image, R.id.iv_delete_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_evaluate_image:
                //选择图片
                showBottomSheet();
                break;
            case R.id.iv_delete_image:
                //选择图片
                mEvaluateImagePath = null;
                mIvEvaluateImage.setImageResource(R.drawable.tianjia_add);
                mIvDeleteImage.setVisibility(View.GONE);
                break;
        }
    }

    private void showBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        View v = LayoutInflater.from(this).inflate(R.layout.view_take_photo, null);
        Button btnGallery = v.findViewById(R.id.btn_gallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outFile = new File(DirUtil.getImageDir(), MySpUtil.getInstance().getUserId() + "_" + System.currentTimeMillis() + ".jpg");
                Uri uri = Uri.fromFile(outFile);
                CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
                CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(1024 * 1024).setMaxPixel(1024).create();
                getTakePhoto().onEnableCompress(compressConfig, true);
                getTakePhoto().onPickFromGalleryWithCrop(uri, cropOptions);
                bottomSheetDialog.dismiss();
            }
        });
        Button btnCapture = v.findViewById(R.id.btn_capture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outFile = new File(DirUtil.getImageDir(), MySpUtil.getInstance().getUserId() + "_" + System.currentTimeMillis() + ".jpg");
                Uri uri = Uri.fromFile(outFile);
                CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
                CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(1024 * 1024).setMaxPixel(1024).create();
                getTakePhoto().onEnableCompress(compressConfig, true);
                getTakePhoto().onPickFromCaptureWithCrop(uri, cropOptions);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.contentView(v)
                .show();
    }

    @Override
    public void takeSuccess(TResult result) {
        final String reportImagePath = result.getImage().getCompressPath();
        //上传图片
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                showLoading("上传图片中...");
                ArrayList<File> files = new ArrayList<>();
                files.add(new File(reportImagePath));
                UploadFileParams params = new UploadFileParams(files, UploadFileParams.FILE_TYPE_IMAGE);
                HttpHelper.getInstance().uploadPic(params, this, new EntityCallback<UploadPicResultBean>() {
                    @Override
                    public void onFailure(Call call, String message) {
                        loadFailed("上传失败," + message);
                    }

                    @Override
                    public void onSuccess(Call call, UploadPicResultBean result) {
                        mEvaluateImagePath = result.getSrc();
                        Glide.with(EvaluateOrderActivity.this).load(mEvaluateImagePath).into(mIvEvaluateImage);
                        mIvDeleteImage.setVisibility(View.VISIBLE);
                        loadSuccess("上传成功", false);
                    }
                });
            }
        });
    }
}
