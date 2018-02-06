package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersRepairItem {
	private String orReItemId;

	private String orReItemName;

	private String ordersId;

	private String orReItemFunction;

	private String orReItemScheme;

	private String orReItemColor;

	private String orReItemPrice;

	private Date orReItemCreated;

	private Date orReItemUpdated;

	private String orReItemPictrue;

	public String getOrReItemId() {
		return orReItemId;
	}

	public void setOrReItemId(String orReItemId) {
		this.orReItemId = orReItemId == null ? null : orReItemId.trim();
	}

	public String getOrReItemName() {
		return orReItemName;
	}

	public void setOrReItemName(String orReItemName) {
		this.orReItemName = orReItemName == null ? null : orReItemName.trim();
	}

	public String getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId == null ? null : ordersId.trim();
	}

	public String getOrReItemFunction() {
		return orReItemFunction;
	}

	public void setOrReItemFunction(String orReItemFunction) {
		this.orReItemFunction = orReItemFunction == null ? null : orReItemFunction.trim();
	}

	public String getOrReItemScheme() {
		return orReItemScheme;
	}

	public void setOrReItemScheme(String orReItemScheme) {
		this.orReItemScheme = orReItemScheme == null ? null : orReItemScheme.trim();
	}

	public String getOrReItemColor() {
		return orReItemColor;
	}

	public void setOrReItemColor(String orReItemColor) {
		this.orReItemColor = orReItemColor == null ? null : orReItemColor.trim();
	}

	public String getOrReItemPrice() {
		return orReItemPrice;
	}

	public void setOrReItemPrice(String orReItemPrice) {
		this.orReItemPrice = orReItemPrice == null ? null : orReItemPrice.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrReItemCreated() {
		return orReItemCreated;
	}

	public void setOrReItemCreated(Date orReItemCreated) {
		this.orReItemCreated = orReItemCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrReItemUpdated() {
		return orReItemUpdated;
	}

	public void setOrReItemUpdated(Date orReItemUpdated) {
		this.orReItemUpdated = orReItemUpdated;
	}

	public String getOrReItemPictrue() {
		return orReItemPictrue;
	}

	public void setOrReItemPictrue(String orReItemPictrue) {
		this.orReItemPictrue = orReItemPictrue == null ? null : orReItemPictrue.trim();
	}
}