package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Waste {
	private String wasteId;

	private String userId;

	private Long categoryId;

	private String wasteRetrieveWay;

	private String wasteName;

	private String wasteVolume;

	private String wasteImage;

	private Date wasteCreated;

	private Date wasteUpdated;

	public String getWasteId() {
		return wasteId;
	}

	public void setWasteId(String wasteId) {
		this.wasteId = wasteId == null ? null : wasteId.trim();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getWasteName() {
		return wasteName;
	}

	public void setWasteName(String wasteName) {
		this.wasteName = wasteName == null ? null : wasteName.trim();
	}

	public String getWasteVolume() {
		return wasteVolume;
	}

	public void setWasteVolume(String wasteVolume) {
		this.wasteVolume = wasteVolume == null ? null : wasteVolume.trim();
	}

	public String getWasteImage() {
		return wasteImage;
	}

	public void setWasteImage(String wasteImage) {
		this.wasteImage = wasteImage == null ? null : wasteImage.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getWasteCreated() {
		return wasteCreated;
	}

	public void setWasteCreated(Date wasteCreated) {
		this.wasteCreated = wasteCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getWasteUpdated() {
		return wasteUpdated;
	}

	public void setWasteUpdated(Date wasteUpdated) {
		this.wasteUpdated = wasteUpdated;
	}

	public String getWasteRetrieveWay() {
		return wasteRetrieveWay;
	}

	public void setWasteRetrieveWay(String wasteRetrieveWay) {
		this.wasteRetrieveWay = wasteRetrieveWay;
	}

}