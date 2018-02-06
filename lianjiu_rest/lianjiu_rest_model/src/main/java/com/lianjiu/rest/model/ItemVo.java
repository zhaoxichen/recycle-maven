package com.lianjiu.rest.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ItemVo {

	private Byte orExcellentStatus;

	private String orItemsId;

	private String ordersId;

	private String orItemsType;

	private String orItemsName;

	private String orItemsProductId;

	private String orItemsStemFrom;

	private String orItemsAccountPrice;

	private String orItemsBeforePrice;

	private String orItemsPrice;

	private String orItemsPicture;

	private String orItemsOrperater;

	private String orItemsNum;

	private Date orItemsCreated;

	private Date orItemsUpdated;

	private String orItemsParam;

	private String orItemsParamModify;

	private Integer orItemsStatus;

	public Byte getOrExcellentStatus() {
		return orExcellentStatus;
	}

	public void setOrExcellentStatus(Byte orExcellentStatus) {
		this.orExcellentStatus = orExcellentStatus;
	}

	public String getOrItemsId() {
		return orItemsId;
	}

	public void setOrItemsId(String orItemsId) {
		this.orItemsId = orItemsId;
	}

	public String getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId;
	}

	public String getOrItemsType() {
		return orItemsType;
	}

	public void setOrItemsType(String orItemsType) {
		this.orItemsType = orItemsType;
	}

	public String getOrItemsName() {
		return orItemsName;
	}

	public void setOrItemsName(String orItemsName) {
		this.orItemsName = orItemsName;
	}

	public String getOrItemsProductId() {
		return orItemsProductId;
	}

	public void setOrItemsProductId(String orItemsProductId) {
		this.orItemsProductId = orItemsProductId;
	}

	public String getOrItemsStemFrom() {
		return orItemsStemFrom;
	}

	public void setOrItemsStemFrom(String orItemsStemFrom) {
		this.orItemsStemFrom = orItemsStemFrom;
	}

	public String getOrItemsBeforePrice() {
		return orItemsBeforePrice;
	}

	public void setOrItemsBeforePrice(String orItemsBeforePrice) {
		this.orItemsBeforePrice = orItemsBeforePrice;
	}

	public String getOrItemsPrice() {
		return orItemsPrice;
	}

	public void setOrItemsPrice(String orItemsPrice) {
		this.orItemsPrice = orItemsPrice;
	}

	public String getOrItemsPicture() {
		return orItemsPicture;
	}

	public void setOrItemsPicture(String orItemsPicture) {
		this.orItemsPicture = orItemsPicture;
	}

	public String getOrItemsOrperater() {
		return orItemsOrperater;
	}

	public void setOrItemsOrperater(String orItemsOrperater) {
		this.orItemsOrperater = orItemsOrperater;
	}

	public String getOrItemsNum() {
		return orItemsNum;
	}

	public void setOrItemsNum(String orItemsNum) {
		this.orItemsNum = orItemsNum;
	}

	public Date getOrItemsCreated() {
		return orItemsCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public void setOrItemsCreated(Date orItemsCreated) {
		this.orItemsCreated = orItemsCreated;
	}

	public Date getOrItemsUpdated() {
		return orItemsUpdated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public void setOrItemsUpdated(Date orItemsUpdated) {
		this.orItemsUpdated = orItemsUpdated;
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

	public Integer getOrItemsStatus() {
		return orItemsStatus;
	}

	public void setOrItemsStatus(Integer orItemsStatus) {
		this.orItemsStatus = orItemsStatus;
	}

	public String getOrItemsAccountPrice() {
		return orItemsAccountPrice;
	}

	public void setOrItemsAccountPrice(String orItemsAccountPrice) {
		this.orItemsAccountPrice = orItemsAccountPrice;
	}

}
