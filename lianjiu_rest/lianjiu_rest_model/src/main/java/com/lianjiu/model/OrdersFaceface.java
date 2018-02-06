package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersFaceface {
	private String orFacefaceId;

	private Long categoryId;

	private String orFacefaceAllianceId;

	private String userId;

	private String userPhone;

	private String addressId;

	private String username;

	private String orFacefaceProvince;

	private String orFacefaceCity;

	private String orFacefaceDistrict;

	private Byte orFacefaceStatus;

	private String orFacefaceBrothersId;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orFacefaceCreated;

	private Date orFacefaceUpdated;

	private String orFacefaceLocation;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date orFacefaceVisittime;

	// 来源(0卖手机、1卖家电、2卖精品等)
	private Integer orItemsStemFrom;

	private String orItemsNamePreview;

	private String orItemsPictruePreview;

	private String orFfDetailsPrice;

	private String orFfDetailsRetrPrice;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orFfDetailsPaytime;

	private String orFfDetailsAllianceId;

	private String orFfDetailsHandlerId;

	private String orFfDetailsHandlerTel;
	// 经度
	private String latitude;
	// 纬度
	private String longitude;
	// 地点位置
	private String userSite;

	// 订单是否过期，废弃
	private Integer isExpire;

	public String getUserSite() {
		return userSite;
	}

	public void setUserSite(String userSite) {
		this.userSite = userSite;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude == null ? null : latitude.trim();
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude == null ? null : longitude.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getOrFacefaceVisittime() {
		return orFacefaceVisittime;
	}

	public void setOrFacefaceVisittime(Date orFacefaceVisittime) {
		this.orFacefaceVisittime = orFacefaceVisittime;
	}

	public String getOrFacefaceId() {
		return orFacefaceId;
	}

	public void setOrFacefaceId(String orFacefaceId) {
		this.orFacefaceId = orFacefaceId == null ? null : orFacefaceId.trim();
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getOrFacefaceAllianceId() {
		return orFacefaceAllianceId;
	}

	public void setOrFacefaceAllianceId(String orFacefaceAllianceId) {
		this.orFacefaceAllianceId = orFacefaceAllianceId == null ? null : orFacefaceAllianceId.trim();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getOrFacefaceProvince() {
		return orFacefaceProvince;
	}

	public void setOrFacefaceProvince(String orFacefaceProvince) {
		this.orFacefaceProvince = orFacefaceProvince == null ? null : orFacefaceProvince.trim();
	}

	public String getOrFacefaceCity() {
		return orFacefaceCity;
	}

	public void setOrFacefaceCity(String orFacefaceCity) {
		this.orFacefaceCity = orFacefaceCity == null ? null : orFacefaceCity.trim();
	}

	public String getOrFacefaceDistrict() {
		return orFacefaceDistrict;
	}

	public void setOrFacefaceDistrict(String orFacefaceDistrict) {
		this.orFacefaceDistrict = orFacefaceDistrict == null ? null : orFacefaceDistrict.trim();
	}

	public Byte getOrFacefaceStatus() {
		return orFacefaceStatus;
	}

	public void setOrFacefaceStatus(Byte orFacefaceStatus) {
		this.orFacefaceStatus = orFacefaceStatus;
	}

	public String getOrFacefaceBrothersId() {
		return orFacefaceBrothersId;
	}

	public void setOrFacefaceBrothersId(String orFacefaceBrothersId) {
		this.orFacefaceBrothersId = orFacefaceBrothersId == null ? null : orFacefaceBrothersId.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrFacefaceCreated() {
		return orFacefaceCreated;
	}

	public void setOrFacefaceCreated(Date orFacefaceCreated) {
		this.orFacefaceCreated = orFacefaceCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrFacefaceUpdated() {
		return orFacefaceUpdated;
	}

	public void setOrFacefaceUpdated(Date orFacefaceUpdated) {
		this.orFacefaceUpdated = orFacefaceUpdated;
	}

	public String getOrFacefaceLocation() {
		return orFacefaceLocation;
	}

	public void setOrFacefaceLocation(String orFacefaceLocation) {
		this.orFacefaceLocation = orFacefaceLocation == null ? null : orFacefaceLocation.trim();
	}

	public Integer getOrItemsStemFrom() {
		return orItemsStemFrom;
	}

	public void setOrItemsStemFrom(Integer orItemsStemFrom) {
		this.orItemsStemFrom = orItemsStemFrom;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getOrItemsNamePreview() {
		return orItemsNamePreview;
	}

	public void setOrItemsNamePreview(String orItemsNamePreview) {
		this.orItemsNamePreview = orItemsNamePreview;
	}

	public String getOrItemsPictruePreview() {
		return orItemsPictruePreview;
	}

	public void setOrItemsPictruePreview(String orItemsPictruePreview) {
		this.orItemsPictruePreview = orItemsPictruePreview;
	}

	public String getOrFfDetailsPrice() {
		return orFfDetailsPrice;
	}

	public void setOrFfDetailsPrice(String orFfDetailsPrice) {
		this.orFfDetailsPrice = orFfDetailsPrice;
	}

	public String getOrFfDetailsRetrPrice() {
		return orFfDetailsRetrPrice;
	}

	public void setOrFfDetailsRetrPrice(String orFfDetailsRetrPrice) {
		this.orFfDetailsRetrPrice = orFfDetailsRetrPrice;
	}

	public Date getOrFfDetailsPaytime() {
		return orFfDetailsPaytime;
	}

	public void setOrFfDetailsPaytime(Date orFfDetailsPaytime) {
		this.orFfDetailsPaytime = orFfDetailsPaytime;
	}

	public String getOrFfDetailsAllianceId() {
		return orFfDetailsAllianceId;
	}

	public void setOrFfDetailsAllianceId(String orFfDetailsAllianceId) {
		this.orFfDetailsAllianceId = orFfDetailsAllianceId;
	}

	public String getOrFfDetailsHandlerId() {
		return orFfDetailsHandlerId;
	}

	public void setOrFfDetailsHandlerId(String orFfDetailsHandlerId) {
		this.orFfDetailsHandlerId = orFfDetailsHandlerId;
	}

	public String getOrFfDetailsHandlerTel() {
		return orFfDetailsHandlerTel;
	}

	public void setOrFfDetailsHandlerTel(String orFfDetailsHandlerTel) {
		this.orFfDetailsHandlerTel = orFfDetailsHandlerTel;
	}

	public Integer getIsExpire() {
		return isExpire;
	}

	public void setIsExpire(Integer isExpire) {
		this.isExpire = isExpire;
	}

}