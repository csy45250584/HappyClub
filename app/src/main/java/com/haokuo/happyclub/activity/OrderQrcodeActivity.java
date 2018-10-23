package com.haokuo.happyclub.activity;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.midtitlebar.MidTitleBar;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import butterknife.BindView;

/**
 * Created by zjf on 2018/10/16.
 */
public class OrderQrcodeActivity extends BaseActivity {
    public static final String EXTRA_ORDER_ID = "com.haokuo.happyclub.extra.EXTRA_ORDER_ID";
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_order_qrcode;
    }

    private void setQrcodeImage(String qrcodeContent) {
        //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode(qrcodeContent, 350, 350, null);
        mIvQrcode.setImageBitmap(qrCodeBitmap);
    }

    @Override
    protected void initData() {
        long orderId = getIntent().getLongExtra(EXTRA_ORDER_ID, -1);
        setQrcodeImage(String.valueOf(orderId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_OK);
    }
}
