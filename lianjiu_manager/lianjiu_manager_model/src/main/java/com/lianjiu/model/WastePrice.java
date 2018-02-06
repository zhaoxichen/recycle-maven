package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class WastePrice {
	private String wPriceId;

	private String wPriceSingle;

	private String wPriceSingleAlliance;

	private String wasteId;
	
	private String name;

	private Integer wPriceUnit;

	private Date wPriceCreated;

	private Date wPriceUpdated;

	public String getwPriceId() {
		return wPriceId;
	}

	public void setwPriceId(String wPriceId) {
		this.wPriceId = wPriceId == null ? null : wPriceId.trim();
	}

	public String getwPriceSingle() {
		return wPriceSingle;
	}

	public void setwPriceSingle(String wPriceSingle) {
		this.wPriceSingle = wPriceSingle == null ? null : wPriceSingle.trim();
	}

	public String getwPriceSingleAlliance() {
		return wPriceSingleAlliance;
	}

	public void setwPriceSingleAlliance(String wPriceSingleAlliance) {
		this.wPriceSingleAlliance = wPriceSingleAlliance == null ? null : wPriceSingleAlliance.trim();
	}

	public String getWasteId() {
		return wasteId;
	}

	public void setWasteId(String wasteId) {
		this.wasteId = wasteId == null ? null : wasteId.trim();
	}

	public Integer getwPriceUnit() {
		return wPriceUnit;
	}

	public void setwPriceUnit(Integer wPriceUnit) {
		this.wPriceUnit = wPriceUnit;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getwPriceCreated() {
		return wPriceCreated;
	}

	public void setwPriceCreated(Date wPriceCreated) {
		this.wPriceCreated = wPriceCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getwPriceUpdated() {
		return wPriceUpdated;
	}

	public void setwPriceUpdated(Date wPriceUpdated) {
		this.wPriceUpdated = wPriceUpdated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}