package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProductRepair {
	private String repairId;

	private String repairName;

	private Byte repairWay;

	private Long categoryId;

	private Date repairCreated;

	private Date repairUpdated;

	private String repairParamTemplate;

	public String getRepairId() {
		return repairId;
	}

	public void setRepairId(String repairId) {
		this.repairId = repairId == null ? null : repairId.trim();
	}

	public String getRepairName() {
		return repairName;
	}

	public void setRepairName(String repairName) {
		this.repairName = repairName == null ? null : repairName.trim();
	}

	public Byte getRepairWay() {
		return repairWay;
	}

	public void setRepairWay(Byte repairWay) {
		this.repairWay = repairWay;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getRepairCreated() {
		return repairCreated;
	}

	public void setRepairCreated(Date repairCreated) {
		this.repairCreated = repairCreated;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getRepairUpdated() {
		return repairUpdated;
	}

	public void setRepairUpdated(Date repairUpdated) {
		this.repairUpdated = repairUpdated;
	}

	public String getRepairParamTemplate() {
		return repairParamTemplate;
	}

	public void setRepairParamTemplate(String repairParamTemplate) {
		this.repairParamTemplate = repairParamTemplate == null ? null : repairParamTemplate.trim();
	}
}