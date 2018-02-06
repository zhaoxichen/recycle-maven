package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersCompany {
	private String orCompanyId;

	private Long categoryId;

	private String orCompanyName;

	private String orCompanyUser;

	private String orCompanyPhone;

	private String orCompanyProvince;

	private String orCompanyCity;

	private String orCompanyDistrict;

	private String orCompanyLocation;

	private String orCompanyOperater;

	private Date orCompanyCreated;

	private Date orCompanyUpdated;

	private String orCompanyProducts;

	public String getOrCompanyId() {
		return orCompanyId;
	}

	public void setOrCompanyId(String orCompanyId) {
		this.orCompanyId = orCompanyId == null ? null : orCompanyId.trim();
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getOrCompanyName() {
		return orCompanyName;
	}

	public void setOrCompanyName(String orCompanyName) {
		this.orCompanyName = orCompanyName == null ? null : orCompanyName.trim();
	}

	public String getOrCompanyUser() {
		return orCompanyUser;
	}

	public void setOrCompanyUser(String orCompanyUser) {
		this.orCompanyUser = orCompanyUser == null ? null : orCompanyUser.trim();
	}

	public String getOrCompanyPhone() {
		return orCompanyPhone;
	}

	public void setOrCompanyPhone(String orCompanyPhone) {
		this.orCompanyPhone = orCompanyPhone == null ? null : orCompanyPhone.trim();
	}

	public String getOrCompanyProvince() {
		return orCompanyProvince;
	}

	public void setOrCompanyProvince(String orCompanyProvince) {
		this.orCompanyProvince = orCompanyProvince == null ? null : orCompanyProvince.trim();
	}

	public String getOrCompanyCity() {
		return orCompanyCity;
	}

	public void setOrCompanyCity(String orCompanyCity) {
		this.orCompanyCity = orCompanyCity == null ? null : orCompanyCity.trim();
	}

	public String getOrCompanyDistrict() {
		return orCompanyDistrict;
	}

	public void setOrCompanyDistrict(String orCompanyDistrict) {
		this.orCompanyDistrict = orCompanyDistrict == null ? null : orCompanyDistrict.trim();
	}

	public String getOrCompanyLocation() {
		return orCompanyLocation;
	}

	public void setOrCompanyLocation(String orCompanyLocation) {
		this.orCompanyLocation = orCompanyLocation == null ? null : orCompanyLocation.trim();
	}

	public String getOrCompanyOperater() {
		return orCompanyOperater;
	}

	public void setOrCompanyOperater(String orCompanyOperater) {
		this.orCompanyOperater = orCompanyOperater == null ? null : orCompanyOperater.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrCompanyCreated() {
		return orCompanyCreated;
	}

	public void setOrCompanyCreated(Date orCompanyCreated) {
		this.orCompanyCreated = orCompanyCreated;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrCompanyUpdated() {
		return orCompanyUpdated;
	}

	public void setOrCompanyUpdated(Date orCompanyUpdated) {
		this.orCompanyUpdated = orCompanyUpdated;
	}

	public String getOrCompanyProducts() {
		return orCompanyProducts;
	}

	public void setOrCompanyProducts(String orCompanyProducts) {
		this.orCompanyProducts = orCompanyProducts == null ? null : orCompanyProducts.trim();
	}
}