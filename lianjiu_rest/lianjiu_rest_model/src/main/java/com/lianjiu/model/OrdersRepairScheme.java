package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersRepairScheme {
	private String orReSchemeId;

	private String orRepairId;

	private String productRepairId;

	private String productRepairName;

	private String orRepairPicture;

	private Date orReSchemeCreated;

	private Date orReSchemeUpdated;

	private String orRepairScheme;
	
	private Byte orRepairStatus;

	public Byte getOrRepairStatus() {
		return orRepairStatus;
	}

	public void setOrRepairStatus(Byte orRepairStatus) {
		this.orRepairStatus = orRepairStatus;
	}

	public String getOrReSchemeId() {
		return orReSchemeId;
	}

	public void setOrReSchemeId(String orReSchemeId) {
		this.orReSchemeId = orReSchemeId == null ? null : orReSchemeId.trim();
	}

	public String getOrRepairId() {
		return orRepairId;
	}

	public void setOrRepairId(String orRepairId) {
		this.orRepairId = orRepairId == null ? null : orRepairId.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrReSchemeCreated() {
		return orReSchemeCreated;
	}

	public void setOrReSchemeCreated(Date orReSchemeCreated) {
		this.orReSchemeCreated = orReSchemeCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrReSchemeUpdated() {
		return orReSchemeUpdated;
	}

	public void setOrReSchemeUpdated(Date orReSchemeUpdated) {
		this.orReSchemeUpdated = orReSchemeUpdated;
	}

	public String getProductRepairId() {
		return productRepairId;
	}

	public void setProductRepairId(String productRepairId) {
		this.productRepairId = productRepairId == null ? null : productRepairId.trim();
	}

	public String getProductRepairName() {
		return productRepairName;
	}

	public void setProductRepairName(String productRepairName) {
		this.productRepairName = productRepairName == null ? null : productRepairName.trim();
	}

	public String getOrRepairPicture() {
		return orRepairPicture;
	}

	public void setOrRepairPicture(String orRepairPicture) {
		this.orRepairPicture = orRepairPicture == null ? null : orRepairPicture.trim();
	}

	public String getOrRepairScheme() {
		return orRepairScheme;
	}

	public void setOrRepairScheme(String orRepairScheme) {
		this.orRepairScheme = orRepairScheme == null ? null : orRepairScheme.trim();
	}

}