package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersExcellentRefund {
	private String orExceRefundId;

	private Byte orExceRefundType;

	private String orExcellentId;

	private Byte orExceProductStatus;

	private String orExceRefundCause;

	private String orExceRefundMoney;

	private String orExceRefundExpress;

	private String orExceRefundExpressnum;

	private Date orExceRefundCreated;

	private Date orExceRefundUpdated;

	private String orExceRefundExpresspic;

	public String getOrExceRefundId() {
		return orExceRefundId;
	}

	public void setOrExceRefundId(String orExceRefundId) {
		this.orExceRefundId = orExceRefundId == null ? null : orExceRefundId.trim();
	}

	public Byte getOrExceRefundType() {
		return orExceRefundType;
	}

	public void setOrExceRefundType(Byte orExceRefundType) {
		this.orExceRefundType = orExceRefundType;
	}

	public String getOrExcellentId() {
		return orExcellentId;
	}

	public void setOrExcellentId(String orExcellentId) {
		this.orExcellentId = orExcellentId == null ? null : orExcellentId.trim();
	}

	public Byte getOrExceProductStatus() {
		return orExceProductStatus;
	}

	public void setOrExceProductStatus(Byte orExceProductStatus) {
		this.orExceProductStatus = orExceProductStatus;
	}

	public String getOrExceRefundCause() {
		return orExceRefundCause;
	}

	public void setOrExceRefundCause(String orExceRefundCause) {
		this.orExceRefundCause = orExceRefundCause == null ? null : orExceRefundCause.trim();
	}

	public String getOrExceRefundMoney() {
		return orExceRefundMoney;
	}

	public void setOrExceRefundMoney(String orExceRefundMoney) {
		this.orExceRefundMoney = orExceRefundMoney == null ? null : orExceRefundMoney.trim();
	}

	public String getOrExceRefundExpress() {
		return orExceRefundExpress;
	}

	public void setOrExceRefundExpress(String orExceRefundExpress) {
		this.orExceRefundExpress = orExceRefundExpress == null ? null : orExceRefundExpress.trim();
	}

	public String getOrExceRefundExpressnum() {
		return orExceRefundExpressnum;
	}

	public void setOrExceRefundExpressnum(String orExceRefundExpressnum) {
		this.orExceRefundExpressnum = orExceRefundExpressnum == null ? null : orExceRefundExpressnum.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrExceRefundCreated() {
		return orExceRefundCreated;
	}

	public void setOrExceRefundCreated(Date orExceRefundCreated) {
		this.orExceRefundCreated = orExceRefundCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrExceRefundUpdated() {
		return orExceRefundUpdated;
	}

	public void setOrExceRefundUpdated(Date orExceRefundUpdated) {
		this.orExceRefundUpdated = orExceRefundUpdated;
	}

	public String getOrExceRefundExpresspic() {
		return orExceRefundExpresspic;
	}

	public void setOrExceRefundExpresspic(String orExceRefundExpresspic) {
		this.orExceRefundExpresspic = orExceRefundExpresspic == null ? null : orExceRefundExpresspic.trim();
	}
}