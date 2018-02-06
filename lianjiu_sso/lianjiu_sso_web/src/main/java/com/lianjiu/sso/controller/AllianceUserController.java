package com.lianjiu.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.ExceptionUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.sso.service.AllianceUserService;

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
@RequestMapping("/alliance")
public class AllianceUserController {

	@Autowired
	private AllianceUserService allianceUserService;

	/**
	 * 加盟商登陆
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 */
	@RequestMapping(value = "/login/{username}/{password}")
	@ResponseBody
	public LianjiuResult allianceLogin(@PathVariable String username, @PathVariable String password) {
		System.out.println("username=" + username + "password=" + password + "loginTpye=");
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			return allianceUserService.allianceLogin(username, password);
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * whd 2017年8月18日 descrption: 加盟商登陆
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param token
	 *            设备标识
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/allianceUserLogin/login", method = RequestMethod.POST)
	@ResponseBody // allianceServiceToken
	public LianjiuResult allianceUserLogin(String allianceBusinessName, String allianceBusinessPassword, String token) {
		System.out.println(token);
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			System.out.println("进入加盟商登录" + "token" + token);
			LianjiuResult result = allianceUserService.allianceUserLogin(allianceBusinessName, allianceBusinessPassword,
					token);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	@RequestMapping("/token/{token}")
	@ResponseBody
	public Object getAllianceUserByToken(@PathVariable String token, String callback) {
		LianjiuResult result = null;
		try {
			result = allianceUserService.getAllianceUserByToken(token);
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

	/**
	 * 
	 * zhaoxi 2017年9月25日 descrption:验证验证码
	 * 
	 * @param phone
	 *            电话号码
	 * @param code
	 *            验证码
	 * @return String
	 */
	@RequestMapping(value = "/phoneCheck/{phone}/{code}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult alliancePhoneCheck(@PathVariable String phone, @PathVariable String code) {
		if(Util.isEmpty(phone)){
			return LianjiuResult.build(401, "phone参数传入错误");
		}
		if(Util.isEmpty(code)){
			return LianjiuResult.build(402, "code参数传入错误");
		}
		return allianceUserService.checkAlliancePhone(phone, code);
	}

	/**
	 * 
	 * zhaoxi 2017年9月25日 descrption:加盟商修改手机号
	 * 
	 * @param allianceId
	 *            加盟商编号
	 * @param phone
	 *            电话号码
	 * @param code
	 *            验证码
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/phoneModify", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult alliancePhoneModify(String allianceId, String phone, String code) {
		if (Util.isEmpty(allianceId)) {
			return LianjiuResult.build(401, "请传入加盟商编号");
		}
		if (Util.isEmpty(phone)) {
			return LianjiuResult.build(401, "请填写手机号码");
		}
		if (Util.isEmpty(code)) {
			return LianjiuResult.build(401, "请填写验证码");
		}
		return allianceUserService.updateAlliancePhone(allianceId, phone, code);
	}

	/**
	 * 
	 * 
	 *
	 * @author whd
	 * @Description: 用户修改密码获取验证码
	 * @param allianceId
	 *            加盟商编号
	 * @return LianjiuResult
	 * @date 2017年10月10日 下午4:45:39
	 */
	@RequestMapping(value = "/sendSMSPwd", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult sendSMSPwd(String allianceId) {
		LianjiuResult result = null;
		try {
			result = allianceUserService.sendSMSPwd(allianceId);
		} catch (Exception e) {
			e.printStackTrace();
			result = LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return result;
	}

	public static void main(String[] args) {
		String password = "123456";
		String reg = "^.{6,12}";

		if (password.matches(reg)) {
			System.out.println("符合");
		} else {
			System.err.println("不符合");
		}
	}

	/**
	 * 
	 * zhaoxi 2017年9月25日 descrption:修改密码
	 * 
	 * @param allianceId
	 *            加盟商编号
	 * @param password
	 *            密码
	 * @param code
	 *            验证码
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/passwordModify", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult alliancePasswordModify(String allianceId, String password, String code) {
		if (Util.isEmpty(allianceId)) {
			return LianjiuResult.build(401, "请传入正确参数");
		}
		String reg = "^.{6,12}";// 仅判断长度
		if (password.matches(reg)) {
			System.out.println("长度够了");
			if (Util.isEmpty(code)) {
				return LianjiuResult.build(402, "请传入验证码");
			}
			return allianceUserService.updateAlliancePassword(allianceId, password, code);
		} else {
			return LianjiuResult.build(403, "密码长度必须六位或以上");
		}
	}

	/**
	 * 
	 *
	 * @author whd
	 * @Description: 校验手机号码发送验证码
	 * @param allianceId
	 *            加盟商编号
	 * @param phone
	 *            电话号码
	 * @return LianjiuResult
	 * @date 2017年10月10日 上午11:03:11
	 */
	@RequestMapping(value = "/sendSms", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult sendSms(String allianceId, String phone) {
		if (Util.isEmpty(phone)) {
			return LianjiuResult.build(401, "请填写手机号码");
		}
		if (Util.isEmpty(allianceId)) {
			return LianjiuResult.build(401, "请传入加盟商编号");
		}
		return allianceUserService.sendSms(allianceId, phone);
	}

	/**
	 * 
	 *
	 * @author whd
	 * @Description: 校验新手机号码发送验证码
	 * @param @param
	 *            phone
	 * @return LianjiuResult
	 * @date 2017年10月10日 上午11:03:11
	 */
	@RequestMapping(value = "/sendSmsAlliance", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult sendSmsAlliance(String allianceId, String phone) {
		if (Util.isEmpty(phone)) {
			return LianjiuResult.build(401, "请输入手机号码");
		}
		if (Util.isEmpty(allianceId)) {
			return LianjiuResult.build(401, "传入参数出错");
		}
		return allianceUserService.sendSmsAlliance(allianceId, phone);
	}

	/**
	 * 
	 *
	 * @author whd
	 * @Description: 退出登陆
	 * @param allianceId
	 *            加盟商编号
	 * @return LianjiuResult
	 * @date 2017年10月10日 上午11:03:11
	 */
	@RequestMapping(value = "/Logout", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult Logout(String allianceId) {
		if (Util.isEmpty(allianceId)) {
			return LianjiuResult.build(400, "请传入加盟商编号");
		}
		return allianceUserService.Logout(allianceId);
	}

}