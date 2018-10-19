package com.haokuo.happyclub.bean.list;

import com.haokuo.happyclub.bean.NewsBean;

import lombok.Data;

/**
 * Created by zjf on 2018/9/13.
 */
@Data
public class NewsListBean extends ListResultBean<NewsBean> {
    private String url;
}
