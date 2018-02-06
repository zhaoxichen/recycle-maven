package com.lianjiu.rest.model;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FaceItemVo {
	private List<String> orItemsStemFrom;
	private String orFacefaceId;

	private Long categoryId;

	private String orFacefaceAllianceId;

	private String userId;

	private String userPhone;

	private String username;

	private String orFacefaceProvince;

	private String orFacefaceCity;

	private String orFacefaceDistrict;

	private Byte orFacefaceStatus;
	
	private Double distance;
	
	

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	private String orFacefaceBrothersId;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date orFacefaceCreated;

	private Date orFacefaceUpdated;

	private String orFacefaceLocation;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date orFacefaceVisittime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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

	public List<String> getorItemsStemFrom() {
		return orItemsStemFrom;
	}

	public void setorItemsStemFrom(List<String> item) {
		this.orItemsStemFrom = item;
	}
}
