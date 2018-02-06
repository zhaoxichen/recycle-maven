package com.lianjiu.rest.service.user;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.User;
import com.lianjiu.model.UserAddress;
import com.lianjiu.model.UserDetails;

public interface UserService {

	LianjiuResult selectAllUser(int pageNum, int pageSize);

	User getUserByToken(String token);

	LianjiuResult getUserByPowerId(String userId);

	LianjiuResult getByRoleId(String userId);

	LianjiuResult deleteUser(String userId);

	LianjiuResult selectAddressDefault(String userId);

	LianjiuResult selectAsset(String userId);

	LianjiuResult selectAssetDetails(String userId);

	LianjiuResult userCertification(String udetailsId, String udetailsIdCard, String userName);

	LianjiuResult modifyUserbankCard(String userId, String udetailsCardNo, String udetailsCardBank, String code);

	LianjiuResult removeUserbankCard(String udetailsId);

	LianjiuResult insertAddressByUser(UserAddress address);

	LianjiuResult updateAddress(UserAddress address);

	LianjiuResult setDefault(String userAddressId, String userId);

	LianjiuResult deleteAdressByUser(String userAddressId, String userId);

	LianjiuResult chooseAdressByUser(String userId);

	LianjiuResult selectAddressByAddressId(String userAddressId);

	LianjiuResult createUser(String username, String userPassword);

	LianjiuResult getExpressOrders(String userId);

	LianjiuResult selectByAddressId(String userAddressId, String userId);

	LianjiuResult selectCouponByUserId(String userId);

	LianjiuResult chooseAdressByUser(int pageNum, int pageSize);

	LianjiuResult subEnvirProtector(String userId,String username, String idCard);
	
	LianjiuResult getUserCare(String userId);

	LianjiuResult getExpressItem(String userId);

	LianjiuResult getExcellentItem(String userId);

	LianjiuResult getRepair(String userId);

	LianjiuResult getFacaface(String userId);

	LianjiuResult getAddressDefultByUser(String userId);

	LianjiuResult isCertification(String userId);

	LianjiuResult isUserbankCard(String userId);

	LianjiuResult updateUserbankCard(UserDetails userDetails, String code);

	String isCertificationPort(String appkey, String name, String cardno, String output);

	LianjiuResult sendSmsForBankCard(String phone);

	LianjiuResult getUserDetailsByUserId(String userId);

	LianjiuResult autoAddRecommend(String userId);

	LianjiuResult getRecommend(String userId);

}
