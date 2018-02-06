package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersPayment {
	private String paymentId;

	private String paymentFee;

	private String ordersId;

	private String ordersFee;

	private String userId;

	private Byte paymentStatus;

	private Byte paymentType;

	private String paymentSign;

	private Byte paymentFrom;

	private Date paymentCreated;

	private Date paymentUpdated;

	private String paymentMessage;

	private String paymentNotifyMessage;

	public OrdersPayment() {
	}

	public OrdersPayment(String paymentId, String ordersId, String ordersFee, String userId) {
		this.paymentId = paymentId;
		this.ordersId = ordersId;
		this.ordersFee = ordersFee;
		this.userId = userId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId == null ? null : paymentId.trim();
	}

	public String getPaymentFee() {
		return paymentFee;
	}

	public void setPaymentFee(String paymentFee) {
		this.paymentFee = paymentFee == null ? null : paymentFee.trim();
	}

	public String getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId == null ? null : ordersId.trim();
	}

	public String getOrdersFee() {
		return ordersFee;
	}

	public void setOrdersFee(String ordersFee) {
		this.ordersFee = ordersFee == null ? null : ordersFee.trim();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public Byte getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Byte paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Byte getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Byte paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentSign() {
		return paymentSign;
	}

	public void setPaymentSign(String paymentSign) {
		this.paymentSign = paymentSign == null ? null : paymentSign.trim();
	}

	public Byte getPaymentFrom() {
		return paymentFrom;
	}

	public void setPaymentFrom(Byte paymentFrom) {
		this.paymentFrom = paymentFrom;
	}

	public Date getPaymentCreated() {
		return paymentCreated;
	}

	public void setPaymentCreated(Date paymentCreated) {
		this.paymentCreated = paymentCreated;
	}

	public Date getPaymentUpdated() {
		return paymentUpdated;
	}

	public void setPaymentUpdated(Date paymentUpdated) {
		this.paymentUpdated = paymentUpdated;
	}

	public String getPaymentMessage() {
		return paymentMessage;
	}

	public void setPaymentMessage(String paymentMessage) {
		this.paymentMessage = paymentMessage;
	}

	public String getPaymentNotifyMessage() {
		return paymentNotifyMessage;
	}

	public void setPaymentNotifyMessage(String paymentNotifyMessage) {
		this.paymentNotifyMessage = paymentNotifyMessage;
	}

}