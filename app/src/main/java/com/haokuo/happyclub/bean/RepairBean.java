package com.haokuo.happyclub.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by zjf on 2018/10/8.
 */
@Data
public class RepairBean implements Serializable {
    public static final int STATE_UNHANDLED = 0;
    public static final int STATE_HANDLING = 1;
    public static final int STATE_HANDLED = 2;
    private Long id;//报修项目id
    private Long userId;//用户id
    private String userName;//用户姓名
    private String telphone;//手机
    private String address;//地址
    private Integer repairType;//报修类型  		 1、水电。2、煤气。3、安防。4、公共。5、其他
    private String repairTypeName;//报修类型
    private String reportTime;//上报时间
    private String reportContent;//上报内容
    private String pictureurl;//照片
    //---------------------------------------------------------------------------
    private String repairUser;//处理人
    private String planTime;//预计完成时间

    private String replyUser;//回复人
    private String replyTime;//回复时间
    private String replyContent;//回复内容
    private Integer state;// 状态:0待处理  1处理中  2已完成
}
