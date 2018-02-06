package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AdHotTopic {
	//热门话题id	
    private String topId;
    //热门话题标题
    private String topTitle;
    //热门话题摘要
    private String topRemark;
    //热门话题内容
    private String topContent;
    //热门话题关键字
    private String topKeyword;
    //热门话题顺序
    private String topOrder;
    //热门话题图片
    private String topPicture;
    //热门话题图片点击链接
    private String topPicLink;
    //添加时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;
    //添加人
    private String addPerson;
    //图片尺寸
    private String actSize;
    //类别
    private String categoryId;
	
	public String getTopId() {
		return topId;
	}

	public void setTopId(String topId) {
		this.topId = topId == null ? null : topId.trim();
	}

	public String getTopTitle() {
		return topTitle;
	}

	public void setTopTitle(String topTitle) {
		this.topTitle = topTitle == null ? null : topTitle.trim();
	}

	public String getTopRemark() {
		return topRemark;
	}

	public void setTopRemark(String topRemark) {
		this.topRemark = topRemark == null ? null : topRemark.trim();
	}

	public String getTopContent() {
		return topContent;
	}

	public void setTopContent(String topContent) {
		this.topContent = topContent == null ? null : topContent.trim();
	}

	public String getTopKeyword() {
		return topKeyword;
	}

	public void setTopKeyword(String topKeyword) {
		this.topKeyword = topKeyword == null ? null : topKeyword.trim();
	}

	public String getTopOrder() {
		return topOrder;
	}

	public void setTopOrder(String topOrder) {
		this.topOrder = topOrder == null ? null : topOrder.trim();
	}

	public String getTopPicture() {
		return topPicture;
	}

	public void setTopPicture(String topPicture) {
		this.topPicture = topPicture == null ? null : topPicture.trim();
	}

	public String getTopPicLink() {
		return topPicLink;
	}

	public void setTopPicLink(String topPicLink) {
		this.topPicLink = topPicLink == null ? null : topPicLink.trim();
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

	public String getActSize() {
		return actSize;
	}

	public void setActSize(String actSize) {
		this.actSize = actSize == null ? null : actSize.trim();
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId == null ? null : categoryId.trim();
	}
}