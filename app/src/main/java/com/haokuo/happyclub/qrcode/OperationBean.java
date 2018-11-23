package com.haokuo.happyclub.qrcode;

import lombok.Data;

/**
 * Created by zjf on 2018/11/22.
 */
@Data
public class OperationBean {
    public static final int OPERATION_VOLUNTEER_ACTIVITY = 1;
    /** 二维码扫描基类，用以标识二维码扫描后的操作 */
    private int operation;

    public OperationBean() {
    }

    public OperationBean(int operation) {
        this.operation = operation;
    }
}
