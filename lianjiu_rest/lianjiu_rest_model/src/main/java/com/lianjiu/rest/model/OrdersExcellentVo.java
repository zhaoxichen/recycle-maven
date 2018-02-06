package com.lianjiu.rest.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersExcellentDetails;
import com.lianjiu.model.OrdersItem;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersExcellentVo {
	private List<OrdersItem> itemList;
	private OrdersExcellent ordersExcellent;
	private OrdersExcellentDetails ordersExcellentDetails;
	public OrdersExcellent getOrdersExcellent() {
		return ordersExcellent;
	}
	public void setOrdersExcellent(OrdersExcellent ordersExcellent) {
		this.ordersExcellent = ordersExcellent;
	}
	public OrdersExcellentDetails getOrdersExcellentDetails() {
		return ordersExcellentDetails;
	}
	public void setOrdersExcellentDetails(OrdersExcellentDetails ordersExcellentDetails) {
		this.ordersExcellentDetails = ordersExcellentDetails;
	}
	public List<OrdersItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<OrdersItem> itemList) {
		this.itemList = itemList;
	}

}
