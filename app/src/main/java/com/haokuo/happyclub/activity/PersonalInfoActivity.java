package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseTakePhotoActivity;
import com.haokuo.happyclub.bean.UploadPicResultBean;
import com.haokuo.happyclub.bean.UserInfoBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.UploadFileParams;
import com.haokuo.happyclub.util.MySpUtil;
import com.haokuo.happyclub.view.SettingItemView;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/12.
 */
public class PersonalInfoActivity extends BaseTakePhotoActivity {

    @BindView(R.id.iv_avatar)
    CircleImageView mIvAvatar;
    @BindView(R.id.ll_avatar)
    LinearLayout mLlAvatar;
    @BindView(R.id.siv_nickname)
    SettingItemView mSivNickname;
    @BindView(R.id.siv_name)
    SettingItemView mSivName;
    @BindView(R.id.siv_sex)
    SettingItemView mSivSex;
    @BindView(R.id.siv_birthday)
    SettingItemView mSivBirthday;
    @BindView(R.id.siv_id_card)
    SettingItemView mSivIdCard;
    @BindView(R.id.siv_address)
    SettingItemView mSivAddress;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        UserInfoBean userInfo = MySpUtil.getInstance().getUserInfo();
        Glide.with(this).load(userInfo.getHeadPhoto()).into(mIvAvatar);
        mSivNickname.setRightText(userInfo.getUserName());
        mSivName.setRightText(userInfo.getRealname());
        mSivSex.setRightText(userInfo.getSex());
        mSivBirthday.setRightText(userInfo.getBirthday());
        mSivIdCard.setRightText(userInfo.getIdCard());
    }

    @OnClick({R.id.ll_avatar, R.id.siv_nickname, R.id.siv_name, R.id.siv_sex, R.id.siv_birthday, R.id.siv_id_card, R.id.siv_address})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.ll_avatar) {
            showTakePhotoDialog();
            return;
        } else if (view.getId() == R.id.siv_address) {
            startActivity(new Intent(PersonalInfoActivity.this, DeliverAddressActivity.class));
            return;
        }
        Intent intent = new Intent(PersonalInfoActivity.this, SetPersonalInfoActivity.class);
        switch (view.getId()) {
            case R.id.siv_nickname:
                intent.putExtra(SetPersonalInfoActivity.EXTRA_TYPE, SetPersonalInfoActivity.TYPE_NICKNAME);
                break;
            case R.id.siv_name:
                intent.putExtra(SetPersonalInfoActivity.EXTRA_TYPE, SetPersonalInfoActivity.TYPE_REAL_NAME);
                break;
            case R.id.siv_sex:
                intent.putExtra(SetPersonalInfoActivity.EXTRA_TYPE, SetPersonalInfoActivity.TYPE_SEX);
                break;
            case R.id.siv_birthday:
                intent.putExtra(SetPersonalInfoActivity.EXTRA_TYPE, SetPersonalInfoActivity.TYPE_BIRTHDAY);
                break;
            case R.id.siv_id_card:
                intent.putExtra(SetPersonalInfoActivity.EXTRA_TYPE, SetPersonalInfoActivity.TYPE_ID_CARD);
                break;
        }
        startActivity(intent);
    }



    @Override
    public void takeSuccess(TResult result) {
        final String compressPath = result.getImage().getCompressPath();
        //        final String fileName = new File(compressPath).getName();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //上传头像
                showLoading("提交修改中...");
                ArrayList<File> files = new ArrayList<>();
                files.add(new File(compressPath));
                UploadFileParams params = new UploadFileParams(files, UploadFileParams.FILE_TYPE_IMAGE);
                HttpHelper.getInstance().uploadPic(params, this, new EntityCallback<UploadPicResultBean>() {
                    @Override
                    public void onFailure(Call call, String message) {
                        loadFailed("修改失败," + message);
                    }

                    @Override
                    public void onSuccess(Call call, UploadPicResultBean result) {
                        MySpUtil.getInstance().saveAvatar(result.getSrc());
                        Glide.with(PersonalInfoActivity.this).load(result.getSrc()).into(mIvAvatar);
                        loadSuccess("修改成功", false);
                    }
                });
            }
        });
    }
}
