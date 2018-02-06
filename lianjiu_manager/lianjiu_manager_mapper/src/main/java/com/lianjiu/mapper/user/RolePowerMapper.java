package com.lianjiu.mapper.user;

import java.util.List;

import com.lianjiu.model.RolePower;
import com.lianjiu.model.vo.SearchObjecVo;

public interface RolePowerMapper {

    int deleteByPrimaryKey(String rolePowerId);

	int deleteByPowerId(String powerId);

	int deleteByRoleId(String roleId);

    int insert(RolePower record);

    int insertSelective(RolePower record);

    RolePower selectByPrimaryKey(String rolePowerId);

    int updateByPrimaryKeySelective(RolePower record);

    int updateByPrimaryKey(RolePower record);

	List<RolePower> selectBySearchObjecVo(SearchObjecVo vo);


}