package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersExpressDetails {
	private String orExpDetailsId;

	private String orExpressId;

	private String productName;

	private String orExpDetailsPrice;

	private String orExpDetailsAlliancePrice;

	private String productId;

	private String orExpDetailsAccount;

	private Date orExpDetailsCreated;

	private Date orExpDetailsUpdated;

	public String getOrExpDetailsId() {
		return orExpDetailsId;
	}

	public void setOrExpDetailsId(String orExpDetailsId) {
		this.orExpDetailsId = orExpDetailsId == null ? null : orExpDetailsId.trim();
	}

	public String getOrExpressId() {
		return orExpressId;
	}

	public void setOrExpressId(String orExpressId) {
		this.orExpressId = orExpressId == null ? null : orExpressId.trim();
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName == null ? null : productName.trim();
	}

	public String getOrExpDetailsPrice() {
		return orExpDetailsPrice;
	}

	public void setOrExpDetailsPrice(String orExpDetailsPrice) {
		this.orExpDetailsPrice = orExpDetailsPrice == null ? null : orExpDetailsPrice.trim();
	}

	public String getOrExpDetailsAlliancePrice() {
		return orExpDetailsAlliancePrice;
	}

	public void setOrExpDetailsAlliancePrice(String orExpDetailsAlliancePrice) {
		this.orExpDetailsAlliancePrice = orExpDetailsAlliancePrice == null ? null : orExpDetailsAlliancePrice.trim();
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId == null ? null : productId.trim();
	}

	public String getOrExpDetailsAccount() {
		return orExpDetailsAccount;
	}

	public void setOrExpDetailsAccount(String orExpDetailsAccount) {
		this.orExpDetailsAccount = orExpDetailsAccount == null ? null : orExpDetailsAccount.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrExpDetailsCreated() {
		return orExpDetailsCreated;
	}

	public void setOrExpDetailsCreated(Date orExpDetailsCreated) {
		this.orExpDetailsCreated = orExpDetailsCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrExpDetailsUpdated() {
		return orExpDetailsUpdated;
	}

	public void setOrExpDetailsUpdated(Date orExpDetailsUpdated) {
		this.orExpDetailsUpdated = orExpDetailsUpdated;
	}
}