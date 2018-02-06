package com.lianjiu.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Role;
import com.lianjiu.model.vo.RoleVo;
import com.lianjiu.service.admin.RoleService;

/**
 * 人员信息
 * 
 * @author huangfu
 *
 */
@Controller
@RequestMapping("/role")
public class RoleController {

	@Autowired
	RoleService roleService;
	
	/**
	 * 
	 * huangfu 2018年1月8日 descrption:根据id获取人员信息
	 * 
	 * @param roleId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRole/{roleId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRoleById(@PathVariable String roleId) {
		if (Util.isEmpty(roleId)) {
			return LianjiuResult.build(401, "请传入正确的roleId");
		}
		return roleService.getRoleById(roleId);
	}
	
	/**
	 * 
	 * huangfu 2018年1月8日 descrption:分页获取人员
	 * 
	 * @param cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRole/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRoleAll(@PathVariable int pageNum,@PathVariable int pageSize) {
		System.out.println("分页获取人员             。");
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return roleService.getRoleAll(pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2018年1月8日 descrption:获取所有人员
	 * 
	 * @param cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRole/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getAll() {
		System.out.println("获取所有人员             。");
		return roleService.getAll();
	}
	
	/**
	 * 
	 * huangfu 2018年1月8日 descrption:添加人员
	 * 
	 * @param role
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addRole", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addRole(RoleVo role) {
		System.out.println("添加人员。");
		if (null == role) {
			return LianjiuResult.build(401, "role对象绑定错误");
		}
		if(null == role.getRole()){
			return LianjiuResult.build(402, "role对象绑定错误");
		}
		if(null == role.getDeptList() || role.getDeptList().length==0){
			return LianjiuResult.build(402, "部门id 不能为空");
		}
		return roleService.addRole(role.getRole(),role.getDeptList(),role.getList());
	}

	/**
	 * 
	 * huangfu 2018年1月8日 descrption:更新人员
	 * 
	 * @param role
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyRole", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateRole(Role role) {
		if (null == role) {
			return LianjiuResult.build(401, "role对象绑定错误");
		}
		return roleService.updateRole(role);
	}

	/**
	 * 
	 * huangfu 2018年1月8日 descrption:移除人员
	 * 
	 * @param roleId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeRole/{roleId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteRole(String roleId) {
		if (Util.isEmpty(roleId) || roleId.equals("{roleId}")) {
			return LianjiuResult.build(401, "请传入正确的roleId");
		}
		return roleService.deleteRole(roleId);
	}
	
	/**
	 * 
	 * huangfu 2018年1月8日 descrption:根据部门id 获取上级领导
	 * 
	 * @param roleId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRoleByDeptId", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getRoleByDeptId(String deptId) {
		if (Util.isEmpty(deptId)) {
			return LianjiuResult.build(401, "请传入正确的deptId");
		}
		return roleService.getRoleByDeptId(deptId);
	}
	
}
