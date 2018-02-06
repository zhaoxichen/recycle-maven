package com.lianjiu.mapper.user;

import java.util.List;

import com.lianjiu.model.AllianceBuappRemarks;
import com.lianjiu.model.vo.SearchObjecVo;

public interface AllianceBuappRemarksMapper {
    int deleteByPrimaryKey(String alBuappRemarksId);

    int insert(AllianceBuappRemarks allianceBuappRemarks);

    int insertSelective(AllianceBuappRemarks allianceBuappRemarks);

    AllianceBuappRemarks selectByPrimaryKey(String alBuappRemarksId);

    int updateByPrimaryKeySelective(AllianceBuappRemarks allianceBuappRemarks);

    int updateByPrimaryKeyWithBLOBs(AllianceBuappRemarks allianceBuappRemarks);

    int updateByPrimaryKey(AllianceBuappRemarks allianceBuappRemarks);

	int addAllBusinessRemarks(AllianceBuappRemarks allianceBuappRemarks);

	List<AllianceBuappRemarks> selectBySearchObjecVo(SearchObjecVo vo);

	AllianceBuappRemarks selectByAapId(String albnessApplicationId);
}