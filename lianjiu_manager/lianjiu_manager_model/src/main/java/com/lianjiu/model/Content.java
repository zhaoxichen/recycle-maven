package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Content {

	private Long contentId;
	/**
	 * 内容类目ID
	 */
	private Long categoryId;
	/**
	 * 主题
	 */
	private String contentTitle;
	/**
	 * 子标题
	 */
	private String contentSubTitle;
	/**
	 * 标题描述
	 */
	private String contentTitleDesc;
	/**
	 * 链接
	 */
	private String contentUrl;
	/**
	 * 广告的播放顺序
	 */
	private Integer contentOrdering;
	/**
	 * 图片绝对路径
	 */
	private String contentImage;
	/**
	 * 内容
	 */
	private String contentContent;

	private Date contentCreated;

	private Date contentUpdated;

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	public String getContentSubTitle() {
		return contentSubTitle;
	}

	public void setContentSubTitle(String contentSubTitle) {
		this.contentSubTitle = contentSubTitle;
	}

	public String getContentTitleDesc() {
		return contentTitleDesc;
	}

	public void setContentTitleDesc(String contentTitleDesc) {
		this.contentTitleDesc = contentTitleDesc;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public Integer getContentOrdering() {
		return contentOrdering;
	}

	public void setContentOrdering(Integer contentOrdering) {
		this.contentOrdering = contentOrdering;
	}

	public String getContentImage() {
		return contentImage;
	}

	public void setContentImage(String contentImage) {
		this.contentImage = contentImage;
	}

	public String getContentContent() {
		return contentContent;
	}

	public void setContentContent(String contentContent) {
		this.contentContent = contentContent;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getContentCreated() {
		return contentCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public void setContentCreated(Date contentCreated) {
		this.contentCreated = contentCreated;
	}

	public Date getContentUpdated() {
		return contentUpdated;
	}

	public void setContentUpdated(Date contentUpdated) {
		this.contentUpdated = contentUpdated;
	}

}