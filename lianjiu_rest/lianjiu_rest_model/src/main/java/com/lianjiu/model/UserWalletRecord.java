package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserWalletRecord {
	private String recordId;

	private String userId;

	private String recordName;

	private String recordPrice;

	private String relativeId;

	private String recordStatus;

	private Byte isIncome;

	private String msg ;
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private Date recordCreated;

	private Date recordUpdated;

	public UserWalletRecord() {
	}

	/**
	 * 
	 * @param userId
	 * @param recordName
	 * @param recordPrice
	 * @param relativeId
	 * @param recordStatus
	 * @param isIncome
	 */
	public UserWalletRecord(String userId, String recordName, String recordPrice, String relativeId, Byte isIncome) {
		this.userId = userId;
		this.recordName = recordName;
		this.recordPrice = recordPrice;
		this.relativeId = relativeId;
		this.isIncome = isIncome;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId == null ? null : recordId.trim();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName == null ? null : recordName.trim();
	}

	public String getRecordPrice() {
		return recordPrice;
	}

	public void setRecordPrice(String recordPrice) {
		this.recordPrice = recordPrice == null ? null : recordPrice.trim();
	}

	public String getRelativeId() {
		return relativeId;
	}

	public void setRelativeId(String relativeId) {
		this.relativeId = relativeId == null ? null : relativeId.trim();
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus == null ? null : recordStatus.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getRecordCreated() {
		return recordCreated;
	}

	public void setRecordCreated(Date recordCreated) {
		this.recordCreated = recordCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getRecordUpdated() {
		return recordUpdated;
	}

	public void setRecordUpdated(Date recordUpdated) {
		this.recordUpdated = recordUpdated;
	}

	public Byte getIsIncome() {
		return isIncome;
	}

	public void setIsIncome(Byte isIncome) {
		this.isIncome = isIncome;
	}
}