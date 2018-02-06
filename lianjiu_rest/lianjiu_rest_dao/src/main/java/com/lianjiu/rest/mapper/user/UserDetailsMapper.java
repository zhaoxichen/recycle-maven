package com.lianjiu.rest.mapper.user;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.UserDetails;

public interface UserDetailsMapper {
	int deleteByPrimaryKey(String udetailsId);

	int insert(UserDetails record);

	UserDetails selectByPrimaryKey(String udetailsId);

	int updateByPrimaryKeySelective(UserDetails record);

	int updateByPrimaryKey(UserDetails record);

	int updateUserIdentity(UserDetails ud);

	int updateUserbankCard(UserDetails ud);

	int removeUserbankCard(String udetailsId);

	UserDetails selectByUserId(String userId);

	void saveOpenId(String openId, String userId);

	// UserDetails selectCardById(String udetailsId);

	Integer getIsIdCard(String userId);

	void insertId(UserDetails userDetails);

	void updateIsCard(Integer states);

	void updateIsCard(String detailsId, String states);

	int upIdCardById(String userDetailsId, String username, String idCard, Date date);

	int upIdCardById(UserDetails userDetails);

	UserDetails getBankCardById(String userId);

	UserDetails getNameCardById(String userId);

	void updatePhone(UserDetails userDetails);

	Integer selectIntegralById(String udetailsId);

	Integer modifyIntegeralById(UserDetails userDetails);

	String selectOpenidByUser(String userId);

	int saveOpenIdH5(String openId, String userId, Date date, String checkCode);

	void modifyEqEdByUserId(UserDetails details);

	String getEdById(String udetailsId);

	UserDetails getDetailsByUserId(String udetailsId);

	int getReferrerByPhone(String phone);

	void modifyRefferByUserId(UserDetails details);

	int updateCheckCode(@Param(value = "userId") String userId, @Param(value = "check") String check);

	UserDetails getIsFirstOrder(String udetailsId);
	
	void modifyIsFirstOrder(@Param(value = "isFirstOrder") Integer isFirstOrder,@Param(value = "udetailsId") String udetailsId);
}