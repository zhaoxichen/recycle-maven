package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AdSecond {
	// 首页二层广告id
	private String secId;
	// 二层广告名称
	private String secTitle;
	// 二层广告内容
	private String secContent;
	// 二层广告位置
	private String secPosition;
	// 二层广告图片
	private String secPicture;
	// 二层广告图片点击链接
	private String secPicLink;
	// 添加时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date addTime;
	// 添加人
	private String addPerson;
	// 尺寸
	private String secSize;
	// 类别
	private String categoryId;

	public String getSecId() {
		return secId;
	}

	public void setSecId(String secId) {
		this.secId = secId == null ? null : secId.trim();
	}

	public String getSecTitle() {
		return secTitle;
	}

	public void setSecTitle(String secTitle) {
		this.secTitle = secTitle == null ? null : secTitle.trim();
	}

	public String getSecContent() {
		return secContent;
	}

	public void setSecContent(String secContent) {
		this.secContent = secContent == null ? null : secContent.trim();
	}

	public String getSecPosition() {
		return secPosition;
	}

	public void setSecPosition(String secPosition) {
		this.secPosition = secPosition == null ? null : secPosition.trim();
	}

	public String getSecPicture() {
		return secPicture;
	}

	public void setSecPicture(String secPicture) {
		this.secPicture = secPicture == null ? null : secPicture.trim();
	}

	public String getSecPicLink() {
		return secPicLink;
	}

	public void setSecPicLink(String secPicLink) {
		this.secPicLink = secPicLink == null ? null : secPicLink.trim();
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