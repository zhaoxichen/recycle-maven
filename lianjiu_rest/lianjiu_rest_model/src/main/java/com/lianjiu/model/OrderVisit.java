package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 上门回收订单
 * @author jdandian.com
 * @date 2017年09月01日
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrderVisit {
    /**
     * 订单编号
     */
    private String ovisitId;
    /**
     * 订单时间
     */
    private Date ovisitTime;
    /**
     * 用户编号
     */
    private String userId;
    /**
     * 用户电话
     */
    private Integer userPhone;
    /**
     * 用户姓名
     */
    private String username;
    /**
     * 用户地址
     */
    private String userAddress;
    /**
     * 加盟商编号
     */
    private String buinessId;
    /**
     * 回收车编号
     */
    private String receclecarId;
    /**
     * 订单价格
     */
    private String ovisitPrice;
    /**
     * 加盟商类型(0普通加盟商1物业加盟商)
     */
    private Integer buinessType;
    /**
     * 最低回收价格
     */
    private String ovisitLessRecyclePrice;
    /**
     * 回收状态(0派单中1上门中2已结算)
     */
    private Integer recrcleState;
    /**
     * 加盟商数量
     */
    private Integer buinessCount;
    /**
     * 上门结果(0回收价格1取消订单)
     */
    private Integer ovisitResult;
    /**
     * 评论编号
     */
    private String ovisitCommentId;

    public String getOvisitId() {
        return ovisitId;
    }
    public void setOvisitId(String ovisitId) {
        this.ovisitId = ovisitId;
    }
    public Date getOvisitTime() {
        return ovisitTime;
    }
    public void setOvisitTime(Date ovisitTime) {
        this.ovisitTime = ovisitTime;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Integer getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(Integer userPhone) {
        this.userPhone = userPhone;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUserAddress() {
        return userAddress;
    }
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
    public String getBuinessId() {
        return buinessId;
    }
    public void setBuinessId(String buinessId) {
        this.buinessId = buinessId;
    }
    public String getRececlecarId() {
        return receclecarId;
    }
    public void setRececlecarId(String receclecarId) {
        this.receclecarId = receclecarId;
    }
    public String getOvisitPrice() {
        return ovisitPrice;
    }
    public void setOvisitPrice(String ovisitPrice) {
        this.ovisitPrice = ovisitPrice;
    }
    public Integer getBuinessType() {
        return buinessType;
    }
    public void setBuinessType(Integer buinessType) {
        this.buinessType = buinessType;
    }
    public String getOvisitLessRecyclePrice() {
        return ovisitLessRecyclePrice;
    }
    public void setOvisitLessRecyclePrice(String ovisitLessRecyclePrice) {
        this.ovisitLessRecyclePrice = ovisitLessRecyclePrice;
    }
    public Integer getRecrcleState() {
        return recrcleState;
    }
    public void setRecrcleState(Integer recrcleState) {
        this.recrcleState = recrcleState;
    }
    public Integer getBuinessCount() {
        return buinessCount;
    }
    public void setBuinessCount(Integer buinessCount) {
        this.buinessCount = buinessCount;
    }
    public Integer getOvisitResult() {
        return ovisitResult;
    }
    public void setOvisitResult(Integer ovisitResult) {
        this.ovisitResult = ovisitResult;
    }
    public String getOvisitCommentId() {
        return ovisitCommentId;
    }
    public void setOvisitCommentId(String ovisitCommentId) {
        this.ovisitCommentId = ovisitCommentId;
    }

}