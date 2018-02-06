package com.lianjiu.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 搜索引擎
 * 
 * @author zhaoxi
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ItemSearch {

	private String productId;
	private String productName;
	private String productMaxPrice;
	private Long category;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductMaxPrice() {
		return productMaxPrice;
	}

	public void setProductMaxPrice(String productMaxPrice) {
		this.productMaxPrice = productMaxPrice;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

}
