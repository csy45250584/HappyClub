package com.haokuo.happyclub.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.util.DirUtil;
import com.haokuo.happyclub.util.MySpUtil;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;

import java.io.File;

/**
 *
 * 子类继承时不覆盖该类onActivityResult方法
 * Created by zjf on 2018-07-16.
 */

public abstract class BaseTakePhotoActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {

    private InvokeParam invokeParam;
    private TakePhoto takePhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getTakePhoto().onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
            TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder()
                    .setCorrectImage(true);
            takePhoto.setTakePhotoOptions(builder.create());
        }
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {

    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    protected void showTakePhotoDialog() {
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
}
