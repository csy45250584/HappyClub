package com.haokuo.happyclub.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.haokuo.happyclub.bean.AddressResultBean;
import com.haokuo.happyclub.bean.EvaluationBean;
import com.haokuo.happyclub.bean.FoodOrderBean;
import com.haokuo.happyclub.bean.RecourseBean;
import com.haokuo.happyclub.bean.SuccessBean;
import com.haokuo.happyclub.bean.UserInfoBean;
import com.haokuo.happyclub.network.bean.ChangeServeStatusParams;
import com.haokuo.happyclub.network.bean.CheckIsNextParams;
import com.haokuo.happyclub.network.bean.GetAcceptedServeParams;
import com.haokuo.happyclub.network.bean.GetRecourseListParams;
import com.haokuo.happyclub.network.bean.GetVolunteerServeParams;
import com.haokuo.happyclub.network.bean.LoginByTelParams;
import com.haokuo.happyclub.network.bean.LoginParams;
import com.haokuo.happyclub.network.bean.RegisterParams;
import com.haokuo.happyclub.network.bean.ResetPasswordParams;
import com.haokuo.happyclub.network.bean.UpdatePasswordParams;
import com.haokuo.happyclub.network.bean.UploadFileParams;
import com.haokuo.happyclub.network.bean.base.IGetParamsMap;
import com.haokuo.happyclub.network.bean.base.IdParams;
import com.haokuo.happyclub.network.bean.base.TelPhoneParams;
import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;
import com.haokuo.happyclub.util.MySpUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by zjf on 2018-07-21.
 */

public class HttpHelper {
    private static final String TAG = "HttpHelper";
    private static HttpHelper mInstance;
    private OkHttpClient mClient = new OkHttpClient();

    public static HttpHelper getInstance() {
        if (mInstance == null) {
            synchronized (HttpHelper.class) {
                if (mInstance == null) {
                    mInstance = new HttpHelper();
                }
            }
        }
        return mInstance;
    }

    private HttpHelper() {
        mClient = new OkHttpClient();
    }

    static class OkHttpCallBack implements Callback {
        private NetworkCallback callback;
        private Handler mHandler;

        public OkHttpCallBack(NetworkCallback callback) {
            this.callback = callback;
            mHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void onFailure(final Call call, final IOException e) {
            Log.e(TAG, "okhttp onFailure, message = " + e.getMessage());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    String message;
                    if (e instanceof SocketTimeoutException) {
                        message = "连接超时";
                    } else if (e instanceof ConnectException) {
                        message = "连接异常";
                    } else {
                        message = e.getMessage();
                    }
                    callback.onFailure(call, message);
                }
            });
        }

        @Override
        public void onResponse(final Call call, Response response) {
            ResponseBody body = response.body();
            if (body == null) {
                Log.e(TAG, "okhttp onResponse, ResponseBody = null.");
                callback.onFailure(call, "服务器异常");
                return;
            }
            try {
                final String json = body.string();
                Log.i(TAG, "okhttp onResponse, json = " + json);
                final SuccessBean successBean = JSON.parseObject(json, SuccessBean.class);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (successBean == null) {
                            callback.onFailure(call, "未知错误");
                        } else if (successBean.isSuccess()) {
                            callback.onSuccess(call, json);
                        } else {
                            String message = successBean.getMessage();
                            Log.e(TAG, "okhttp onResponse, success = false, message = " + message);
                            callback.onFailure(call, message);
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //        private void doPost(IGetParamsMap iGetParamsMap, String url, NetworkCallback callback) {
    //            String jsonString = JSON.toJSONString(iGetParamsMap);
    //            Log.i(TAG, "doPost: " + "jsonString = " + jsonString);
    //            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
    //            MultipartBody.Builder builder = new MultipartBody.Builder()
    //                    .setType(MultipartBody.FORM);
    //            if (iGetParamsMap.getParamsMap().entrySet().contains(UserIdApikeyParams.PARAM_APIKEY)) {
    //                FormBody.Builder builder2 = new FormBody.Builder();
    //                builder2.add(UserIdApikeyParams.PARAM_APIKEY, iGetParamsMap.getParamsMap().get(UserIdApikeyParams.PARAM_APIKEY));
    //                builder.addPart(builder2.build());
    //            }
    //            builder.addPart(requestBody);
    //            Request request = new Request.Builder()
    //                    .post(requestBody)
    //                    .url(url)
    //                    .build();//创建Request 对象
    //            mClient.newCall(request).enqueue(new OkHttpCallBack(callback));
    //        }

    private void doPost(Object entity, String url, NetworkCallback callback) {
        if (entity == null) {
            entity = new UserIdTokenParams();
        }
        Log.i(TAG, "doPost: " + "jsonString = " + JSON.toJSONString(entity));
        Request request = new Request.Builder()
                .post(buildFormBody(entity))
                .url(UrlConfig.BASE_URL + url)
                .build();//创建Request 对象
        mClient.newCall(request).enqueue(new OkHttpCallBack(callback));
    }

    private void doPostWithJson(Object json, String url, NetworkCallback callback) {
        Log.i(TAG, "doPostWithJson: " + "jsonString = " + JSON.toJSONString(json));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(json));
        Request request = new Request.Builder()
                .post(requestBody)
                .url(UrlConfig.BASE_URL + url + "?" + "userId=" + MySpUtil.getInstance().getUserId() + "&token=" + MySpUtil.getInstance().getToken())
                .build();//创建Request 对象
        mClient.newCall(request).enqueue(new OkHttpCallBack(callback));
    }

    private void doPostWithoutApiKey(Object object, String url, NetworkCallback callback) {
        String jsonString = JSON.toJSONString(object);
        Log.i(TAG, "doPost: " + "jsonString = " + jsonString);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();//创建Request 对象
        mClient.newCall(request).enqueue(new OkHttpCallBack(callback));
    }

    private void doPostUploadFile(UploadFileParams uploadFileParams, String url, Object tag, NetworkCallback callback) {
        List<File> files = uploadFileParams.getFile();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("userId", String.valueOf(MySpUtil.getInstance().getUserId()));
        builder.addFormDataPart("token", MySpUtil.getInstance().getToken());
        for (File file : files) {
            RequestBody fileBody = RequestBody.create(MediaType.parse(uploadFileParams.getType()), file);
            builder.addFormDataPart("file", file.getName(), fileBody);
        }
        MultipartBody requestBody = builder.build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(UrlConfig.BASE_URL + url)
                .tag(tag)
                .build();//创建Request 对象
        mClient.newCall(request).enqueue(new OkHttpCallBack(callback));
    }

    public void cancelRequest(Object tag) {
        if (tag == null)
            return;
        for (Call call : mClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    private void doPost(IGetParamsMap iGetParamsMap, String url, Object tag, NetworkCallback callback) {
        Request request = new Request.Builder()
                .post(buildFormBody(iGetParamsMap))
                .url(url)
                .tag(tag)
                .build();//创建Request 对象
        mClient.newCall(request).enqueue(new OkHttpCallBack(callback));
    }

    private void doGet(IGetParamsMap iGetParamsMap, String url, NetworkCallback callback) {
        url = buildFullUrl(url, iGetParamsMap);
        Log.v(TAG, url);
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();//创建Request 对象
        mClient.newCall(request).enqueue(new OkHttpCallBack(callback));
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

        //        for (Map.Entry<String, String> entry : params.entrySet()) {
        //            String value = entry.getValue();
        //            if (value != null) {
        //                builder.add(entry.getKey(), value);
        //            }
        //        }
        return builder.build();
    }

    private String buildFullUrl(String url, IGetParamsMap iGetParamsMap) {
        StringBuilder builder = new StringBuilder(url);
        builder.append("?");
        Map<String, String> params = iGetParamsMap.getParamsMap();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = entry.getValue();
            if (value != null) {
                builder.append(entry.getKey()).append("=").append(value).append("&");
            }
        }
        builder.delete(builder.length() - 1, builder.length());
        return builder.toString();
    }

    /** 注册 **/
    public void register(RegisterParams params, NetworkCallback callback) {
        doPost(params, UrlConfig.REGISTER_URL, callback);
    }

    /** 获取登录验证码 **/
    public void getLoginVerifyCode(TelPhoneParams params, NetworkCallback callback) {
        doPost(params, UrlConfig.GET_LOGIN_VERIFY_CODE_URL, callback);
    }

    /** 获取登录验证码 **/
    public void getRegisterVerifyCode(TelPhoneParams params, NetworkCallback callback) {
        doPost(params, UrlConfig.GET_REGISTER_VERIFY_CODE_URL, callback);
    }

    /** 手机验证码登录 **/
    public void loginByTel(LoginByTelParams params, NetworkCallback callback) {
        doPost(params, UrlConfig.LOGIN_BY_TEL_URL, callback);
    }

    /** 账号密码登录 **/
    public void login(LoginParams params, NetworkCallback callback) {
        doPost(params, UrlConfig.LOGIN_URL, callback);
    }

    /** 获取重置密码验证码 **/
    public void getResetVerifyCode(TelPhoneParams params, NetworkCallback callback) {
        doPost(params, UrlConfig.GET_RESET_VERIFY_CODE_URL, callback);
    }

    /** 是否允许重置密码 **/
    public void checkIsNext(CheckIsNextParams params, NetworkCallback callback) {
        doPost(params, UrlConfig.CHECK_IS_NEXT_URL, callback);
    }

    /** 重置密码 **/
    public void resetPassword(ResetPasswordParams params, NetworkCallback callback) {
        doPost(params, UrlConfig.RESET_PASSWORD_URL, callback);
    }

    /** 修改密码 **/
    public void updatePassword(UpdatePasswordParams params, NetworkCallback callback) {
        doPost(params, UrlConfig.UPDATE_PASSWORD_URL, callback);
    }

    /** 获取个人信息 **/
    public void getUserInfo(NetworkCallback callback) {
        doPost(null, UrlConfig.GET_USER_INFO_URL, callback);
    }

    /** 头像上传 **/
    public void uploadPic(UploadFileParams params, Object tag, NetworkCallback callback) {
        doPostUploadFile(params, UrlConfig.UPLOAD_PIC_URL, tag, callback);
    }

    /** 获取收货地址列表 **/
    public void getAddress(NetworkCallback callback) {
        doPost(null, UrlConfig.GET_ADDRESS_URL, callback);
    }

    /** 获取收货地址详情 **/
    public void getAddressInfo(IdParams params, NetworkCallback callback) {
        doPost(params, UrlConfig.GET_ADDRESS_INFO_URL, callback);
    }

    /** 新建收货地址 **/
    public void insertAddress(AddressResultBean json, NetworkCallback callback) {
        doPostWithJson(json, UrlConfig.INSERT_ADDRESS_URL, callback);
    }

    /** 删除收货地址 **/
    public void deleteAddress(IdParams params, NetworkCallback callback) {
        doPost(params, UrlConfig.DELETE_ADDRESS_URL, callback);
    }

    /** 修改收货地址 **/
    public void updateAddress(AddressResultBean json, NetworkCallback callback) {
        doPostWithJson(json, UrlConfig.UPDATE_ADDRESS_URL, callback);
    }

    /** 更新个人信息 **/
    public void updateUserInfo(UserInfoBean json, NetworkCallback callback) {
        doPostWithJson(json, UrlConfig.UPDATE_USER_INFO_URL, callback);
    }

    /** 获取我的求助列表 **/
    public void getRecourseList(GetRecourseListParams entity, NetworkCallback callback) {
        doPost(entity, UrlConfig.GET_RECOURSE_LIST_URL, callback);
    }

    /** 新建求助 **/
    public void newRecourse(RecourseBean json, NetworkCallback callback) {
        doPostWithJson(json, UrlConfig.NEW_RECOURSE_URL, callback);
    }

    /** 删除我的求助 **/
    public void deleteRecourse(IdParams entity, NetworkCallback callback) {
        doPost(entity, UrlConfig.DELETE_SERVE_URL, callback);
    }

    /** 重新发布我的求助 **/
    public void republishRecourse(RecourseBean json, NetworkCallback callback) {
        doPostWithJson(json, UrlConfig.REPUBLISH_SERVE_URL, callback);
    }

    /** 评价求助 **/
    public void evaluateRecourse(EvaluationBean json, NetworkCallback callback) {
        doPostWithJson(json, UrlConfig.EVALUATE_SERVE_URL, callback);
    }

    /** 获取我接的志愿单 **/
    public void getAcceptedServe(GetAcceptedServeParams entity, NetworkCallback callback) {
        doPost(entity, UrlConfig.GET_ACCEPTED_SERVE_URL, callback);
    }

    /**
     * 志愿者及用户状态操作(status见详细说明)
     * 用户:完成:44
     * 志愿者:放弃求助:88,去服务:22,完成服务:33
     **/
    public void changeServeStatus(ChangeServeStatusParams entity, NetworkCallback callback) {
        doPost(entity, UrlConfig.CHANGE_SERVE_STATUS_URL, callback);
    }

    /** 获取食堂全部菜品分类 **/
    public void getAllFoodList(NetworkCallback callback) {
        doPost(null, UrlConfig.GET_ALL_FOOD_LIST_URL, callback);
    }

    /** 食堂下单 **/
    public void insertFoodOrder(FoodOrderBean json, NetworkCallback callback) {
        doPostWithJson(json, UrlConfig.INSERT_FOOD_ORDER_URL, callback);
    }

    /** 志愿工单列表(所有发布的志愿者活动) **/
    public void getVolunteerServeList(GetVolunteerServeParams entity, NetworkCallback callback) {
        doPost(entity, UrlConfig.VOLUNTEER_SERVE_LIST_URL, callback);
    }

    /** 志愿工单列表(所有发布的志愿者活动) **/
    public void acceptServe(IdParams entity, NetworkCallback callback) {
        doPost(entity, UrlConfig.ACCEPT_SERVE_URL, callback);
    }
}
