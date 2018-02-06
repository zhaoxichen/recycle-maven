package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 * 发货单
 * @author jdandian.com
 * @date 2017年09月01日
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersShipping {
    /**
     * 出车订单编号
     */
    private String oshipOrderId;
    /**
     * 收货人全名
     */
    private String oshipReceiverName;
    /**
     * 固定电话
     */
    private String oshipReceiverPhone;
    /**
     * 移动电话
     */
    private String oshipReceiverMobile;
    /**
     * 省份
     */
    private String oshipReceiverState;
    /**
     * 城市
     */
    private String oshipReceiverCity;
    /**
     * 区/县
     */
    private String oshipReceiverDistrict;
    /**
     * 收货地址，如：xx路xx号
     */
    private String oshipReceiverAddress;
    /**
     * 邮政编码,如：310001
     */
    private String oshipReceiverZip;
    /**
     * 订单创建时间
     */
    private Date oshipCreated;
    /**
     * 订单更新时间
     */
    private Date oshipUpdated;

    public String getOshipOrderId() {
        return oshipOrderId;
    }
    public void setOshipOrderId(String oshipOrderId) {
        this.oshipOrderId = oshipOrderId;
    }
    public String getOshipReceiverName() {
        return oshipReceiverName;
    }
    public void setOshipReceiverName(String oshipReceiverName) {
        this.oshipReceiverName = oshipReceiverName;
    }
    public String getOshipReceiverPhone() {
        return oshipReceiverPhone;
    }
    public void setOshipReceiverPhone(String oshipReceiverPhone) {
        this.oshipReceiverPhone = oshipReceiverPhone;
    }
    public String getOshipReceiverMobile() {
        return oshipReceiverMobile;
    }
    public void setOshipReceiverMobile(String oshipReceiverMobile) {
        this.oshipReceiverMobile = oshipReceiverMobile;
    }
    public String getOshipReceiverState() {
        return oshipReceiverState;
    }
    public void setOshipReceiverState(String oshipReceiverState) {
        this.oshipReceiverState = oshipReceiverState;
    }
    public String getOshipReceiverCity() {
        return oshipReceiverCity;
    }
    public void setOshipReceiverCity(String oshipReceiverCity) {
        this.oshipReceiverCity = oshipReceiverCity;
    }
    public String getOshipReceiverDistrict() {
        return oshipReceiverDistrict;
    }
    public void setOshipReceiverDistrict(String oshipReceiverDistrict) {
        this.oshipReceiverDistrict = oshipReceiverDistrict;
    }
    public String getOshipReceiverAddress() {
        return oshipReceiverAddress;
    }
    public void setOshipReceiverAddress(String oshipReceiverAddress) {
        this.oshipReceiverAddress = oshipReceiverAddress;
    }
    public String getOshipReceiverZip() {
        return oshipReceiverZip;
    }
    public void setOshipReceiverZip(String oshipReceiverZip) {
        this.oshipReceiverZip = oshipReceiverZip;
    }
    public Date getOshipCreated() {
        return oshipCreated;
    }
    public void setOshipCreated(Date oshipCreated) {
        this.oshipCreated = oshipCreated;
    }
    public Date getOshipUpdated() {
        return oshipUpdated;
    }
    public void setOshipUpdated(Date oshipUpdated) {
        this.oshipUpdated = oshipUpdated;
    }

}