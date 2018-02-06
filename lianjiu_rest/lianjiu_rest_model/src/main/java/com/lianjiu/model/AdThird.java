package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AdThird {
	// 三层广告id
	private String thiId;
	// 三层广告标题
	private String thiTitle;
	// 三层广告内容
	private String thiContent;
	// 三层广告位置
	private String thiPosition;
	// 三层广告图片
	private String thiPicture;
	// 三层广告图片点击链接
	private String thiPicLink;
	// 添加时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date addTime;
	// 添加人
	private String addPerson;
	// 图片尺寸
	private String thiSize;
	// 类别
	private String categoryId;

	public String getThiId() {
		return thiId;
	}

	public void setThiId(String thiId) {
		this.thiId = thiId == null ? null : thiId.trim();
	}

	public String getThiTitle() {
		return thiTitle;
	}

	public void setThiTitle(String thiTitle) {
		this.thiTitle = thiTitle == null ? null : thiTitle.trim();
	}

	public String getThiContent() {
		return thiContent;
	}

	public void setThiContent(String thiContent) {
		this.thiContent = thiContent == null ? null : thiContent.trim();
	}

	public String getThiPosition() {
		return thiPosition;
	}

	public void setThiPosition(String thiPosition) {
		this.thiPosition = thiPosition == null ? null : thiPosition.trim();
	}

	public String getThiPicture() {
		return thiPicture;
	}

	public void setThiPicture(String thiPicture) {
		this.thiPicture = thiPicture == null ? null : thiPicture.trim();
	}

	public String getThiPicLink() {
		return thiPicLink;
	}

	public void setThiPicLink(String thiPicLink) {
		this.thiPicLink = thiPicLink == null ? null : thiPicLink.trim();
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

	public String getThiSize() {
		return thiSize;
	}

	public void setThiSize(String thiSize) {
		this.thiSize = thiSize == null ? null : thiSize.trim();
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId == null ? null : categoryId.trim();
	}
}