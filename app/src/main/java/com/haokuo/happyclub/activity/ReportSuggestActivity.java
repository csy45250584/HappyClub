package com.haokuo.happyclub.activity;

import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseTakePhotoActivity;
import com.haokuo.happyclub.bean.SuggestBean;
import com.haokuo.happyclub.bean.UploadPicResultBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.UploadFileParams;
import com.haokuo.happyclub.util.DirUtil;
import com.haokuo.happyclub.util.MySpUtil;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.TitleEditorView;
import com.haokuo.midtitlebar.MidTitleBar;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/8.
 */
public class ReportSuggestActivity extends BaseTakePhotoActivity {

    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.et_suggest_name)
    TitleEditorView mEtSuggestName;
    @BindView(R.id.et_suggest_tel)
    TitleEditorView mEtSuggestTel;
    @BindView(R.id.et_suggest_content)
    EditText mEtSuggestContent;
    @BindView(R.id.iv_suggest_image)
    ImageView mIvSuggestImage;
    private String mReportImagePath;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_report_suggest;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complete, menu);
        return true;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        mMidTitleBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.item_complete) {
                    //点击完成
                    //检查数据
                    String name = mEtSuggestName.getText();
                    String tel = mEtSuggestTel.getText();
                    String content = mEtSuggestContent.getEditableText().toString();
                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(tel) ||
                            TextUtils.isEmpty(content) || TextUtils.isEmpty(mReportImagePath)) {
                        ToastUtils.showShort("请填写完整信息");
                        return true;
                    }
                    //上传数据
                    SuggestBean suggestBean = new SuggestBean();
                    suggestBean.setUserId(MySpUtil.getInstance().getUserId());
                    suggestBean.setUserName(name);
                    suggestBean.setReportContent(content);
                    suggestBean.setPictureurl(mReportImagePath);
                    showLoading("提交投诉中...");
                    HttpHelper.getInstance().reportSuggest(suggestBean, new NetworkCallback() {
                        @Override
                        public void onSuccess(Call call, String json) {
                            setResult(RESULT_OK);
                            loadSuccess("提交成功");
                        }

                        @Override
                        public void onFailure(Call call, String message) {
                            loadFailed("提交失败," + message);
                        }
                    });
                }
                return true;
            }
        });
    }

    @OnClick({R.id.iv_suggest_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_suggest_image:
                //选择图片
                showBottomSheet();
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
                HttpHelper.getInstance().uploadOneFile(params, this, new EntityCallback<UploadPicResultBean>() {
                    @Override
                    public void onFailure(Call call, String message) {
                        loadFailed("上传失败," + message);
                    }

                    @Override
                    public void onSuccess(Call call, UploadPicResultBean result) {
                        mReportImagePath = result.getSrc();
                        Glide.with(ReportSuggestActivity.this).load(mReportImagePath).into(mIvSuggestImage);
                        loadSuccess("上传成功", false);
                    }
                });
            }
        });
    }
}
