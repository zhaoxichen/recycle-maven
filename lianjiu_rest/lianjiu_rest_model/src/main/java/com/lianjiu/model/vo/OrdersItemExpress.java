package com.lianjiu.model.vo;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.common.pojo.MachineCheck;
import com.lianjiu.model.OrdersItem;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersItemExpress extends OrdersItem {
	private String orExpressNum;

	private List<MachineCheck> machineCheckList;
	
	private Byte orExpressStatus;

	public String getOrExpressNum() {
		return orExpressNum;
	}

	public void setOrExpressNum(String orExpressNum) {
		this.orExpressNum = orExpressNum;
	}

	public List<MachineCheck> getMachineCheckList() {
		return machineCheckList;
	}

	public void setMachineCheckList(List<MachineCheck> machineCheckList) {
		this.machineCheckList = machineCheckList;
	}

	public Byte getOrExpressStatus() {
		return orExpressStatus;
	}

	public void setOrExpressStatus(Byte orExpressStatus) {
		this.orExpressStatus = orExpressStatus;
	}

}
