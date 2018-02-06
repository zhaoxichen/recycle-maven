package com.lianjiu.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.service.order.OrderFurnitureService;

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
	 * zhaoxi 2018年1月8日 descrption:分页获取家具订单
	 * 
	 * @param cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getOrders", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getOrders(Long cid, int pageNum, int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderFurnitureService.getOrders(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2018年1月8日 descrption:取消订单
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/cancel/{ordersId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult cancelOrders(@PathVariable String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderFurnitureService.cancelOrders(ordersId);
	}
}
