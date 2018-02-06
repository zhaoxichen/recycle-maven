package com.lianjiu.rest.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.OrdersFacefaceDetails;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersFacefaceWasteVo {
	private String price;
	private OrdersFaceface ordersFaceface;
	private OrdersFacefaceDetails ordersFacefaceDetails;
	private List<WasteVo> wasteList;
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public List<WasteVo> getWasteList() {
		return wasteList;
	}
	
	public void setWasteList(List<WasteVo> wasteList) {
		this.wasteList = wasteList;
	}
	
	public OrdersFacefaceDetails getOrdersFacefaceDetails() {
		return ordersFacefaceDetails;
	}

	public void setOrdersFacefaceDetails(OrdersFacefaceDetails ordersFacefaceDetails) {
		this.ordersFacefaceDetails = ordersFacefaceDetails;
	}

	public OrdersFaceface getOrdersFaceface() {
		return ordersFaceface;
	}
	public void setOrdersFaceface(OrdersFaceface ordersFaceface) {
		this.ordersFaceface = ordersFaceface;
	}

}
