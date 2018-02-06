package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersFacefaceDetails {
	private String orFfDetailsId;

	private String orFacefaceId;

	private String orFfDetailsPrice;

	private String orFfDetailsRetrPrice;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orFfDetailsPaytime;

	private String orFfDetailsAllianceId;

	private String orFfDetailsHandlerId;

	private Date orFfDetailsUpdated;

	private String orFfDetailsHandlerTel;

	private Byte orFacefaceStatus;

	public String getOrFfDetailsHandlerTel() {
		return orFfDetailsHandlerTel;
	}

	public void setOrFfDetailsHandlerTel(String orFfDetailsHandlerTel) {
		this.orFfDetailsHandlerTel = orFfDetailsHandlerTel;
	}

	public String getOrFfDetailsId() {
		return orFfDetailsId;
	}

	public void setOrFfDetailsId(String orFfDetailsId) {
		this.orFfDetailsId = orFfDetailsId == null ? null : orFfDetailsId.trim();
	}

	public String getOrFacefaceId() {
		return orFacefaceId;
	}

	public void setOrFacefaceId(String orFacefaceId) {
		this.orFacefaceId = orFacefaceId == null ? null : orFacefaceId.trim();
	}

	public String getOrFfDetailsPrice() {
		return orFfDetailsPrice;
	}

	public void setOrFfDetailsPrice(String orFfDetailsPrice) {
		this.orFfDetailsPrice = orFfDetailsPrice == null ? null : orFfDetailsPrice.trim();
	}

	public String getOrFfDetailsRetrPrice() {
		return orFfDetailsRetrPrice;
	}

	public void setOrFfDetailsRetrPrice(String orFfDetailsRetrPrice) {
		this.orFfDetailsRetrPrice = orFfDetailsRetrPrice == null ? null : orFfDetailsRetrPrice.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrFfDetailsPaytime() {
		return orFfDetailsPaytime;
	}

	public void setOrFfDetailsPaytime(Date orFfDetailsPaytime) {
		this.orFfDetailsPaytime = orFfDetailsPaytime;
	}

	public String getOrFfDetailsAllianceId() {
		return orFfDetailsAllianceId;
	}

	public void setOrFfDetailsAllianceId(String orFfDetailsAllianceId) {
		this.orFfDetailsAllianceId = orFfDetailsAllianceId == null ? null : orFfDetailsAllianceId.trim();
	}

	public String getOrFfDetailsHandlerId() {
		return orFfDetailsHandlerId;
	}

	public void setOrFfDetailsHandlerId(String orFfDetailsHandlerId) {
		this.orFfDetailsHandlerId = orFfDetailsHandlerId == null ? null : orFfDetailsHandlerId.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getOrFfDetailsUpdated() {
		return orFfDetailsUpdated;
	}

	public void setOrFfDetailsUpdated(Date orFfDetailsUpdated) {
		this.orFfDetailsUpdated = orFfDetailsUpdated;
	}

	public Byte getOrFacefaceStatus() {
		return orFacefaceStatus;
	}

	public void setOrFacefaceStatus(Byte orFacefaceStatus) {
		this.orFacefaceStatus = orFacefaceStatus;
	}

}