package com.lianjiu.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersRepairScheme;
import com.lianjiu.service.order.OrderRepairSchemeService;

/**
 * 维修手机的方案之维修方案
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/orderScheme")
public class OrderRepairSchemeController {
	@Autowired
	private OrderRepairSchemeService orderRepairSchemeService;

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:查询所有维修方案
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getScheme/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getSchemeAll() {
		return orderRepairSchemeService.getSchemeAll();
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:分页查询所有维修方案
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getScheme/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getSchemeAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderRepairSchemeService.getSchemeAll(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前订单的维修方案
	 * 
	 * @param Cid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getScheme/{orRepairId}/ByOid", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getSchemeByOid(@PathVariable String orRepairId) {
		if(Util.isEmpty(orRepairId)){
			return LianjiuResult.build(401, "请传入正确的orRepairId");
		}
		return orderRepairSchemeService.getSchemeByOid(orRepairId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:根据主键id查询
	 * 
	 * @param schemeId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getScheme/{schemeId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getSchemeByCid(@PathVariable String schemeId) {
		if(Util.isEmpty(schemeId)){
			return LianjiuResult.build(401, "请传入正确的schemeId");
		}
		return orderRepairSchemeService.getSchemeById(schemeId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:添加维修方案
	 * 
	 * @param scheme
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addScheme", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addScheme(OrdersRepairScheme scheme) {
		if(null == scheme){
			return LianjiuResult.build(401, "scheme对象绑定错误");
		}
		Util.printModelDetails(scheme);
		return orderRepairSchemeService.addScheme(scheme);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:更新维修方案
	 * 
	 * @param scheme
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyScheme", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateScheme(OrdersRepairScheme scheme) {
		if(null == scheme){
			return LianjiuResult.build(401, "scheme对象绑定错误");
		}
		return orderRepairSchemeService.updateScheme(scheme);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:删除维修方案
	 * 
	 * @param schemeId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeScheme/{schemeId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteScheme(@PathVariable String schemeId) {
		if(Util.isEmpty(schemeId)){
			return LianjiuResult.build(401, "请传入正确的schemeId");
		}
		return orderRepairSchemeService.deleteScheme(schemeId);
	}
}
