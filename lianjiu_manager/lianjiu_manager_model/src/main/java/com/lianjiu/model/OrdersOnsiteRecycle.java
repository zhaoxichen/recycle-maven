package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersOnsiteRecycle {
    /**
     * 上门回收编号
     */
    private String ositereId;
    /**
     * 用户编号
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户电话号码
     */
    private Integer userPhonenum;
    /**
     * 用户地址
     */
    private String userAddress;
    /**
     * 订单开始时间
     */
    private Date ositereStartTime;
    /**
     * 上门回收订单状态（1上门中，2已结算，3已取消）
     */
    private Integer ositereStatus;
    /**
     * 第一次价钱
     */
    private double ositereFirstPrice;
    /**
     * 最终价钱
     */
    private double ositereLastPrice;
    /**
     * 回收付款时间
     */
    private Date ositerePayTime;
    /**
     * 回收商编号
     */
    private Integer recyclePersonId;

    private Integer itemId;
    /**
     * 回收人员
     */
    private String ositereRecyclePerson;
    /**
     * 上门回收订单评论
     */
    private String ositereCustomerRemark;

    public String getOsitereId() {
        return ositereId;
    }
    public void setOsitereId(String ositereId) {
        this.ositereId = ositereId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Integer getUserPhonenum() {
        return userPhonenum;
    }
    public void setUserPhonenum(Integer userPhonenum) {
        this.userPhonenum = userPhonenum;
    }
    public String getUserAddress() {
        return userAddress;
    }
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
    public Date getOsitereStartTime() {
        return ositereStartTime;
    }
    public void setOsitereStartTime(Date ositereStartTime) {
        this.ositereStartTime = ositereStartTime;
    }
    public Integer getOsitereStatus() {
        return ositereStatus;
    }
    public void setOsitereStatus(Integer ositereStatus) {
        this.ositereStatus = ositereStatus;
    }
    public double getOsitereFirstPrice() {
        return ositereFirstPrice;
    }
    public void setOsitereFirstPrice(double ositereFirstPrice) {
        this.ositereFirstPrice = ositereFirstPrice;
    }
    public double getOsitereLastPrice() {
        return ositereLastPrice;
    }
    public void setOsitereLastPrice(double ositereLastPrice) {
        this.ositereLastPrice = ositereLastPrice;
    }
    public Date getOsiterePayTime() {
        return ositerePayTime;
    }
    public void setOsiterePayTime(Date ositerePayTime) {
        this.ositerePayTime = ositerePayTime;
    }
    public Integer getRecyclePersonId() {
        return recyclePersonId;
    }
    public void setRecyclePersonId(Integer recyclePersonId) {
        this.recyclePersonId = recyclePersonId;
    }
    public Integer getItemId() {
        return itemId;
    }
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    public String getOsitereRecyclePerson() {
        return ositereRecyclePerson;
    }
    public void setOsitereRecyclePerson(String ositereRecyclePerson) {
        this.ositereRecyclePerson = ositereRecyclePerson;
    }
    public String getOsitereCustomerRemark() {
        return ositereCustomerRemark;
    }
    public void setOsitereCustomerRemark(String ositereCustomerRemark) {
        this.ositereCustomerRemark = ositereCustomerRemark;
    }

}