package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.AddressAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.AddressResultBean;
import com.haokuo.happyclub.bean.list.AddressListBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.base.IdParams;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.haokuo.midtitlebar.MidTitleBar;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/13.
 */
public class DeliverAddressActivity extends BaseActivity {
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.rv_deliver_address)
    RecyclerView mRvDeliverAddress;
    private AddressAdapter mAddressAdapter;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_deliver_address;
    }

    @Override
    protected void initData() {
        //RecyclerView初始化
        mRvDeliverAddress.setLayoutManager(new LinearLayoutManager(this));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvDeliverAddress.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDivider,
                0, 0));
        mAddressAdapter = new AddressAdapter(R.layout.item_address);
        mRvDeliverAddress.setAdapter(mAddressAdapter);
    }

    @Override
    protected void loadData() {
        getAddressList();
    }

    private void getAddressList() {
        HttpHelper.getInstance().getAddress(new EntityCallback<AddressListBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("获取地址列表失败，" + message);
            }

            @Override
            public void onSuccess(Call call, AddressListBean result) {
                mAddressAdapter.setNewData(result.getData());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            getAddressList();
        }
    }

    @Override
    protected void initListener() {
        mAddressAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                AddressResultBean item = mAddressAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.btn_modify:
                        Intent intent = new Intent(DeliverAddressActivity.this, DeliverAddressDetailActivity.class);
                        intent.putExtra(DeliverAddressDetailActivity.EXTRA_ADDRESS, item);
                        startActivityForResult(intent, 0);
                        break;
                    case R.id.btn_delete:
                        IdParams params = new IdParams(item.getId());
                        HttpHelper.getInstance().deleteAddress(params, new NetworkCallback() {
                            @Override
                            public void onSuccess(Call call, String json) {
                              getAddressList();
                            }

                            @Override
                            public void onFailure(Call call, String message) {
                                ToastUtils.showShort("删除地址失败，" + message);
                            }
                        });
                        break;
                }
            }
        });
        mMidTitleBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.item_add_address) {
                    startActivityForResult(new Intent(DeliverAddressActivity.this, DeliverAddressDetailActivity.class), 0);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_deliver_address, menu);
        return true;
    }
}
