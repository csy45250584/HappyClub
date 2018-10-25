package com.haokuo.happyclub.network;

/**
 * Created by zjf on 2018-07-21.
 */

public class UrlConfig {
    public static final String IMAGE_STRING_SPLIT = ",";
    public static final String BASE_URL = "http://192.168.1.193:9088/xfhsi/";
    //    public static final String BASE_URL = "http://18t69t8992.51mypc.cn:9088/xfhsi/";
    public static final String IMAGE_BASE_URL = "http://18t69t8992.51mypc.cn:9088/xfhs/upload/";
    //个人信息
    public static final String REGISTER_URL = "/custom/register.do";
    public static final String GET_LOGIN_VERIFY_CODE_URL = "/custom/loginVerificationCode.do";
    public static final String GET_REGISTER_VERIFY_CODE_URL = "/custom/registerVerificationCode.do";
    public static final String LOGIN_BY_TEL_URL = "/custom/loginTel.do";
    public static final String LOGIN_URL = "/custom/login.do";
    public static final String GET_RESET_VERIFY_CODE_URL = "/custom/resetVerificationCode.do";
    public static final String GET_BIND_VERIFY_CODE_URL = "/custom/app/bindingVerificationCode.do";
    public static final String BIND_USER_TEL_URL = "/custom/app/updateUserTel.do";
    public static final String CHECK_IS_NEXT_URL = "/custom/checkIsNext.do";
    public static final String RESET_PASSWORD_URL = "/custom/resetPassword.do";
    public static final String UPDATE_PASSWORD_URL = "/custom/app/updatePassword.do";
    public static final String GET_USER_INFO_URL = "/custom/app/getUserInfo.do";
    public static final String UPDATE_USER_INFO_URL = "/custom/app/updateUserInfo.do";
    public static final String UPLOAD_PIC_URL = "/custom/app/uploadPic.do";
    public static final String GET_ADDRESS_URL = "/custom/app/getAddress.do";
    public static final String GET_ADDRESS_INFO_URL = "/custom/app/getAddressInfoById.do";
    public static final String INSERT_ADDRESS_URL = "/custom/app/insertAddress.do";
    public static final String DELETE_ADDRESS_URL = "/custom/app/delAddress.do";
    public static final String UPDATE_ADDRESS_URL = "/custom/app/updateAddress.do";
    public static final String GET_MY_WALLET_URL = "/custom/app/getMyWallet.do";
    public static final String GET_MY_WALLET_DETAIL_URL = "/custom/app/getMyWalletOp.do";
    public static final String UPLOAD_ONE_FILE_URL = "/custom/app/uploadOneFile.do";

    //志愿者
    public static final String GET_RECOURSE_LIST_URL = "/custom/app/myPopServe.do";
    public static final String NEW_RECOURSE_URL = "/custom/app/insertServe.do";
    public static final String DELETE_SERVE_URL = "/custom/app/deletedMyServe.do";
    public static final String GET_RECOURSE_DETAIL_URL = "/custom/app/myPopServeById.do";
    public static final String REPUBLISH_SERVE_URL = "/custom/app/updateResetServe.do";
    public static final String EVALUATE_SERVE_URL = "/custom/app/insertEvaluation.do";
    public static final String CHANGE_SERVE_STATUS_URL = "/custom/app/toChangeStatus.do";
    public static final String GET_ACCEPTED_SERVE_URL = "/custom/app/myPushServe.do";
    public static final String VOLUNTEER_SERVE_LIST_URL = "/custom/app/volunteerServer.do";
    public static final String ACCEPT_SERVE_URL = "/custom/app/getVolunteerServe.do";
    public static final String APPLY_2BE_VOLUNTEER_URL = "/custom/app/insertVolunteer.do";
    //食堂
    public static final String GET_ALL_FOOD_LIST_URL = "/custom/app/getAllFoodlist.do";
    public static final String INSERT_FOOD_ORDER_URL = "/custom/app/insertFoodOrder.do";
    public static final String UPDATE_FOOD_ORDER_URL = "/custom/app/updateFoodOrder.do";
    public static final String GET_ORDER_DETAIL_URL = "/custom/app/getOrderComplexById.do";

    //报修投诉
    public static final String REPORT_REPAIR_URL = "/custom/app/insertPropertyRepair.do";
    public static final String GET_MY_REPAIR_URL = "/custom/app/getMyPropertyRepair.do";
    public static final String REPORT_SUGGEST_URL = "/custom/app/insertPropertySuggest.do";
    public static final String GET_MY_SUGGEST_URL = "/custom/app/getMyPropertySuggest.do";
    //会所服务
    public static final String GET_SERVICE_TYPE_URL = "/custom/app/getAllServicelist.do";
    public static final String GET_CLUB_SERVICE_URL = "/custom/app/getAllService.do";
    public static final String INSERT_SERVICE_ORDER_URL = "/custom/app/insertServiceOrder.do";
    //积分商城
    public static final String GET_MALL_LIST_URL = "/business/app/getAllProductlist.do";
    public static final String GET_ALL_MALL_PRODUCT_URL = "/custom/app/getAllProduct.do";
    public static final String INSERT_MALL_ORDER_URL = "/custom/app/insertIntegralOrder.do";

    //订单
    public static final String GET_ALL_ORDER_LIST_URL = "/custom/app/getAllOrderListPlus.do";
    public static final String EVALUATE_ORDER_URL = "/custom/app/updateOrderEvaluation.do";
    public static final String UPDATE_ORDER_WITH_REASON_URL = "/custom/app/updateOrderReason.do";

    //活动新闻
    public static final String GET_NEWS_LIST = "/custom/app/getNewsList.do";
    public static final String GET_NEWS_INFO = "/custom/app/infoNews.do";
    public static final String GET_NEWS_SORT_LIST_URL = "/custom/app/getSortList.do";
    //签到
    public static final String SIGN_IN_URL = "/custom/app/insertSignin.do";
    public static final String GET_MONTH_SIGN_IN_URL = "/custom/app/getMonthSignin.do";

    public static String buildImageUrl(String url) {
        return IMAGE_BASE_URL + url;
    }
}
