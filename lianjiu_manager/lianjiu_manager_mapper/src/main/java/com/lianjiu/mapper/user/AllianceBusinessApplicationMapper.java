package com.lianjiu.mapper.user;

import java.util.List;

import com.lianjiu.model.AllianceBusinessApplication;
import com.lianjiu.model.vo.SearchObjecVo;

public interface AllianceBusinessApplicationMapper {
    int deleteByPrimaryKey(String albnessApplicationId);

    int insert(AllianceBusinessApplication allianceBusinessApplication);

    int insertSelective(AllianceBusinessApplication allianceBusinessApplication);

    AllianceBusinessApplication selectByPrimaryKey(String albnessApplicationId);

    int updateByPrimaryKeySelective(AllianceBusinessApplication allianceBusinessApplication);

    int updateByPrimaryKey(AllianceBusinessApplication allianceBusinessApplication);
    
    List<AllianceBusinessApplication> selectBySearchObjecVo(SearchObjecVo vo);
}