package com.lianjiu.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.UserAddress;
import com.lianjiu.rest.mapper.user.UserAddressMapper;
import com.lianjiu.sso.service.UserAddressService;

@Service
public class UserAddressServiceImpl implements UserAddressService {

	@Autowired
	private UserAddressMapper userAddressMapper;
	
	private static Logger loggerAddress = Logger.getLogger("address");
	/**
	 * 根据userID查用户地址
	 */
	@Override
	public LianjiuResult chooseAdressByUser(String userId) {
		// 执行查询
		List<UserAddress> listAddress = userAddressMapper.selectAllByUser(userId);
		if(null == listAddress || 0 == listAddress.size()){
			loggerAddress.info("地址列表为空");
			return LianjiuResult.build(501, "地址为空");
		}
		LianjiuResult result = new LianjiuResult(listAddress);
		return result;
	}

	/**
	 * 根据userID删除用户地址
	 */
	@Override
	public LianjiuResult deleteAdressByUser(String userAddressId, String userId) {
		if (Util.isEmpty(userAddressId)) {
			return LianjiuResult.build(501, "请指定企业回收订单id");
		}
		// 去数据库查询
		userAddressMapper.deleteByPrimaryKey(userAddressId);
		// 执行查询
		List<UserAddress> listAddress = userAddressMapper.selectAllByUser(userId);
		if(null == listAddress || 0 == listAddress.size()){
			return LianjiuResult.build(501, "地址为空");
		}
		LianjiuResult result = new LianjiuResult(listAddress);
		return result;
	}

	/**
	 * 插入用户地址
	 * 
	 * @param address
	 * @return
	 */
	@Override
	public LianjiuResult insertAddressByUser(UserAddress address) {
		if (Util.isEmpty(address.getUserId())) {
			loggerAddress.info("地址对象中没有用户编号");
			return LianjiuResult.build(501, "请传入用户编号");
		}

		List<UserAddress> listAddress = userAddressMapper.selectAllByUser(address.getUserId());
		if (listAddress.size() <= 0) {
			address.setUserDefault(true);
		}
		address.setUserAddressId(IDUtils.genUserAddressId());
		address.setUserCreated(new Date());
		address.setUserUpdated(new Date());
		// 去数据库添加
		userAddressMapper.insertSelective(address);
		// 执行查询
		listAddress = userAddressMapper.selectAllByUser(address.getUserId());
		if(null == listAddress || 0 == listAddress.size()){
			return LianjiuResult.build(502, "地址为空");
		}
		LianjiuResult result = new LianjiuResult(listAddress);
		return result;
	}

	/**
	 * 更新用户地址
	 * 
	 * @param address
	 * @return
	 */
	@Override
	public LianjiuResult updateAddress(UserAddress address) {
		if (null == address.getUserId()) {
			loggerAddress.info("地址对象中没有用户编号");
			return LianjiuResult.build(501, "请传入用户编号");
		}
		address.setUserUpdated(new Date());
		// 去数据库更新
		userAddressMapper.updateByPrimaryKeySelective(address);
		// 执行查询
		List<UserAddress> listAddress = userAddressMapper.selectAllByUser(address.getUserId());
		if(null == listAddress || 0 == listAddress.size()){
			return LianjiuResult.build(501, "地址为空");
		}
		LianjiuResult result = new LianjiuResult(listAddress);
		return result;
	}

	/**
	 * 更新用户地址
	 * 
	 * @param userAddressId,userId,userDefault
	 * @return
	 */
	@Override
	public LianjiuResult setDefault(String userAddressId, String userId) {
		UserAddress userAddress = new UserAddress();
		userAddress.setUserAddressId(userAddressId);
		userAddress.setUserId(userId);
		//将默认状态为true的地址，设为false
		userAddressMapper.setDefaultFalse(userId);
		// 将当前地址默认状态设为true
		int rowsAffected = userAddressMapper.setDefaultTrue(userAddressId);
		return LianjiuResult.ok(rowsAffected);
	}
	/**
	 * 查当前
	 */
	@Override
	public LianjiuResult selectByAddressId(String userAddressId, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
