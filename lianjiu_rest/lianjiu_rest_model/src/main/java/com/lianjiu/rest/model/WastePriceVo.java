package com.lianjiu.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.WastePrice;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class WastePriceVo extends WastePrice{
	
	private String wasteId;
	
	private String wasteImage;

	public String getWasteId() {
		return wasteId;
	}

	public void setWasteId(String wasteId) {
		this.wasteId = wasteId;
	}

	public String getWasteImage() {
		return wasteImage;
	}

	public void setWasteImage(String wasteImage) {
		this.wasteImage = wasteImage;
	}
	
	
}
