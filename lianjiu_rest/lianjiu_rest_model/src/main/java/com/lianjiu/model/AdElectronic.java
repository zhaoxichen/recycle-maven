package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AdElectronic {
	// 主键
	private String eleId;
	// 电器标题
	private String eleTitle;
	// 电器订单号
	private String eleOrder;
	// 电器图片
	private String elePicture;
	// 电器图片点击的链接
	private String elePicLink;
	// 添加时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date addTime;
	// 添加人
	private String addPerson;
	// 状态码
	private String eleState;
	// 图片尺寸
	private String eleSize;
	// 类别
	private String categoryId;

	public String getEleId() {
		return eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId == null ? null : eleId.trim();
	}

	public String getEleTitle() {
		return eleTitle;
	}

	public void setEleTitle(String eleTitle) {
		this.eleTitle = eleTitle == null ? null : eleTitle.trim();
	}

	public String getEleOrder() {
		return eleOrder;
	}

	public void setEleOrder(String eleOrder) {
		this.eleOrder = eleOrder == null ? null : eleOrder.trim();
	}

	public String getElePicture() {
		return elePicture;
	}

	public void setElePicture(String elePicture) {
		this.elePicture = elePicture == null ? null : elePicture.trim();
	}

	public String getElePicLink() {
		return elePicLink;
	}

	public void setElePicLink(String elePicLink) {
		this.elePicLink = elePicLink == null ? null : elePicLink.trim();
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

	public String getEleState() {
		return eleState;
	}

	public void setEleState(String eleState) {
		this.eleState = eleState == null ? null : eleState.trim();
	}

	public String getEleSize() {
		return eleSize;
	}

	public void setEleSize(String eleSize) {
		this.eleSize = eleSize == null ? null : eleSize.trim();
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId == null ? null : categoryId.trim();
	}
}