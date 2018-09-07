package com.haokuo.happyclub.network1;

import android.support.annotation.NonNull;

import com.haokuo.happyclub.bean.SuccessResultBean;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zjf on 2018/9/6.
 */
public class RetrofitUtil {
    private static RetrofitUtil mInstance;
    private final MediaType mMediaType;
    private Retrofit mRetrofit;
    private ILoginService mLoginService;

    private RetrofitUtil() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(UrlConfig.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mMediaType = MediaType.parse("application/json; charset=utf-8");
        mLoginService = mRetrofit.create(ILoginService.class);
    }

    public static RetrofitUtil getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtil.class) {
                if (mInstance == null)
                    mInstance = new RetrofitUtil();
            }
        }
        return mInstance;
    }

    public void getUserInfo(final MyCallback<SuccessResultBean> callback) {

        Call<SuccessResultBean> login = mLoginService.getUserInfo(null);
        login.enqueue(new Callback<SuccessResultBean>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResultBean> call, @NonNull Response<SuccessResultBean> response) {
                response.body();
                if (response.isSuccessful()) {
                    callback.onSuccess(call, response.body());
                } else {
                    callback.onFailure(call, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResultBean> call, @NonNull Throwable t) {
                String message;
                if (t instanceof SocketTimeoutException) {
                    message = "连接超时";
                } else if (t instanceof ConnectException) {
                    message = "连接异常";
                } else {
                    message = t.getMessage();
                }
                callback.onFailure(call, message);
            }
        });
    }
}
