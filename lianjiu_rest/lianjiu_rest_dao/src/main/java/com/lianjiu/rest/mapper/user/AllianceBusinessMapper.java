package com.lianjiu.rest.mapper.user;

import java.util.List;

import com.lianjiu.model.AllianceBusiness;
import com.lianjiu.model.vo.SearchObjecVo;

public interface AllianceBusinessMapper {
	int deleteByPrimaryKey(String allianceBusinessId);

	int insert(AllianceBusiness allianceBusiness );

	int insertSelective(AllianceBusiness allianceBusiness );
	
	AllianceBusiness selectByPrimaryKey(String allianceBusinessId);

	int updateAllBusiness(AllianceBusiness allianceBusiness);

	int updateByPrimaryKeySelective(AllianceBusiness allianceBusiness);

	//创建登陆信息账号对象
	void insertAllian(AllianceBusiness allianceBusiness);

	AllianceBusiness selectAllByabusName(String username);

	void updateLogined(AllianceBusiness alliance);

	void updateAlliance(AllianceBusiness allianceBusiness);

	AllianceBusiness selectAllianceByUserId(String userId);

	List<String> getAllPhone();

	String getPhoneById(String allianceId);

	String getTokenById(String allianceId);

	List<AllianceBusiness> selectBySearchObjecVo(SearchObjecVo vo);

	List<String> selectAllianceIdAppoint();

	String getIdByPhone(String phone);

}