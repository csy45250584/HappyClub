package com.haokuo.happyclub.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import com.haokuo.happyclub.util.SafeHandler;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import butterknife.ButterKnife;

/**
 * Created by zjf on 2018-07-18.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            BaseActivity.this.handleMessage(msg);
            return true;
        }
    };
    private LoadingDialog mLoadingDialog;

    /**
     * handler的信息处理，如果使用handler必须重写该方法。
     *
     * @param msg
     */
    protected void handleMessage(Message msg) {
        throw new RuntimeException("It is required to override handleMessage(Message msg) before using handler!");
    }

    /**
     * 防止内存泄露的handler，使用必须重写handleMessage方法。
     *
     * @param msg
     */
    protected SafeHandler mHandler = new SafeHandler<Activity>(this, mCallback);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initContentLayout());
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
        loadData();
    }

    protected void loadData() {
    }

    public void showLoading() {
        getLoadingDialog().show();
    }

    public void showLoading(String loadingText, String successText, String failedText) {
        getLoadingDialog().setLoadingText(loadingText)
                .setSuccessText(successText)
                .setFailedText(failedText);
        getLoadingDialog().show();
    }

    public void showLoading(String loadingText) {
        getLoadingDialog().setLoadingText(loadingText);
        getLoadingDialog().show();
    }

    public void loadSuccess() {
        getLoadingDialog().loadSuccess();
        mLoadingDialog = null;
    }

    protected void setLoadingText(String loadingText) {
        getLoadingDialog().setLoadingText(loadingText);
    }

    public void loadSuccess(String successText) {
        getLoadingDialog().setSuccessText(successText);
        getLoadingDialog().loadSuccess();
        mLoadingDialog = null;
    }

    public void loadSuccess(String successText, boolean isFinish) {
        getLoadingDialog().setSuccessText(successText);
        getLoadingDialog().setFinishSuccess(isFinish);
        getLoadingDialog().loadSuccess();
        mLoadingDialog = null;
    }

    public void loadSuccess(String successText, LoadingDialog.OnFinishListener listener) {
        getLoadingDialog().setSuccessText(successText);
        getLoadingDialog().setOnFinishListener(listener);
        getLoadingDialog().loadSuccess();
        mLoadingDialog = null;
    }

    public void loadFailed() {
        getLoadingDialog().loadFailed();
        mLoadingDialog = null;
    }

    public void loadFailed(String failedText) {
        getLoadingDialog().setFailedText(failedText);
        getLoadingDialog().loadFailed();
        mLoadingDialog = null;
    }

    public void loadClose() {
      loadClose(false);
    }

    public void loadClose(boolean isFinish) {
        getLoadingDialog().close();
        mLoadingDialog = null;
        if (isFinish) {
            finish();
        }
    }

    protected abstract int initContentLayout();

    protected void initView() {

    }

    protected abstract void initData();

    protected abstract void initListener();

    public LoadingDialog getLoadingDialog() {
        if (mLoadingDialog == null) {
            synchronized (BaseActivity.class) {
                if (mLoadingDialog == null) {
                    mLoadingDialog = new LoadingDialog(this);
                }
            }
        }
        return mLoadingDialog;
    }

    protected void unableInputSpace(EditText... editTexts) {
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) {
                    return "";
                }
                return null;
            }
        };

        for (EditText editText : editTexts) {
            InputFilter[] filters = editText.getFilters();
            if (filters != null && filters.length != 0) {
                InputFilter[] newInputFilters = new InputFilter[filters.length + 1];
                for (int i = 0; i < filters.length; i++) {
                    newInputFilters[i] = filters[i];
                }
                newInputFilters[newInputFilters.length - 1] = inputFilter;
                editText.setFilters(newInputFilters);
            } else {
                InputFilter[] inputFilters = {inputFilter};
                editText.setFilters(inputFilters);
            }
        }
    }

}