package com.lianjiu.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.UserAddress;
import com.lianjiu.sso.service.UserAddressService;

/**
 * 用户地址
 * 
 * @author whd
 *
 */
@Controller
@RequestMapping("/userAddress")
public class UserAddressController {

	@Autowired
	private UserAddressService userAddressService;

	/**
	 * 
	 * whd 2017年9月20日 descrption:根据userId查询所有用户地址
	 * 
	 * @param userId
	 *            用户编号
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/chooseAdress/{userId}/byUser", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult chooseAdressByUser(@PathVariable String userId) {
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(401, "请传入用户编号");
		}
	//	userAddressService.chooseAdressByUser(userId);
		return userAddressService.chooseAdressByUser(userId);
	}

	/**
	 * 
	 * whd 2017年9月20日 descrption:添加用户地址
	 * 
	 * @return LianjiuResult userAddressService.insertAddress(address);
	 */
	@RequestMapping(value = "/insertAddress/address/byUser", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult insertAddressByUser(UserAddress address) {
		Util.printModelDetails(address);
		if(null == address || "".equals(address)){
			return LianjiuResult.build(401, "地址对象为空");
		}
		return userAddressService.insertAddressByUser(address);
	}

	/**
	 * 
	 * whd 2017年9月20日 descrption:更改用户地址
	 * 
	 * @return LianjiuResult userAddressService.insertAddress(address);
	 */
	@RequestMapping(value = "/updateAddress/address/byUser", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateAddress(UserAddress address) {
		Util.printModelDetails(address);
		if(null == address || "".equals(address)){
			return LianjiuResult.build(401, "地址对象为空");
		}
		return userAddressService.updateAddress(address);
	}

	/**
	 * 
	 * whd 2017年9月20日 descrption:根据userId删除用户地址
	 * 
	 * @param userAddressId
	 *            地址编号
	 * @param userId
	 *            用户编号
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/deleteAdress/{userAddressId}/{userId}/byUser", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteAdressByUser(@PathVariable String userAddressId, @PathVariable String userId) {
		return userAddressService.deleteAdressByUser(userAddressId, userId);
	}

	/**
	 * 设置默认接口
	 * 
	 * @param userAddressId
	 *            地址编号
	 * @param userId
	 *            用户编号
	 * @return
	 */
	@RequestMapping(value = "/setDefault/{userId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult setDefault(
			@RequestParam(value = "userAddressId", defaultValue = "123456789") String userAddressId,
			@PathVariable String userId) {
		System.out.println(userId);
		System.out.println(userAddressId);
		if (Util.isEmpty(userAddressId)) {
			return LianjiuResult.build(401, "sssss");
		}
		if ("123456789".equals(userAddressId)) {
			return LianjiuResult.build(401, "111");
		}
		return LianjiuResult.ok(userAddressId);
		// return userAddressService.setDefault(userAddressId, userId);
	}

	/**
	 * 编辑查单个地址数据
	 * 
	 * @param userAddressId
	 *            地址编号
	 * @param userId
	 *            用户编号
	 * @return
	 */
	@RequestMapping(value = "/selectByAddressId/{userAddressId}/{userId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult selectByAddressId(@PathVariable String userAddressId, @PathVariable String userId) {
		if(Util.isEmpty(userAddressId)){
			return LianjiuResult.build(401, "参数传入错误");
		}
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(401, "参数传入错误");
		}
		return userAddressService.selectByAddressId(userAddressId, userId);
	}
}
