package com.haokuo.happyclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 23;//定义请求码常量
    @BindView(R.id.tv_hello)
    TextView mTvHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_hello)
    public void onViewClicked() {

//        Matisse
//                .from(MainActivity.this)
//                .choose(MimeType.ofImage())//照片视频全部显示
//                .countable(false)//有序选择图片
//                .maxSelectable(1)//最大选择数量为9
//                .gridExpectedSize(ScreenUtils.getScreenWidth() / 4)//图片显示表格的大小getResources().getDimensionPixelSize(R.dimen.grid_expected_size)
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//图像选择和预览活动所需的方向。
//                .thumbnailScale(0.85f)//缩放比例
//                .theme(R.style.Matisse_Zhihu)//主题  暗色主题 R.style.Matisse_Dracula
//                .imageEngine(new Glide4Engine())//加载方式
//                .capture(true) //是否提供拍照功能
//                .captureStrategy(new CaptureStrategy(true,"com.haokuo.happyclub.fileprovider"))//存储到哪里
//                .forResult(REQUEST_CODE_CHOOSE);//请求码
    }

    @Override      //接收返回的地址
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
        }
    }
}
