package com.lianjiu.rest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.DeptRole;
import com.lianjiu.model.Role;

public interface RoleMapper {

    int deleteByPrimaryKey(String roleId);

    int insert(Role record);

    Role selectByPrimaryKey(String roleId);

    int updateByPrimaryKeySelective(Role record);
    
    List<Role> getAll();

	List<Role> getRoleList(@Param(value = "roleList") List<DeptRole> roleList);

}