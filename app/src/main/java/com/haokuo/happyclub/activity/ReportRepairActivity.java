package com.haokuo.happyclub.activity;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
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
import com.haokuo.happyclub.bean.RepairBean;
import com.haokuo.happyclub.bean.UploadPicResultBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.UploadFileParams;
import com.haokuo.happyclub.util.DirUtil;
import com.haokuo.happyclub.util.MySpUtil;
import com.haokuo.happyclub.util.ResUtils;
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
public class ReportRepairActivity extends BaseTakePhotoActivity {
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.et_repair_name)
    TitleEditorView mEtRepairName;
    @BindView(R.id.et_repair_tel)
    TitleEditorView mEtRepairTel;
    @BindView(R.id.et_repair_address)
    TitleEditorView mEtRepairAddress;
    @BindView(R.id.et_repair_type)
    TitleEditorView mEtRepairType;
    @BindView(R.id.et_repair_content)
    EditText mEtRepairContent;
    @BindView(R.id.iv_repair_image)
    ImageView mIvRepairImage;
    private int mReportType;
    private AlertDialog mTypeDialog;
    private String mReportImagePath;
    private String[] mReportTypeArray;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_report_repair;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complete, menu);
        return true;
    }

    @Override
    protected void initData() {
        mReportType = -1;
        mReportTypeArray = ResUtils.getStringArray(R.array.report_repair_type);
    }

    @Override
    protected void initListener() {
        mMidTitleBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.item_complete) {
                    //点击完成
                    //检查数据
                    String name = mEtRepairName.getText();
                    String tel = mEtRepairTel.getText();
                    String address = mEtRepairAddress.getText();
                    String content = mEtRepairContent.getEditableText().toString();
                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(tel) || TextUtils.isEmpty(address) ||
                            TextUtils.isEmpty(content) || mReportType == -1 || TextUtils.isEmpty(mReportImagePath)) {
                        ToastUtils.showShort("请填写完整信息");
                        return true;
                    }
                    //上传数据
                    RepairBean repairBean = new RepairBean();
                    repairBean.setUserId(MySpUtil.getInstance().getUserId());
                    repairBean.setUserName(name);
                    repairBean.setAddress(address);
                    repairBean.setReportContent(content);
                    repairBean.setRepairType(mReportType + 1);
                    repairBean.setRepairTypeName(mReportTypeArray[mReportType]);
                    repairBean.setPictureurl(mReportImagePath);
                    showLoading("提交报修中...");
                    HttpHelper.getInstance().reportRepair(repairBean, new NetworkCallback() {
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

    @OnClick({R.id.et_repair_type, R.id.iv_repair_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_repair_type:
                showSelectTypeDialog();
                break;
            case R.id.iv_repair_image:
                //选择图片
                showBottomSheet();
                break;
        }
    }

    private void showSelectTypeDialog() {
        if (mTypeDialog == null) {
            mTypeDialog = new AlertDialog.Builder(this)
                    .setTitle("请选择报修类型")
                    .setSingleChoiceItems(mReportTypeArray, mReportType, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mReportType = which;
                            mEtRepairType.setText(mReportTypeArray[which]);
                            mTypeDialog.dismiss();
                        }
                    })
                    .create();
        }
        mTypeDialog.show();
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
                        mReportImagePath = result.getSrc();
                        Glide.with(ReportRepairActivity.this).load(mReportImagePath).into(mIvRepairImage);
                        loadSuccess("上传成功", false);
                    }
                });
            }
        });
    }
}
