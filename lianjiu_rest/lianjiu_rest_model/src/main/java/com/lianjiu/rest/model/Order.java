package com.lianjiu.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.Orders;
import com.lianjiu.model.OrdersShipping;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Order extends Orders {

	private OrdersShipping ordersShipping;

	public OrdersShipping getOrderShipping() {
		return ordersShipping;
	}
	public void setOrderShipping(OrdersShipping ordersShipping) {
		this.ordersShipping = ordersShipping;
	}
	
	
}
