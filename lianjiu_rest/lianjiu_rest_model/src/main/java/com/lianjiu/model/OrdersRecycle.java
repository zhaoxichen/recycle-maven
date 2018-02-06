package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 * 快递回收\r\n
 * @author jdandian.com
 * @date 2017年09月01日
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersRecycle {

    private Integer orecycleId;
    /**
     * 用户发布回收商品生成的订单表
     */
    private String userName;

    private Integer userPhone;

    private String userAddress;

    private String userId;

    private String postNum;
    /**
     * 个人用户回收订单
     */
    private Integer oitemId;

    private String rebackPodtNum;
    /**
     * 回收类型（上门回收、去加盟商回收、快递回收）
     */
    private String recycleType;

    private double firstVertifytotalPrice;

    private double lastVertifytotalPrice;
    /**
     * 支付状态 ：0已经付款 1未付款 
     */
    private Integer salePayStatus;
    /**
     * 备注和评论
     */
    private String remark;

    private String operatePerson;

    private Integer recyclePersonNum;
    /**
     * 订单生成日期
     */
    private Date orderStartTime;
    /**
     * 订单结束日期
     */
    private Date orderEndTime;
    /**
     * 加盟商交货编号
     */
    private String providerOrderId;
    /**
     * 买方货款信息（0未付款、1已付款）
     */
    private Integer buyPaymentStatus;
    /**
     * 订单状态（0未交货、1已交货、2交货中、3已交货、4退货）
     */
    private Integer orderStatus;

    public Integer getOrecycleId() {
        return orecycleId;
    }
    public void setOrecycleId(Integer orecycleId) {
        this.orecycleId = orecycleId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Integer getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(Integer userPhone) {
        this.userPhone = userPhone;
    }
    public String getUserAddress() {
        return userAddress;
    }
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getPostNum() {
        return postNum;
    }
    public void setPostNum(String postNum) {
        this.postNum = postNum;
    }
    public Integer getOitemId() {
        return oitemId;
    }
    public void setOitemId(Integer oitemId) {
        this.oitemId = oitemId;
    }
    public String getRebackPodtNum() {
        return rebackPodtNum;
    }
    public void setRebackPodtNum(String rebackPodtNum) {
        this.rebackPodtNum = rebackPodtNum;
    }
    public String getRecycleType() {
        return recycleType;
    }
    public void setRecycleType(String recycleType) {
        this.recycleType = recycleType;
    }
    public double getFirstVertifytotalPrice() {
        return firstVertifytotalPrice;
    }
    public void setFirstVertifytotalPrice(double firstVertifytotalPrice) {
        this.firstVertifytotalPrice = firstVertifytotalPrice;
    }
    public double getLastVertifytotalPrice() {
        return lastVertifytotalPrice;
    }
    public void setLastVertifytotalPrice(double lastVertifytotalPrice) {
        this.lastVertifytotalPrice = lastVertifytotalPrice;
    }
    public Integer getSalePayStatus() {
        return salePayStatus;
    }
    public void setSalePayStatus(Integer salePayStatus) {
        this.salePayStatus = salePayStatus;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getOperatePerson() {
        return operatePerson;
    }
    public void setOperatePerson(String operatePerson) {
        this.operatePerson = operatePerson;
    }
    public Integer getRecyclePersonNum() {
        return recyclePersonNum;
    }
    public void setRecyclePersonNum(Integer recyclePersonNum) {
        this.recyclePersonNum = recyclePersonNum;
    }
    public Date getOrderStartTime() {
        return orderStartTime;
    }
    public void setOrderStartTime(Date orderStartTime) {
        this.orderStartTime = orderStartTime;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getOrderEndTime() {
        return orderEndTime;
    }
    public void setOrderEndTime(Date orderEndTime) {
        this.orderEndTime = orderEndTime;
    }
    public String getProviderOrderId() {
        return providerOrderId;
    }
    public void setProviderOrderId(String providerOrderId) {
        this.providerOrderId = providerOrderId;
    }
    public Integer getBuyPaymentStatus() {
        return buyPaymentStatus;
    }
    public void setBuyPaymentStatus(Integer buyPaymentStatus) {
        this.buyPaymentStatus = buyPaymentStatus;
    }
    public Integer getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

}