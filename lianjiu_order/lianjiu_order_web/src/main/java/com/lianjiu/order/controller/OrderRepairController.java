package com.lianjiu.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersRepair;
import com.lianjiu.model.OrdersRepairScheme;
import com.lianjiu.order.service.OrderRepairService;

/**
 * 维修手机的订单
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/orderRepair")
public class OrderRepairController {
	@Autowired
	private OrderRepairService orderRepairService;

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前用户的所有维修订单，分页
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepair/{uid}/{status}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRepairByStatus(@PathVariable String uid, @PathVariable Byte status) {
		if (Util.isEmpty(uid)) {
			return LianjiuResult.build(401, "请传入正确的uid");
		}
		if (null == status) {
			return LianjiuResult.build(402, "status绑定错误");
		}
		return orderRepairService.getRepairByUid(uid, status);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前用户的所有维修订单，分页
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepairByStatusArr", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getRepairByStatusArr(String uid, @RequestParam(value = "statusList") List<Byte> status) {
		if (Util.isEmpty(uid)) {
			return LianjiuResult.build(401, "请传入正确的uid");
		}
		if (null == status) {
			return LianjiuResult.build(402, "status绑定错误");
		}
		return orderRepairService.getRepairByUid(uid, status);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:根据主键id查询
	 * 
	 * @param repairId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepair/{repairId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRepairByCid(@PathVariable String repairId) {
		if (Util.isEmpty(repairId)) {
			return LianjiuResult.build(401, "请传入正确的repairId");
		}
		return orderRepairService.getRepairById(repairId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:添加维修订单
	 * 
	 * @param repair
	 * @param adressId
	 * @param scheme
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addRepair(OrdersRepair repair, OrdersRepairScheme scheme) {
		if (null == repair) {
			return LianjiuResult.build(401, "repair对象绑定错误");
		}
		if (null == scheme) {
			return LianjiuResult.build(402, "scheme对象绑定错误");
		}
		// AD1508123552447
		if (Util.isEmpty(repair.getAddressId()) || !repair.getAddressId().matches(GlobalValueUtil.REGEX_USER_ADDRESS)) {
			return LianjiuResult.build(403, "请传入正确的addressId");
		}
		Util.printModelDetails(repair);
		return orderRepairService.addRepair(repair, scheme);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:更新维修订单
	 * 
	 * @param repair
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyRepair", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateRepair(OrdersRepair repair) {
		if (null == repair) {
			return LianjiuResult.build(401, "repair对象绑定错误");
		}
		return orderRepairService.updateRepair(repair);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:删除维修订单
	 * 
	 * @param repairId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeRepair/{repairId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteRepair(@PathVariable String repairId) {
		if (Util.isEmpty(repairId)) {
			return LianjiuResult.build(401, "请传入正确的repairId");
		}
		return orderRepairService.deleteRepair(repairId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月15日 descrption:获取本订单中的所有维修方案
	 * 
	 * @param ordersId
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getItem/{ordersId}/{pageNum}/{pageSize}/ByOrdersId", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getItemByOrdersId(@PathVariable String ordersId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的repairId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderRepairService.getItemByOrdersId(ordersId, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年10月17日 descrption:修改状态
	 * 
	 * @param status
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyStatus", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateRepair(String ordersId, Byte status) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		if (null == status) {
			return LianjiuResult.build(402, "status绑定错误");
		}
		return orderRepairService.updateStatus(ordersId, status);
	}
}
