package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserAddress {
	// 用户地址ID
	private String userAddressId;
	// 用户ID
	private String userId;
	// 收货人姓名
	private String userAddressName;
	// 收货人电话
	private String userAddressPhone;
	// 省份
	private String userProvince;
	// 城市
	private String userCity;
	// 区/县
	private String userDistrict;
	// 街道，门牌号
	private String userLocation;
	// 地点位置
	private String userSite;
	// 经度
	private String latitude;
	// 纬度
	private String longitude;
	// 地址创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date userCreated;
	// 地址更新时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date userUpdated;
	// 是否默认地址(默认true)
	private Boolean userDefault;

	public String getUserAddressName() {
		return userAddressName;
	}

	public void setUserAddressName(String userAddressName) {
		this.userAddressName = userAddressName;
	}

	public String getUserAddressPhone() {
		return userAddressPhone;
	}

	public void setUserAddressPhone(String userAddressPhone) {
		this.userAddressPhone = userAddressPhone;
	}

	public String getUserAddressId() {
		return userAddressId;
	}

	public void setUserAddressId(String userAddressId) {
		this.userAddressId = userAddressId == null ? null : userAddressId.trim();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getUserProvince() {
		return userProvince;
	}

	public void setUserProvince(String userProvince) {
		this.userProvince = userProvince == null ? null : userProvince.trim();
	}

	public String getUserCity() {
		return userCity;
	}

	public void setUserCity(String userCity) {
		this.userCity = userCity == null ? null : userCity.trim();
	}

	public String getUserDistrict() {
		return userDistrict;
	}

	public void setUserDistrict(String userDistrict) {
		this.userDistrict = userDistrict == null ? null : userDistrict.trim();
	}

	public String getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation == null ? null : userLocation.trim();
	}
	
	public String getUserSite() {
        return userSite;
    }

    public void setUserSite(String userSite) {
        this.userSite = userSite == null ? null : userSite.trim();
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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getUserCreated() {
		return userCreated;
	}

	public void setUserCreated(Date userCreated) {
		this.userCreated = userCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getUserUpdated() {
		return userUpdated;
	}

	public void setUserUpdated(Date userUpdated) {
		this.userUpdated = userUpdated;
	}

	public Boolean getUserDefault() {
		return userDefault;
	}

	public void setUserDefault(Boolean userDefault) {
		this.userDefault = userDefault;
	}
}