package com.lianjiu.rest.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.OrdersExpress;
import com.lianjiu.model.OrdersItem;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersExpressItemVo extends OrdersExpress {
	private List<OrdersItem> itemList;
	
	private String image;
	
	private String productName;
	
	private int productNum;
	
	public int getProductNum() {
		return productNum;
	}

	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<OrdersItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<OrdersItem> itemList) {
		this.itemList = itemList;
	}
	
}
