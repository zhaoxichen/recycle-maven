package com.lianjiu.rest.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.AllianceBusinessApplication;
import com.lianjiu.model.vo.SearchObjecVo;

public interface AllianceBusinessApplicationMapper {
    int deleteByPrimaryKey(String albnessApplicationId);

    int insert(AllianceBusinessApplication allianceBusinessApplication);

    int insertSelective(AllianceBusinessApplication allianceBusinessApplication);

    AllianceBusinessApplication selectByPrimaryKey(String albnessApplicationId);

    int updateByPrimaryKeySelective(AllianceBusinessApplication allianceBusinessApplication);

    int updateByPrimaryKey(AllianceBusinessApplication allianceBusinessApplication);

	String getIdByPhone(String alliancePhone);

	List<AllianceBusinessApplication> selectBySearchObjecVo(SearchObjecVo vo);

	List<AllianceBusinessApplication> vagueQuery(@Param(value = "app") AllianceBusinessApplication allianceBusinessApplication,
			@Param(value = "cratedStart") String cratedStart,@Param(value = "cratedOver") String cratedOver); 
    
}