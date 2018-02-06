package com.lianjiu.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersExcellentRefund;
import com.lianjiu.service.order.OrderExcellentRefundService;

/**
 * 精品订单退款详情
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/orExcellentRefund")
public class OrderExcellentRefundController {

	@Autowired
	private OrderExcellentRefundService orderExcellentRefundService;

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:查询所有退款
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRefund/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getRefundAll() {
		return orderExcellentRefundService.getRefundAll();
	}

	/**
	 * 
	 * XYZ 2017年9月6日 descrption:查询所有退款
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/selectRefoundList", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectRefoundList() {
		return orderExcellentRefundService.selectRefoundList();
	}

	
	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:分页查询所有退款
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRefund/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRefundAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderExcellentRefundService.getRefundAll(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:通过精品订单获取退款
	 * 
	 * @param excellentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRefund/{excellentId}/ByEid/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRefundByExcellentId(@PathVariable String excellentId) {
		if(Util.isEmpty(excellentId)){
			return LianjiuResult.build(401, "请传入正确的excellentId");
		}
		return orderExcellentRefundService.getRefundByExcellentId(excellentId);
	}


	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:根据主键id查询
	 * 
	 * @param refundId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRefund/{refundId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRefundById(@PathVariable String refundId) {
		if(Util.isEmpty(refundId)){
			return LianjiuResult.build(401, "请传入正确的refundId");
		}
		return orderExcellentRefundService.getRefundById(refundId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:添加退款
	 * 
	 * @param refund
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addRefund", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addRefund(OrdersExcellentRefund refund) {
		if(null == refund){
			return LianjiuResult.build(401, "refund对象绑定错误");
		}
		Util.printModelDetails(refund);
		return orderExcellentRefundService.addRefund(refund);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:更新退款
	 * 
	 * @param refund
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyRefund", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateRefund(OrdersExcellentRefund refund) {
		if(null == refund){
			return LianjiuResult.build(401, "refund对象绑定错误");
		}
		return orderExcellentRefundService.updateRefund(refund);
	}
	
	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:确认退款
	 * 
	 * @param refund
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/confirmRefund", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult confirmRefund(OrdersExcellentRefund refund) {
		if(null == refund){
			return LianjiuResult.build(401, "refund对象绑定错误");
		}
		return orderExcellentRefundService.confirmRefund(refund);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:删除退款
	 * 
	 * @param refundId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeRefund/{refundId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteRefund(@PathVariable String refundId) {
		if(Util.isEmpty(refundId)){
			return LianjiuResult.build(401, "请传入正确的refundId");
		}
		return orderExcellentRefundService.deleteRefund(refundId);
	}

}
