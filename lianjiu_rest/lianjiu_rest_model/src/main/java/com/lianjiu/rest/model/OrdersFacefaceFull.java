package com.lianjiu.rest.model;

/**
 * 上门预约订单集合
 */
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.OrdersFaceface;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersFacefaceFull extends OrdersFaceface{

	private String maxImage;

	private String orFfDetailsId;

	private String orFfDetailsPrice;

	private String orFfDetailsRetrPrice;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orFfDetailsPaytime;

	private String orFfDetailsAllianceId;

	private String orFfDetailsHandlerId;

	private Date orFfDetailsUpdated;

	private String orFfDetailsHandlerTel;

	public String getMaxImage() {
		return maxImage;
	}

	public void setMaxImage(String maxImage) {
		this.maxImage = maxImage;
	}

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
}