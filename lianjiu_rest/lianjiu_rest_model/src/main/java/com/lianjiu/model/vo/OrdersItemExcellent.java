package com.lianjiu.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.OrdersItem;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersItemExcellent extends OrdersItem {
	private String orExceDetailsExpressNum;

	private String orExceDetailsExpressName;

	public String getOrExceDetailsExpressNum() {
		return orExceDetailsExpressNum;
	}

	public void setOrExceDetailsExpressNum(String orExceDetailsExpressNum) {
		this.orExceDetailsExpressNum = orExceDetailsExpressNum;
	}

	public String getOrExceDetailsExpressName() {
		return orExceDetailsExpressName;
	}

	public void setOrExceDetailsExpressName(String orExceDetailsExpressName) {
		this.orExceDetailsExpressName = orExceDetailsExpressName;
	}
}
