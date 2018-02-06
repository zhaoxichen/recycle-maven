package com.lianjiu.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.Waste;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class WasteVo extends Waste{
	private String wasteNum;
	private String wPriceId;
	public String getwPriceId() {
		return wPriceId;
	}
	public void setwPriceId(String wPriceId) {
		this.wPriceId = wPriceId;
	}

	public String getWasteNum() {
		return wasteNum;
	}

	public void setWasteNum(String wasteNum) {
		this.wasteNum = wasteNum;
	}
	
}
