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
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersExpress;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.order.service.OrderExpressService;

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
	 * zhaoxi 2017年10月30日 descrption:结算
	 * 
	 * @param productIdlist
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/productBalance", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult productBalance(@RequestParam("itemIdList") List<String> itemIdList, String userId) {
		if(null == itemIdList ){
			return LianjiuResult.build(401, "请传入正确的itemIdList");
		}
		if(itemIdList.size()==0){
			return LianjiuResult.build(402, "请传入正确的itemIdList");
		}
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(403, "请传入正确的userId");
		}
		return orderExpressService.productBalance(itemIdList, userId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月31日 descrption:取结算之后的数据
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/balanceAfter/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult productBalanceAfter(@PathVariable String userId) {
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return orderExpressService.productBalanceAfter(userId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月31日 descrption:在回收车中删除
	 * 
	 * @param itemId
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/reduceProductFromCar", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult reduceFromExpressCar(String itemId, String userId) {
		if(Util.isEmpty(itemId)){
			return LianjiuResult.build(401, "请传入正确的itemId");
		}
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(402, "请传入正确的userId");
		}
		return orderExpressService.reduceFromExpressCar(itemId, userId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月31日 descrption:
	 * 
	 * @param ordersExpress
	 * @param orItemsBuyway
	 * @param checkCode
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult submit(OrdersExpress ordersExpress, String orItemsBuyway, String checkCode) {
		if(Util.isEmpty(orItemsBuyway)){
			return LianjiuResult.build(401, "请传入正确的orItemsBuyway");
		}
		if(Util.isEmpty(checkCode)){
			return LianjiuResult.build(402, "请传入正确的checkCode");
		}
		if(null == ordersExpress){
			return LianjiuResult.build(403, "ordersExpress对象绑定错误");
		}
		Util.printModelDetails(ordersExpress);
		return orderExpressService.submit(ordersExpress, orItemsBuyway, checkCode);
	}

	/**
	 * 
	 * zhaoxi 2017年9月5日 descrption:获取快递回收方式的订单详情
	 * 
	 * @param orderId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getOrder/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getOrderById(@PathVariable String orderId) {
		if(Util.isEmpty(orderId)){
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
		if(null == ordersExpress){
			return LianjiuResult.build(401, "ordersExpress对象绑定错误");
		}
		return orderExpressService.modifyOrder(ordersExpress);
	}

	
	/**
	 * 
	 * zhaoxi 2017年9月14日 descrption:
	 * 
	 * @param orExpItemId
	 *            //获取一条订单商品记录
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getItem/{orExpItemId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getOrderItemsById(@PathVariable String orExpItemId) {
		if(Util.isEmpty(orExpItemId)){
			return LianjiuResult.build(401, "请传入正确的orExpItemId");
		}
		return orderExpressService.getOrderItemsById(orExpItemId);
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
		if(Util.isEmpty(productId)){
			return LianjiuResult.build(401, "请传入正确的productId");
		}
		return orderExpressService.getParamData(productId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月15日 descrption:获取一组用户验机参数 ，，，，使用get容易造成中文乱码
	 * 
	 * @param productId
	 * @param majorName
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getParam/productId/majorName", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getParamData(String productId, String majorName) {
		if(Util.isEmpty(productId)){
			return LianjiuResult.build(401, "请传入正确的productId");
		}
		if(Util.isEmpty(majorName)){
			return LianjiuResult.build(402, "请传入正确的majorName");
		}
		return orderExpressService.getParamData(productId, majorName);
	}

	/**
	 * 
	 * zhaoxi 2017年9月15日 descrption:修改用户验机参数
	 * 
	 * @param orItemsId
	 * @param orItemsParamModify
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "modifyParam", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyParam(String orItemsId, String orItemsParamModify) {
		if(Util.isEmpty(orItemsId)){
			return LianjiuResult.build(401, "请传入正确的orItemsId");
		}
		if(Util.isEmpty(orItemsParamModify)){
			return LianjiuResult.build(402, "请传入正确的orItemsParamModify");
		}
		return orderExpressService.modifyParam(orItemsId, orItemsParamModify);
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
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		if(null == ordersStatus){
			return LianjiuResult.build(402, "请传入正确的ordersStatus");
		}
		return orderExpressService.modifyOrderStatus(ordersId, ordersStatus);
	}

	/**
	 * 
	 * zhaoxi 2017年9月16日 descrption:修改运单编号
	 * 
	 * @param ordersId
	 * @param ordersStatus
	 * @param ordersExpressNum
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "modifyOrderExpressNum", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyOrderExpressNum(String ordersId, Byte ordersStatus, String ordersExpressNum) {
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		if(null == ordersStatus){
			return LianjiuResult.build(402, "请传入正确的ordersStatus");
		}
		if(Util.isEmpty(ordersExpressNum)){
			return LianjiuResult.build(403, "请传入正确的ordersExpressNum");
		}
		return orderExpressService.modifyOrderExpressNum(ordersId, ordersStatus, ordersExpressNum);
	}

	/**
	 * 
	 * zxy 2017年10月18日 descrption:添加快递回收订单
	 * 
	 * @param ordersExpress
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addExpresssNum", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addExpresssNum(OrdersExpress ordersExpress) {
		if(null == ordersExpress){
			return LianjiuResult.build(401, "请传入正确的ordersExpress");
		}
		String expressNum = ordersExpress.getOrExpressNum();
		Util.printModelDetails(ordersExpress);
		if(Util.isExpressNum(expressNum) == true){
			return orderExpressService.addExpresssNum(ordersExpress);
		}
		else{
			return LianjiuResult.build(402, "请填写正确的快递单号");
		}
	}
	/**
	 * 
	 * zxy 2017年10月20日 descrption:查询所有我的快递订单
	 * 
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExpressList/{userId}/{pageNum}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExpressList(@PathVariable String userId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if(1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderExpressService.getExpressList(userId, pageNum, pageSize);
	}

	/**
	 * 
	 * zxy 2017年10月20日 descrption:各种订单运行状态查询
	 * 
	 * @param userId
	 * @param type
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExpressStutsList/{userId}/{type}/{pageNum}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExpressStutsList(@PathVariable String userId, @PathVariable int type,
			@PathVariable int pageNum, @PathVariable int pageSize) {
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if(1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderExpressService.getExpressStutsList(userId, type, pageNum, pageSize);
	}

	/**
	 * 
	 * zxy 2017年10月21日 descrption:确认价格 修改订单状态为待结算
	 * 
	 * @param ordersExpress
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/ModifyExpressOrderStatus", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult ModifyExpressOrderStatus(OrdersExpress ordersExpress) {
		if(null == ordersExpress){
			return LianjiuResult.build(401, "请传入正确的ordersExpress");
		}
		return orderExpressService.ModifyExpressOrderStatus(ordersExpress);
	}

	/**
	 * 
	 * zhaoxi 2017年11月14日 descrption:确认价格，同意价格
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/priceConfirm", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult orderPriceConfirm(String ordersId) {
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderExpressService.orderPriceConfirm(ordersId);
	}

	/**
	 * 
	 * zhaoxi 2017年11月14日 descrption:不卖了，拒绝价格
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/priceRefuse", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult orderPriceRefuse(String ordersId) {
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderExpressService.orderPriceRefuse(ordersId);
	}

	/**
	 * 
	 * zhaoxi 2017年11月14日 descrption:取消订单
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult orderCancel(String ordersId) {
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderExpressService.orderCancel(ordersId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月31日 descrption:直接上门回收
	 * 
	 * @param productItem
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/directExpressSale", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult directExpressSale(OrdersItem productItem, String userId) {
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if(null == productItem){
			return LianjiuResult.build(402, "productItem对象绑定错误");
		}
		return orderExpressService.directExpressSale(productItem, userId);
	}

	/**
	 * 
	 * zhaoxi 2017年11月22日 descrption:质检明细
	 * 
	 * @param orItemsId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/qualityCheckingDetails/{orItemsId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult qualityCheckingDetails(@PathVariable String orItemsId) {
		if(Util.isEmpty(orItemsId)){
			return LianjiuResult.build(401, "请传入正确的orItemsId");
		}
		return orderExpressService.qualityCheckingDetails(orItemsId);
	}
	
	/**
	 * 
	 * zxy 2017年10月12日 descrption:查询订单中所有产品详情页
	 * 
	 * @param orFacefaceId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/selectOrderDetails/{ordersId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectOrderDetails(@PathVariable String ordersId) {
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderExpressService.selectExpressItemByOrderId(ordersId);
	}
}
