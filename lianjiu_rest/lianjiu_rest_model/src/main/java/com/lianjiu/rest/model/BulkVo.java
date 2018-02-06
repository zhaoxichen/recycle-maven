package com.lianjiu.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.ProductBulk;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BulkVo extends ProductBulk {
	private String bulkNum;

	private String priceSingle;

	private Integer priceUnit;
	
	private Byte bulkVolume;

	public String getBulkNum() {
		return bulkNum;
	}

	public void setBulkNum(String bulkNum) {
		this.bulkNum = bulkNum;
	}

	public String getPriceSingle() {
		return priceSingle;
	}

	public void setPriceSingle(String priceSingle) {
		this.priceSingle = priceSingle;
	}

	public Integer getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(Integer priceUnit) {
		this.priceUnit = priceUnit;
	}

	public Byte getBulkVolume() {
		return bulkVolume;
	}

	public void setBulkVolume(Byte bulkVolume) {
		this.bulkVolume = bulkVolume;
	}
	

}
