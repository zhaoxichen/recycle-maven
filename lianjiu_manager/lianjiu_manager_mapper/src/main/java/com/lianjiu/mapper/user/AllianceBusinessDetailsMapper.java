package com.lianjiu.mapper.user;

import java.util.List;

import com.lianjiu.model.AllianceBusinessDetails;
import com.lianjiu.model.vo.SearchObjecVo;

public interface AllianceBusinessDetailsMapper {
	int deleteByPrimaryKey(String abunesId);

	int insert(AllianceBusinessDetails allianceBusinessDetails );

	int insertSelective(AllianceBusinessDetails allianceBusinessDetails );
	
	AllianceBusinessDetails selectByPrimaryKey(String abunesId);

	int updateAllianceBusinessDetails(AllianceBusinessDetails allianceBusinessDetails);

	int updateByPrimaryKeySelective(AllianceBusinessDetails allianceBusinessDetails);

	List<AllianceBusinessDetails> selectBySearchObjecVo(SearchObjecVo vo);

	String getAppIdById(String albnessApplicationId);

	int modifyByAbunesId(AllianceBusinessDetails details);
}