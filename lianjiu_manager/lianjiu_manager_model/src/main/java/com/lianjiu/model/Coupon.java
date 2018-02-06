package com.lianjiu.model;

import java.sql.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 优惠券信息
 * @author jdandian.com
 * @date 2017年09月01日
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Coupon {
    /**
     * 优惠券编号
     */
    private String couponId;
    /**
     * 使用开始时间
     */
    private Date startTime;
    /**
     * 使用到期时间
     */
    private Date endTime;
    /**
     * 优惠券主题
     */
    private String couponTitle;
    /**
     * 优惠金额
     */
    private Integer couponMoney;
    /**
     * 优惠比例
     */
    private Integer couponRatio;
    /**
     * 用户ID
     */
    private String userId;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }
    public String getCouponId() {
        return couponId;
    }
    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public String getCouponTitle() {
        return couponTitle;
    }
    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }
    public Integer getCouponMoney() {
        return couponMoney;
    }
    public void setCouponMoney(Integer couponMoney) {
        this.couponMoney = couponMoney;
    }
    public Integer getCouponRatio() {
        return couponRatio;
    }
    public void setCouponRatio(Integer couponRatio) {
        this.couponRatio = couponRatio;
    }

}