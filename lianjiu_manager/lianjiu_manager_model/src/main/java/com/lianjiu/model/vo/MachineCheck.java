package com.lianjiu.model.vo;

/**
 * 验机详情
 * 
 * @author zhaoxi
 *
 */
public class MachineCheck {
	private String paramKey;
	private String paramValue;
	private String paramValueModify;

	public MachineCheck() {
	}

	public MachineCheck(String paramKey, String paramValue, String paramValueModify) {
		this.paramKey = paramKey;
		this.paramValue = paramValue;
		this.paramValueModify = paramValueModify;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamValueModify() {
		return paramValueModify;
	}

	public void setParamValueModify(String paramValueModify) {
		this.paramValueModify = paramValueModify;
	}
}
