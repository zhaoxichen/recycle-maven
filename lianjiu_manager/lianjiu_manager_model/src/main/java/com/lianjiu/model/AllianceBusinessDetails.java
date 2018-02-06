package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 加盟商
 * 
 * @author jdandian.com
 * @date 2017年09月08日
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AllianceBusinessDetails {
	/**
	 * 加盟商编号
	 */
	private String abunesId;
	/**
	 * 加盟商姓名
	 */
	private String abunesName;
	/**
	 * 加盟商的电话
	 */
	private String abunesPhone;
	/**
	 * 是否接单 0 不接单，1 接单  2取消资格默认是0
	 */
	private Integer abunesAcceptOrder;
	/**
	 * 七天内完成的订单
	 */
	private Integer abunesWeekOrder;
	/**
	 * 省份
	 */
	private String abunesAllianceProvince;
	/**
	 * 城市
	 */
	private String abunesAllianceCity;
	/**
	 * 区/县
	 */
	private String abunesAllianceDistrict;
	/**
	 * 加盟商的联系地址:xx路xx号
	 */
	private String abunesAllianceLocation;
	/**
	 * 加盟商类型(0.普通加盟商 1.物业加盟商)
	 */
	private String abunesBuinessType;
	/**
	 * 加盟时间(操作员点击开通加盟账户时间)
	 */
	private Date abunesCreated;
	/**
	 * 加盟商信息更新时间
	 */
	private Date abunesUpdated;
	/**
	 * 合同图片地址
	 */
	private String abunesImage;
	/**
	 * 超过七天未上门的订单
	 */
	private Integer abunesUnfinishedOrder;
	/**
	 * 操作(0取消资格，1未开通资格，2开通资格)
	 */
	private String abunesOperation;
	/**
	 * 3个月内取消订单数量
	 */
	private Integer abunesCancelOrders;
	/**
	 * 
	 * 所属分类
	 */
	private Long categoryId;
	/**
	 * 加盟商申请表ID
	 */
	private String abunesAppId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getAbunesCancelOrders() {
		return abunesCancelOrders;
	}

	public void setAbunesCancelOrders(Integer abunesCancelOrders) {
		this.abunesCancelOrders = abunesCancelOrders;
	}

	public String getAbunesId() {
		return abunesId;
	}

	public void setAbunesId(String abunesId) {
		this.abunesId = abunesId;
	}

	public String getAbunesName() {
		return abunesName;
	}

	public void setAbunesName(String abunesName) {
		this.abunesName = abunesName;
	}

	public String getAbunesPhone() {
		return abunesPhone;
	}

	public void setAbunesPhone(String abunesPhone) {
		this.abunesPhone = abunesPhone;
	}

	public Integer getAbunesAcceptOrder() {
		return abunesAcceptOrder;
	}

	public void setAbunesAcceptOrder(Integer abunesAcceptOrder) {
		this.abunesAcceptOrder = abunesAcceptOrder;
	}

	public Integer getAbunesWeekOrder() {
		return abunesWeekOrder;
	}

	public void setAbunesWeekOrder(Integer abunesWeekOrder) {
		this.abunesWeekOrder = abunesWeekOrder;
	}

	public String getAbunesAllianceProvince() {
		return abunesAllianceProvince;
	}

	public void setAbunesAllianceProvince(String abunesAllianceProvince) {
		this.abunesAllianceProvince = abunesAllianceProvince;
	}

	public String getAbunesAllianceCity() {
		return abunesAllianceCity;
	}

	public void setAbunesAllianceCity(String abunesAllianceCity) {
		this.abunesAllianceCity = abunesAllianceCity;
	}

	public String getAbunesAllianceDistrict() {
		return abunesAllianceDistrict;
	}

	public void setAbunesAllianceDistrict(String abunesAllianceDistrict) {
		this.abunesAllianceDistrict = abunesAllianceDistrict;
	}

	public String getAbunesAllianceLocation() {
		return abunesAllianceLocation;
	}

	public void setAbunesAllianceLocation(String abunesAllianceLocation) {
		this.abunesAllianceLocation = abunesAllianceLocation;
	}

	public String getAbunesBuinessType() {
		return abunesBuinessType;
	}

	public void setAbunesBuinessType(String abunesBuinessType) {
		this.abunesBuinessType = abunesBuinessType;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getAbunesCreated() {
		return abunesCreated;
	}

	public void setAbunesCreated(Date abunesCreated) {
		this.abunesCreated = abunesCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getAbunesUpdated() {
		return abunesUpdated;
	}

	public void setAbunesUpdated(Date abunesUpdated) {
		this.abunesUpdated = abunesUpdated;
	}

	public String getAbunesImage() {
		return abunesImage;
	}

	public void setAbunesImage(String abunesImage) {
		this.abunesImage = abunesImage;
	}

	public Integer getAbunesUnfinishedOrder() {
		return abunesUnfinishedOrder;
	}

	public void setAbunesUnfinishedOrder(Integer abunesUnfinishedOrder) {
		this.abunesUnfinishedOrder = abunesUnfinishedOrder;
	}

	public String getAbunesOperation() {
		return abunesOperation;
	}

	public void setAbunesOperation(String abunesOperation) {
		this.abunesOperation = abunesOperation;
	}

	public String getAbunesAppId() {
		return abunesAppId;
	}

	public void setAbunesAppId(String abunesAppId) {
		this.abunesAppId = abunesAppId == null ? null : abunesAppId.trim();
	}

}