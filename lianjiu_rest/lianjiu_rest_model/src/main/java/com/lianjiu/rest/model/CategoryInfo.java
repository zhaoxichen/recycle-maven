package com.lianjiu.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CategoryInfo {
	/**
	 * 类目ID
	 */
	private Long categoryId;
	/**
	 * 父类目ID=0时，代表的是一级的类目
	 */
	private Long categoryParentId;
	/**
	 * 类目名称
	 */
	private String categoryName;
	/**
	 * 图标
	 */
	private String categoryImage;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getCategoryParentId() {
		return categoryParentId;
	}

	public void setCategoryParentId(Long categoryParentId) {
		this.categoryParentId = categoryParentId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryImage() {
		return categoryImage;
	}

	public void setCategoryImage(String categoryImage) {
		this.categoryImage = categoryImage;
	}
}
