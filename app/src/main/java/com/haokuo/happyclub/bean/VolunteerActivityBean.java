package com.haokuo.happyclub.bean;

import lombok.Data;

/**
 * Created by zjf on 2018/11/21.
 */
@Data
public class VolunteerActivityBean {
    public  static  final  int STATUS_PROCESS = 2;
    public  static  final  int STATUS_FINISHED = 3;
    private String activityName;
    private String address;
    private Integer count;
    private Integer credit;
    private String description;
    private String endTime;
    private String startTime;
    private String image;
    private Long id;
    private Integer joinCount;
    private String latitude;
    private String longitude;
    private Integer state;
    private Integer status; //查询(活动状态)    2:进行中  3.已结束
}
