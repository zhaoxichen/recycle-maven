package com.lianjiu.rest.mapper;

import java.util.List;

import com.lianjiu.model.Dept;

public interface DeptMapper {
	
    int deleteByPrimaryKey(String deptId);

    int insert(Dept record);

    Dept selectByPrimaryKey(String deptId);

    int updateByPrimaryKeySelective(Dept record);

	List<Dept> getAll();

}