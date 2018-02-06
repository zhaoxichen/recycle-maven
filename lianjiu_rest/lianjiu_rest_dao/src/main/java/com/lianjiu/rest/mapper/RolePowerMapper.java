package com.lianjiu.rest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.RolePower;

public interface RolePowerMapper {

    int deleteByPrimaryKey(String rolePowerId);
    
    int deleteByRoleId(String roleId);

	int deleteByPowerId(String powerId);

    int insert(RolePower record);

    RolePower selectByPrimaryKey(String rolePowerId);

    int updateByPrimaryKeySelective(RolePower record);
    
    
	int addList(@Param(value = "powerList") List<RolePower> powerList);

	List<RolePower> getRpListByRoleId(String roleId);

}