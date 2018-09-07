package com.haokuo.happyclub.network;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by zjf on 2018-07-21.
 */

public abstract class EntityCallback<T> implements NetworkCallback {
    @Override
    public void onSuccess(Call call, String json) {
        T result = JSON.parseObject(json, getTClass());
        onSuccess(call, result);
    }

    public abstract void onSuccess(Call call, T result);

    public Class<T> getTClass() {
        Type sType = getClass().getGenericSuperclass();
        Type[] generics = ((ParameterizedType) sType).getActualTypeArguments();
        return (Class<T>) (generics[0]);
    }
}