package com.haokuo.happyclub.network.bean.base;

import lombok.Data;

/**
 * Created by zjf on 2018/9/13.
 */
@Data
public class PageParams extends UserIdTokenParams {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private int pageIndex;
    private int pageSize;

    public PageParams(int pageSize) {
        this.pageIndex = 0;
        this.pageSize = pageSize;
    }

    public PageParams() {
        this.pageIndex = 0;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }


}
