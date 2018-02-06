package com.lianjiu.service.admin;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.Dept;

public interface DeptService {

	LianjiuResult getDeptAll(int pageNum, int pageSize);

	LianjiuResult deleteDept(String deptId);

	LianjiuResult addDept(Dept dept);

	LianjiuResult updateDept(Dept dept);

	LianjiuResult getDeptById(String deptId);

	LianjiuResult getAll();

}
