package com.lianjiu.order.controller;

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
import com.lianjiu.model.OrdersBulk;
import com.lianjiu.order.service.OrderBulkService;

/**
 * 大宗回收订单
 * 
 * @author hongda
 *
 */
@Controller
@RequestMapping("/orderBulk")
public class OrderBulkController {

	@Autowired
	private OrderBulkService orderBulkService;

	/**
	 * 
	 * zhaoxi 2018年1月15日 descrption:获取订单提交的视图
	 * 
	 * @param TAKEN
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/ordersPrice", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult ordersPrice(String TAKEN) {

		if (Util.isEmpty(TAKEN)) {
			return LianjiuResult.build(401, "请传入正确的TAKEN");
		}

		return orderBulkService.getOrdersPrice(TAKEN);

	}
	/**
	 * 
	 * zhaoxi 2018年1月15日 descrption:获取订单提交的视图
	 * 
	 * @param TAKEN
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/ordersPreview", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult ordersPreview(String TAKEN) {

		if (Util.isEmpty(TAKEN)) {
			return LianjiuResult.build(401, "请传入正确的TAKEN");
		}

		return orderBulkService.getOrdersPreview(TAKEN);

	}

	/**
	 * 
	 * hongda 2018年1月8日 descrption:分页获取大宗回收订单
	 * 
	 * @param cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addOrders(String TAKEN, OrdersBulk ordersBulk) {
		if (null == ordersBulk) {
			return LianjiuResult.build(401, "ordersBulk对象绑定错误");
		}
		if (Util.isEmpty(TAKEN)) {
			return LianjiuResult.build(402, "请传入正确的TAKEN");
		}
		if (Util.isEmpty(ordersBulk.getUserId()) || !ordersBulk.getUserId().matches(GlobalValueUtil.REGEX_USER_ID)) {
			return LianjiuResult.build(403, "请传入正确的userId");
		}
		if (Util.isEmpty(ordersBulk.getUserName())) {
			return LianjiuResult.build(405, "请传入正确的姓名");
		}
		if (Util.isEmpty(ordersBulk.getUserPhone())
				|| !ordersBulk.getUserPhone().matches(GlobalValueUtil.REGEX_PHONE)) {
			return LianjiuResult.build(406, "请传入正确的手机号");
		}

		return orderBulkService.submit(TAKEN, ordersBulk);

	}

	/**
	 * 
	 * zhaoxi 2018年1月13日 descrption:获取订单
	 * 
	 * @param status
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getOrdersByUserStatus", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getOrdersByUserStatus(@RequestParam(defaultValue = "0") Integer status, String userId,
			@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "15") int pageSize) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "用户id不能为空");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderBulkService.getOrdersByUserStatus(status, userId, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2018年1月16日 descrption:确认订单
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/agree", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult agreeOrders(String ordersId) {

		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的订单编号");
		}

		return orderBulkService.agreeOrders(ordersId);

	}

	/**
	 * 
	 * hongda 2018年1月8日 descrption:取消订单
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult cancelOrders(String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderBulkService.cancelOrders(ordersId);
	}

	/**
	 * 
	 * zhaoxi 2018年1月16日 descrption:获取订单详情
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/ordersDetails/{ordersId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult ordersDetails(@PathVariable String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderBulkService.ordersDetails(ordersId);
	}
}
