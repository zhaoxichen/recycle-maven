package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * alliance_business_application
 * 
 * @author jdandian.com
 * @date 2017年09月07日
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AllianceBusinessApplication {
	/**
	 * 申请表ID
	 */
	private String albnessApplicationId;
	/**
	 * 姓名
	 */
	private String albnessApplicationName;
	/**
	 * 联系方式
	 */
	private String albnessApplicationPhone;
	/**
	 * 省份
	 */
	private String albnessApplicationProvince;
	/**
	 * 城市
	 */
	private String albnessApplicationCity;
	/**
	 * 区/县
	 */
	private String albnessApplicationDistrict;
	/**
	 * 联系地址，路，街道
	 */
	private String albnessApplicationLocation;
	/**
	 * 可回收范围
	 */
	private String albnessApplicationRetrievalRange;
	/**
	 * 可回收总类
	 */
	private String albnessApplicationType;
	/**
	 * 操作员ID
	 */
	private String albnessApplicationOperatorId;
	/**
	 * 申请时间
	 */
	private Date albnessApplicationTime;
	/**
	 * 备注时间
	 */
	private Date albnessApplicationRemarkTime;
	/**
	 * 备注内容
	 */
	private String albnessApplicationRemarks;
	/**
	 * 申请合同图片地址
	 */
	private String albnessApplicationImage;
	/**
	 * 申请状态
	 */
	private String albnessApplicationStatus;
	/**
	 * 
	 * 所属分类
	 */
	private Long categoryId;
	/**
	 * 用户信息ID
	 */
	private String abunesDetailsId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getAlbnessApplicationStatus() {
		return albnessApplicationStatus;
	}

	public void setAlbnessApplicationStatus(String albnessApplicationStatus) {
		this.albnessApplicationStatus = albnessApplicationStatus;
	}

	public String getAlbnessApplicationId() {
		return albnessApplicationId;
	}

	public void setAlbnessApplicationId(String albnessApplicationId) {
		this.albnessApplicationId = albnessApplicationId;
	}

	public String getAlbnessApplicationName() {
		return albnessApplicationName;
	}

	public void setAlbnessApplicationName(String albnessApplicationName) {
		this.albnessApplicationName = albnessApplicationName;
	}

	public String getAlbnessApplicationPhone() {
		return albnessApplicationPhone;
	}

	public void setAlbnessApplicationPhone(String albnessApplicationPhone) {
		this.albnessApplicationPhone = albnessApplicationPhone;
	}

	public String getAlbnessApplicationProvince() {
		return albnessApplicationProvince;
	}

	public void setAlbnessApplicationProvince(String albnessApplicationProvince) {
		this.albnessApplicationProvince = albnessApplicationProvince;
	}

	public String getAlbnessApplicationCity() {
		return albnessApplicationCity;
	}

	public void setAlbnessApplicationCity(String albnessApplicationCity) {
		this.albnessApplicationCity = albnessApplicationCity;
	}

	public String getAlbnessApplicationDistrict() {
		return albnessApplicationDistrict;
	}

	public void setAlbnessApplicationDistrict(String albnessApplicationDistrict) {
		this.albnessApplicationDistrict = albnessApplicationDistrict;
	}

	public String getAlbnessApplicationLocation() {
		return albnessApplicationLocation;
	}

	public void setAlbnessApplicationLocation(String albnessApplicationLocation) {
		this.albnessApplicationLocation = albnessApplicationLocation;
	}

	public String getAlbnessApplicationRetrievalRange() {
		return albnessApplicationRetrievalRange;
	}

	public void setAlbnessApplicationRetrievalRange(String albnessApplicationRetrievalRange) {
		this.albnessApplicationRetrievalRange = albnessApplicationRetrievalRange;
	}

	public String getAlbnessApplicationType() {
		return albnessApplicationType;
	}

	public void setAlbnessApplicationType(String albnessApplicationType) {
		this.albnessApplicationType = albnessApplicationType;
	}

	public String getAlbnessApplicationOperatorId() {
		return albnessApplicationOperatorId;
	}

	public void setAlbnessApplicationOperatorId(String albnessApplicationOperatorId) {
		this.albnessApplicationOperatorId = albnessApplicationOperatorId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getAlbnessApplicationTime() {
		return albnessApplicationTime;
	}

	public void setAlbnessApplicationTime(Date albnessApplicationTime) {
		this.albnessApplicationTime = albnessApplicationTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getAlbnessApplicationRemarkTime() {
		return albnessApplicationRemarkTime;
	}

	public void setAlbnessApplicationRemarkTime(Date albnessApplicationRemarkTime) {
		this.albnessApplicationRemarkTime = albnessApplicationRemarkTime;
	}

	public String getAlbnessApplicationRemarks() {
		return albnessApplicationRemarks;
	}

	public void setAlbnessApplicationRemarks(String albnessApplicationRemarks) {
		this.albnessApplicationRemarks = albnessApplicationRemarks;
	}

	public String getAlbnessApplicationImage() {
		return albnessApplicationImage;
	}

	public void setAlbnessApplicationImage(String albnessApplicationImage) {
		this.albnessApplicationImage = albnessApplicationImage;
	}

	public String getAbunesDetailsId() {
		return abunesDetailsId;
	}

	public void setAbunesDetailsId(String abunesDetailsId) {
		this.abunesDetailsId = abunesDetailsId == null ? null : abunesDetailsId.trim();
	}

}