package com.lianjiu.mapper.user;

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

	List<AllianceBusiness> selectBySearchObjecVo(SearchObjecVo vo);
}