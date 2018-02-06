package com.lianjiu.mapper.user;

import java.util.List;

import com.lianjiu.model.UserWalletRecord;

public interface UserWalletRecordMapper {

	int deleteByPrimaryKey(String recordId);

	int insertSelective(UserWalletRecord record);

	UserWalletRecord selectByPrimaryKey(String recordId);

	int updateByPrimaryKeySelective(UserWalletRecord record);

	List<UserWalletRecord> selectRecordByUserId(String userId);

	List<UserWalletRecord> selectByUserId(String userId);

	UserWalletRecord selectWalletMoneyidByOrderid(String outTradeNo);
}