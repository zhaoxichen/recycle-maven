package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersItem {
	private String orItemsId;

	private String ordersId;

	private String orItemsType;

	private String orItemsName;

	private String orItemsProductId;
	// 来源(0卖手机、1卖家电、2卖精品、 3卖废品等)
	private Integer orItemsStemFrom;

	private String orItemsBeforePrice;

	private String orItemsPrice;
	
	private String orItemsAccountPrice;

	private String orItemsPicture;

	private String orItemsOrperater;

	private String orItemsNum;
	
	private Long orItemsVolume;
	
	private Long orItemsPriceUnit;

	private Date orItemsCreated;

	private Date orItemsUpdated;

	private String orItemsParam;

	private String orItemsParamModify;

	private Integer orItemsStatus;

	private String orItemsUser;
	
	private String orItemsRecycleType;
	
	private String orItemsBuyway;
	
	private Integer orItemsChooseFlag;
	
	private Long categoryId;
	
	
	
	public String getOrItemsUser() {
		return orItemsUser;
	}

	public void setOrItemsUser(String orItemsUser) {
		this.orItemsUser = orItemsUser;
	}

	public String getOrItemsBuyway() {
		return orItemsBuyway;
	}

	public void setOrItemsBuyway(String orItemsBuyway) {
		this.orItemsBuyway = orItemsBuyway;
	}

	public String getOrItemsRecycleType() {
		return orItemsRecycleType;
	}

	public void setOrItemsRecycleType(String orItemsRecycleType) {
		this.orItemsRecycleType = orItemsRecycleType;
	}

	public String getOrItemsId() {
		return orItemsId;
	}

	public void setOrItemsId(String orItemsId) {
		this.orItemsId = orItemsId == null ? null : orItemsId.trim();
	}

	public String getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId == null ? null : ordersId.trim();
	}

	public String getOrItemsType() {
		return orItemsType;
	}

	public void setOrItemsType(String orItemsType) {
		this.orItemsType = orItemsType == null ? null : orItemsType.trim();
	}

	public String getOrItemsName() {
		return orItemsName;
	}

	public void setOrItemsName(String orItemsName) {
		this.orItemsName = orItemsName == null ? null : orItemsName.trim();
	}

	public String getOrItemsProductId() {
		return orItemsProductId;
	}

	public void setOrItemsProductId(String orItemsProductId) {
		this.orItemsProductId = orItemsProductId == null ? null : orItemsProductId.trim();
	}

	public Integer getOrItemsStemFrom() {
		return orItemsStemFrom;
	}

	public void setOrItemsStemFrom(Integer orItemsStemFrom) {
		this.orItemsStemFrom = orItemsStemFrom;
	}

	public String getOrItemsBeforePrice() {
		return orItemsBeforePrice;
	}

	public void setOrItemsBeforePrice(String orItemsBeforePrice) {
		this.orItemsBeforePrice = orItemsBeforePrice == null ? null : orItemsBeforePrice.trim();
	}

	public String getOrItemsPrice() {
		return orItemsPrice;
	}

	public void setOrItemsPrice(String orItemsPrice) {
		this.orItemsPrice = orItemsPrice == null ? null : orItemsPrice.trim();
	}

	public String getOrItemsPicture() {
		return orItemsPicture;
	}

	public void setOrItemsPicture(String orItemsPicture) {
		this.orItemsPicture = orItemsPicture == null ? null : orItemsPicture.trim();
	}

	public String getOrItemsOrperater() {
		return orItemsOrperater;
	}

	public void setOrItemsOrperater(String orItemsOrperater) {
		this.orItemsOrperater = orItemsOrperater == null ? null : orItemsOrperater.trim();
	}

	public String getOrItemsNum() {
		return orItemsNum;
	}

	public void setOrItemsNum(String orItemsNum) {
		this.orItemsNum = orItemsNum;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrItemsCreated() {
		return orItemsCreated;
	}

	public void setOrItemsCreated(Date orItemsCreated) {
		this.orItemsCreated = orItemsCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrItemsUpdated() {
		return orItemsUpdated;
	}

	public void setOrItemsUpdated(Date orItemsUpdated) {
		this.orItemsUpdated = orItemsUpdated;
	}

	public Integer getOrItemsStatus() {
		return orItemsStatus;
	}

	public void setOrItemsStatus(Integer orItemsStatus) {
		this.orItemsStatus = orItemsStatus;
	}

	public String getOrItemsParam() {
		return orItemsParam;
	}

	public void setOrItemsParam(String orItemsParam) {
		this.orItemsParam = orItemsParam;
	}

	public String getOrItemsParamModify() {
		return orItemsParamModify;
	}

	public void setOrItemsParamModify(String orItemsParamModify) {
		this.orItemsParamModify = orItemsParamModify;
	}

	public Long getOrItemsVolume() {
		return orItemsVolume;
	}

	public void setOrItemsVolume(Long orItemsVolume) {
		this.orItemsVolume = orItemsVolume;
	}

	public Long getOrItemsPriceUnit() {
		return orItemsPriceUnit;
	}

	public void setOrItemsPriceUnit(Long orItemsPriceUnit) {
		this.orItemsPriceUnit = orItemsPriceUnit;
	}

	public Integer getOrItemsChooseFlag() {
		return orItemsChooseFlag;
	}

	public void setOrItemsChooseFlag(Integer orItemsChooseFlag) {
		this.orItemsChooseFlag = orItemsChooseFlag;
	}

	public String getOrItemsAccountPrice() {
		return orItemsAccountPrice;
	}

	public void setOrItemsAccountPrice(String orItemsAccountPrice) {
		this.orItemsAccountPrice = orItemsAccountPrice;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

}