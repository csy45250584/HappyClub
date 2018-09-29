package com.haokuo.happyclub.eventbus;

import lombok.Data;

/**
 * Created by zjf on 2018/9/29.
 */
@Data
public class ServeChangeEvent {
    private Integer preStatus;
    private Integer nextStatus;

    public ServeChangeEvent(Integer preStatus, int nextStatus) {
        this.preStatus = preStatus;
        this.nextStatus = nextStatus;
    }
}
