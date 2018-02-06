package com.lianjiu.timer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.timer.service.OrderFacefaceService;

/**
 * 上门回收订单定时器
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/orderFaceface")
public class OrderFaceFaceController {

	@Autowired
	private OrderFacefaceService orderFacefaceService;

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
		return orderFacefaceService.orderConfirm(ordersId);
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
		return orderFacefaceService.orderConfirmDel(ordersId);
	}

	/**
	 * 
	 * zhaoxi 2017年11月23日 descrption:自动派单
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/orderDispatch/{ordersId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult orderDispatch(@PathVariable String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderFacefaceService.orderDispatch(ordersId);
	}
	
	/**
	 * 
	 * zhaoxi 2017年11月23日 descrption:自动派单
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/orderDispatchDel/{ordersId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult orderDispatchDel(@PathVariable String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderFacefaceService.orderDispatchDel(ordersId);
	}

	/**
	 * 
	 * zhaoxi 2017年11月28日 descrption:夜猫订单
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/orderNightDispatch/{ordersId}/{price}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult orderNightDispatch(@PathVariable String ordersId, @PathVariable String price) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		if (Util.isEmpty(price)) {
			return LianjiuResult.build(401, "请传入正确的price");
		}
		return orderFacefaceService.orderNightDispatch(ordersId, price);
	}
}
