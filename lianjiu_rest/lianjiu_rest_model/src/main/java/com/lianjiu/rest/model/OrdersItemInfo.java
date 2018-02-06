package com.lianjiu.rest.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersItemInfo {
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

    private String orItemsUser;

    private String orItemsRecycleType;

    private Integer unit;
    

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
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

    public String getOrItemsStemFrom() {
        return orItemsStemFrom;
    }

    public void setOrItemsStemFrom(String orItemsStemFrom) {
        this.orItemsStemFrom = orItemsStemFrom == null ? null : orItemsStemFrom.trim();
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

    public String getOrItemsUser() {
        return orItemsUser;
    }

    public void setOrItemsUser(String orItemsUser) {
        this.orItemsUser = orItemsUser == null ? null : orItemsUser.trim();
    }

    public String getOrItemsRecycleType() {
        return orItemsRecycleType;
    }

    public void setOrItemsRecycleType(String orItemsRecycleType) {
        this.orItemsRecycleType = orItemsRecycleType == null ? null : orItemsRecycleType.trim();
    }

	public String getOrItemsAccountPrice() {
		return orItemsAccountPrice;
	}

	public void setOrItemsAccountPrice(String orItemsAccountPrice) {
		this.orItemsAccountPrice = orItemsAccountPrice;
	}
}