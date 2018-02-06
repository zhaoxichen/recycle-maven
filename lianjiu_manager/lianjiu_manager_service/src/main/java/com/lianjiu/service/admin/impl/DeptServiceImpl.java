package com.lianjiu.service.admin.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.model.Dept;
import com.lianjiu.model.DeptRole;
import com.lianjiu.rest.mapper.DeptMapper;
import com.lianjiu.rest.mapper.DeptRoleMapper;
import com.lianjiu.rest.mapper.RoleMapper;
import com.lianjiu.rest.mapper.RolePowerMapper;
import com.lianjiu.service.admin.DeptService;

@Service
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptMapper deptMapper;
	
	@Autowired
	private DeptRoleMapper deptRoleMapper;

	@Autowired
	private RolePowerMapper rolePowerMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	
	@Override
	public LianjiuResult getDeptAll(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Dept> dept = deptMapper.getAll();		
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Dept> pageBulk = (Page<Dept>) dept;
		System.out.println("总页数：" + pageBulk.getTotal());
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(dept);
	result.setTotal(pageBulk.getTotal());
		return result;
	}

	@Override
	public LianjiuResult deleteDept(String deptId) {
		try {
			//先查询出要删除的部门是否存在人员  如果存在 先删除人员与权限关联的记录  之后删除人员  删除部门人员的记录 最后删除部门
			List<DeptRole> list = deptRoleMapper.getRoleByDeptId(deptId);
			if(list.size()>0){
				System.out.println("部门中还有残存人员~~开始删除");
				for(int i=0;i<list.size();i++){
					System.out.println("开始删除人员的权限 - role_power:"+list.get(i).getRoleId());
					//删除人员的权限
					rolePowerMapper.deleteByRoleId(list.get(i).getRoleId());
					//删除人员的权限
					roleMapper.deleteByPrimaryKey(list.get(i).getRoleId());
				}
				System.out.println("删除完毕");
			}
			int count = deptRoleMapper.deleteDept(deptId);
			System.out.println("删除dept_role 数据："+count);
			int rowsAffected = deptMapper.deleteByPrimaryKey(deptId);
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			System.out.println("捕获异常：" + e.getMessage());
			return LianjiuResult.build(501, "数据异常！");
		}
	}

	@Override
	public LianjiuResult addDept(Dept dept) {
		//loggerProductBulk.info("添加家具");
		dept.setDeptId(IDUtils.genGUIDs());
		dept.setDeptCreated(new Date());
		dept.setDeptUpdated(new Date());
		try {
			int rowsAffected = deptMapper.insert(dept);
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			System.out.println("捕获异常：" + e.getMessage());
			return LianjiuResult.build(501, "数据异常！");
		}
	}

	@Override
	public LianjiuResult updateDept(Dept dept) {
		try {
			
			dept.setDeptUpdated(new Date());
			int rowsAffected = deptMapper.updateByPrimaryKeySelective(dept);
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			System.out.println("捕获异常：" + e.getMessage());
			return LianjiuResult.build(501, "数据异常！");
		}
	}

	@Override
	public LianjiuResult getDeptById(String deptId) {
		Dept dept = deptMapper.selectByPrimaryKey(deptId);
		return LianjiuResult.ok(dept);
	}

	@Override
	public LianjiuResult getAll() {
		List<Dept> list = deptMapper.getAll();
		return LianjiuResult.ok(list);
	}
	

}
