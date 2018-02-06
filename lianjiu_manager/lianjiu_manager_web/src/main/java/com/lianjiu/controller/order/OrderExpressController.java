package com.lianjiu.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.pojo.MachineCheck;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersExpress;
import com.lianjiu.service.order.OrderExpressService;

/**
 * 快递回收方式的订单
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
	 * zhaoxi 2017年9月5日 descrption:分页查询所有快递回收方式的订单
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getOrder/{pageNum}/{pageSize}/all", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getAllOrderExpress(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderExpressService.getAllOrderExpress(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月5日 descrption:查询所有快递回收方式的订单
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getOrder/all", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getAllOrderExpress() {
		return orderExpressService.getAllOrderExpress();
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前分类的所有精品订单
	 * 
	 * @param Cid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getOrder/{Cid}/ByCid/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getgetOrderByCid(@PathVariable Long Cid) {
		if (null == Cid) {
			return LianjiuResult.build(401, "Cid对象绑定错误");
		}
		return orderExpressService.getgetOrderByCid(Cid);
	}

	/**
	 * 
	 * zhaoxi 2017年11月13日 descrption:订单过滤器
	 * 
	 * @param orders
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/ordersFilter", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult ordersFilter(OrdersExpress orders, @RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(defaultValue = "15") int pageSize) {
		if (null == orders) {
			return LianjiuResult.build(401, "orders对象绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderExpressService.ordersFilter(orders, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前分类的所有精品订单，分页
	 * 
	 * @param Cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getOrder/{Cid}/{pageNum}/{pageSize}/ByCid", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getOrderByCid(@PathVariable Long Cid, @PathVariable int pageNum, @PathVariable int pageSize) {
		if (null == Cid) {
			return LianjiuResult.build(401, "orders对象绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderExpressService.getgetOrderByCid(Cid, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月5日 descrption:通过状态查询所有快递回收方式的订单
	 * 
	 * @param state
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getOrder/{status}/byState", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getAllOrderExpressByStatus(@PathVariable Integer status) {
		if (null == status) {
			return LianjiuResult.build(401, "status对象绑定错误");
		}
		return orderExpressService.getAllOrderExpressByStatus(status);
	}

	/**
	 * 
	 * zhaoxi 2017年9月5日 descrption:通过状态查询所有快递回收方式的订单，分页显示
	 * 
	 * @param state
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getOrder/{status}/{pageNum}/{pageSize}/byState", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getAllOrderExpressByStatus(@PathVariable Integer status, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (null == status) {
			return LianjiuResult.build(401, "status对象绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderExpressService.getAllOrderExpressByStatus(status, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月5日 descrption:获取快递回收方式的订单详情
	 * 
	 * @param detailsId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getOrder/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getOrderById(@PathVariable String orderId) {
		if (Util.isEmpty(orderId)) {
			return LianjiuResult.build(401, "请传入正确的orderId");
		}
		return orderExpressService.getOrderById(orderId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月5日 descrption:修改快递回收方式的订单
	 * 
	 * @param ordersExpress
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "modifyOrder", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyOrder(OrdersExpress ordersExpress) {
		if (null == ordersExpress) {
			return LianjiuResult.build(401, "ordersExpress对象绑定错误");
		}
		return orderExpressService.modifyOrder(ordersExpress);
	}

	/***
	 * 
	 * zhaoxi 2017年9月15日 descrption:分页获取快递订单的详情
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
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderExpressService.getItemByOrdersId(ordersId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月14日 descrption:
	 * 
	 * @param itemsId
	 *            //获取一条订单商品记录
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getItem/{orExpItemId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getOrderItemsById(@PathVariable String orExpItemId) {
		if (Util.isEmpty(orExpItemId)) {
			return LianjiuResult.build(401, "请传入正确的orExpItemId");
		}
		return orderExpressService.getOrderItemsById(orExpItemId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月14日 descrption:根据id获取详情
	 * 
	 * @param orderId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getDetails/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getOrderDetailsById(@PathVariable String orderId) {
		if (Util.isEmpty(orderId)) {
			return LianjiuResult.build(401, "请传入正确的orderId");
		}
		return orderExpressService.getOrderDetailsById(orderId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月15日 descrption:获取用户验机参数
	 * 
	 * @param productId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getParam/{productId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getParamData(@PathVariable String productId) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(501, "请指定要查询的产品id");
		}
		return orderExpressService.getParamData(productId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月15日 descrption:获取一组用户验机参数 ，，，，使用get容易造成中文乱码
	 * 
	 * @param productId
	 * @param paramGroupName
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getParam/productId/majorName", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getParamData(String productId, String majorName) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(501, "请指定要查询的产品id");
		}
		if (Util.isEmpty(majorName)) {
			return LianjiuResult.build(502, "请指定要查询参数名");
		}
		return orderExpressService.getParamData(productId, majorName);
	}

	/**
	 * 
	 * zhaoxi 2017年9月15日 descrption:修改用户验机参数
	 * 
	 * @param orItemsId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "modifyParam", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyParam(String orItemsId, String orItemsParam, MachineCheck machineCheck) {
		if (Util.isEmpty(orItemsId)) {
			return LianjiuResult.build(401, "请传入正确的orItemsId");
		}
		if (Util.isEmpty(orItemsParam)) {
			return LianjiuResult.build(402, "请传入正确的orItemsParam");
		}
		if (null == machineCheck) {
			return LianjiuResult.build(403, "machineCheck对象绑定错误");
		}
		return orderExpressService.modifyParam(orItemsId, orItemsParam, machineCheck);
	}

	/**
	 * 
	 * zhaoxi 2017年11月20日 descrption:验机结束
	 * 
	 * @param orItemsId
	 * @param isModify
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "checkFinish", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult checkFinish(String ordersId, Integer isModify) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		if (null == isModify) {
			return LianjiuResult.build(402, "isModify对象绑定错误");
		}
		return orderExpressService.checkFinish(ordersId, isModify);
	}

	/**
	 * 
	 * zhaoxi 2017年9月16日 descrption:付款给用户
	 * 
	 * @param ordersId
	 * @param ordersStatus
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "payForOrders", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult payForOrders(String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(402, "请传入正确的ordersId");
		}
		return orderExpressService.payForOrders(ordersId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月16日 descrption:修改订单状态
	 * 
	 * @param ordersId
	 * @param ordersStatus
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "modifyOrderStatus", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyOrderStatus(String ordersId, Byte ordersStatus) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		if (null == ordersStatus) {
			return LianjiuResult.build(402, "请传入正确的ordersStatus");
		}
		return orderExpressService.modifyOrderStatus(ordersId, ordersStatus);
	}

	/**
	 * 
	 * zhaoxi 2017年9月16日 descrption:修改运单编号
	 * 
	 * @param ordersId
	 * @param ordersExpressNum
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "modifyOrderExpressNum", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyOrderExpressNum(OrdersExpress ordersExpress) {
		if (null == ordersExpress) {
			return LianjiuResult.build(401, "请传入正确的ordersExpress");
		}
		if (Util.isEmpty(ordersExpress.getOrExpressNumCancel())) {
			return LianjiuResult.build(402, "请传入正确的orExpressNumCancel");
		}
		return orderExpressService.modifyOrderExpressNum(ordersExpress);
	}
	
	/**
	 * 
	 * huangfu 2017年1月9日 descrption:维修订单模糊查询
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/vagueQuery", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult vagueQuery(OrdersExpress ordersExpress, int pageNum,int pageSize,String cratedStart,String cratedOver) {
//		if(null == ordersRepair){
//			return LianjiuResult.build(401, "ordersRepair对象绑定错误");
//		}
		System.out.println("OrdersExpress getOrExpressUserId："+ordersExpress.getOrExpressUserId());
		System.out.println("OrdersExpress getOrExcellentId："+ordersExpress.getOrExpressId());
		System.out.println("OrdersExpress getOrExpressStatus："+ordersExpress.getOrExpressStatus());
		System.out.println("cratedStart："+cratedStart);
		System.out.println("cratedOver："+cratedOver);
		System.out.println("pageNum："+pageNum);
		System.out.println("pageSize："+pageSize);
		
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderExpressService.vagueQuery(ordersExpress, pageNum,pageSize,cratedStart,cratedOver);
	}
}
