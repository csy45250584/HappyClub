package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/11/14.
 */
@Data
public class TransferPointsParams extends UserIdTokenParams {
   private Long  oppositeId;
   private Integer  credit;

   public TransferPointsParams(Long oppositeId, Integer credit) {
      this.oppositeId = oppositeId;
      this.credit = credit;
   }
}
