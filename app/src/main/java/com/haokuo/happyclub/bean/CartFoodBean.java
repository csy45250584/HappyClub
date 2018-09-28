package com.haokuo.happyclub.bean;

import org.litepal.crud.LitePalSupport;

import lombok.Data;

/**
 * Created by zjf on 2018/9/20.
 */
@Data
public class CartFoodBean extends LitePalSupport {
    private int count;
    private double price;
    private int score;
    private long foodId;
    private String name;
    private String pic;

    public CartFoodBean() {
    }

    public CartFoodBean(int count, double price, int score, long foodId, String name, String pic) {
        this.count = count;
        this.price = price;
        this.score = score;
        this.foodId = foodId;
        this.name = name;
        this.pic = pic;
    }
}
