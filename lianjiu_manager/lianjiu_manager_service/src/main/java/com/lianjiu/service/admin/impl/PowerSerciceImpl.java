package com.lianjiu.service.admin.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.DeptRole;
import com.lianjiu.model.Power;
import com.lianjiu.model.Role;
import com.lianjiu.model.RolePower;
import com.lianjiu.model.vo.PowerVo;
import com.lianjiu.rest.mapper.DeptRoleMapper;
import com.lianjiu.rest.mapper.PowerMapper;
import com.lianjiu.rest.mapper.RoleMapper;
import com.lianjiu.rest.mapper.RolePowerMapper;
import com.lianjiu.service.admin.PowerService;

/**
 * 权限
 * 
 * @author huangfu
 *
 */
@Service
public class PowerSerciceImpl implements PowerService
{
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private PowerMapper powerMapper;
	
	@Autowired
	private RolePowerMapper rolePowerMapper;
	
	@Autowired
	private DeptRoleMapper deptRoleMapper;
	
	//获取所有权限
	@Override
	public LianjiuResult getPowerAll() {
		// 执行查询
		List<PowerVo> power = powerMapper.getAllVo();
		LianjiuResult result = new LianjiuResult(power);
		return result;
	}
	//获取所有权限 分页
	@Override
	public LianjiuResult getPowerAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<Power> power = powerMapper.getAll();
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Power> listPower = (Page<Power>) power;
		System.out.println("总页数：" + listPower.getPages());
		LianjiuResult result = new LianjiuResult(power);
		result.setTotal(listPower.getTotal());
		return result;
	}
	//根据权限id获取权限
	@Override
	public LianjiuResult getPowerById(String powerId) {
		// 执行查询
		Power power = powerMapper.selectByPrimaryKey(powerId);
		LianjiuResult result = new LianjiuResult(power);
		return result;
	}
	//添加权限
	@Override
	public LianjiuResult addPower(Power power) {
		if (null == power) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		power.setPowerId(IDUtils.genPowerModuleId());
		power.setPowerCreated(new Date());
		power.setPowerUpdated(new Date());
		// 去数据库添加
		int rowsAffected = powerMapper.insert(power);
		return LianjiuResult.ok(rowsAffected);
	}
	//更新权限
	@Override
	public LianjiuResult updatePower(Power power) {
		power.setPowerUpdated(new Date());
		// 去数据库更新
		int rowsAffected = powerMapper.updateByPrimaryKeySelective(power);
		return LianjiuResult.ok(rowsAffected);
	}
	//删除权限
	@Override
	public LianjiuResult deletePower(String powerId) {
		if (Util.isEmpty(powerId)) {
			return LianjiuResult.build(501, "请指定要权限id");
		}
		// 去数据库删除  权限表的权限
		int rowsAffected = powerMapper.deleteByPrimaryKey(powerId);
		//删除用户角色关联表的权限
		int rolePowerCount = rolePowerMapper.deleteByPowerId(powerId);
		System.out.println("删除role_power表中："+rolePowerCount+"条数据");
		return LianjiuResult.ok(rowsAffected);
	}
	//分配权限
	@Override
	public LianjiuResult distribution(List<PowerVo> list,String [] deptList, String roleId) {
		//deptId 暂不处理
		Role role = roleMapper.selectByPrimaryKey(roleId);
		LianjiuResult lianjiuResult = new LianjiuResult();
		if(null == role){
			lianjiuResult.setStatus(501);
			lianjiuResult.setLianjiuData("没有这个人员！");
			return lianjiuResult;
		}
		System.out.println("deptList.length:"+deptList.length);
		List<DeptRole> drList = new ArrayList<>();
		for(int i=0;i<deptList.length ; i++){
			DeptRole deptRole = new DeptRole();
			deptRole.setDeptRoleId(IDUtils.genPowerModuleId());
			deptRole.setRoleId(roleId);
			deptRole.setDeptId(deptList[i]);
			drList.add(deptRole);
			System.out.println("     - - - - - - - -deptList[i]:["+deptList[i]+"]");
		}
		System.out.println("list.size():"+drList.size());
		deptRoleMapper.addDRList(drList);
		
		List<RolePower> powerList = new ArrayList<>();
		for(PowerVo powerVo : list ){
			if(null != powerVo.getPitchOn()&&powerVo.getPitchOn().equals("1")){
				RolePower rp = new RolePower();
				rp.setRolePowerId(IDUtils.genPowerModuleId());
				rp.setRoleId(roleId);
				rp.setPowerId(powerVo.getPowerId());
				powerList.add(rp);
			}
		}
		int count = rolePowerMapper.deleteByRoleId(roleId);
		if(list.size()==0){
			lianjiuResult.setStatus(200);
			lianjiuResult.setTotal(count);
			lianjiuResult.setLianjiuData("权限分配成功。(清空所有权限)");
			return lianjiuResult;
		}else{
			rolePowerMapper.addList(powerList);
			lianjiuResult.setStatus(200);
			lianjiuResult.setTotal(powerList.size());
			lianjiuResult.setLianjiuData("权限分配成功。");
			return lianjiuResult;
		}
	}

}
