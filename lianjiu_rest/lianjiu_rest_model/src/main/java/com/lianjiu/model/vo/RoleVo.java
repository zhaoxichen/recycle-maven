package com.lianjiu.model.vo;

import java.util.List;

import com.lianjiu.model.Role;

public class RoleVo {
	
	private Role role;
	
	private String [] deptList;
	
	private List<PowerVo> list;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<PowerVo> getList() {
		return list;
	}

	public void setList(List<PowerVo> list) {
		this.list = list;
	}

	public String[] getDeptList() {
		return deptList;
	}

	public void setDeptList(String[] deptList) {
		this.deptList = deptList;
	}

}
