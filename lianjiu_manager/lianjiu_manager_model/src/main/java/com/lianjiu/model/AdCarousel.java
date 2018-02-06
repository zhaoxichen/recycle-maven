package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AdCarousel {
	//轮播图id
    private String caId;
    //轮播图标题
    private String caTitle;
    //轮播图位置
    private String caPosition;
    //轮播图图片
    private String caPicture;
    //点击轮播图跳转的链接
    private String caPicLink;
    //图片尺寸
    private String caSize;
    //添加时间	
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;
    //添加人
    private String addPerson;
    //类别
    private String categoryId;

    public String getCaId() {
        return caId;
    }

    public void setCaId(String caId) {
        this.caId = caId == null ? null : caId.trim();
    }

    public String getCaTitle() {
        return caTitle;
    }

    public void setCaTitle(String caTitle) {
        this.caTitle = caTitle == null ? null : caTitle.trim();
    }

    public String getCaPosition() {
        return caPosition;
    }

    public void setCaPosition(String caPosition) {
        this.caPosition = caPosition == null ? null : caPosition.trim();
    }

    public String getCaPicture() {
        return caPicture;
    }

    public void setCaPicture(String caPicture) {
        this.caPicture = caPicture == null ? null : caPicture.trim();
    }

    public String getCaPicLink() {
        return caPicLink;
    }

    public void setCaPicLink(String caPicLink) {
        this.caPicLink = caPicLink == null ? null : caPicLink.trim();
    }

    public String getCaSize() {
        return caSize;
    }

    public void setCaSize(String caSize) {
        this.caSize = caSize == null ? null : caSize.trim();
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }
}