package com.lianjiu.rest.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.OrdersItem;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersFacefaceItemVo {
	private OrdersFaceface ordersFaceface;
	private OrdersItem ordersItem;
	private List<OrdersItem> itemList;

	public OrdersItem getOrdersItem() {
		return ordersItem;
	}
	public void setOrdersItem(OrdersItem ordersItem) {
		this.ordersItem = ordersItem;
	}
	public OrdersFaceface getOrdersFaceface() {
		return ordersFaceface;
	}
	public void setOrdersFaceface(OrdersFaceface ordersFaceface) {
		this.ordersFaceface = ordersFaceface;
	}
	public List<OrdersItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<OrdersItem> itemList) {
		this.itemList = itemList;
	}
	
}
