package com.lianjiu.timer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.timer.service.OrderFurnitureService;

/**
 * 家具处理
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/orderFurniture")
public class OrderFurnitureController {

	@Autowired
	private OrderFurnitureService orderFurnitureService;

	/**
	 * 
	 * zhaoxi 2018年1月22日 descrption:30分钟不付款，取消订单
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/orderCancel/{ordersId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult orderCancel(@PathVariable String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderFurnitureService.orderCancel(ordersId);
	}
}
