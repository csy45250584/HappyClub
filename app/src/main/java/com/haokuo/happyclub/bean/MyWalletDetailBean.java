package com.haokuo.happyclub.bean;

import lombok.Data;

/**
 * Created by zjf on 2018/10/22.
 */
@Data
public class MyWalletDetailBean {
    private String createDate;
    private String creator;
    private String message;
    private String operationCode;
    private int money;
    private long userId;
    private int status;
    private long id;
}
