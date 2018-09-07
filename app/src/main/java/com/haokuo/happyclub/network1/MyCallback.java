package com.haokuo.happyclub.network1;

import retrofit2.Call;

/**
 * Created by zjf on 2018/9/6.
 */
public interface MyCallback<T> {
    void onSuccess(Call call, T result);

    void onFailure(Call call, String message);
}
