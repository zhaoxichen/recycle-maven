package com.lianjiu.rest.mapper;

import java.util.List;

import com.lianjiu.model.Power;
import com.lianjiu.model.vo.PowerVo;

public interface PowerMapper {

    int deleteByPrimaryKey(String powerId);

    int insert(Power record);

    Power selectByPrimaryKey(String powerId);

    int updateByPrimaryKeySelective(Power record);

	List<Power> getAll();

	List<PowerVo> getAllVo();

}