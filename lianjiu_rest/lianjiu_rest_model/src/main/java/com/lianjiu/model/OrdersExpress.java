package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersExpress {
	private String orExpressId;

	private String orExpressUserId;

	private String orExpressUserPhone;

	private String orExpressEvaluatedPrice; // 估价

	private String orExpressOperater;

	private Byte orExpressStatus;

	private String orExpressNum;

	private String orExpressRecyclePrice; // 回收价格

	private Long categoryId;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orExpressCreated;

	private Date orExpressUpdated;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orExpressCancel;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orExpressAccount;

	private String orExpressUserName;

	private String orExpressReturnNum;

	private String orItemsNamePreview;

	private String orItemsPictruePreview;

	private String orExpDetailsPrice;

	private String orExpDetailsAlliancePrice;

	private String productId;

	private String orExpDetailsAccount;
	
	private String orExpressNumCancel;

	public OrdersExpress() {
	}

	public OrdersExpress(String orExpressId, Date orExpressUpdated) {
		this.orExpressId = orExpressId;
		this.orExpressUpdated = orExpressUpdated;
	}

	public String getOrExpressReturnNum() {
		return orExpressReturnNum;
	}

	public void setOrExpressReturnNum(String orExpressReturnNum) {
		this.orExpressReturnNum = orExpressReturnNum;
	}

	public String getOrExpressUserName() {
		return orExpressUserName;
	}

	public void setOrExpressUserName(String orExpressUserName) {
		this.orExpressUserName = orExpressUserName;
	}

	public String getOrExpressId() {
		return orExpressId;
	}

	public void setOrExpressId(String orExpressId) {
		this.orExpressId = orExpressId == null ? null : orExpressId.trim();
	}

	public String getOrExpressUserId() {
		return orExpressUserId;
	}

	public void setOrExpressUserId(String orExpressUserId) {
		this.orExpressUserId = orExpressUserId == null ? null : orExpressUserId.trim();
	}

	public String getOrExpressUserPhone() {
		return orExpressUserPhone;
	}

	public void setOrExpressUserPhone(String orExpressUserPhone) {
		this.orExpressUserPhone = orExpressUserPhone == null ? null : orExpressUserPhone.trim();
	}

	public String getOrExpressEvaluatedPrice() {
		return orExpressEvaluatedPrice;
	}

	public void setOrExpressEvaluatedPrice(String orExpressEvaluatedPrice) {
		this.orExpressEvaluatedPrice = orExpressEvaluatedPrice == null ? null : orExpressEvaluatedPrice.trim();
	}

	public String getOrExpressOperater() {
		return orExpressOperater;
	}

	public void setOrExpressOperater(String orExpressOperater) {
		this.orExpressOperater = orExpressOperater == null ? null : orExpressOperater.trim();
	}

	public Byte getOrExpressStatus() {
		return orExpressStatus;
	}

	public void setOrExpressStatus(Byte orExpressStatus) {
		this.orExpressStatus = orExpressStatus;
	}

	public String getOrExpressNum() {
		return orExpressNum;
	}

	public void setOrExpressNum(String orExpressNum) {
		this.orExpressNum = orExpressNum == null ? null : orExpressNum.trim();
	}

	public String getOrExpressRecyclePrice() {
		return orExpressRecyclePrice;
	}

	public void setOrExpressRecyclePrice(String orExpressRecyclePrice) {
		this.orExpressRecyclePrice = orExpressRecyclePrice == null ? null : orExpressRecyclePrice.trim();
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrExpressCreated() {
		return orExpressCreated;
	}

	public void setOrExpressCreated(Date orExpressCreated) {
		this.orExpressCreated = orExpressCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getOrExpressUpdated() {
		return orExpressUpdated;
	}

	public void setOrExpressUpdated(Date orExpressUpdated) {
		this.orExpressUpdated = orExpressUpdated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrExpressCancel() {
		return orExpressCancel;
	}

	public void setOrExpressCancel(Date orExpressCancel) {
		this.orExpressCancel = orExpressCancel;
	}

	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrExpressAccount() {
		return orExpressAccount;
	}

	public void setOrExpressAccount(Date orExpressAccount) {
		this.orExpressAccount = orExpressAccount;
	}

	public String getOrItemsNamePreview() {
		return orItemsNamePreview;
	}

	public void setOrItemsNamePreview(String orItemsNamePreview) {
		this.orItemsNamePreview = orItemsNamePreview;
	}

	public String getOrItemsPictruePreview() {
		return orItemsPictruePreview;
	}

	public void setOrItemsPictruePreview(String orItemsPictruePreview) {
		this.orItemsPictruePreview = orItemsPictruePreview;
	}

	public String getOrExpDetailsPrice() {
		return orExpDetailsPrice;
	}

	public void setOrExpDetailsPrice(String orExpDetailsPrice) {
		this.orExpDetailsPrice = orExpDetailsPrice;
	}

	public String getOrExpDetailsAlliancePrice() {
		return orExpDetailsAlliancePrice;
	}

	public void setOrExpDetailsAlliancePrice(String orExpDetailsAlliancePrice) {
		this.orExpDetailsAlliancePrice = orExpDetailsAlliancePrice;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getOrExpDetailsAccount() {
		return orExpDetailsAccount;
	}

	public void setOrExpDetailsAccount(String orExpDetailsAccount) {
		this.orExpDetailsAccount = orExpDetailsAccount;
	}

	public String getOrExpressNumCancel() {
		return orExpressNumCancel;
	}

	public void setOrExpressNumCancel(String orExpressNumCancel) {
		this.orExpressNumCancel = orExpressNumCancel;
	}

}