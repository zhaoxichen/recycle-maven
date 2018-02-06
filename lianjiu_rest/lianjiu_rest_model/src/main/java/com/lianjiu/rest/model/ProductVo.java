package com.lianjiu.rest.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.Product;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProductVo extends Product{
	
	private Long ProductNum;

	private String userId;
	
	private String productlist;
	
	public String getProductlist() {
		return productlist;
	}

	public void setProductlist(String productlist) {
		this.productlist = productlist;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public Long getProductNum() {
		return ProductNum;
	}

	public void setProductNum(Long productNum) {
		ProductNum = productNum;
	}
	
}
