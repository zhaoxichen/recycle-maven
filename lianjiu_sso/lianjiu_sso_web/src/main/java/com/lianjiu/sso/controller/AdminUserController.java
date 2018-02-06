package com.lianjiu.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.ExceptionUtil;
import com.lianjiu.model.Admin;
import com.lianjiu.sso.service.AdminUserService;

/**
 * 用户Controller
 * <p>
 * Title: UserController
 * </p>
 * <p>
 * Description:
 * </p>
 */
@Controller
@RequestMapping("/admin")
public class AdminUserController {

	@Autowired
	private AdminUserService adminUserService;

	/**
	 * 
	 * zhaoxi 2017年8月18日 descrption: 登录，账号登录，微信登录，qq登录，短信登录 区别：loginTpye
	 * 
	 * @param username
	 * @param password
	 * @param loginTpye
	 * @param request
	 * @param response
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/adminLogin")
	@ResponseBody
	public LianjiuResult adminUserLogin(Admin admin,HttpServletRequest request, HttpServletResponse response) {
		System.out.println("管理员登陆");
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = adminUserService.adminUserLogin(admin,request,response);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	@RequestMapping("/token/{token}")
	@ResponseBody
	public Object getAdminUserByToken(@PathVariable String token, String callback) {
		LianjiuResult result = null;
		try {
			result = adminUserService.getAdminUserByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			result = LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}

		// 判断是否为jsonp调用
		if (StringUtils.isBlank(callback)) {
			return result;
		} else {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}

	}
}