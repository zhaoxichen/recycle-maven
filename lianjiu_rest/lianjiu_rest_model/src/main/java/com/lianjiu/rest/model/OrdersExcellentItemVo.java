package com.lianjiu.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersItem;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersExcellentItemVo {
	private OrdersExcellent ordersExcellent;
	private OrdersItem ordersItem;
	public OrdersExcellent getOrdersExcellent() {
		return ordersExcellent;
	}
	public void setOrdersExcellent(OrdersExcellent ordersExcellent) {
		this.ordersExcellent = ordersExcellent;
	}
	public OrdersItem getOrdersItem() {
		return ordersItem;
	}
	public void setOrdersItem(OrdersItem ordersItem) {
		this.ordersItem = ordersItem;
	}
	
}
