package com.haokuo.happyclub.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by zjf on 2018/9/14.
 */
@Data
public class RecourseBean implements Serializable {
    private long id;
    private String serveName;   //服务名称
    private String startTime;    //开始时间
    private String endTime;        //结束时间
    private String address;        //服务地址
    private String longitude;    //经度
    private String latitude;    //维度
    private String level;        //任务等级

    private Double price;        //所得金币
    private Double integral;    //所得积分
    private String claim;        //任务要求
    private String content;        //任务内容
    private Integer origin;        //服务来源
    private Integer reviewState;    //审核状态(0:未审核; 1:已审核; 2:已拒绝; 11:已接单; 22:服务中; 33:已服务; 44:已完成;55:已评价;88:已放弃)
    private String description; //描述
    private String volunteer;    //接单人
    private String userTel;        //求助人信息
    private float distance;        //手机定位距离服务距离
}
