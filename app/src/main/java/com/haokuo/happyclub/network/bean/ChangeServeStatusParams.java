package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.IdParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/18.
 */
@Data
public class ChangeServeStatusParams extends IdParams {
    private int status;

    public ChangeServeStatusParams(long id, int status) {
        super(id);
        this.status = status;
    }
}
