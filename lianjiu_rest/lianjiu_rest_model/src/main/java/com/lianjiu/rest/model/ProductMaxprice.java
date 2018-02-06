package com.lianjiu.rest.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProductMaxprice {
	/**
	 * 表格主键
	 */
	private String productId;
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 图片地址
	 */
	private String productImage;
	/**
	 * 商品状态，1-正常，2-下架，3-删除
	 */
	private Integer productStatus;
	/**
	 * 商品编号
	 */
	private String productBarcode;
	/**
	 * 产品参数集合，存的是json数据
	 */
	private String productMasterPicture;

	private String productSubPicture;

	private String productParamData;

	/**
	 * 价格,单位是 分
	 */
	private String productPrice;
	/**
	 * 加盟商价格,单位是 分
	 */
	private String productPriceAlliance;
	/**
	 * 所属分类
	 */
	private Long categoryId;
	/**
	 * 体积;单位 立方厘米
	 */
	private Long productVolume;
	/**
	 * 创建时间
	 */
	private Date productCreated;
	/**
	 * 更新时间
	 */
	private Date productUpdated;
	/**
	 * 回收方式，0快递，1上门回收，2其他
	 */
	private Integer productRetriType;
	/**
	 * 最高回收价格
	 */
	private String maxrecyclePrice;

	public String getMaxrecyclePrice() {
		return maxrecyclePrice;
	}

	public void setMaxrecyclePrice(String maxrecyclePrice) {
		this.maxrecyclePrice = maxrecyclePrice;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public Integer getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
	}

	public String getProductBarcode() {
		return productBarcode;
	}

	public void setProductBarcode(String productBarcode) {
		this.productBarcode = productBarcode;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductPriceAlliance() {
		return productPriceAlliance;
	}

	public void setProductPriceAlliance(String productPriceAlliance) {
		this.productPriceAlliance = productPriceAlliance;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getProductVolume() {
		return productVolume;
	}

	public void setProductVolume(Long productVolume) {
		this.productVolume = productVolume;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getProductCreated() {
		return productCreated;
	}

	public void setProductCreated(Date productCreated) {
		this.productCreated = productCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getProductUpdated() {
		return productUpdated;
	}

	public void setProductUpdated(Date productUpdated) {
		this.productUpdated = productUpdated;
	}

	public Integer getProductRetriType() {
		return productRetriType;
	}

	public void setProductRetriType(Integer productRetriType) {
		this.productRetriType = productRetriType;
	}

	public String getProductMasterPicture() {
		return productMasterPicture;
	}

	public void setProductMasterPicture(String productMasterPicture) {
		this.productMasterPicture = productMasterPicture == null ? null : productMasterPicture.trim();
	}

	public String getProductSubPicture() {
		return productSubPicture;
	}

	public void setProductSubPicture(String productSubPicture) {
		this.productSubPicture = productSubPicture == null ? null : productSubPicture.trim();
	}

	public String getProductParamData() {
		return productParamData;
	}

	public void setProductParamData(String productParamData) {
		this.productParamData = productParamData == null ? null : productParamData.trim();
	}

}