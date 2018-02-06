package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Orders {
	/**
	 * 订单id
	 */
	private String ordersId;
	/**
	 * 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
	 */
	private String ordersPayment;
	/**
	 * 支付类型，1、在线支付，2、货到付款
	 */
	private Integer ordersPaymentType;
	/**
	 * 邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分
	 */
	private String ordersPostFee;
	/**
	 * 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
	 */
	private Integer ordersStatus;
	/**
	 * 付款时间
	 */
	private Date ordersPaymentTime;
	/**
	 * 发货时间
	 */
	private Date ordersConsignTime;
	/**
	 * 交易完成时间
	 */
	private Date ordersEndTime;
	/**
	 * 交易关闭时间
	 */
	private Date ordersCloseTime;
	/**
	 * 物流名称
	 */
	private String ordersShippingName;
	/**
	 * 物流单号
	 */
	private String ordersShippingCode;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 买家留言
	 */
	private String ordersBuyerMessage;
	/**
	 * 买家昵称
	 */
	private String ordersBuyerNick;
	/**
	 * 买家是否已经评价
	 */
	private Integer ordersBuyerRate;
	/**
	 * 订单创建时间
	 */
	private Date ordersCreateTime;
	/**
	 * 订单更新时间
	 */
	private Date ordersUpdateTime;

	public String getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId;
	}

	public String getOrdersPayment() {
		return ordersPayment;
	}

	public void setOrdersPayment(String ordersPayment) {
		this.ordersPayment = ordersPayment;
	}

	public Integer getOrdersPaymentType() {
		return ordersPaymentType;
	}

	public void setOrdersPaymentType(Integer ordersPaymentType) {
		this.ordersPaymentType = ordersPaymentType;
	}

	public String getOrdersPostFee() {
		return ordersPostFee;
	}

	public void setOrdersPostFee(String ordersPostFee) {
		this.ordersPostFee = ordersPostFee;
	}

	public Integer getOrdersStatus() {
		return ordersStatus;
	}

	public void setOrdersStatus(Integer ordersStatus) {
		this.ordersStatus = ordersStatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrdersPaymentTime() {
		return ordersPaymentTime;
	}

	public void setOrdersPaymentTime(Date ordersPaymentTime) {
		this.ordersPaymentTime = ordersPaymentTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrdersConsignTime() {
		return ordersConsignTime;
	}

	public void setOrdersConsignTime(Date ordersConsignTime) {
		this.ordersConsignTime = ordersConsignTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrdersEndTime() {
		return ordersEndTime;
	}

	public void setOrdersEndTime(Date ordersEndTime) {
		this.ordersEndTime = ordersEndTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrdersCloseTime() {
		return ordersCloseTime;
	}

	public void setOrdersCloseTime(Date ordersCloseTime) {
		this.ordersCloseTime = ordersCloseTime;
	}

	public String getOrdersShippingName() {
		return ordersShippingName;
	}

	public void setOrdersShippingName(String ordersShippingName) {
		this.ordersShippingName = ordersShippingName;
	}

	public String getOrdersShippingCode() {
		return ordersShippingCode;
	}

	public void setOrdersShippingCode(String ordersShippingCode) {
		this.ordersShippingCode = ordersShippingCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrdersBuyerMessage() {
		return ordersBuyerMessage;
	}

	public void setOrdersBuyerMessage(String ordersBuyerMessage) {
		this.ordersBuyerMessage = ordersBuyerMessage;
	}

	public String getOrdersBuyerNick() {
		return ordersBuyerNick;
	}

	public void setOrdersBuyerNick(String ordersBuyerNick) {
		this.ordersBuyerNick = ordersBuyerNick;
	}

	public Integer getOrdersBuyerRate() {
		return ordersBuyerRate;
	}

	public void setOrdersBuyerRate(Integer ordersBuyerRate) {
		this.ordersBuyerRate = ordersBuyerRate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrdersCreateTime() {
		return ordersCreateTime;
	}

	public void setOrdersCreateTime(Date ordersCreateTime) {
		this.ordersCreateTime = ordersCreateTime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrdersUpdateTime() {
		return ordersUpdateTime;
	}

	public void setOrdersUpdateTime(Date ordersUpdateTime) {
		this.ordersUpdateTime = ordersUpdateTime;
	}

}