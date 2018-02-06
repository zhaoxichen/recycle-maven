package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AdHotProduct {
	//热卖产品id
    private String hotId;
    //商品id
    private String productId;
    //热卖产品名称
    private String hotTitle;
    //热卖产品价格
    private String proPrice;
    //热卖产品位置
    private String hotPosition;
    //热卖产品图片
    private String hotPicture;
    //热卖产品图片点击链接
    private String hotPicLink;
    //热卖产品添加时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;
    //热卖产品添加人
    private String addPerson;
    //标签
    private String hotSize;
    //类别
    private String categoryId;

    public String getHotId() {
        return hotId;
    }

    public void setHotId(String hotId) {
        this.hotId = hotId == null ? null : hotId.trim();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getHotTitle() {
        return hotTitle;
    }

    public void setHotTitle(String hotTitle) {
        this.hotTitle = hotTitle == null ? null : hotTitle.trim();
    }

    public String getProPrice() {
        return proPrice;
    }

    public void setProPrice(String proPrice) {
        this.proPrice = proPrice == null ? null : proPrice.trim();
    }

    public String getHotPosition() {
        return hotPosition;
    }

    public void setHotPosition(String hotPosition) {
        this.hotPosition = hotPosition == null ? null : hotPosition.trim();
    }

    public String getHotPicture() {
        return hotPicture;
    }

    public void setHotPicture(String hotPicture) {
        this.hotPicture = hotPicture == null ? null : hotPicture.trim();
    }

    public String getHotPicLink() {
        return hotPicLink;
    }

    public void setHotPicLink(String hotPicLink) {
        this.hotPicLink = hotPicLink == null ? null : hotPicLink.trim();
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAddPerson() {
        return addPerson;
    }

    public void setAddPerson(String addPerson) {
        this.addPerson = addPerson == null ? null : addPerson.trim();
    }

    public String getHotSize() {
        return hotSize;
    }

    public void setHotSize(String hotSize) {
        this.hotSize = hotSize == null ? null : hotSize.trim();
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }
}