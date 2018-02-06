package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersRemarks {
	private String orRemarksId;

	private String ordersId;

	private String adminUsername;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orRemarksCreated;

	private Date orRemarksUpdated;

	private String orRemarksContent;

	public String getOrRemarksId() {
		return orRemarksId;
	}

	public void setOrRemarksId(String orRemarksId) {
		this.orRemarksId = orRemarksId == null ? null : orRemarksId.trim();
	}

	public String getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId == null ? null : ordersId.trim();
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername == null ? null : adminUsername.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrRemarksCreated() {
		return orRemarksCreated;
	}

	public void setOrRemarksCreated(Date orRemarksCreated) {
		this.orRemarksCreated = orRemarksCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getOrRemarksUpdated() {
		return orRemarksUpdated;
	}

	public void setOrRemarksUpdated(Date orRemarksUpdated) {
		this.orRemarksUpdated = orRemarksUpdated;
	}

	public String getOrRemarksContent() {
		return orRemarksContent;
	}

	public void setOrRemarksContent(String orRemarksContent) {
		this.orRemarksContent = orRemarksContent == null ? null : orRemarksContent.trim();
	}
}