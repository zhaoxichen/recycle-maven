package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersRepair {
	private String orRepairId;

	private String userId;

	private Long categoryId;

	private String orRepairUser;

	private String orRepairPhone;

	private String orRepairTechnicians;

	private Byte orRepairStatus;

	private String addressId;

	private String orRepairProvince;

	private String orRepairCity;

	private String orRepairDistrict;

	private String orRepairLocation;
	
	private String orRepairScheme;

	private Date orRepairCreated;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orRepairHandleTime;
	// 预约上门时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date orRepairVisitTime;

	private Date orRepairUpdated;

	private String orRepairRemarks;

	public String getOrRepairId() {
		return orRepairId;
	}

	public void setOrRepairId(String orRepairId) {
		this.orRepairId = orRepairId == null ? null : orRepairId.trim();
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

	public String getOrRepairUser() {
		return orRepairUser;
	}

	public void setOrRepairUser(String orRepairUser) {
		this.orRepairUser = orRepairUser == null ? null : orRepairUser.trim();
	}

	public String getOrRepairPhone() {
		return orRepairPhone;
	}

	public void setOrRepairPhone(String orRepairPhone) {
		this.orRepairPhone = orRepairPhone == null ? null : orRepairPhone.trim();
	}

	public String getOrRepairTechnicians() {
		return orRepairTechnicians;
	}

	public void setOrRepairTechnicians(String orRepairTechnicians) {
		this.orRepairTechnicians = orRepairTechnicians == null ? null : orRepairTechnicians.trim();
	}

	public Byte getOrRepairStatus() {
		return orRepairStatus;
	}

	public void setOrRepairStatus(Byte orRepairStatus) {
		this.orRepairStatus = orRepairStatus;
	}

	public String getOrRepairProvince() {
		return orRepairProvince;
	}

	public void setOrRepairProvince(String orRepairProvince) {
		this.orRepairProvince = orRepairProvince == null ? null : orRepairProvince.trim();
	}

	public String getOrRepairCity() {
		return orRepairCity;
	}

	public void setOrRepairCity(String orRepairCity) {
		this.orRepairCity = orRepairCity == null ? null : orRepairCity.trim();
	}

	public String getOrRepairDistrict() {
		return orRepairDistrict;
	}

	public void setOrRepairDistrict(String orRepairDistrict) {
		this.orRepairDistrict = orRepairDistrict == null ? null : orRepairDistrict.trim();
	}

	public String getOrRepairLocation() {
		return orRepairLocation;
	}

	public void setOrRepairLocation(String orRepairLocation) {
		this.orRepairLocation = orRepairLocation == null ? null : orRepairLocation.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getOrRepairCreated() {
		return orRepairCreated;
	}

	public void setOrRepairCreated(Date orRepairCreated) {
		this.orRepairCreated = orRepairCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrRepairHandleTime() {
		return orRepairHandleTime;
	}

	public void setOrRepairHandleTime(Date orRepairHandleTime) {
		this.orRepairHandleTime = orRepairHandleTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrRepairUpdated() {
		return orRepairUpdated;
	}

	public void setOrRepairUpdated(Date orRepairUpdated) {
		this.orRepairUpdated = orRepairUpdated;
	}

	public String getOrRepairRemarks() {
		return orRepairRemarks;
	}

	public void setOrRepairRemarks(String orRepairRemarks) {
		this.orRepairRemarks = orRepairRemarks == null ? null : orRepairRemarks.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getOrRepairVisitTime() {
		return orRepairVisitTime;
	}

	public void setOrRepairVisitTime(Date orRepairVisitTime) {
		this.orRepairVisitTime = orRepairVisitTime;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getOrRepairScheme() {
		return orRepairScheme;
	}

	public void setOrRepairScheme(String orRepairScheme) {
		this.orRepairScheme = orRepairScheme;
	}

}