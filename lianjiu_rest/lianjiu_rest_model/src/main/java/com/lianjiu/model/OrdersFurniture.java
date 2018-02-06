package com.lianjiu.model;

import java.util.Date;

public class OrdersFurniture {
    private String orFurnitureId;

    private Long categoryId;

    private String orFurnitureAllianceId;

    private String addressId;

    private String userId;

    private String username;

    private Byte orFurnitureStatus;

    private String orFurnitureBrothersId;

    private Date orFurnitureVisittime;

    private String ordersPrice;

    private String ordersRetrPrice;

    private Date orFurniturePaytime;

    private String orFurnitureHandlerId;

    private String orFurnitureHandlerTel;

    private String orItemsNamePreview;

    private String orItemsPictruePreview;

    private Date orFurnitureCreated;

    private Date orFurnitureUpdated;

    public String getOrFurnitureId() {
        return orFurnitureId;
    }

    public void setOrFurnitureId(String orFurnitureId) {
        this.orFurnitureId = orFurnitureId == null ? null : orFurnitureId.trim();
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getOrFurnitureAllianceId() {
        return orFurnitureAllianceId;
    }

    public void setOrFurnitureAllianceId(String orFurnitureAllianceId) {
        this.orFurnitureAllianceId = orFurnitureAllianceId == null ? null : orFurnitureAllianceId.trim();
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId == null ? null : addressId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Byte getOrFurnitureStatus() {
        return orFurnitureStatus;
    }

    public void setOrFurnitureStatus(Byte orFurnitureStatus) {
        this.orFurnitureStatus = orFurnitureStatus;
    }

    public String getOrFurnitureBrothersId() {
        return orFurnitureBrothersId;
    }

    public void setOrFurnitureBrothersId(String orFurnitureBrothersId) {
        this.orFurnitureBrothersId = orFurnitureBrothersId == null ? null : orFurnitureBrothersId.trim();
    }

    public Date getOrFurnitureVisittime() {
        return orFurnitureVisittime;
    }

    public void setOrFurnitureVisittime(Date orFurnitureVisittime) {
        this.orFurnitureVisittime = orFurnitureVisittime;
    }

    public String getOrdersPrice() {
        return ordersPrice;
    }

    public void setOrdersPrice(String ordersPrice) {
        this.ordersPrice = ordersPrice == null ? null : ordersPrice.trim();
    }

    public String getOrdersRetrPrice() {
        return ordersRetrPrice;
    }

    public void setOrdersRetrPrice(String ordersRetrPrice) {
        this.ordersRetrPrice = ordersRetrPrice == null ? null : ordersRetrPrice.trim();
    }

    public Date getOrFurniturePaytime() {
        return orFurniturePaytime;
    }

    public void setOrFurniturePaytime(Date orFurniturePaytime) {
        this.orFurniturePaytime = orFurniturePaytime;
    }

    public String getOrFurnitureHandlerId() {
        return orFurnitureHandlerId;
    }

    public void setOrFurnitureHandlerId(String orFurnitureHandlerId) {
        this.orFurnitureHandlerId = orFurnitureHandlerId == null ? null : orFurnitureHandlerId.trim();
    }

    public String getOrFurnitureHandlerTel() {
        return orFurnitureHandlerTel;
    }

    public void setOrFurnitureHandlerTel(String orFurnitureHandlerTel) {
        this.orFurnitureHandlerTel = orFurnitureHandlerTel == null ? null : orFurnitureHandlerTel.trim();
    }

    public String getOrItemsNamePreview() {
        return orItemsNamePreview;
    }

    public void setOrItemsNamePreview(String orItemsNamePreview) {
        this.orItemsNamePreview = orItemsNamePreview == null ? null : orItemsNamePreview.trim();
    }

    public String getOrItemsPictruePreview() {
        return orItemsPictruePreview;
    }

    public void setOrItemsPictruePreview(String orItemsPictruePreview) {
        this.orItemsPictruePreview = orItemsPictruePreview == null ? null : orItemsPictruePreview.trim();
    }

    public Date getOrFurnitureCreated() {
        return orFurnitureCreated;
    }

    public void setOrFurnitureCreated(Date orFurnitureCreated) {
        this.orFurnitureCreated = orFurnitureCreated;
    }

    public Date getOrFurnitureUpdated() {
        return orFurnitureUpdated;
    }

    public void setOrFurnitureUpdated(Date orFurnitureUpdated) {
        this.orFurnitureUpdated = orFurnitureUpdated;
    }
}