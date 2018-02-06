package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProductExcellent {
	private String excellentId;

	private String excellentName;

	private String excellentPrice;

	private String excellentRetriPrice;

	private Integer excellentStatus;

	private String excellentBarcode;

	private Long categoryId;

	private Long excellentStock;

	private Long soldCount; // 销量
	
	private String excellentMasterPicture;

	private String excellentSubPicture;

	private String excellentAttributePicture;

	private Date excellentCreated;

	private Date excellentUpdated;

	private String excellentParamData;

	public String getExcellentId() {
		return excellentId;
	}

	public void setExcellentId(String excellentId) {
		this.excellentId = excellentId == null ? null : excellentId.trim();
	}

	public String getExcellentName() {
		return excellentName;
	}

	public void setExcellentName(String excellentName) {
		this.excellentName = excellentName == null ? null : excellentName.trim();
	}

	public String getExcellentPrice() {
		return excellentPrice;
	}

	public void setExcellentPrice(String excellentPrice) {
		this.excellentPrice = excellentPrice == null ? null : excellentPrice.trim();
	}

	public String getExcellentRetriPrice() {
		return excellentRetriPrice;
	}

	public void setExcellentRetriPrice(String excellentRetriPrice) {
		this.excellentRetriPrice = excellentRetriPrice == null ? null : excellentRetriPrice.trim();
	}

	public Integer getExcellentStatus() {
		return excellentStatus;
	}

	public void setExcellentStatus(Integer excellentStatus) {
		this.excellentStatus = excellentStatus;
	}

	public String getExcellentBarcode() {
		return excellentBarcode;
	}

	public void setExcellentBarcode(String excellentBarcode) {
		this.excellentBarcode = excellentBarcode == null ? null : excellentBarcode.trim();
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getExcellentStock() {
		return excellentStock;
	}

	public void setExcellentStock(Long excellentStock) {
		this.excellentStock = excellentStock;
	}

	public String getExcellentMasterPicture() {
		return excellentMasterPicture;
	}

	public void setExcellentMasterPicture(String excellentMasterPicture) {
		this.excellentMasterPicture = excellentMasterPicture == null ? null : excellentMasterPicture.trim();
	}

	public String getExcellentSubPicture() {
		return excellentSubPicture;
	}

	public void setExcellentSubPicture(String excellentSubPicture) {
		this.excellentSubPicture = excellentSubPicture == null ? null : excellentSubPicture.trim();
	}

	public String getExcellentAttributePicture() {
		return excellentAttributePicture;
	}

	public void setExcellentAttributePicture(String excellentAttributePicture) {
		this.excellentAttributePicture = excellentAttributePicture;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getExcellentCreated() {
		return excellentCreated;
	}

	public void setExcellentCreated(Date excellentCreated) {
		this.excellentCreated = excellentCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getExcellentUpdated() {
		return excellentUpdated;
	}

	public void setExcellentUpdated(Date excellentUpdated) {
		this.excellentUpdated = excellentUpdated;
	}

	public String getExcellentParamData() {
		return excellentParamData;
	}

	public void setExcellentParamData(String excellentParamData) {
		this.excellentParamData = excellentParamData == null ? null : excellentParamData.trim();
	}

	public Long getSoldCount() {
		return soldCount;
	}

	public void setSoldCount(Long soldCount) {
		this.soldCount = soldCount;
	}
}