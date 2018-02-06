package com.lianjiu.mapper;

import java.util.List;

import com.lianjiu.model.Power;
import com.lianjiu.model.vo.SearchObjecVo;

public interface PowerMapper {


    int deleteByPrimaryKey(String powerId);

    int insert(Power record);

    int insertSelective(Power record);

    Power selectByPrimaryKey(String powerId);

    int updateByPrimaryKeySelective(Power record);

    int updateByPrimaryKey(Power record);

	List<PowerMapper> selectBySearchObjecVo(SearchObjecVo vo);
}