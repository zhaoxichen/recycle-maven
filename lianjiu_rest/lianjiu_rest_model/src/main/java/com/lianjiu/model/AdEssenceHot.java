package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AdEssenceHot {
	//精品热门 主键id
    private String essId;
    //产品编号
    private String productId;
    //产品标题
    private String essTitle;
    //产品价格
    private String proPrice;
    //位置
    private String essPosition;
    //对应产品图
    private String essPicture;
    //对应产品图跳转的链接地址，可为空
    private String essPicLink;
    //添加时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;
    //添加人
    private String addPerson;
    //标签
    private String essSize;
    //类别id
    private String categoryId;

    public String getEssId() {
        return essId;
    }

    public void setEssId(String essId) {
        this.essId = essId == null ? null : essId.trim();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getEssTitle() {
        return essTitle;
    }

    public void setEssTitle(String essTitle) {
        this.essTitle = essTitle == null ? null : essTitle.trim();
    }

    public String getProPrice() {
        return proPrice;
    }

    public void setProPrice(String proPrice) {
        this.proPrice = proPrice == null ? null : proPrice.trim();
    }

    public String getEssPosition() {
        return essPosition;
    }

    public void setEssPosition(String essPosition) {
        this.essPosition = essPosition == null ? null : essPosition.trim();
    }

    public String getEssPicture() {
        return essPicture;
    }

    public void setEssPicture(String essPicture) {
        this.essPicture = essPicture == null ? null : essPicture.trim();
    }

    public String getEssPicLink() {
        return essPicLink;
    }

    public void setEssPicLink(String essPicLink) {
        this.essPicLink = essPicLink == null ? null : essPicLink.trim();
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

    public String getEssSize() {
        return essSize;
    }

    public void setEssSize(String essSize) {
        this.essSize = essSize == null ? null : essSize.trim();
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }
}