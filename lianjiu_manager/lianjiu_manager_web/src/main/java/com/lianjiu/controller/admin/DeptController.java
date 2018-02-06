package com.lianjiu.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Dept;
import com.lianjiu.service.admin.DeptService;

/**
 * 部门信息
 * 
 * @author huangfu
 *
 */
@Controller
@RequestMapping("/dept")
public class DeptController {

	@Autowired
	DeptService deptService;
	
	/**
	 * 
	 * huangfu 2018年1月8日 descrption:根据id获取部门信息
	 * 
	 * @param deptId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getDeptById/{deptId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getDeptById(@PathVariable String deptId) {
		if (Util.isEmpty(deptId) || deptId.equals("{deptId}")) {
			return LianjiuResult.build(401, "请传入正确的deptId");
		}
		return deptService.getDeptById(deptId);
	}
	
	/**
	 * 
	 * huangfu 2018年1月8日 descrption:分页获取部门
	 * 
	 * @param cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getDept/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getDeptAll(@PathVariable int pageNum,@PathVariable int pageSize) {
		System.out.println("分页获取部门             。");
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return deptService.getDeptAll(pageNum, pageSize);
	}

	

	/**
	 * 
	 * huangfu 2018年1月8日 descrption:分页获取部门
	 * 
	 * @param cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getAll() {
		System.out.println("获取所有部门             。");
		return deptService.getAll();
	}
	
	
	/**
	 * 
	 * huangfu 2018年1月8日 descrption:添加部门
	 * 
	 * @param dept
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addDept", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addDept(Dept dept) {
		System.out.println("添加部门             。");
		if (null == dept) {
			return LianjiuResult.build(401, "dept对象绑定错误");
		}
		if(null==dept.getDeptName()||dept.getDeptName().equals("")){
			return LianjiuResult.build(402, "请输入部门名称");
		}
		return deptService.addDept(dept);
	}

	/**
	 * 
	 * huangfu 2018年1月8日 descrption:更新部门
	 * 
	 * @param dept
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyDept", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateDept(Dept dept) {
		if (null == dept) {
			return LianjiuResult.build(401, "dept对象绑定错误");
		}
		if(null==dept.getDeptName()||dept.getDeptName().equals("")){
			return LianjiuResult.build(402, "请输入部门名称");
		}
		return deptService.updateDept(dept);
	}

	/**
	 * 
	 * huangfu 2018年1月8日 descrption:移除部门
	 * 
	 * @param deptId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteDept(String deptId) {
		if (Util.isEmpty(deptId)) {
			return LianjiuResult.build(401, "请传入正确的deptId");
		}
		return deptService.deleteDept(deptId);
	}

}
