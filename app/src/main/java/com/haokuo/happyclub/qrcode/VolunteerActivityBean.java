package com.haokuo.happyclub.qrcode;

import lombok.Data;

/**
 * Created by zjf on 2018/11/22.
 */
@Data
public class VolunteerActivityBean extends OperationBean {
    public static final int TYPE_SIGN_UP = 1;
    public static final int TYPE_SIGN_OUT = 2;

    private long activityId;
    private int signType; // 1 : 签到 , 2: 签退

    public VolunteerActivityBean(int operation, long activityId, int signType) {
        super(operation);
        this.activityId = activityId;
        this.signType = signType;
    }
}
