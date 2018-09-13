package com.haokuo.happyclub.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.AddressResultBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.midtitlebar.MidTitleBar;
import com.rey.material.widget.CheckBox;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/13.
 */
public class DeliverAddressDetailActivity extends BaseActivity {
    public static final String EXTRA_ADDRESS = "com.haokuo.happyclub.extra.EXTRA_ADDRESS";
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_tel)
    EditText mEtTel;
    @BindView(R.id.et_region)
    EditText mEtRegion;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.et_zip_code)
    EditText mEtZipCode;
    @BindView(R.id.cb_default_address)
    CheckBox mCbDefaultAddress;
    private boolean isEditMode;
    private AddressResultBean mAddressBean;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_deliver_address_detail;
    }

    @Override
    protected void initData() {
        mAddressBean = (AddressResultBean) getIntent().getSerializableExtra(EXTRA_ADDRESS);
        isEditMode = mAddressBean != null;
        if (isEditMode) {
            mEtName.setText(mAddressBean.getName());
            mEtName.setSelection(mAddressBean.getName().length());
            mEtTel.setText(mAddressBean.getTelphone());
            mEtTel.setSelection(mAddressBean.getTelphone().length());
            mEtRegion.setText(mAddressBean.getArea());
            mEtRegion.setSelection(mAddressBean.getArea().length());
            mEtAddress.setText(mAddressBean.getAddress());
            mEtAddress.setSelection(mAddressBean.getAddress().length());
            mEtZipCode.setText(mAddressBean.getZip());
            mEtZipCode.setSelection(mAddressBean.getZip().length());
            mCbDefaultAddress.setChecked(mAddressBean.isDefaultFlag());
        }
    }

    @Override
    protected void initListener() {
        mMidTitleBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.item_complete) {
                    //检查数据
                    String name = mEtName.getEditableText().toString().trim();
                    String tel = mEtTel.getEditableText().toString().trim();
                    String region = mEtRegion.getEditableText().toString().trim();
                    String address = mEtAddress.getEditableText().toString().trim();
                    String zipCode = mEtZipCode.getEditableText().toString().trim();
                    boolean isDefaultAddress = mCbDefaultAddress.isChecked();
                    if (isEditMode) {
                        mAddressBean.setName(name);
                        mAddressBean.setTelphone(tel);
                        mAddressBean.setArea(region);
                        mAddressBean.setAddress(address);
                        mAddressBean.setZip(zipCode);
                        mAddressBean.setDefaultFlag(isDefaultAddress);
                        showLoading("修改中...");
                        HttpHelper.getInstance().updateAddress(mAddressBean, new NetworkCallback() {
                            @Override
                            public void onSuccess(Call call, String json) {
                                setResult(RESULT_OK);
                                loadSuccess("修改成功");
                            }

                            @Override
                            public void onFailure(Call call, String message) {
                                loadFailed("修改失败，" + message);
                            }
                        });
                    } else {
                        AddressResultBean jsonBean = new AddressResultBean(name, tel, region, address, zipCode, isDefaultAddress);
                        showLoading("添加中...");
                        HttpHelper.getInstance().insertAddress(jsonBean, new NetworkCallback() {
                            @Override
                            public void onSuccess(Call call, String json) {
                                setResult(RESULT_OK);
                                loadSuccess("添加成功");
                            }

                            @Override
                            public void onFailure(Call call, String message) {
                                loadFailed("添加失败，" + message);
                            }
                        });
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complete, menu);
        return true;
    }
}
