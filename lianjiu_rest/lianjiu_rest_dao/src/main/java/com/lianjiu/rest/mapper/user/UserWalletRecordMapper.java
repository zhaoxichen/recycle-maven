package com.lianjiu.rest.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.UserWalletRecord;

public interface UserWalletRecordMapper {

	int deleteByPrimaryKey(String recordId);

	int insertSelective(UserWalletRecord record);

	UserWalletRecord selectByPrimaryKey(String recordId);

	int updateByPrimaryKeySelective(UserWalletRecord record);

	List<UserWalletRecord> selectRecordByUserId(String userId);

	List<UserWalletRecord> selectByUserId(String userId);

	String getAmountByUserId(String userId);

	UserWalletRecord selectWalletMoneyidByOrderid(String outTradeNo);

	List<UserWalletRecord> selectAll();
	
	List<UserWalletRecord> selectAlls();

	List<UserWalletRecord> vagueQuery(@Param(value = "userWalletRecord") UserWalletRecord userWalletRecord,
			@Param(value = "cratedStart") String cratedStart, @Param(value = "cratedOver") String cratedOver);

	List<UserWalletRecord> getByReferrer(@Param(value = "referrer") String referrer,@Param(value = "isIncome") int categoryId,
			@Param(value = "startTime") String startTime, @Param(value = "endTime") String endTime);

	List<UserWalletRecord> vagueQuerys(@Param(value = "userWalletRecord") UserWalletRecord userWalletRecord,
			@Param(value = "cratedStart") String cratedStart, @Param(value = "cratedOver") String cratedOver);
}