package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/11/9.
 */
@Data
public class CourseIdParams extends UserIdTokenParams {
    private Long courseId;

    public CourseIdParams(Long courseId) {
        this.courseId = courseId;
    }
}
