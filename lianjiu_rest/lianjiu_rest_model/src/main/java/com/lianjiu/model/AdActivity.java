package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AdActivity {
	private String actId;

	private String actTitle;

	private String actContent;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date actStartTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date actEndTime;

	private String actPicture;

	private String actPicLink;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date addTime;

	private String addPerson;

	private String actSize;

	private Long categoryId;

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId == null ? null : actId.trim();
	}

	public String getActTitle() {
		return actTitle;
	}

	public void setActTitle(String actTitle) {
		this.actTitle = actTitle == null ? null : actTitle.trim();
	}

	public String getActContent() {
		return actContent;
	}

	public void setActContent(String actContent) {
		this.actContent = actContent == null ? null : actContent.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getActStartTime() {
		return actStartTime;
	}

	public void setActStartTime(Date actStartTime) {
		this.actStartTime = actStartTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getActEndTime() {
		return actEndTime;
	}

	public void setActEndTime(Date actEndTime) {
		this.actEndTime = actEndTime;
	}

	public String getActPicture() {
		return actPicture;
	}

	public void setActPicture(String actPicture) {
		this.actPicture = actPicture == null ? null : actPicture.trim();
	}

	public String getActPicLink() {
		return actPicLink;
	}

	public void setActPicLink(String actPicLink) {
		this.actPicLink = actPicLink == null ? null : actPicLink.trim();
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}