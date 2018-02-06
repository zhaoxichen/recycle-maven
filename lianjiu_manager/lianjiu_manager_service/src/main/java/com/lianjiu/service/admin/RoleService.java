package com.lianjiu.service.admin;

import java.util.List;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.Role;
import com.lianjiu.model.vo.PowerVo;

public interface RoleService {

	LianjiuResult getRoleAll(int pageNum, int pageSize);

	LianjiuResult getRoleById(String roleId);

	LianjiuResult addRole(Role role,String [] deptList,List<PowerVo> list);

	LianjiuResult updateRole(Role role);

	LianjiuResult deleteRole(String roleId);

	LianjiuResult getAll();

	LianjiuResult getRoleByDeptId(String deptId);

}
