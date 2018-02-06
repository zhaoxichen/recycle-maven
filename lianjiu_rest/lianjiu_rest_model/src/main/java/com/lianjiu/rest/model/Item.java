package com.lianjiu.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Item {

	private String id;
	private String name;
	private String image;
	private String firstPrice;
	private String secondPrice;
	private String categoryName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(String firstPrice) {
		this.firstPrice = firstPrice;
	}

	public String getSecondPrice() {
		return secondPrice;
	}

	public void setSecondPrice(String secondPrice) {
		this.secondPrice = secondPrice;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
