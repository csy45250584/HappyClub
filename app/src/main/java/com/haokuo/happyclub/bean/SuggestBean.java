package com.haokuo.happyclub.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by zjf on 2018/10/8.
 */
@Data
public class SuggestBean implements Serializable {
    public static final int STATE_UNHANDLED = 0;
    public static final int STATE_HANDLED = 1;
    private Long id;
    private Long userId;
    private String reportTime;//上报时间
    private String reportContent;//上报内容
    private String replyTime;//回复时间

    private String userName;//姓名
    private String telphone;//手机号
    private String pictureurl;//投诉照片
    //------------------------------------------------------------------------------

    private Integer state; //0、未处理。1、已处理

    private String repairUser;//处理人
    private String planTime;//预计完成时间

    private String replyContent;//回复内容
    private String replyUser;//回复人
}
