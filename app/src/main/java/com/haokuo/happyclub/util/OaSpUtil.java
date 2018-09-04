package com.haokuo.happyclub.util;

import com.haokuo.wenyanoa.bean.UserInfoBean;
import com.haokuo.wenyanoa.bean.UserInfoDetailBean;
import com.haokuo.wenyanoa.consts.SpConsts;
import com.haokuo.wenyanoa.util.utilscode.SPUtils;

/**
 * Created by zjf on 2018-08-17.
 */

public class OaSpUtil {
    public static UserInfoBean getUserInfo() {
        SPUtils utils = SPUtils.getInstance(SpConsts.FILE_PERSONAL_INFORMATION);
        int userId = utils.getInt(SpConsts.KEY_USER_ID, -1);
        String apiKey = utils.getString(SpConsts.KEY_API_KEY);
        String username = utils.getString(SpConsts.KEY_USERNAME);
        return new UserInfoBean(userId, username, apiKey);
    }

    public static void saveUserInfo(UserInfoBean userInfoBean) {
        SPUtils utils = SPUtils.getInstance(SpConsts.FILE_PERSONAL_INFORMATION);
        utils.put(SpConsts.KEY_USERNAME, userInfoBean.getUserName());
        utils.put(SpConsts.KEY_USER_ID, userInfoBean.getUserId());
        utils.put(SpConsts.KEY_API_KEY, userInfoBean.getApikey());
    }

    public static void saveUserDetailInfo(UserInfoDetailBean userInfoDetailBean) {
        SPUtils utils = SPUtils.getInstance(SpConsts.FILE_PERSONAL_INFORMATION);
        utils.put(SpConsts.KEY_USERNAME, userInfoDetailBean.getUserName());
        utils.put(SpConsts.KEY_REAL_NAME, userInfoDetailBean.getRealname());
        utils.put(SpConsts.KEY_SEX, userInfoDetailBean.getSex());
        utils.put(SpConsts.KEY_BIRTHDAY, userInfoDetailBean.getBirthday());
        utils.put(SpConsts.KEY_TEL_PHONE, userInfoDetailBean.getTelPhone());
        utils.put(SpConsts.KEY_ADDRESS, userInfoDetailBean.getAddress());
        utils.put(SpConsts.KEY_USER_TYPE, userInfoDetailBean.getUserType());
        utils.put(SpConsts.KEY_USER_TYPE_NAME, userInfoDetailBean.getUserTypeName());
        utils.put(SpConsts.KEY_USER_JOB, userInfoDetailBean.getUserJob());
        utils.put(SpConsts.KEY_USER_SECITION, userInfoDetailBean.getUserSecition());
        utils.put(SpConsts.KEY_MOBILE_PHONE, userInfoDetailBean.getMobilePhone());
        utils.put(SpConsts.KEY_SECTION_PHONE, userInfoDetailBean.getSectionPhone());
        utils.put(SpConsts.KEY_WE_CHAT, userInfoDetailBean.getWeChat());
        utils.put(SpConsts.KEY_HEAD_PHOTO, userInfoDetailBean.getHeadPhoto());
    }

    public static UserInfoDetailBean getUserInfoDetail() {
        SPUtils utils = SPUtils.getInstance(SpConsts.FILE_PERSONAL_INFORMATION);
        int userId = utils.getInt(SpConsts.KEY_USER_ID, -1);
        String apiKey = utils.getString(SpConsts.KEY_API_KEY);
        String username = utils.getString(SpConsts.KEY_USERNAME);
        String realName = utils.getString(SpConsts.KEY_REAL_NAME);
        String sex = utils.getString(SpConsts.KEY_SEX);
        String birthday = utils.getString(SpConsts.KEY_BIRTHDAY);
        String telPhone = utils.getString(SpConsts.KEY_TEL_PHONE);
        String address = utils.getString(SpConsts.KEY_ADDRESS);
        String qq = utils.getString(SpConsts.KEY_QQ);
        int userType = utils.getInt(SpConsts.KEY_USER_TYPE, -1);
        String userTypeName = utils.getString(SpConsts.KEY_USER_TYPE_NAME);
        String userJob = utils.getString(SpConsts.KEY_USER_JOB);
        String userSecition = utils.getString(SpConsts.KEY_USER_SECITION);
        String mobilePhone = utils.getString(SpConsts.KEY_MOBILE_PHONE);
        String sectionPhone = utils.getString(SpConsts.KEY_SECTION_PHONE);
        String weChat = utils.getString(SpConsts.KEY_WE_CHAT);
        String headPhoto = utils.getString(SpConsts.KEY_HEAD_PHOTO);
        return new UserInfoDetailBean(userId, apiKey, username, realName, sex, birthday, telPhone, address, qq, userType, userTypeName, userJob, userSecition, mobilePhone, sectionPhone, weChat, headPhoto);
    }
}
