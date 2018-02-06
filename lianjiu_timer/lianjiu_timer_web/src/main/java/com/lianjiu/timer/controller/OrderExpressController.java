package com.lianjiu.timer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.timer.service.OrderExpressService;

/**
 * 上门回收订单定时器
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/orderExpress")
public class OrderExpressController {

	@Autowired
	private OrderExpressService orderExpressService;

	/**
	 * 
	 * zhaoxi 2017年11月20日 descrption:确认价格超时
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/orderConfirm/{ordersId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult orderConfirm(@PathVariable String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderExpressService.orderConfirm(ordersId);
	}
	
	/**
	 * 
	 * zhaoxi 2017年11月20日 descrption:确认价格超时，取消倒计时
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/orderConfirmDel/{ordersId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult orderConfirmDel(@PathVariable String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderExpressService.orderConfirmDel(ordersId);
	}
}
