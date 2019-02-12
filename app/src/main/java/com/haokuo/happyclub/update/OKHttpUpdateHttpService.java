/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haokuo.happyclub.update;

import android.support.annotation.NonNull;

import com.haokuo.happyclub.network.OkDownloadCallBack;
import com.haokuo.happyclub.util.MySpUtil;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 使用okhttp
 *
 * @author xuexiang
 * @since 2018/7/10 下午4:04
 */
public class OKHttpUpdateHttpService implements IUpdateHttpService {

    private OkHttpClient mClient;

    public OKHttpUpdateHttpService() {
        mClient = new OkHttpClient();
    }

    @NonNull
    private FormBody buildFormBody(Object object) {
        FormBody.Builder builder = new FormBody.Builder();
        //        Field[] fields = object.getClass().getDeclaredFields();
        ArrayList<Field> fields = new ArrayList<>();
        Class<?> tempClass = object.getClass();
        while (tempClass != null && tempClass != Object.class) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        for (Field field : fields) {
            field.setAccessible(true);
            //返回输出指定对象a上此 Field表示的字段名和字段值
            try {
                if (!field.isSynthetic() && !field.getName().equals("serialVersionUID") && field.get(object) != null) { //android studio如果开启的话编译器就会对所有类都添加$change成员变量
                    builder.add(field.getName(), field.get(object).toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return builder.build();
    }

    private String buildFullUrl(String url, Map<String, Object> iGetParamsMap) {
        StringBuilder builder = new StringBuilder(url);
        builder.append("?");
        for (Map.Entry<String, Object> entry : iGetParamsMap.entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                builder.append(entry.getKey()).append("=").append(value.toString()).append("&");
            }
        }
        builder.delete(builder.length() - 1, builder.length());
        return builder.toString();
    }

    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, Object> params, final @NonNull Callback callBack) {
        url = buildFullUrl(url, params);
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();//创建Request 对象
        mClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onSuccess(response.body().string());
            }
        });
        //        okhttp.get()
        //                .url(url)
        //                .params(transform(params))
        //                .build()
        //                .execute(new StringCallback() {
        //                    @Override
        //                    public void onError(Call call, Response response, Exception e, int id) {
        //                        callBack.onError(e);
        //                    }
        //
        //                    @Override
        //                    public void onResponse(String response, int id) {
        //                        callBack.onSuccess(response);
        //                    }
        //                });
    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, Object> params, final @NonNull Callback callBack) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String value = entry.getValue().toString();
            if (value != null) {
                builder.add(entry.getKey(), value);
            }
        }

        Request request = new Request.Builder()
                .post(builder.build())
                .url(url)
                .build();//创建Request 对象
        mClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onSuccess(response.body().string());
            }
        });
        //        OkHttpUtils.post()
        //                .url(url)
        //                .params(transform(params))
        //                .build()
        //                .execute(new StringCallback() {
        //                    @Override
        //                    public void onError(Call call, Response response, Exception e, int id) {
        //                        callBack.onError(e);
        //                    }
        //
        //                    @Override
        //                    public void onResponse(String response, int id) {
        //                        callBack.onSuccess(response);
        //                    }
        //                });
    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, final @NonNull DownloadCallback callback) {
        MySpUtil.getInstance().saveUpdatePath(path);
        new File(path).mkdirs();
        File file = new File(path, fileName);
        Request request = new Request.Builder()
                .url(url)
                .build();
        mClient.newCall(request).enqueue(new OkDownloadCallBack(new com.haokuo.happyclub.network.DownloadCallback(file.getAbsolutePath()) {
            @Override
            public void onStart(Call call, long fileLength) {
                callback.onStart();
            }

            @Override
            public void onProgress(Call call, long progress, long total) {
                callback.onProgress(progress, total);
            }

            @Override
            public void onSuccess(Call call) {
                callback.onSuccess(new File(getFilePath()));
            }

            @Override
            public void onFailure(Call call, String message) {
                callback.onError(new IOException(message));
            }
        }));
    }

    @Override
    public void cancelDownload(@NonNull String url) {
        ToastUtils.showShort("已取消更新");
    }

    private Map<String, String> transform(Map<String, Object> params) {
        Map<String, String> map = new TreeMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            map.put(entry.getKey(), entry.getValue().toString());
        }
        return map;
    }
}