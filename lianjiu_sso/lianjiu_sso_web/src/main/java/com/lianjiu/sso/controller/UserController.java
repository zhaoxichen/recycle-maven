package com.lianjiu.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
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
import com.lianjiu.sso.service.UserService;

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
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 
	 * zhaoxi 2017年8月19日 descrption: 校验用户是否已经存在，支持跨域请求，返回json
	 * 
	 * @param userPhone
	 *            电话号码
	 * @return Object
	 */
	@RequestMapping("/check/{userPhone}")
	@ResponseBody
	public Object checkData(@PathVariable String param, String callback) {

		LianjiuResult result = null;

		System.out.println("param:" + param);
		// System.out.println("type:"+type);
		System.out.println("callback:" + callback);

		// 参数有效性校验
		if (StringUtils.isBlank(param)) {
			result = LianjiuResult.build(400, "校验内容不能为空");
		}
		// 校验出错
		if (null != result) {
			if (null != callback) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			} else {
				return result;
			}
		}
		// 调用服务
		try {
			result = userService.checkData(param, callback);

		} catch (Exception e) {
			result = LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}

		if (null != callback) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			return result;
		}
	}

	// 创建用户
	@RequestMapping(value = "/register/{username}/{userPassword}")
	@ResponseBody
	public LianjiuResult createUser(@PathVariable String username, @PathVariable String userPassword) {
		if(Util.isEmpty(username)){
			return LianjiuResult.build(401, "请传入用户名");
		}
		if(Util.isEmpty(userPassword)){
			return LianjiuResult.build(401, "请传入密码");
		}
		try {
			LianjiuResult result = userService.createUser(username, userPassword);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 短信登陆获取验证码
	 * 
	 * @param phone
	 *            电话号码
	 * @return
	 */
	@RequestMapping(value = "sendSmsLogin/{phone}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult sendSmsLogin(@PathVariable String phone) {
		try {
			boolean lean = Util.isMobile(phone);
			if (true == lean) {
				LianjiuResult result = userService.sendSmsLogin(phone);
				return result;
			}
			return LianjiuResult.build(401, "请输入正确的电话号码");
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * whd 2017年9月25日 descrption:短信登录
	 * 
	 * @param phone
	 *            电话号码
	 * @param uCode
	 *            验证码
	 * @param udetailsEquipment
	 *            登陆设备
	 * @param udetailsReferrer
	 *            推荐码
	 * @return
	 */
	@RequestMapping(value = "/sMsLogin", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult sMsLogin(String phone, String uCode, String udetailsEquipment, String udetailsReferrer) {
		try {
			boolean lean = Util.isMobile(phone);
			if (true == lean) {
				LianjiuResult result = userService.sMsLogin(phone, uCode, udetailsEquipment, udetailsReferrer);
				return result;
			}
			return LianjiuResult.build(401, "请输入正确的电话号码");
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

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
	@RequestMapping(value = "/login/{username}/{userPassword}/{udetailsEquipment}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult userLogin(@PathVariable String username, @PathVariable String userPassword,
			@PathVariable String udetailsEquipment) {
		System.out.println("username=" + username + "password=" + userPassword + "loginTpye=");
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = userService.userLogin(username, userPassword, udetailsEquipment);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	@RequestMapping("/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback) {
		LianjiuResult result = null;
		try {
			result = userService.getUserByToken(token);
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
	 * whd 2017年9月25日 descrption:获取短信验证码
	 * 
	 * @param phone
	 *            电话号码
	 * @return
	 */
	@RequestMapping(value = "/sendSms/{phone}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult sendSms(@PathVariable String phone) {
		try {
			boolean lean = Util.isMobile(phone);
			if (true == lean) {
				LianjiuResult result = userService.sendSms(phone);
				return result;
			}
			return LianjiuResult.build(401, "请输入正确的电话号码");
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * whd 2017年9月25日 descrption:短信登录token获取
	 * 
	 * @param token
	 * @param callback
	 * @return
	 */
	@RequestMapping(value = "/smsToken/{token}")
	@ResponseBody
	public Object getSmsByToken(@PathVariable String token) {
		LianjiuResult result = null;
		try {
			result = userService.getSmsByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			result = LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return result;
	}

	/**
	 * 
	 * 
	 *
	 * @author whd
	 * @Description: 设置密码
	 * @param userId
	 *            用户编号
	 * @param userPassword
	 *            密码
	 * @return LianjiuResult
	 * @date 2017年10月10日 下午4:45:39
	 */
	@RequestMapping(value = "/modifyPwd/{userId}/{userPassword}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyPwd(@PathVariable String userId, @PathVariable String userPassword) {
		LianjiuResult result = null;
		try {
			result = userService.modifyPwd(userId, userPassword);
		} catch (Exception e) {
			e.printStackTrace();
			result = LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return result;
	}

	/**
	 * 
	 * 
	 *
	 * @author whd
	 * @Description: 用户修改密码获取验证码
	 * @param userId
	 *            用户编号
	 * @return LianjiuResult
	 * @date 2017年10月10日 下午4:45:39
	 */
	@RequestMapping(value = "/sendSMSPwd", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult sendSMSPwd(String userId) {
		LianjiuResult result = null;
		try {
			result = userService.sendSMSPwd(userId);
		} catch (Exception e) {
			e.printStackTrace();
			result = LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return result;
	}

	/**
	 * 
	 * 
	 *
	 * @author whd
	 * @Description: 忘记密码更新密码
	 * @param userPassword
	 *            密码
	 * @param code
	 *            验证码
	 * @param phone
	 *            电话号码
	 * @return LianjiuResult
	 * @date 2017年10月10日 下午5:41:44
	 */
	@RequestMapping(value = "/forgetPwdSms", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult forgetPwdSms(String phone) {
		try {
			boolean lean = Util.isMobile(phone);
			if (true == lean) {
				LianjiuResult result = userService.forgetPwdSms(phone);
				return result;
			}
			return LianjiuResult.build(401, "请输入正确的电话号码");
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * 
	 *
	 * @author whd
	 * @Description: 忘记密码更新密码
	 * @param userPassword
	 *            密码
	 * @param code
	 *            验证码
	 * @param phone
	 *            电话号码
	 * @return LianjiuResult
	 * @date 2017年10月10日 下午5:41:44
	 */
	@RequestMapping(value = "/updatePwd/{userPassword}/{code}/{phone}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updatePwd(@PathVariable String userPassword, @PathVariable String code,
			@PathVariable String phone) {
		try {
			boolean lean = Util.isMobile(phone);
			if (true == lean) {
				LianjiuResult result = userService.updatePwd(userPassword, code, phone);
				return result;
			}
			return LianjiuResult.build(401, "请输入正确的电话号码");
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * @author whd
	 * @Description: 修改密码
	 * @param userPassword
	 *            密码
	 * @param code
	 *            验证码
	 * @param phone
	 *            电话号码
	 * @return LianjiuResult
	 * @date 2017年10月10日 下午5:41:44
	 */
	@RequestMapping(value = "/changePwd/{phone}/{code}/{userPassword}/pwd=25654", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult changePwd(@PathVariable String phone, @PathVariable String code,
			@PathVariable String userPassword) {
		try {
			boolean lean = Util.isMobile(phone);
			String reg = "^.{6,25}";// 仅判断长度
			// 判断电话号码
			if (true == lean) {
				// 判断密码长度
				if (userPassword.matches(reg)) {
					LianjiuResult result = userService.changePwd(phone, code, userPassword);
					return result;
				}
				return LianjiuResult.build(401, "密码长度必须六位或以上");
			}
			return LianjiuResult.build(402, "请输入正确的电话号码");
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 *
	 * @author whd
	 * @Description: 校验手机号码发送验证码(修改电话号码)
	 * @param userId
	 *            用户编号
	 * @param phone
	 *            电话号码
	 * @return LianjiuResult
	 * @date 2017年10月10日 上午11:03:11
	 */
	@RequestMapping(value = "/sendSmsPhone/{userId}/{phone}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult sendSmsPhone(@PathVariable String userId, @PathVariable String phone) {
		try {
			boolean lean = Util.isMobile(phone);
			if (true == lean) {
				LianjiuResult result = userService.sendSmsPhone(userId, phone);
				return result;
			}
			return LianjiuResult.build(401, "请输入正确的电话号码");
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * zhaoxi 2017年9月25日 descrption:原手机号码验证验证码
	 * 
	 * @param phone
	 *            电话号码
	 * @param code
	 *            验证码
	 * @return String
	 */
	@RequestMapping(value = "/phoneCheck/{phone}/{code}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult phoneCheck(@PathVariable String phone, @PathVariable String code) {
		try {
			boolean lean = Util.isMobile(phone);
			if (true == lean) {
				LianjiuResult result = userService.phoneCheck(phone, code);
				return result;
			}
			return LianjiuResult.build(401, "请输入正确的电话号码");
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * zhaoxi 2017年9月25日 descrption:向新手机发送验证码
	 * 
	 * @param userId
	 *            用户编号
	 * @param phone
	 *            电话号码
	 * @return String
	 */
	@RequestMapping(value = "/sendNewPhone/{userId}/{phone}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult sendNewPhone(@PathVariable String userId, @PathVariable String phone) {
		try {
			boolean lean = Util.isMobile(phone);
			if (true == lean) {
				LianjiuResult result = userService.sendNewPhone(userId, phone);
				return result;
			}
			return LianjiuResult.build(401, "请输入正确的电话号码");
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * zhaoxi 2017年9月25日 descrption:修改手机号
	 * 
	 * @param userId
	 *            用户编号
	 * @param phone
	 *            电话号码
	 * @param code
	 *            验证码
	 * @return String
	 */
	@RequestMapping(value = "/phoneChange/{userId}/{phone}/{code}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult phoneChange(@PathVariable String userId, @PathVariable String phone,
			@PathVariable String code) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "参数获取失败");
		}
		if (Util.isEmpty(phone)) {
			return LianjiuResult.build(402, "电话号码不能为空");
		}
		if (Util.isEmpty(code)) {
			return LianjiuResult.build(403, "验证码不能为空");
		}
		try {
			boolean lean = Util.isMobile(phone);
			if (true == lean) {
				LianjiuResult result = userService.phoneChange(userId, phone, code);
				return result;
			}
			return LianjiuResult.build(401, "请输入正确的电话号码");
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * zhaoxi 2017年9月25日 descrption:判断用户是否设置过密码
	 * 
	 * @param username
	 *            用户名
	 * @return String
	 */
	@RequestMapping(value = "/isPwd/{username}/username=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult isPwd(@PathVariable String username) {
		System.out.println("进来了" + username);
		try {
			return userService.isPwd(username);
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * WHD 2017年9月25日 descrption:QQ\微信登陆成功(作废)
	 * 
	 * @return String
	 */
	@RequestMapping(value = "/otherLogin/{num}/{nickname}/{picture}/{type}/username=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult OtherLogin(@PathVariable String num, @PathVariable String nickname,
			@PathVariable String picture, @PathVariable int type) {
		System.out.println("登陆获取信息：" + "Num:" + num + "Nickname:" + nickname + "Picture:" + picture + "type" + type);
		return userService.OtherLogin(num, nickname, picture, type);
	}

	/**
	 * 
	 * WHD 2017年9月25日 descrption:QQ\微信绑定手机号码(作废)
	 * 
	 * @return String
	 */
	@RequestMapping(value = "/bindingPhone/{userId}/{phone}/{code}/username=183", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult bindingPhone(@PathVariable String userId, @PathVariable String phone,
			@PathVariable String code) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入用户编号");
		}
		if (Util.isEmpty(phone)) {
			return LianjiuResult.build(402, "请传入手机号");
		}
		if (Util.isEmpty(code)) {
			return LianjiuResult.build(402, "请传入验证码");
		}
		return userService.bindingPhone(userId, phone, code);
	}

	/**
	 * 
	 *
	 * @author whd
	 * @Description: 绑定手机发送验证码
	 * @param phone
	 *            电话号码
	 * @return LianjiuResult
	 * @date 2017年11月20日 上午11:03:11
	 */
	@RequestMapping(value = "/sendSmsByBinding/{phone}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult sendSmsByBinding(@PathVariable String phone) {
		try {
			boolean lean = Util.isMobile(phone);
			if (true == lean) {
				LianjiuResult result = userService.sendSmsByBinding(phone);
				return result;
			}
			return LianjiuResult.build(401, "请输入正确的电话号码");
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 *
	 * @author whd
	 * @Description: 通过userId查用户信息
	 * @param userId
	 *            用户编号
	 * @return LianjiuResult
	 * @date 2017年11月20日 上午11:03:11
	 */
	@RequestMapping(value = "/getUserMessage/{userId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getUserMessage(@PathVariable String userId) {
		System.out.println(userId);
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入用户编号");
		}
		if ("null".equals(userId)) {
			return LianjiuResult.build(402, "请传入用户编号");
		}
		return userService.getUserMessage(userId);
	}

	/**
	 * 
	 * WHD 2017年9月25日 descrption:QQ\微信授权校验登陆
	 * 
	 * @param openId
	 *            授权编号
	 * @param type
	 *            0微信 1QQ
	 * @param udetailsEquipment
	 *            登陆设备
	 * @return String
	 */
	@RequestMapping(value = "/QWLogin", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult QWLogin(String openId, Integer type, String udetailsEquipment) {
		if (Util.isEmpty(openId)) {
			return LianjiuResult.build(400, "请传入openId");
		}
		System.out.println("openId" + openId + "--type" + type);
		return userService.QWLogin(openId, type, udetailsEquipment);
	}

	/**
	 * 校验手机号码，创建用户
	 * 
	 * @param openId
	 *            设备编号
	 * @param userPhone
	 *            用户电话号码
	 * @param code
	 *            验证码
	 * @param nickname
	 *            昵称
	 * @param picture
	 *            头像地址
	 * @param type
	 *            QQ/微信登陆
	 * @param udetailsEquipment
	 *            登陆设备
	 * @return
	 */
	@RequestMapping(value = "/CreateUser", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult CreateUser(String openId, String userPhone, String code, String nickname, String picture,
			Integer type, String udetailsEquipment) {
		if (Util.isEmpty(openId)) {
			return LianjiuResult.build(401, "请传入openId");
		}
		if (Util.isEmpty(userPhone)) {
			return LianjiuResult.build(402, "请传入userPhone");
		}
		if (Util.isEmpty(code)) {
			return LianjiuResult.build(403, "请传入code");
		}
		if (Util.isEmpty(nickname)) {
			return LianjiuResult.build(404, "请传入nickname");
		}
		if (Util.isEmpty(picture)) {
			return LianjiuResult.build(405, "请传入picture");
		}
		if (Util.isEmpty(udetailsEquipment)) {
			return LianjiuResult.build(406, "请传入udetailsEquipment");
		}
		if (type != 0) {
			if (type != 1) {
				return LianjiuResult.build(407, "传入type值错误");
			}
		}
		try {
			boolean lean = Util.isMobile(userPhone);
			if (true == lean) {
				LianjiuResult result = userService.CreateUser(openId, userPhone, code, nickname, picture, type,
						udetailsEquipment);
				return result;
			}
			return LianjiuResult.build(401, "请输入正确的电话号码");
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
	public LianjiuResult loginOut(String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(400, "请传入userId");
		}
		return userService.loginOut(userId);
	}
}