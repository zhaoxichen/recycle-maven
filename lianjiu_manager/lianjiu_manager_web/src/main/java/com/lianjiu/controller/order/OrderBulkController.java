package com.lianjiu.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersBulk;
import com.lianjiu.model.OrdersBulkItem;
import com.lianjiu.service.order.OrdersBulkService;

/**
 * 大宗回收订单
 * 
 * @author hongda
 *
 */
@Controller
@RequestMapping("/ordersBulk")
public class OrderBulkController {

	@Autowired
	private OrdersBulkService ordersBulkService;

	/**
	 * 
	 * hongda 2018年1月8日 descrption:分页获取大宗回收订单
	 * 
	 * @param cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getOrders", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getOrders(Long cid, int pageNum, int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return ordersBulkService.getOrders(pageNum, pageSize);
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
	public LianjiuResult cancelOrders(String orBulkId) {
		if (Util.isEmpty(orBulkId)) {
			return LianjiuResult.build(401, "请传入正确的orBulkId");
		}
		return ordersBulkService.cancelOrders(orBulkId);
	}

	/**
	 * 
	 * hongda 2018年1月8日 descrption:根据ID查订单详情
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getOrdersItem", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getOrdersItem(String orBulkId) {
		if (Util.isEmpty(orBulkId)) {
			return LianjiuResult.build(401, "请传入正确的orBulkId");
		}
		return ordersBulkService.getOrdersItem(orBulkId);
	}

	/**
	 * 
	 * hongda 2018年1月8日 descrption:修改订单详情
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modiFyOrdersItem", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modiFyOrdersItem(OrdersBulkItem item) {
		if (Util.isEmpty(item.getBulkItemId())) {
			return LianjiuResult.build(401, "请传入正确的bulkItemId");
		}
		if (Util.isEmpty(item.getOrdersId())) {
			return LianjiuResult.build(402, "请传入订单id");
		}
		if (Util.isEmpty(item.getBulkItemNumModify())) {
			return LianjiuResult.build(403, "请传入要修改的数量");
		}
		if (false == Util.isNumeric(item.getBulkItemNumModify())) {
			return LianjiuResult.build(408, "请传入正确的修改数量");
		}
		if (null == item.getBulkItemPriceCurrent()) {
			return LianjiuResult.build(405, "请传入最新单价");
		}
		if (false == Util.isNumeric(item.getBulkItemPriceCurrent())) {
			return LianjiuResult.build(408, "请传入正确的最新单价");
		}
		if (null == item.getBulkItemCreated()) {
			return LianjiuResult.build(406, "请传入时间");
		}
		return ordersBulkService.modiFyOrdersItem(item);
	}

	/**
	 * 
	 * zhaoxi 2018年1月17日 descrption:修改完毕
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modiFyOrdersFinish", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modiFyOrdersFinish(OrdersBulk ordersBulk) {

		if (null == ordersBulk) {
			return LianjiuResult.build(401, "对象绑定错误");
		}
		if (Util.isEmpty(ordersBulk.getOrBulkId())) {
			return LianjiuResult.build(402, "请传入订单id");
		}
		if (Util.isEmpty(ordersBulk.getUserId())) {
			return LianjiuResult.build(403, "请传入userId");
		}
		if (Util.isEmpty(ordersBulk.getOrdersRetrPrice())) {
			return LianjiuResult.build(405, "请传入订单回收价格");
		}
		if (Util.isEmpty(ordersBulk.getOrBulkHandlerId())) {
			/**
			 * 操作人id,后续需要前端传入
			 */
			ordersBulk.setOrBulkHandlerId("orBulkHandlerId");
		}
		if (Util.isEmpty(ordersBulk.getOrBulkHandlerTel())) {
			/**
			 * 操作人手机号,后续需要前端传入
			 */
			ordersBulk.setOrBulkHandlerTel("10086");
		}
		return ordersBulkService.modiFyOrdersFinish(ordersBulk);
	}

	/**
	 * 
	 * hongda 2018年1月17日 descrption:多条件搜索
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/searchOrders", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult searchOrders(String parameter, int pageNum, int pageSize) {
		if (Util.isEmpty(parameter)) {
			return LianjiuResult.build(401, "请传入订单id");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return ordersBulkService.searchOrders(parameter, pageNum, pageSize);
	}

	/**
	 * 
	 * hongda 2018年1月8日 descrption:根据ID查订单详情
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getOrdersById", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getOrdersById(String orBulkId) {
		if (Util.isEmpty(orBulkId)) {
			return LianjiuResult.build(401, "请传入正确的orBulkId");
		}
		return ordersBulkService.getOrdersById(orBulkId);
	}

}
