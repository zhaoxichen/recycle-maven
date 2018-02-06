package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 商品类目
 * 
 * @author jdandian.com
 * @date 2017年09月01日
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Category {
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
	 * 图片地址
	 */
	private String categoryImage;
	/**
	 * 回收方式：0为快递，1为上门回收
	 */
	private Integer categoryRetrieveType;
	/**
	 * 状态。可选值:1(正常),2(删除)
	 */
	private Integer categoryStatus;
	/**
	 * 排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
	 */
	private Integer categorySortOrder;
	/**
	 * 该类目是否为父类目，1为true，0为false
	 */
	private Integer categoryIsParent;
	/**
	 * 参数模版
	 */
	private String categoryParamTemplateId;
	/**
	 * 创建时间
	 */
	private Date categoryCreated;
	/**
	 * 更新时间
	 */
	private Date categoryUpdated;

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

	public Integer getCategoryRetrieveType() {
		return categoryRetrieveType;
	}

	public void setCategoryRetrieveType(Integer categoryRetrieveType) {
		this.categoryRetrieveType = categoryRetrieveType;
	}

	public Integer getCategoryStatus() {
		return categoryStatus;
	}

	public void setCategoryStatus(Integer categoryStatus) {
		this.categoryStatus = categoryStatus;
	}

	public Integer getCategorySortOrder() {
		return categorySortOrder;
	}

	public void setCategorySortOrder(Integer categorySortOrder) {
		this.categorySortOrder = categorySortOrder;
	}

	public Integer getCategoryIsParent() {
		return categoryIsParent;
	}

	public void setCategoryIsParent(Integer categoryIsParent) {
		this.categoryIsParent = categoryIsParent;
	}

	public String getCategoryParamTemplateId() {
		return categoryParamTemplateId;
	}

	public void setCategoryParamTemplateId(String categoryParamTemplateId) {
		this.categoryParamTemplateId = categoryParamTemplateId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCategoryCreated() {
		return categoryCreated;
	}

	public void setCategoryCreated(Date categoryCreated) {
		this.categoryCreated = categoryCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCategoryUpdated() {
		return categoryUpdated;
	}

	public void setCategoryUpdated(Date categoryUpdated) {
		this.categoryUpdated = categoryUpdated;
	}

}