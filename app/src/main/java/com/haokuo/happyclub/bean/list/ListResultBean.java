package com.haokuo.happyclub.bean.list;

import java.util.List;

import lombok.Data;

/**
 * Created by zjf on 2018/9/13.
 */
@Data
public class ListResultBean<T> {
    protected int count; //总共数据量
    protected List<T> data; //数据
}
