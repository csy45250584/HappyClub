package com.haokuo.happyclub.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by zjf on 2018/9/7.
 */
@Data
public class UserInfoBean implements Serializable {

    public static final int VOLUNTEER_STATUS_NONE = 0;
    public static final int VOLUNTEER_STATUS_UNCHECKED = 1;
    public static final int VOLUNTEER_STATUS_AGREED = 2;
    public static final int VOLUNTEER_STATUS_REJECTED = 3;

    private Long id;
    private String birthday;
    private String idCard;
    private String sex;
    private String userName;
    private String headPhoto;
    private String realname;
    private int volunteerStatus;
}
