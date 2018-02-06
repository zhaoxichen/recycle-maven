package com.lianjiu.rest.controller.user;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.ExceptionUtil;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.HttpClientUtil;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.UserAddress;
import com.lianjiu.model.UserDetails;
import com.lianjiu.rest.service.user.UserService;

/**
 * 用户管理表现层
 * 
 * @author whd
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;

	// 创建用户
	@RequestMapping(value = "/register/{username}/{userPassword}")
	@ResponseBody
	public LianjiuResult createUser(@PathVariable String username, @PathVariable String userPassword) {
		if (Util.isEmpty(username)) {
			return LianjiuResult.build(401, "请传入正确的username");
		}
		if (Util.isEmpty(userPassword)) {
			return LianjiuResult.build(402, "请传入正确的userPassword");
		}
		System.out.println("注册");
		try {
			return userService.createUser(username, userPassword);
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * whd 2017年9月20日 descrption:根据userId查询所有用户地址
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/chooseAdress/{userId}/byUser", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult chooseAdressByUser(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.chooseAdressByUser(userId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月17日 descrption:根据用户id查默认地址
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/chooseAddressDefult/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult chooseAddressDefultByUser(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.getAddressDefultByUser(userId);
	}

	/**
	 * 
	 * whd 2017年9月20日 descrption:根据userAddressId查询所有用户地址
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/selectAddressByAddressId/{userAddressId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectAddressByAddressId(@PathVariable String userAddressId) {
		if (Util.isEmpty(userAddressId)) {
			return LianjiuResult.build(401, "请传入正确的userAddressId");
		}
		return userService.selectAddressByAddressId(userAddressId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月20日 descrption:为用户新增一个地址
	 * 
	 * @param id
	 * @return String
	 */
	@RequestMapping(value = "/addAdress/toUser", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addAdressToUser(UserAddress address) {
		if (null == address) {
			return LianjiuResult.build(401, "address对象绑定错误");
		}
		if (Util.isEmpty(address.getUserAddressPhone())) {
			return LianjiuResult.build(402, "电话号码为空，请传入联系电话");
		}
		if (Util.isEmpty(address.getUserProvince())) {
			return LianjiuResult.build(403, "省份为空，请传入省份");
		}
		if (Util.isEmpty(address.getUserCity())) {
			return LianjiuResult.build(404, "城市为空，请传入城市");
		}
		if (Util.isEmpty(address.getUserDistrict())) {
			return LianjiuResult.build(405, "区为空，请传入区");
		}
		if (Util.isEmpty(address.getUserLocation())) {
			return LianjiuResult.build(406, "详细地址为空，请传入详细地址");
		}
		if (Util.isEmpty(address.getUserSite())) {
			return LianjiuResult.build(407, "用户地点为空，请传入用户地点");
		}
		if (Util.isEmpty(address.getLongitude())) {
			return LianjiuResult.build(408, "未获取到当前位置信息");
		}
		if (Util.isEmpty(address.getLatitude())) {
			return LianjiuResult.build(409, "未获取到当前位置信息");
		}
		if (null == address.getUserDefault()) {
			return LianjiuResult.build(410, "默认地址为空，请传入默认地址");
		}
		if (address.getUserAddressPhone().matches(GlobalValueUtil.REGEX_PHONE)) {
			return userService.insertAddressByUser(address);
		} else {
			return LianjiuResult.build(411, "请填写正确的手机号码");
		}
	}

	/**
	 * 
	 * zhaoxi 2017年9月20日 descrption:更新地址
	 * 
	 * @param address
	 * @return String
	 */
	@RequestMapping(value = "/modifyAdress/toUser", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyAdressToUser(UserAddress address) {
		if (null == address) {
			return LianjiuResult.build(401, "address对象绑定错误");
		}
		String reg = "[0-9]{11}";
		if (address.getUserAddressPhone().matches(reg)) {
			return userService.updateAddress(address);
		} else {
			return LianjiuResult.build(411, "电话号码格式不正确");
		}
	}

	/**
	 * 
	 * zhaoxi 2017年9月21日 descrption:修改地址为默认地址
	 * 
	 * @param addressId
	 * @param userId
	 * @return String
	 */
	@RequestMapping(value = "/setAdressDefault/toUser", method = RequestMethod.POST)
	@ResponseBody
	public String setAdressDefault(String addressId, String userId) {
		if (Util.isEmpty(addressId)) {
			return "请传入正确的addressId";
		}
		if (Util.isEmpty(userId)) {
			return "请传入正确的userId";
		}
		if (Util.isEmpty(addressId)) {
			LianjiuResult result = new LianjiuResult();
			result.setStatus(501);
			result.setMsg("请传入地址id");
			return JsonUtils.objectToJson(result);
		}
		// 封装参数
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("addressId", addressId);
		paramMap.put("userId", userId);
		return HttpClientUtil.doPost(GlobalValueUtil.SSO_BASE_URL + "userAddress/updateAddress/address/byUser",
				paramMap);
	}

	/**
	 * 
	 * whd 2017年9月20日 descrption:根据userId删除用户地址
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/deleteAdress/{userAddressId}/{userId}/byUser", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteAdressByUser(@PathVariable String userAddressId, @PathVariable String userId) {
		if (Util.isEmpty(userAddressId)) {
			return LianjiuResult.build(401, "请传入正确的userAddressId");
		}
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(402, "请传入正确的userId");
		}
		return userService.deleteAdressByUser(userAddressId, userId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月26日 descrption:
	 * 
	 * @param addressId
	 * @param userId
	 * @return String
	 */
	@RequestMapping(value = "/selectAddressDefault/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectAddressDefault(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		/*
		 * if (Util.isEmpty(userId)) { LianjiuResult result = new
		 * LianjiuResult(); result.setStatus(501); result.setMsg("请传入地址id");
		 * return result; }
		 */
		return userService.selectAddressDefault(userId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月28日 descrption:修改默认地址
	 * 
	 * @param userAddressId
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/setDefault/{userAddressId}/{userId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult setDefault(@PathVariable String userAddressId, @PathVariable String userId) {
		if (Util.isEmpty(userAddressId)) {
			return LianjiuResult.build(402, "请传入正确的userAddressId");
		}
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(402, "请传入正确的userId");
		}
		return userService.setDefault(userAddressId, userId);
	}

	/**
	 * 编辑查单个地址数据
	 * 
	 * @param userAddressId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/selectByAddressId/{userAddressId}/{userId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult selectByAddressId(@PathVariable String userAddressId, @PathVariable String userId) {
		if (Util.isEmpty(userAddressId)) {
			return LianjiuResult.build(402, "请传入正确的userAddressId");
		}
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(402, "请传入正确的userId");
		}
		return userService.selectByAddressId(userAddressId, userId);
	}

	/**
	 * 
	 * huangfu 2017年9月26日 用户登录成功之后查看他的金额等
	 * 
	 * @param addressId
	 * @param userId
	 * @return String
	 */
	@RequestMapping(value = "/selectAsset/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectAsset(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.selectAsset(userId);
	}

	/**
	 * 
	 * huangfu 2017年9月26日 钱包信息
	 * 
	 * @param addressId
	 * @param userId
	 * @return String
	 */
	@RequestMapping(value = "/selectAssetDetails/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectAssetDetails(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.selectAssetDetails(userId);
	}

	/**
	 * 
	 * huangfu 2017年9月26日 实名认证
	 * 
	 * @param addressId
	 * @param userId
	 * @return String
	 */
	@RequestMapping(value = "/modifyUserInformation", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult userCertification(String udetailsId, String udetailsIdCard, String userName) {
		if (Util.isEmpty(udetailsId)) {
			return LianjiuResult.build(401, "请传入正确的udetailsId");
		}
		if (Util.isEmpty(udetailsIdCard)) {
			return LianjiuResult.build(402, "请传入正确的udetailsIdCard");
		}
		if (Util.isEmpty(userName)) {
			return LianjiuResult.build(403, "请传入正确的userName");
		}
		return userService.userCertification(udetailsId, udetailsIdCard, userName);
	}

	/**
	 * 
	 * huangfu 2017年9月26日 用户绑定银行卡
	 * 
	 * @param addressId
	 * @param userId
	 * @return String
	 */
	@RequestMapping(value = "/modifyUserbankCard", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyUserbankCard(String userId, String udetailsCardNo, String udetailsCardBank,
			String code) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (Util.isEmpty(udetailsCardNo)) {
			return LianjiuResult.build(402, "请传入正确的udetailsCardNo");
		}
		if (Util.isEmpty(udetailsCardBank)) {
			return LianjiuResult.build(403, "请传入正确的udetailsCardBank");
		}
		if (Util.isEmpty(code)) {
			return LianjiuResult.build(404, "请传入正确的code");
		}
		return userService.modifyUserbankCard(userId, udetailsCardNo, udetailsCardBank, code);
	}

	/**
	 * 
	 * huangfu 2017年9月26日 用户删除银行卡
	 * 
	 * @param addressId
	 * @param userId
	 * @return String
	 */
	@RequestMapping(value = "/removeUserbankCard", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult removeUserbankCard(String udetailsId) {
		if (Util.isEmpty(udetailsId)) {
			return LianjiuResult.build(401, "请传入正确的udetailsId");
		}
		return userService.removeUserbankCard(udetailsId);
	}

	/**
	 * 
	 * huangfu 2017年9月26日 用户修改银行卡
	 * 
	 * @param addressId
	 * @param userId
	 * @return String
	 */
	@RequestMapping(value = "/updateUserbankCard", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateUserbankCard(UserDetails userDetails, String code) {
		if (null == userDetails) {
			return LianjiuResult.build(401, "请传入用户信息");
		}
		if (Util.isEmpty(code)) {
			return LianjiuResult.build(402, "请传入短信验证码");
		}
		return userService.updateUserbankCard(userDetails, code);
	}

	/**
	 * 
	 * zhaoxi 2017年10月11日 descrption:订单中心
	 * 
	 * @param uid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExpressOrders/{uid}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExpressOrders(@PathVariable String uid) {
		if (Util.isEmpty(uid)) {
			return LianjiuResult.build(401, "请传入短信验证码");
		}
		return userService.getExpressOrders(uid);
	}

	/**
	 * 
	 * zhaoxi 2017年10月11日 descrption:订单中心
	 * 
	 * @param uid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRecycleOrders/{uid}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRecycleOrders(@PathVariable String uid) {
		return LianjiuResult.ok("未开发");
	}

	/**
	 * 
	 * zhaoxi 2017年10月11日 descrption:获取指定订单分类下的订单
	 * 
	 * @param cid
	 * @param uid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getOrdersList/{uid}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getOrdersList(@PathVariable String uid) {
		return LianjiuResult.ok("未开发");
	}

	/**
	 * 
	 * zhaoxi 2017年10月11日 descrption:用户设置
	 * 
	 * @param cid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyUser/{uid}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult modifyUser(@PathVariable String uid) {
		return LianjiuResult.ok("未开发");
	}

	/**
	 * 
	 * zhaoxi 2017年10月11日 descrption:优惠券管理，获取优惠券列表
	 * 
	 * @param uid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getCouponList/{userId}/byUser", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getCouponList(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.selectCouponByUserId(userId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月11日 descrption:地址管理，获取用户地址列表
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getUserAddress/{userId}/byUser", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getUserAddress(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.chooseAdressByUser(userId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月11日 descrption:地址管理，获取用户地址列表
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getActivity/{pageNum}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getActivity(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}

		return userService.chooseAdressByUser(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年10月11日 descrption:环保达人，返回当前用户的环保积分
	 * 
	 * @param uid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getEnvirProtector/{uid}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getEnvirProtector(@PathVariable String uid) {
		return LianjiuResult.ok("未开发");
	}

	/**
	 * 
	 * zhaoxi 2017年10月11日 descrption:实名认证
	 * 
	 * @param uid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/subCertification/id=123", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult subEnvirProtector(String userId, String username, String idCard) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (Util.isEmpty(username)) {
			return LianjiuResult.build(401, "请传入正确的username");
		}
		if (Util.isEmpty(idCard)) {
			return LianjiuResult.build(401, "请传入正确的idCard");
		}
		return userService.subEnvirProtector(userId, username, idCard);
	}

	/**
	 * 
	 * huangfu 2017年10月13日 descrption:回收车
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getUserCare/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getUserCare(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.getUserCare(userId);
	}

	/**
	 * 
	 * huangfu 2017年10月13日 descrption:快递
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExpressItem/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExpressItem(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.getExpressItem(userId);
	}

	/**
	 * 
	 * huangfu 2017年10月13日 descrption:精品订单
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellentItem/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentItem(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.getExcellentItem(userId);
	}

	/**
	 * 
	 * huangfu 2017年10月13日 descrption:手机维修订单
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepair/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRepair(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.getRepair(userId);
	}

	/**
	 * 
	 * huangfu 2017年10月13日 descrption:废品订单
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getFacaface/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getFacaface(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.getFacaface(userId);
	}

	/**
	 * 
	 * huangfu 2017年10月13日 descrption:校验实名认证
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/isCertification/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult isCertification(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.isCertification(userId);
	}

	/**
	 * 
	 * huangfu 2017年10月13日 descrption:校验是否绑定银行卡
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/isUserbankCard/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult isUserbankCard(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.isUserbankCard(userId);
	}

	/**
	 * 
	 * huangfu 2017年10月13日 descrption:校验身份证信息接口
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/isCertificationPort")
	@ResponseBody
	public String isCertificationPort(String name, String cardno, String output) {
		if (Util.isEmpty(name)) {
			return "请传入正确的name";
		}
		if (Util.isEmpty(cardno)) {
			return "请传入正确的cardno";
		}
		if (Util.isEmpty(output)) {
			return "请传入正确的output";
		}
		String appkey = "2384e15e471eb8b119f90c43045a22d1";
		System.out.println("name:" + name + "cardno:" + cardno);
		// return
		// HttpClientUtil.doGet("http://api.id98.cn/api/idcard?appkey=d10a8e06284cf889deaf93ffb5d9c60a&name=%E9%82%93%E6%B0%B8%E6%9C%9B&cardno=610922197401232578");
		return userService.isCertificationPort(appkey, name, cardno, output);
	}

	/**
	 * whd 2017年9月25日 descrption:绑定身份证获取验证码
	 * 
	 * @param phone
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sendSms/bankCard/{phone}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult sendSms(@PathVariable String phone) {
		if (Util.isEmpty(phone)) {
			return LianjiuResult.build(401, "请传入正确的phone");
		}
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = userService.sendSmsForBankCard(phone);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	public static void main(String[] args) {
		String phone = "1760767912";
		// String reg =
		// "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
		String reg = "[0-9]{11}";
		if (phone.matches(reg)) {
			System.out.println("true");
		}
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(phone);
		System.out.println(m.find());

	}

	/**
	 * whd 2017年9月25日 descrption:提现获取用户基本信息
	 * 
	 * @param userId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getUserDetailsByUserId", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getUserDetailsByUserId(String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = userService.getUserDetailsByUserId(userId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * huangfu 2017年9月19日 descrption:自动生成推荐人
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRecommendFirst/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult autoAddRecommend(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入用户id");
		}
		try {
			System.out.println("推荐人添加control");
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = userService.autoAddRecommend(userId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * zhaoxi 2018年1月4日 descrption:获取该用户已经生成的推荐码
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRecommend/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRecommend(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入用户id");
		}
		try {
			System.out.println("推荐人添加control");
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = userService.getRecommend(userId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

}
