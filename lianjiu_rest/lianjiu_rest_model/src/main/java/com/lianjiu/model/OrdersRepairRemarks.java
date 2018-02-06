package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersRepairRemarks {
	private String orReRemarksId;

	private String orReSchemeId;

	private String adminUsername;

	private Date orReRemarksCreated;

	private Date orReRemarksUpdated;

	private String orReRemarksContent;

	public String getOrReRemarksId() {
		return orReRemarksId;
	}

	public void setOrReRemarksId(String orReRemarksId) {
		this.orReRemarksId = orReRemarksId == null ? null : orReRemarksId.trim();
	}

	public String getOrReSchemeId() {
		return orReSchemeId;
	}

	public void setOrReSchemeId(String orReSchemeId) {
		this.orReSchemeId = orReSchemeId == null ? null : orReSchemeId.trim();
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername == null ? null : adminUsername.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrReRemarksCreated() {
		return orReRemarksCreated;
	}

	public void setOrReRemarksCreated(Date orReRemarksCreated) {
		this.orReRemarksCreated = orReRemarksCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrReRemarksUpdated() {
		return orReRemarksUpdated;
	}

	public void setOrReRemarksUpdated(Date orReRemarksUpdated) {
		this.orReRemarksUpdated = orReRemarksUpdated;
	}

	public String getOrReRemarksContent() {
		return orReRemarksContent;
	}

	public void setOrReRemarksContent(String orReRemarksContent) {
		this.orReRemarksContent = orReRemarksContent == null ? null : orReRemarksContent.trim();
	}
}