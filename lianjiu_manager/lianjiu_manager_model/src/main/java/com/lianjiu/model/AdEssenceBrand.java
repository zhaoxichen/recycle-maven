package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AdEssenceBrand {
    private String brId;

    private String brTitle;

    private String brPosition;

    private String brPicture;

    private String brPicLink;

    private String brSize;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String addPerson;

    private Long categoryId;

    public String getBrId() {
        return brId;
    }

    public void setBrId(String brId) {
        this.brId = brId == null ? null : brId.trim();
    }

    public String getBrTitle() {
        return brTitle;
    }

    public void setBrTitle(String brTitle) {
        this.brTitle = brTitle == null ? null : brTitle.trim();
    }

    public String getBrPosition() {
        return brPosition;
    }

    public void setBrPosition(String brPosition) {
        this.brPosition = brPosition == null ? null : brPosition.trim();
    }

    public String getBrPicture() {
        return brPicture;
    }

    public void setBrPicture(String brPicture) {
        this.brPicture = brPicture == null ? null : brPicture.trim();
    }

    public String getBrPicLink() {
        return brPicLink;
    }

    public void setBrPicLink(String brPicLink) {
        this.brPicLink = brPicLink == null ? null : brPicLink.trim();
    }

    public String getBrSize() {
        return brSize;
    }

    public void setBrSize(String brSize) {
        this.brSize = brSize == null ? null : brSize.trim();
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAddPerson() {
        return addPerson;
    }

    public void setAddPerson(String addPerson) {
        this.addPerson = addPerson == null ? null : addPerson.trim();
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}