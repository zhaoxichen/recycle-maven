package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersExpressItem extends OrdersItem {
	private String orExpItemId;

	private String ordersId;

	private String productName;

	private String productId;

	private String orExpItemPrice;

	private String orExpItemAlliancePrice;

	private String orExpItemAccount;

	private Date orExpItemCreated;

	private Date orExpItemUpdated;

	public String getOrExpItemId() {
		return orExpItemId;
	}

	public void setOrExpItemId(String orExpItemId) {
		this.orExpItemId = orExpItemId == null ? null : orExpItemId.trim();
	}

	public String getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId == null ? null : ordersId.trim();
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName == null ? null : productName.trim();
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId == null ? null : productId.trim();
	}

	public String getOrExpItemPrice() {
		return orExpItemPrice;
	}

	public void setOrExpItemPrice(String orExpItemPrice) {
		this.orExpItemPrice = orExpItemPrice == null ? null : orExpItemPrice.trim();
	}

	public String getOrExpItemAlliancePrice() {
		return orExpItemAlliancePrice;
	}

	public void setOrExpItemAlliancePrice(String orExpItemAlliancePrice) {
		this.orExpItemAlliancePrice = orExpItemAlliancePrice == null ? null : orExpItemAlliancePrice.trim();
	}

	public String getOrExpItemAccount() {
		return orExpItemAccount;
	}

	public void setOrExpItemAccount(String orExpItemAccount) {
		this.orExpItemAccount = orExpItemAccount == null ? null : orExpItemAccount.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrExpItemCreated() {
		return orExpItemCreated;
	}

	public void setOrExpItemCreated(Date orExpItemCreated) {
		this.orExpItemCreated = orExpItemCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrExpItemUpdated() {
		return orExpItemUpdated;
	}

	public void setOrExpItemUpdated(Date orExpItemUpdated) {
		this.orExpItemUpdated = orExpItemUpdated;
	}
}