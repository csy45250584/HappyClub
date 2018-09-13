package com.haokuo.happyclub.bean.list;

import java.util.List;

import lombok.Data;

/**
 * Created by zjf on 2018/9/13.
 */
@Data
public class ListResultBean<T> {
    protected int count;
    protected List<T> data;
}
