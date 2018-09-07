package com.haokuo.happyclub.network.bean.base;

import lombok.Data;

/**
 * Created by zjf on 2018-08-15.
 */
@Data
public class UserIdTokenParams {
    private long userId;
    private String token;

//    public UserIdTokenParams(long userId, String token) {
//        this.userId = userId;
//        this.token = token;
//    }

    public UserIdTokenParams() {
        //        this.userId = MySpUtil.getInstance().getUserId();
        //        this.token = MySpUtil.getInstance().getToken();
        this.userId = 20;
        this.token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJFU0JQIiwibmFtZSI6IjEzNzU0MzI3MjMyIiwiZXhwIjoxNTM2NDcxMTAwLCJpYXQiOjE1MzYyMTE5MDAsImp0aSI6MjAsImFjY291bnQiOiIxMzc1NDMyNzIzMiJ9.wCwqJ3Gnz_rTk2dyfkPoTHj9GP2vzsXho4lw6eXSYa4";
    }


}
