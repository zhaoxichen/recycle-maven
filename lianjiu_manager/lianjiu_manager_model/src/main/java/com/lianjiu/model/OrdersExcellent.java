package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersExcellent {
	private String orExcellentId;

	private String userId;

	private Long categoryId;

	private String orExcellentUser;

	private String orExcellentPhone;

	private String orExcellentHandler;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orExcellentHandleTime;

	private Byte orExcellentStatus;

	private String orExcellentProvince;

	private String orExcellentCity;

	private String orExcellentDistrict;

	private String orExcellentLocation;

	private Date orExcellentCreated;

	private Date orExcellentUpdated;

	private String addressId;

	private String orItemsNamePreview;

	private String orItemsPictruePreview;

	private String ordersPrice;

	private String orExceDetailsPrice;

	private String orExceDetailsExpressNum;
	
	private String orExceDetailsExpressName;

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getOrExcellentId() {
		return orExcellentId;
	}

	public void setOrExcellentId(String orExcellentId) {
		this.orExcellentId = orExcellentId == null ? null : orExcellentId.trim();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getOrExcellentUser() {
		return orExcellentUser;
	}

	public void setOrExcellentUser(String orExcellentUser) {
		this.orExcellentUser = orExcellentUser == null ? null : orExcellentUser.trim();
	}

	public String getOrExcellentPhone() {
		return orExcellentPhone;
	}

	public void setOrExcellentPhone(String orExcellentPhone) {
		this.orExcellentPhone = orExcellentPhone == null ? null : orExcellentPhone.trim();
	}

	public String getOrExcellentHandler() {
		return orExcellentHandler;
	}

	public void setOrExcellentHandler(String orExcellentHandler) {
		this.orExcellentHandler = orExcellentHandler == null ? null : orExcellentHandler.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrExcellentHandleTime() {
		return orExcellentHandleTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public void setOrExcellentHandleTime(Date orExcellentHandleTime) {
		this.orExcellentHandleTime = orExcellentHandleTime;
	}

	public Byte getOrExcellentStatus() {
		return orExcellentStatus;
	}

	public void setOrExcellentStatus(Byte orExcellentStatus) {
		this.orExcellentStatus = orExcellentStatus;
	}

	public String getOrExcellentProvince() {
		return orExcellentProvince;
	}

	public void setOrExcellentProvince(String orExcellentProvince) {
		this.orExcellentProvince = orExcellentProvince == null ? null : orExcellentProvince.trim();
	}

	public String getOrExcellentCity() {
		return orExcellentCity;
	}

	public void setOrExcellentCity(String orExcellentCity) {
		this.orExcellentCity = orExcellentCity == null ? null : orExcellentCity.trim();
	}

	public String getOrExcellentDistrict() {
		return orExcellentDistrict;
	}

	public void setOrExcellentDistrict(String orExcellentDistrict) {
		this.orExcellentDistrict = orExcellentDistrict == null ? null : orExcellentDistrict.trim();
	}

	public String getOrExcellentLocation() {
		return orExcellentLocation;
	}

	public void setOrExcellentLocation(String orExcellentLocation) {
		this.orExcellentLocation = orExcellentLocation == null ? null : orExcellentLocation.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrExcellentCreated() {
		return orExcellentCreated;
	}

	public void setOrExcellentCreated(Date orExcellentCreated) {
		this.orExcellentCreated = orExcellentCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrExcellentUpdated() {
		return orExcellentUpdated;
	}

	public void setOrExcellentUpdated(Date orExcellentUpdated) {
		this.orExcellentUpdated = orExcellentUpdated;
	}

	public String getOrItemsNamePreview() {
		return orItemsNamePreview;
	}

	public void setOrItemsNamePreview(String orItemsNamePreview) {
		this.orItemsNamePreview = orItemsNamePreview;
	}

	public String getOrItemsPictruePreview() {
		return orItemsPictruePreview;
	}

	public void setOrItemsPictruePreview(String orItemsPictruePreview) {
		this.orItemsPictruePreview = orItemsPictruePreview;
	}

	public String getOrdersPrice() {
		return ordersPrice;
	}

	public void setOrdersPrice(String ordersPrice) {
		this.ordersPrice = ordersPrice;
	}

	public String getOrExceDetailsPrice() {
		return orExceDetailsPrice;
	}

	public void setOrExceDetailsPrice(String orExceDetailsPrice) {
		this.orExceDetailsPrice = orExceDetailsPrice;
	}

	public String getOrExceDetailsExpressNum() {
		return orExceDetailsExpressNum;
	}

	public void setOrExceDetailsExpressNum(String orExceDetailsExpressNum) {
		this.orExceDetailsExpressNum = orExceDetailsExpressNum;
	}

	public String getOrExceDetailsExpressName() {
		return orExceDetailsExpressName;
	}

	public void setOrExceDetailsExpressName(String orExceDetailsExpressName) {
		this.orExceDetailsExpressName = orExceDetailsExpressName;
	}

}