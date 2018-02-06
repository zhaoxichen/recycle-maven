package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProductParamTemplate {
	/**
	 * 主键 globlid
	 */
	private String pptemplId;
	/**
	 * 参数模版的名称
	 */
	private String pptemplName;
	/**
	 * 产品的参数模版，存的是json数据
	 */
	private String pptemplData;
	/**
	 * 创建的时间
	 */
	private Date pptemplCreated;
	/**
	 * 更新时间
	 */
	private Date pptemplUpdated;

	public String getPptemplId() {
		return pptemplId;
	}

	public void setPptemplId(String pptemplId) {
		this.pptemplId = pptemplId;
	}

	public String getPptemplName() {
		return pptemplName;
	}

	public void setPptemplName(String pptemplName) {
		this.pptemplName = pptemplName;
	}

	public String getPptemplData() {
		return pptemplData;
	}

	public void setPptemplData(String pptemplData) {
		this.pptemplData = pptemplData;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getPptemplCreated() {
		return pptemplCreated;
	}

	public void setPptemplCreated(Date pptemplCreated) {
		this.pptemplCreated = pptemplCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getPptemplUpdated() {
		return pptemplUpdated;
	}

	public void setPptemplUpdated(Date pptemplUpdated) {
		this.pptemplUpdated = pptemplUpdated;
	}

}