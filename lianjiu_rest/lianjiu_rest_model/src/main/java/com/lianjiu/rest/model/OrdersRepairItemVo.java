package com.lianjiu.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.OrdersRepair;
import com.lianjiu.model.OrdersRepairItem;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersRepairItemVo {
	private OrdersRepair ordersRepair;
	private OrdersRepairItem ordersRepairItem;
	public OrdersRepair getOrdersRepair() {
		return ordersRepair;
	}
	public void setOrdersRepair(OrdersRepair ordersRepair) {
		this.ordersRepair = ordersRepair;
	}
	public OrdersRepairItem getOrdersRepairItem() {
		return ordersRepairItem;
	}
	public void setOrdersRepairItem(OrdersRepairItem ordersRepairItem) {
		this.ordersRepairItem = ordersRepairItem;
	}
	
}
