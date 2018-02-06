package com.lianjiu.rest.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.AllianceBusinessDetails;

public interface AllianceBusinessDetailsMapper {
	int deleteByPrimaryKey(String abunesId);

	int insert(AllianceBusinessDetails allianceBusinessDetails);

	int insertSelective(AllianceBusinessDetails allianceBusinessDetails);

	AllianceBusinessDetails selectByPrimaryKey(String abunesId);

	int updateAllianceBusinessDetails(AllianceBusinessDetails allianceBusinessDetails);

	int updateByPrimaryKeySelective(AllianceBusinessDetails allianceBusinessDetails);

	int updateBusinessById(AllianceBusinessDetails details);

	AllianceBusinessDetails selectById(String allianceId);

	List<String> getAllId();

	int updateWeekOrderById(AllianceBusinessDetails newDetails);

	int modifyWeeklyState(String outTradeNo);

	int updateCancelById(String allianceId);

	List<AllianceBusinessDetails> selectBySearchObjecVo(Object object);

	int modifyByAbunesId(AllianceBusinessDetails details);

	List<String> getAllianceIdByStatus();

	void updatePhone(AllianceBusinessDetails allianceBusinessDetails);

	List<AllianceBusinessDetails> vagueQuery(@Param(value="abDetails") AllianceBusinessDetails allianceBusinessDetails,@Param(value="cratedStart")  String cratedStart,
			@Param(value="cratedOver") String cratedOver);

	List<String> getAllOrderId(); 
}