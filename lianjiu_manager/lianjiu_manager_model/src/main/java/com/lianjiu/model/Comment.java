package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Comment {
	private String commentId;

	private String userId;

	private String username;

	private Long categoryId;

	private Integer commentType;

	private String relativeId;
	
	private Integer commentEmoji;

	private String commentLabelPrice;

	private String commentLabelRemit;

	private String commentLabelService;

	private Date commentCreated;

	private Date commentUpdated;

	private String commentDetails;
	
	private String ordersId;

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId == null ? null : commentId.trim();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getCommentType() {
		return commentType;
	}

	public void setCommentType(Integer commentType) {
		this.commentType = commentType;
	}

	public String getRelativeId() {
		return relativeId;
	}

	public void setRelativeId(String relativeId) {
		this.relativeId = relativeId == null ? null : relativeId.trim();
	}

	public Integer getCommentEmoji() {
		return commentEmoji;
	}

	public void setCommentEmoji(Integer commentEmoji) {
		this.commentEmoji = commentEmoji;
	}

	public String getCommentLabelPrice() {
		return commentLabelPrice;
	}

	public void setCommentLabelPrice(String commentLabelPrice) {
		this.commentLabelPrice = commentLabelPrice == null ? null : commentLabelPrice.trim();
	}

	public String getCommentLabelRemit() {
		return commentLabelRemit;
	}

	public void setCommentLabelRemit(String commentLabelRemit) {
		this.commentLabelRemit = commentLabelRemit == null ? null : commentLabelRemit.trim();
	}

	public String getCommentLabelService() {
		return commentLabelService;
	}

	public void setCommentLabelService(String commentLabelService) {
		this.commentLabelService = commentLabelService == null ? null : commentLabelService.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCommentCreated() {
		return commentCreated;
	}

	public void setCommentCreated(Date commentCreated) {
		this.commentCreated = commentCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCommentUpdated() {
		return commentUpdated;
	}

	public void setCommentUpdated(Date commentUpdated) {
		this.commentUpdated = commentUpdated;
	}

	public String getCommentDetails() {
		return commentDetails;
	}

	public void setCommentDetails(String commentDetails) {
		this.commentDetails = commentDetails == null ? null : commentDetails.trim();
	}

	public String getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId;
	}
	
}