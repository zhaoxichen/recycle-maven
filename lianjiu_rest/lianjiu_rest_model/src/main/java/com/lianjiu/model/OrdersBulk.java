package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersBulk {
	private String orBulkId;

	private Long categoryId;

	private String userId;

	private String userName;

	private String userPhone;

	private Byte orBulkStatus;

	private Byte orBulkExpire;

	private String ordersPrice;

	private String ordersRetrPrice;

	private Date orBulkPaytime;

	private String orBulkHandlerId;

	private String orBulkHandlerTel;

	private Date orBulkCreated;

	private Date orBulkUpdated;

	private String categoryName;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getOrBulkId() {
		return orBulkId;
	}

	public void setOrBulkId(String orBulkId) {
		this.orBulkId = orBulkId == null ? null : orBulkId.trim();
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone == null ? null : userPhone.trim();
	}

	public Byte getOrBulkStatus() {
		return orBulkStatus;
	}

	public void setOrBulkStatus(Byte orBulkStatus) {
		this.orBulkStatus = orBulkStatus;
	}

	public Byte getOrBulkExpire() {
		return orBulkExpire;
	}

	public void setOrBulkExpire(Byte orBulkExpire) {
		this.orBulkExpire = orBulkExpire;
	}

	public String getOrdersPrice() {
		return ordersPrice;
	}

	public void setOrdersPrice(String ordersPrice) {
		this.ordersPrice = ordersPrice == null ? null : ordersPrice.trim();
	}

	public String getOrdersRetrPrice() {
		return ordersRetrPrice;
	}

	public void setOrdersRetrPrice(String ordersRetrPrice) {
		this.ordersRetrPrice = ordersRetrPrice == null ? null : ordersRetrPrice.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrBulkPaytime() {
		return orBulkPaytime;
	}

	public void setOrBulkPaytime(Date orBulkPaytime) {
		this.orBulkPaytime = orBulkPaytime;
	}

	public String getOrBulkHandlerId() {
		return orBulkHandlerId;
	}

	public void setOrBulkHandlerId(String orBulkHandlerId) {
		this.orBulkHandlerId = orBulkHandlerId == null ? null : orBulkHandlerId.trim();
	}

	public String getOrBulkHandlerTel() {
		return orBulkHandlerTel;
	}

	public void setOrBulkHandlerTel(String orBulkHandlerTel) {
		this.orBulkHandlerTel = orBulkHandlerTel == null ? null : orBulkHandlerTel.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrBulkCreated() {
		return orBulkCreated;
	}

	public void setOrBulkCreated(Date orBulkCreated) {
		this.orBulkCreated = orBulkCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrBulkUpdated() {
		return orBulkUpdated;
	}

	public void setOrBulkUpdated(Date orBulkUpdated) {
		this.orBulkUpdated = orBulkUpdated;
	}
}