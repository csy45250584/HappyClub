package com.haokuo.happyclub.network;

import okhttp3.Call;

/**
 * Created by zjf on 2018/12/27.
 */
public abstract class DownloadCallback {
    private String mFilePath;

    public DownloadCallback(String filePath) {
        mFilePath = filePath;
    }

    public abstract void onStart(Call call, long fileLength);

    public abstract void onProgress(Call call, long progress, long total);

    public abstract void onSuccess(Call call);

    public abstract void onFailure(Call call, String message);

    public String getFilePath() {
        return mFilePath;
    }
}
