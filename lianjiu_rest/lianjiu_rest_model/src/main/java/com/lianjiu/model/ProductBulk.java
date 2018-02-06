package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 大宗回收
 * 
 * @author wuhongda
 *
 */
public class ProductBulk {
	private String bulkId;

	private Long categoryId;

	private String bulkName;

	private String bulkHandleWay;

	private Byte bulkVolume;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date bulkCreated;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date bulkUpdated;

	public String getBulkId() {
		return bulkId;
	}

	public void setBulkId(String bulkId) {
		this.bulkId = bulkId == null ? null : bulkId.trim();
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getBulkName() {
		return bulkName;
	}

	public void setBulkName(String bulkName) {
		this.bulkName = bulkName == null ? null : bulkName.trim();
	}

	public String getBulkHandleWay() {
		return bulkHandleWay;
	}

	public void setBulkHandleWay(String bulkHandleWay) {
		this.bulkHandleWay = bulkHandleWay == null ? null : bulkHandleWay.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getBulkCreated() {
		return bulkCreated;
	}

	public void setBulkCreated(Date bulkCreated) {
		this.bulkCreated = bulkCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getBulkUpdated() {
		return bulkUpdated;
	}

	public void setBulkUpdated(Date bulkUpdated) {
		this.bulkUpdated = bulkUpdated;
	}

	public Byte getBulkVolume() {
		return bulkVolume;
	}

	public void setBulkVolume(Byte bulkVolume) {
		this.bulkVolume = bulkVolume;
	}
	
}