package com.haokuo.happyclub.network;

import okhttp3.Call;

/**
 * Created by zjf on 2018-07-21.
 */

public interface NetworkCallback {

    void onSuccess(Call call, String json);

    void onFailure(Call call, String message);
}