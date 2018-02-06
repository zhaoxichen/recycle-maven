package com.lianjiu.model.vo;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.common.pojo.MachineCheck;
import com.lianjiu.model.OrdersItem;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersItemFaceface extends OrdersItem {

	private List<MachineCheck> machineCheckList;

	public List<MachineCheck> getMachineCheckList() {
		return machineCheckList;
	}

	public void setMachineCheckList(List<MachineCheck> machineCheckList) {
		this.machineCheckList = machineCheckList;
	}

}
