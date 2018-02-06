package com.lianjiu.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Power;
import com.lianjiu.model.vo.RoleVo;
import com.lianjiu.service.admin.PowerService;

/**
 * 权限详情
 * 
 * @author huangfu
 *
 */
@Controller
@RequestMapping("/power")
public class PowerController {

	@Autowired
	private PowerService powerService;
	
	/**
	 * 
	 * huangfu 2017年9月14日 descrption:查询所有权限
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getPower/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getPowerAll() {
		return powerService.getPowerAll();
	}
	
	/**
	 * 
	 * huangfu 2017年9月14日 descrption:分页查询所有权限
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getPower/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getPowerAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return powerService.getPowerAll(pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2017年9月7日 descrption:根据主键id查询
	 * 
	 * @param powerId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getPower/{powerId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getPowerById(@PathVariable String powerId) {
		if(Util.isEmpty(powerId)){
			return LianjiuResult.build(401, "请传入正确的powerId");
		}
		return powerService.getPowerById(powerId);
	}

	/**
	 * 
	 * huangfu 2017年9月14日 descrption:添加权限
	 * 
	 * @param power
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addPower", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addPower(Power power) {
		if(null == power){
			return LianjiuResult.build(401, "power对象绑定错误");
		}
		Util.printModelDetails(power);
		return powerService.addPower(power);
	}

	/**
	 * 
	 * huangfu 2017年9月14日 descrption:更新权限
	 * 
	 * @param power
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyPower", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updatePower(Power power) {
		if (null == power) {
			return LianjiuResult.build(401, "传入的对象为空");
		}
		if (Util.isEmpty(power.getPowerId())) {
			return LianjiuResult.build(402, "请指定要权限id");
		}
		return powerService.updatePower(power);
	}

	/**
	 * 
	 * huangfu 2017年9月14日 descrption:删除权限
	 * 
	 * @param powerId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removePower/{powerId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deletePower(@PathVariable String powerId) {
		if(Util.isEmpty(powerId) || powerId.equals("{powerId}")){
			return LianjiuResult.build(401, "请传入正确的powerId");
		}
		return powerService.deletePower(powerId);
	}


	/**
	 * 
	 * huangfu 2017年9月14日 descrption:分配权限
	 * 
	 * @param powerId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/distribution", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult distribution(RoleVo role) {
		if (null == role) {
			return LianjiuResult.build(401, "role对象绑定错误");
		}
		if(null == role.getRole()){
			return LianjiuResult.build(402, "role对象绑定错误");
		}
		if(null == role.getRole().getRoleId()){
			return LianjiuResult.build(403, "role对象绑定错误");
		}
		if(null == role.getDeptList() || role.getDeptList().length==0){
			return LianjiuResult.build(404, "部门id 不能为空");
		}
		return powerService.distribution(role.getList(),role.getDeptList(),role.getRole().getRoleId());
	}

}
