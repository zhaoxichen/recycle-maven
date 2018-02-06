package com.lianjiu.sso.service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.UserAddress;

public interface UserAddressService {

	// 查询用户收获地址
	LianjiuResult chooseAdressByUser(String userId);

	// 删除收获地址
	LianjiuResult deleteAdressByUser(String userAddressId, String userId);

	// 添加收获数据
	LianjiuResult insertAddressByUser(UserAddress address);

	// 更新收货地址
	LianjiuResult updateAddress(UserAddress address);

	// 改变默认地址
	LianjiuResult setDefault(String userAddressId, String userId);

	// 查询当前地址
	LianjiuResult selectByAddressId(String userAddressId, String userId);

}
