package com.lianjiu.service.admin.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.DeptRole;
import com.lianjiu.model.Role;
import com.lianjiu.model.RolePower;
import com.lianjiu.model.vo.PowerVo;
import com.lianjiu.rest.mapper.DeptRoleMapper;
import com.lianjiu.rest.mapper.PowerMapper;
import com.lianjiu.rest.mapper.RoleMapper;
import com.lianjiu.rest.mapper.RolePowerMapper;
import com.lianjiu.service.admin.RoleService;

/**
 * 人员管理
 * 
 * @author huangfu
 *
 */
@Service
public class RoleSerciceImpl implements RoleService{

	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private RolePowerMapper rolePowerMapper;
	
	@Autowired
	private DeptRoleMapper deptRoleMapper;
	
	@Autowired
	private PowerMapper powerMapper;
	//分页获取所有
	@Override
	public LianjiuResult getRoleAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<Role> role = roleMapper.getAll();
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Role> listRole = (Page<Role>) role;
		System.out.println("总页数：" + listRole.getPages());
		LianjiuResult result = new LianjiuResult(role);
		result.setTotal(listRole.getTotal());
		return result;
	}
	//根据角色id获取
	@Override
	public LianjiuResult getRoleById(String roleId) {
		// 执行查询
		Role role = roleMapper.selectByPrimaryKey(roleId);
		List<RolePower> rpList = rolePowerMapper.getRpListByRoleId(roleId);
		//获取所有权限
		List<PowerVo> list = powerMapper.getAllVo(); 
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("allVo", list);
		map.put("role", role);
		for(PowerVo pw : list){
			for(RolePower rp : rpList){
				if(null != pw.getPowerId() && null!=rp.getPowerId() && pw.getPowerId().equals(rp.getPowerId())){
					pw.setPitchOn("1");
				}
			}
		}
		LianjiuResult result = new LianjiuResult(map);
		return result;
	}
	//添加角色
	@Override
	public LianjiuResult addRole(Role role,String [] deptList,List<PowerVo> powerList) {
		String id = IDUtils.genPowerModuleId();		
		System.out.println("权限记录："+powerList.size());
		List<RolePower> list = new ArrayList<>();
		if(null != powerList && powerList.size()>=0){
			for(PowerVo pw : powerList){
				if(null != pw.getPitchOn() && pw.getPitchOn().equals("1")){
					RolePower rp = new RolePower();
					rp.setRolePowerId(IDUtils.genPowerModuleId());
					rp.setRoleId(id);
					rp.setPowerId(pw.getPowerId());	
					list.add(rp);
				}
			}
		}
		if(list.size()>0){
			rolePowerMapper.addList(list);
			System.out.println("成功添加："+list.size()+"条数据");
		}
		role.setRoleId(id);
		role.setRoleCreated(new Date());
		role.setRoleUpdated(new Date());
		// 去数据库添加
		int rowsAffected = roleMapper.insert(role);
		System.out.println("deptList.length:"+deptList.length);
		List<DeptRole> drList = new ArrayList<>();
		for(int i=0;i<deptList.length ; i++){
			DeptRole deptRole = new DeptRole();
			deptRole.setDeptRoleId(IDUtils.genPowerModuleId());
			deptRole.setRoleId(id);
			deptRole.setDeptId(deptList[i]);
			drList.add(deptRole);
			System.out.println("     - - - - - - - -deptList[i]:["+deptList[i]+"]");
		}
		System.out.println("list.size():"+drList.size());
		deptRoleMapper.addDRList(drList);
	
	return LianjiuResult.ok(rowsAffected);
	}
	//更新人员（自己更新  不涉及权限部门操作）
	@Override
	public LianjiuResult updateRole(Role role) {
		if (Util.isEmpty(role.getRoleId())) {
			return LianjiuResult.build(501, "请指定要人员id");
		}
		role.setRoleUpdated(new Date());
//		String roleId = role.getRoleId();
		// 去数据库删除 并更新 
//		List<RolePower> list = new ArrayList<>();
//		//吧选择好的权限筛选出来
//		for(PowerVo pv : powerList){
//			if(null!=pv.getPitchOn()&&pv.getPitchOn().equals("1")){
//				RolePower rp = new RolePower();
//				rp.setRolePowerId(IDUtils.genPowerModuleId());
//				rp.setRoleId(roleId);
//				rp.setPowerId(pv.getPowerId());
//				list.add(rp);
//			}
//		}
//		rolePowerMapper.deleteByRoleId(roleId);
//		//判断是否有选取权限
//		if(list.size()>0){
//			rolePowerMapper.addList(list);
//		}
		int rowsAffected = roleMapper.updateByPrimaryKeySelective(role);
		return LianjiuResult.ok(rowsAffected);
	}
	//删除角色
	@Override
	public LianjiuResult deleteRole(String roleId) {
		if (Util.isEmpty(roleId)) {
			return LianjiuResult.build(501, "请指定要人员id");
		}
		// 去数据库删除 部门人员管理表   人员权限关联表  人员表
		deptRoleMapper.deleteByRoleId(roleId);
		rolePowerMapper.deleteByRoleId(roleId);
		int rowsAffected = roleMapper.deleteByPrimaryKey(roleId);
		//int rolePowerCount = rolePowerMapper.deleteByRoleId(roleId);
		//int userRoleCount = userRoleMapper.deleteByRoleId(roleId);
		//System.out.println("删除user_role表中："+userRoleCount+"条数据");
		//System.out.println("删除rolePowerCount表中："+rolePowerCount+"条数据");
		return LianjiuResult.ok(rowsAffected);
	}
	//获取所有角色
	@Override
	public LianjiuResult getAll() {
		List<Role> role = roleMapper.getAll();
		return LianjiuResult.ok(role);
	}
	//根据部门id 获取上级领导
	@Override
	public LianjiuResult getRoleByDeptId(String deptId) {
		List<DeptRole> list = deptRoleMapper.getRoleByDeptId(deptId);
		if(list.size()>0){
			List<Role> roleList = roleMapper.getRoleList(list);
			return LianjiuResult.ok(roleList);
		}
		return LianjiuResult.ok();
	}
	
}
