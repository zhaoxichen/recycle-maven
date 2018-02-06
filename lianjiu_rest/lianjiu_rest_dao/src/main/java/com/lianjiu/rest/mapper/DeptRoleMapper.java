package com.lianjiu.rest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.DeptRole;

public interface DeptRoleMapper {

    int deleteByPrimaryKey(String deptRoleId);

    int insert(DeptRole record);

    DeptRole selectByPrimaryKey(String deptRoleId);

    int updateByPrimaryKeySelective(DeptRole record);

	List<DeptRole> getRoleByDeptId(String deptId);

	int deleteDept(String deptId);

	int deleteByRoleId(String roleId);

	int addDRList(@Param(value = "drList") List<DeptRole> drList);

}