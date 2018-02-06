package com.lianjiu.rest.mapper.user;

import com.lianjiu.model.UserRole;

public interface UserRoleMapper {
    int deleteByPrimaryKey(String userRoleId);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(String userRoleId);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

	int deleteByRoleId(String roleId);
	
	int deleteByUserId(String userId);
}