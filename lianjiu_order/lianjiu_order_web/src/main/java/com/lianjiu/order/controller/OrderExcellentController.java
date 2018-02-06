package com.lianjiu.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersExcellentRefund;
import com.lianjiu.order.service.OrderExcellentService;
import com.lianjiu.rest.model.OrdersExcellentVo;

/**
 * 精品订单
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/ordersExcellent")
public class OrderExcellentController {

	@Autowired
	private OrderExcellentService orderExcellentService;

	/**
	 * 
	 * zhaoxi 2017年10月19日 descrption:添加精品订单
	 * 
	 * @param ordersExcellentVo
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addOrdersExcellent(OrdersExcellentVo ordersExcellentVo) {
		if (null == ordersExcellentVo) {
			return LianjiuResult.build(401, "传入的对象为空");
		}
		if (null == ordersExcellentVo.getOrdersExcellent()) {
			return LianjiuResult.build(402, "请传入userid和收货地址id");
		}
		return orderExcellentService.addOrdersExcellent(ordersExcellentVo);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:更新精品订单
	 * 
	 * @param excellent
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyExcellent", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateExcellent(OrdersExcellent excellent) {
		if (null == excellent) {
			return LianjiuResult.build(401, "excellent对象绑定错误");
		}
		return orderExcellentService.updateExcellent(excellent);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:删除精品订单
	 * 
	 * @param excellentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeExcellent/{excellentId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteExcellent(@PathVariable String excellentId) {
		if (Util.isEmpty(excellentId)) {
			return LianjiuResult.build(401, "请传入正确的excellentId");
		}
		return orderExcellentService.deleteExcellent(excellentId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月20日 descrption:修改精品订单的状态
	 * 
	 * @param excellentId
	 * @param orExcellentStatus
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyExcellentState", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyExcellent(String excellentId, Byte orExcellentStatus) {
		if (Util.isEmpty(excellentId)) {
			return LianjiuResult.build(401, "请传入正确的excellentId");
		}
		if (null == orExcellentStatus) {
			return LianjiuResult.build(402, "请传入正确的orExcellentStatus");
		}
		return orderExcellentService.modifyExcellentState(excellentId, orExcellentStatus);
	}

	/**
	 * 
	 * zhaoxi 2017年9月20日 descrption:设置处理订单时间
	 * 
	 * @param excellentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyExcellentHandleTime/{excellentId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyExcellentHandleTime(@PathVariable String excellentId) {
		if (Util.isEmpty(excellentId)) {
			return LianjiuResult.build(401, "请传入正确的excellentId");
		}
		return orderExcellentService.modifyExcellentHandleTime(excellentId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月26日 descrption:确认订单，返回当前用户的默认地址，返回链旧钱包的余额
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/selectAddressDefault/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectAddressDefault(@PathVariable String userId) {
		// LJ1509693019317
		if (Util.isEmpty(userId) || !userId.matches(GlobalValueUtil.REGEX_USER_ID)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return orderExcellentService.selectAddressDefault(userId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月20日 descrption:付款，修改精品订单的状态
	 * 
	 * @param excellentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/ordersExcellentPay", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult ordersExcellentPay(String excellentId) {
		if (Util.isEmpty(excellentId)) {
			return LianjiuResult.build(401, "请传入正确的订单id");
		}
		Byte orExcellentStatus = GlobalValueUtil.ORDER_STATUS_DELIVERY_YES;
		return orderExcellentService.modifyExcellentState(excellentId, orExcellentStatus);
	}

	/**
	 * 
	 * zhaoxi 2017年10月9日 descrption:展开订单
	 * 
	 * @param odersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/orderShow/{odersId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult ordersExcellentShow(@PathVariable String odersId) {
		if (Util.isEmpty(odersId)) {
			return LianjiuResult.build(401, "请传入正确的订单id");
		}
		return orderExcellentService.getExcellentItems(odersId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月30日 descrption:30分钟不付款，取消订单
	 * 
	 * @param excellentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/ordersExcellentCancel", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult ordersExcellentCancel(String excellentId) {
		if (Util.isEmpty(excellentId)) {
			return LianjiuResult.build(401, "请传入正确的订单id");
		}
		return orderExcellentService.modifyExcellentState(excellentId, GlobalValueUtil.ORDER_STATUS_CANCEL);
	}

	/**
	 * 
	 * zhaoxi 2017年9月30日 descrption:仅退款
	 * 
	 * @param refund
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/ordersExcellentRefund", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult ordersExcellentRefund(OrdersExcellentRefund refund) {
		if (null == refund) {
			return LianjiuResult.build(401, "传入的对象为空");
		}
		if (Util.isEmpty(refund.getOrExcellentId())) {
			return LianjiuResult.build(402, "请传入正确的订单id");
		}
		refund.setOrExceRefundExpress("");
		refund.setOrExceRefundExpressnum("");
		refund.setOrExceRefundType((byte) 0);// 退款类型
		Byte ordersStatus = GlobalValueUtil.ORDER_STATUS_REFUND;// 仅退款
		return orderExcellentService.addRefund(refund, ordersStatus);
	}

	/**
	 * 
	 * zhaoxi 2017年9月30日 descrption:退款退款
	 * 
	 * @param refund
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/ordersExcellentRefundGoods", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult ordersExcellentRefundGoods(OrdersExcellentRefund refund) {
		if (null == refund) {
			return LianjiuResult.build(401, "传入的对象为空");
		}
		if (Util.isEmpty(refund.getOrExcellentId())) {
			return LianjiuResult.build(402, "请传入正确的订单id");
		}
		refund.setOrExceRefundType((byte) 1);// 退款类型
		refund.setOrExceProductStatus((byte) 1);// 必然收到货了
		Byte ordersStatus = GlobalValueUtil.ORDER_STATUS_REFUND_GOODS;// 退货退款
		return orderExcellentService.addRefund(refund, ordersStatus);
	}

	/**
	 * 
	 * zhaoxi 2017年9月30日 descrption:输入快递订单号
	 * 
	 * @param orExcellentId
	 * @param refundExpress
	 * @param refundExpressNum
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/ordersExcellentRefundExp", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult ordersExcellentRefundExpress(String orExcellentId, String refundExpress,
			String refundExpressNum) {
		if (Util.isEmpty(orExcellentId)) {
			return LianjiuResult.build(401, "请传入正确的订单id");
		}
		if (Util.isEmpty(refundExpress)) {
			return LianjiuResult.build(402, "请传入正确的快递名");
		}
		if (Util.isEmpty(refundExpressNum)) {
			return LianjiuResult.build(403, "请传入正确的快递号");
		}
		return orderExcellentService.modifyRefundByOrdersId(orExcellentId, refundExpress, refundExpressNum);
	}

	/**
	 * 
	 * zhaoxi 2017年10月19日 descrption:确认收货,订单进入待评价状态
	 * 
	 * @param orExcellentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/finish", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult ordersExcellentFinish(String orExcellentId) {
		if (Util.isEmpty(orExcellentId)) {
			return LianjiuResult.build(401, "请传入正确的订单id");
		}
		return orderExcellentService.modifyExcellentState(orExcellentId, GlobalValueUtil.ORDER_STATUS_EVALUATE_NO);
	}

	/**
	 * 
	 * zhaoxi 2017年11月15日 descrption:
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getOneComment/{ordersId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getOneComment(@PathVariable String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的订单id");
		}
		return orderExcellentService.getOneComment(ordersId);
	}

	/**
	 * 
	 * zhaoxi 2017年11月15日 descrption:获取当前订单剩余支付时间
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "timeRemaining/{ordersId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult timeRemaining(@PathVariable String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的订单id");
		}
		return orderExcellentService.getTimeRemaining(ordersId);
	}
}
