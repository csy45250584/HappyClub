package com.haokuo.happyclub.network1;

import com.haokuo.happyclub.bean.SuccessResultBean;
import com.haokuo.happyclub.network.UrlConfig;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by zjf on 2018/9/6.
 */
public interface ILoginService {
    @POST("custom/login.do")
    @FormUrlEncoded
    Call<SuccessResultBean> login(@FieldMap Map<String, Object> map);

    @POST(UrlConfig.GET_USER_INFO_URL)
    @FormUrlEncoded
    Call<SuccessResultBean> getUserInfo(@FieldMap Map<String, Object> map);
}
