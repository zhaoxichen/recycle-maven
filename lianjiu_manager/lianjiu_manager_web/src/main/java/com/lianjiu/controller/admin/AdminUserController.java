package com.lianjiu.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.service.admin.AdminUserService;

/**
 * 系统管理表现层
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/adminUser")
public class AdminUserController {
	@Autowired
	private AdminUserService adminUserService;

	/**
	 * 
	 * zhaoxi 2017年8月24日 descrption:获取所有的用户信息
	 * 
	 * @param pageBegin
	 * @param pageTotal
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/adminUserAll/{pageNum}/{pageSize}")
	@ResponseBody
	public LianjiuResult selectAllAdminUser(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return adminUserService.selectAllAdminUser(pageNum, pageSize);
	}
}
