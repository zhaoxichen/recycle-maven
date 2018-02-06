package com.lianjiu.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.ExceptionUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.sso.service.WarehousemanService;

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
@RequestMapping("/warehouseman")
public class WarehousemanController {

	@Autowired
	private WarehousemanService warehousemanService;

	/**
	 * 
	 * zhaoxi 2017年8月18日 descrption: 链旧 登陆
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param udetailsEquipment
	 *            登陆设备
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult userLogin(String username, String userPassword) {
		if (Util.isEmpty(username)) {
			return LianjiuResult.build(401, "请传入用户名");
		}
		if (Util.isEmpty(userPassword)) {
			return LianjiuResult.build(402, "请传入密码");
		}
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = warehousemanService.userLogin(username, userPassword);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * hongda 修改密码
	 * 
	 * @param recyclerId
	 *            用户名
	 * @param oldPassword
	 *            旧密码
	 * @param newPassword
	 *            新密码
	 * @return
	 */
	@RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyPwd(String recyclerId, String oldPassword, String newPassword) {
		if (Util.isEmpty(recyclerId)) {
			return LianjiuResult.build(401, "请传入用户编号");
		}
		if (Util.isEmpty(oldPassword)) {
			return LianjiuResult.build(402, "请传入原始密码");
		}
		if (Util.isEmpty(newPassword)) {
			return LianjiuResult.build(403, "请传入新密码");
		}
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = warehousemanService.modifyPwd(recyclerId, oldPassword, newPassword);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 
	 * WHD 2017年9月25日 descrption:退出登陆
	 * 
	 * @param 用户编号
	 * @return String
	 */
	@RequestMapping(value = "/loginOut", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult loginOut(String recyclerId) {
		if (Util.isEmpty(recyclerId)) {
			return LianjiuResult.build(400, "请传入recyclerId");
		}
		return warehousemanService.loginOut(recyclerId);
	}
}