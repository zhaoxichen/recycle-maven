package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AdTheme {
	// 广告主题id
	private String theId;
	// 广告主题标题
	private String theTitle;
	// 广告主题内容
	private String theContent;
	// 广告主题位置
	private String thePosition;
	// 广告主题图片
	private String thePicture;
	// 广告主题图片点击链接
	private String thePicLink;
	// 添加时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date addTime;
	// 添加人
	private String addPerson;
	// 图片尺寸
	private String secSize;
	// 类别
	private String categoryId;

	public String getTheId() {
		return theId;
	}

	public void setTheId(String theId) {
		this.theId = theId == null ? null : theId.trim();
	}

	public String getTheTitle() {
		return theTitle;
	}

	public void setTheTitle(String theTitle) {
		this.theTitle = theTitle == null ? null : theTitle.trim();
	}

	public String getTheContent() {
		return theContent;
	}

	public void setTheContent(String theContent) {
		this.theContent = theContent == null ? null : theContent.trim();
	}

	public String getThePosition() {
		return thePosition;
	}

	public void setThePosition(String thePosition) {
		this.thePosition = thePosition == null ? null : thePosition.trim();
	}

	public String getThePicture() {
		return thePicture;
	}

	public void setThePicture(String thePicture) {
		this.thePicture = thePicture == null ? null : thePicture.trim();
	}

	public String getThePicLink() {
		return thePicLink;
	}

	public void setThePicLink(String thePicLink) {
		this.thePicLink = thePicLink == null ? null : thePicLink.trim();
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

	public String getSecSize() {
		return secSize;
	}

	public void setSecSize(String secSize) {
		this.secSize = secSize == null ? null : secSize.trim();
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId == null ? null : categoryId.trim();
	}
}