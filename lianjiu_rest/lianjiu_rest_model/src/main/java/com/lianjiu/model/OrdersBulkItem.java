package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersBulkItem {
	private String bulkItemId;

	private String ordersId;

	private String bulkItemName;

	private String bulkItemProductId;

	private Long bulkItemProductCid;

	private String bulkItemPrice;

	private String bulkItemPriceCurrent;

	private String bulkItemAccountPrice;

	private String bulkItemAccountRetrPrice;

	private String bulkItemNum;

	private String bulkItemNumModify;

	private Byte bulkItemUnit;

	private Byte bulkItemVolume;
	// 添加时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date bulkItemCreated;

	private Date bulkItemUpdated;

	public String getBulkItemId() {
		return bulkItemId;
	}

	public void setBulkItemId(String bulkItemId) {
		this.bulkItemId = bulkItemId == null ? null : bulkItemId.trim();
	}

	public String getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId == null ? null : ordersId.trim();
	}

	public String getBulkItemName() {
		return bulkItemName;
	}

	public void setBulkItemName(String bulkItemName) {
		this.bulkItemName = bulkItemName == null ? null : bulkItemName.trim();
	}

	public String getBulkItemProductId() {
		return bulkItemProductId;
	}

	public void setBulkItemProductId(String bulkItemProductId) {
		this.bulkItemProductId = bulkItemProductId == null ? null : bulkItemProductId.trim();
	}

	public Long getBulkItemProductCid() {
		return bulkItemProductCid;
	}

	public void setBulkItemProductCid(Long bulkItemProductCid) {
		this.bulkItemProductCid = bulkItemProductCid;
	}

	public String getBulkItemPrice() {
		return bulkItemPrice;
	}

	public void setBulkItemPrice(String bulkItemPrice) {
		this.bulkItemPrice = bulkItemPrice == null ? null : bulkItemPrice.trim();
	}

	public String getBulkItemAccountPrice() {
		return bulkItemAccountPrice;
	}

	public void setBulkItemAccountPrice(String bulkItemAccountPrice) {
		this.bulkItemAccountPrice = bulkItemAccountPrice == null ? null : bulkItemAccountPrice.trim();
	}

	public String getBulkItemNum() {
		return bulkItemNum;
	}

	public void setBulkItemNum(String bulkItemNum) {
		this.bulkItemNum = bulkItemNum == null ? null : bulkItemNum.trim();
	}

	public String getBulkItemNumModify() {
		return bulkItemNumModify;
	}

	public void setBulkItemNumModify(String bulkItemNumModify) {
		this.bulkItemNumModify = bulkItemNumModify == null ? null : bulkItemNumModify.trim();
	}

	public Byte getBulkItemUnit() {
		return bulkItemUnit;
	}

	public void setBulkItemUnit(Byte bulkItemUnit) {
		this.bulkItemUnit = bulkItemUnit;
	}

	public Byte getBulkItemVolume() {
		return bulkItemVolume;
	}

	public void setBulkItemVolume(Byte bulkItemVolume) {
		this.bulkItemVolume = bulkItemVolume;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getBulkItemCreated() {
		return bulkItemCreated;
	}

	public void setBulkItemCreated(Date bulkItemCreated) {
		this.bulkItemCreated = bulkItemCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getBulkItemUpdated() {
		return bulkItemUpdated;
	}

	public String getBulkItemPriceCurrent() {
		return bulkItemPriceCurrent;
	}

	public void setBulkItemPriceCurrent(String bulkItemPriceCurrent) {
		this.bulkItemPriceCurrent = bulkItemPriceCurrent;
	}

	public String getBulkItemAccountRetrPrice() {
		return bulkItemAccountRetrPrice;
	}

	public void setBulkItemAccountRetrPrice(String bulkItemAccountRetrPrice) {
		this.bulkItemAccountRetrPrice = bulkItemAccountRetrPrice;
	}

	public void setBulkItemUpdated(Date bulkItemUpdated) {
		this.bulkItemUpdated = bulkItemUpdated;
	}

}