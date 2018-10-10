package com.haokuo.happyclub.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.UserInfoBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.util.MySpUtil;
import com.haokuo.happyclub.view.SettingItemView;
import com.haokuo.happyclub.view.SexRadioButton;
import com.haokuo.midtitlebar.MidTitleBar;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/12.
 */
public class SetPersonalInfoActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    public static final String EXTRA_TYPE = "com.haokuo.happyclub.extra.EXTRA_TYPE";
    public static final int TYPE_NICKNAME = 1;
    public static final int TYPE_REAL_NAME = 2;
    public static final int TYPE_SEX = 3;
    public static final int TYPE_BIRTHDAY = 4;
    public static final int TYPE_ID_CARD = 5;
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.et_info)
    EditText mEtInfo;
    @BindView(R.id.siv_birthday)
    SettingItemView mSivBirthday;
    @BindView(R.id.siv_sex_male)
    SexRadioButton mSivSexMale;
    @BindView(R.id.siv_sex_female)
    SexRadioButton mSivSexFemale;
    @BindView(R.id.ll_sex_select)
    LinearLayout mLlSexSelect;
    private int mType;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_set_personal_info;
    }

    @Override
    protected void initData() {
        UserInfoBean userInfo = MySpUtil.getInstance().getUserInfo();
        mType = getIntent().getIntExtra(EXTRA_TYPE, -1);
        switch (mType) {
            case TYPE_NICKNAME:
                mMidTitleBar.setMidTitle("昵称");
                mLlSexSelect.setVisibility(View.GONE);
                mSivBirthday.setVisibility(View.GONE);
                mEtInfo.setVisibility(View.VISIBLE);
                mEtInfo.setHint("请输入昵称");
                break;
            case TYPE_REAL_NAME:
                mMidTitleBar.setMidTitle("姓名");
                mLlSexSelect.setVisibility(View.GONE);
                mSivBirthday.setVisibility(View.GONE);
                mEtInfo.setVisibility(View.VISIBLE);
                mEtInfo.setHint("请输入姓名");
                break;
            case TYPE_SEX:
                mMidTitleBar.setMidTitle("性别");
                mLlSexSelect.setVisibility(View.VISIBLE);
                mSivBirthday.setVisibility(View.GONE);
                mEtInfo.setVisibility(View.GONE);
                mSivSexMale.setChecked("男".equals(userInfo.getSex()));
                mSivSexFemale.setChecked("女".equals(userInfo.getSex()));
                break;
            case TYPE_BIRTHDAY:
                mMidTitleBar.setMidTitle("生日");
                mLlSexSelect.setVisibility(View.GONE);
                mSivBirthday.setVisibility(View.VISIBLE);
                mEtInfo.setVisibility(View.GONE);
                break;
            case TYPE_ID_CARD:
                mMidTitleBar.setMidTitle("身份证号");
                mLlSexSelect.setVisibility(View.GONE);
                mSivBirthday.setVisibility(View.GONE);
                mEtInfo.setVisibility(View.VISIBLE);
                mEtInfo.setHint("请输入身份证号码");
                break;
        }

    }

    @Override
    protected void initListener() {
        mMidTitleBar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info_setting, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        final UserInfoBean userInfoOrignal = MySpUtil.getInstance().getUserInfo();
        final UserInfoBean userInfo = new UserInfoBean();
        if (menuItem.getItemId() == R.id.item_commit) {
            switch (mType) {
                case TYPE_NICKNAME:
                    userInfo.setUserName(mEtInfo.getEditableText().toString());
                    userInfoOrignal.setUserName(mEtInfo.getEditableText().toString());
                    break;
                case TYPE_REAL_NAME:
                    userInfo.setRealname(mEtInfo.getEditableText().toString());
                    userInfoOrignal.setRealname(mEtInfo.getEditableText().toString());
                    break;
                case TYPE_SEX:
                    userInfo.setSex(mSivSexMale.isChecked() ? "男" : "女");
                    userInfoOrignal.setSex(mSivSexMale.isChecked() ? "男" : "女");
                    break;
                case TYPE_BIRTHDAY:
                    userInfo.setBirthday(mSivBirthday.getLeftText());
                    userInfoOrignal.setBirthday(mSivBirthday.getLeftText());
                    break;
                case TYPE_ID_CARD:
                    userInfo.setIdCard(mEtInfo.getEditableText().toString());
                    userInfoOrignal.setIdCard(mEtInfo.getEditableText().toString());
                    break;
            }
            showLoading("提交中...");
            HttpHelper.getInstance().updateUserInfo(userInfo, new NetworkCallback() {
                @Override
                public void onSuccess(Call call, String json) {
                    loadSuccess("修改成功");
                    MySpUtil.getInstance().saveUserInfo(userInfoOrignal);
                }

                @Override
                public void onFailure(Call call, String message) {
                    loadFailed("修改失败，" + message);
                }
            });
        }
        return true;
    }



    @OnClick({R.id.siv_birthday, R.id.siv_sex_male, R.id.siv_sex_female})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.siv_birthday:
                break;
            case R.id.siv_sex_male:
                mSivSexMale.setChecked(true);
                mSivSexFemale.setChecked(false);
                break;
            case R.id.siv_sex_female:
                mSivSexMale.setChecked(false);
                mSivSexFemale.setChecked(true);
                break;
        }
    }
}
