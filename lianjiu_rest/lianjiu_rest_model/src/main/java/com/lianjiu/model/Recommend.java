package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Recommend {
	/**
	 * 推荐人ID
	 */
	private String recommendId;
	/**
	 * 用户编号
	 */
	private String userId;
	/**
	 * 推荐人姓名
	 */
	private String recommendName;
	/**
	 * 推荐人电话
	 */
	private String recommendPhone;
	/**
	 * 推荐人编号
	 */
	private String recommendNum;
	/**
	 * 推荐人上级ID
	 */
	private String recommendSuperior;
	/**
	 * 推荐人创建时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date recommendCreated;
	/**
	 * 推荐人更新时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date recommendUpdated;
	/**
	 * 推荐总数
	 */
	private Integer recommendTotalNum;
	/**
	 * 操作人
	 */
	private String recemmendOperator;

	public String getRecemmendOperator() {
		return recemmendOperator;
	}

	public void setRecemmendOperator(String recemmendOperator) {
		this.recemmendOperator = recemmendOperator;
	}

	public Integer getRecommendTotalNum() {
		return recommendTotalNum;
	}

	public void setRecommendTotalNum(Integer recommendTotalNum) {
		this.recommendTotalNum = recommendTotalNum;
	}

	public String getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(String recommendId) {
		this.recommendId = recommendId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRecommendName() {
		return recommendName;
	}

	public void setRecommendName(String recommendName) {
		this.recommendName = recommendName;
	}

	public String getRecommendPhone() {
		return recommendPhone;
	}

	public void setRecommendPhone(String recommendPhone) {
		this.recommendPhone = recommendPhone;
	}

	public String getRecommendNum() {
		return recommendNum;
	}

	public void setRecommendNum(String recommendNum) {
		this.recommendNum = recommendNum;
	}

	public String getRecommendSuperior() {
		return recommendSuperior;
	}

	public void setRecommendSuperior(String recommendSuperior) {
		this.recommendSuperior = recommendSuperior;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getRecommendCreated() {
		return recommendCreated;
	}

	public void setRecommendCreated(Date recommendCreated) {
		this.recommendCreated = recommendCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getRecommendUpdated() {
		return recommendUpdated;
	}

	public void setRecommendUpdated(Date recommendUpdated) {
		this.recommendUpdated = recommendUpdated;
	}

}