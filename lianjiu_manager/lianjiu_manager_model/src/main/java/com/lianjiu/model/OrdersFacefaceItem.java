package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersFacefaceItem extends OrdersItem{
	private String orFfItemId;

	private String ordersId;

	private String orFfItemType;

	private String orFfItemBrand;

	private String orFfItemName;

	private Long orFfItemNum;

	private String orFfItemPrice;

	private String orFfItemRetrPrice;

	private Date orFfItemCreated;

	private Date orFfItemUpdated;

	private String orFfItemRetrPicture;

	public String getOrFfItemId() {
		return orFfItemId;
	}

	public void setOrFfItemId(String orFfItemId) {
		this.orFfItemId = orFfItemId == null ? null : orFfItemId.trim();
	}

	public String getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId == null ? null : ordersId.trim();
	}

	public String getOrFfItemType() {
		return orFfItemType;
	}

	public void setOrFfItemType(String orFfItemType) {
		this.orFfItemType = orFfItemType == null ? null : orFfItemType.trim();
	}

	public String getOrFfItemBrand() {
		return orFfItemBrand;
	}

	public void setOrFfItemBrand(String orFfItemBrand) {
		this.orFfItemBrand = orFfItemBrand == null ? null : orFfItemBrand.trim();
	}

	public String getOrFfItemName() {
		return orFfItemName;
	}

	public void setOrFfItemName(String orFfItemName) {
		this.orFfItemName = orFfItemName == null ? null : orFfItemName.trim();
	}

	public Long getOrFfItemNum() {
		return orFfItemNum;
	}

	public void setOrFfItemNum(Long orFfItemNum) {
		this.orFfItemNum = orFfItemNum;
	}

	public String getOrFfItemPrice() {
		return orFfItemPrice;
	}

	public void setOrFfItemPrice(String orFfItemPrice) {
		this.orFfItemPrice = orFfItemPrice == null ? null : orFfItemPrice.trim();
	}

	public String getOrFfItemRetrPrice() {
		return orFfItemRetrPrice;
	}

	public void setOrFfItemRetrPrice(String orFfItemRetrPrice) {
		this.orFfItemRetrPrice = orFfItemRetrPrice == null ? null : orFfItemRetrPrice.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrFfItemCreated() {
		return orFfItemCreated;
	}

	public void setOrFfItemCreated(Date orFfItemCreated) {
		this.orFfItemCreated = orFfItemCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrFfItemUpdated() {
		return orFfItemUpdated;
	}

	public void setOrFfItemUpdated(Date orFfItemUpdated) {
		this.orFfItemUpdated = orFfItemUpdated;
	}

	public String getOrFfItemRetrPicture() {
		return orFfItemRetrPicture;
	}

	public void setOrFfItemRetrPicture(String orFfItemRetrPicture) {
		this.orFfItemRetrPicture = orFfItemRetrPicture == null ? null : orFfItemRetrPicture.trim();
	}
}