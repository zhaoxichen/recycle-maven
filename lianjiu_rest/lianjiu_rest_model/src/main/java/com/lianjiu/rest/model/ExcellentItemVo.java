package com.lianjiu.rest.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersItem;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ExcellentItemVo {
	
	private OrdersExcellent ordersExcellent;
	
	private List<OrdersItem> list = new ArrayList<OrdersItem>();

	public OrdersExcellent getOrdersExcellent() {
		return ordersExcellent;
	}

	public void setOrdersExcellent(OrdersExcellent ordersExcellent) {
		this.ordersExcellent = ordersExcellent;
	}

	public List<OrdersItem> getList() {
		return list;
	}

	public void setList(List<OrdersItem> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "ExcellentItemVo [ordersExcellent=" + ordersExcellent + ", list=" + list + "]";
	}
	
	
}
