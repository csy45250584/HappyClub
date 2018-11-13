package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.PageParams;

import lombok.Data;

/**
 * Created by zjf on 2018/11/9.
 */
@Data
public class GetCourseListParams extends PageParams {
    private String courseName;
    private Long courselistId;

    public GetCourseListParams(String courseName, Long courselistId) {
        this.courseName = courseName;
        this.courselistId = courselistId;
    }
}
