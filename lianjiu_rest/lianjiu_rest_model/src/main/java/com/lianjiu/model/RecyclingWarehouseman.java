package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RecyclingWarehouseman {
	private String recyclerId;

	private String categoryId;

	private Byte recyclerType;

	private String recyclerName;

	private String recyclerPhone;

	private String recyclerPassword;

	private String recyclerOperator;

	private Byte isExist;

	private Date recyclerCreated;

	private Date recyclerUpdated;

	public String getRecyclerId() {
		return recyclerId;
	}

	public void setRecyclerId(String recyclerId) {
		this.recyclerId = recyclerId == null ? null : recyclerId.trim();
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId == null ? null : categoryId.trim();
	}

	public Byte getRecyclerType() {
		return recyclerType;
	}

	public void setRecyclerType(Byte recyclerType) {
		this.recyclerType = recyclerType;
	}

	public String getRecyclerName() {
		return recyclerName;
	}

	public void setRecyclerName(String recyclerName) {
		this.recyclerName = recyclerName == null ? null : recyclerName.trim();
	}

	public String getRecyclerPhone() {
		return recyclerPhone;
	}

	public void setRecyclerPhone(String recyclerPhone) {
		this.recyclerPhone = recyclerPhone == null ? null : recyclerPhone.trim();
	}

	public String getRecyclerPassword() {
		return recyclerPassword;
	}

	public void setRecyclerPassword(String recyclerPassword) {
		this.recyclerPassword = recyclerPassword == null ? null : recyclerPassword.trim();
	}

	public String getRecyclerOperator() {
		return recyclerOperator;
	}

	public void setRecyclerOperator(String recyclerOperator) {
		this.recyclerOperator = recyclerOperator == null ? null : recyclerOperator.trim();
	}

	public Byte getIsExist() {
		return isExist;
	}

	public void setIsExist(Byte isExist) {
		this.isExist = isExist;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getRecyclerCreated() {
		return recyclerCreated;
	}

	public void setRecyclerCreated(Date recyclerCreated) {
		this.recyclerCreated = recyclerCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getRecyclerUpdated() {
		return recyclerUpdated;
	}

	public void setRecyclerUpdated(Date recyclerUpdated) {
		this.recyclerUpdated = recyclerUpdated;
	}
}