package com.haokuo.happyclub.bean;

import lombok.Data;

/**
 * Created by zjf on 2018/12/26.
 */
@Data
public class UpdateBean {
    private Boolean updateFlag; //标识是否有更新，由请求参数中的versionCode与服务器存储的versionCode对比得到
    private VersionBean version; //标识是否有更新，由请求参数中的versionCode与服务器存储的versionCode对比得到

    @Data
    public static class VersionBean {
        private Integer id;
        private Integer versionCode; //5位数版本号 例：10001
        private String versionName; //例如："1.0.1"
        private String url; //下载地址
        private String content; //更新内容 分行时注意使用\r\n
        private Long size; //文件大小，使用file.length()获得，单位Byte
        private Integer type; //用于区别用户端(1)和商家端(2)
        private Boolean compulsive; //标识是否强制更新，某些情况下必须更
        private Boolean ignorable; //标识本次更新是否可以忽略，一般情况下为true
    }
}
