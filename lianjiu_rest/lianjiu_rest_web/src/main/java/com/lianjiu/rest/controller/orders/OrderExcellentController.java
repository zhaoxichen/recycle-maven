package com.lianjiu.rest.controller.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.rest.model.ExcellentItemVo;
import com.lianjiu.rest.service.orders.OrderExcellentService;

/**
 * 精品订单 请求转发到订单系统
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
	 * zhaoxi 2017年9月6日 descrption:分页查询所有精品订单,请求转发到订单系统
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		/*
		 * return HttpClientUtil.doGet( GlobalValueUtil.ORDER_BASE_URL +
		 * "ordersExcellent/getExcellent/" + pageNum + "/" + pageSize + "/All");
		 */
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderExcellentService.getExcellentAll(pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2017年10月18日 descrption:查询所有精品订单（根据用户id）
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{userId}/{pageNum}/{pageSize}/ByUserId", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentByUserId(@PathVariable String userId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "用户id不能为空");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderExcellentService.getExcellentByUserId(userId, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:通过状态分页查询所有精品订单
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{orExcellentStatus}/{pageNum}/{pageSize}/ByStatus", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentAll(@PathVariable Byte orExcellentStatus, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderExcellentService.findAllByStatus(orExcellentStatus, pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2017年10月18日 descrption:通过用户id与状态查询所有精品订单
	 * 
	 * @param state
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{orExcellentStatus}/{userId}/{pageNum}/{pageSize}/ByUserStatus", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentByUserStatus(@PathVariable Byte orExcellentStatus, @PathVariable String userId,
			@PathVariable int pageNum, @PathVariable int pageSize) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "用户id不能为空");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderExcellentService.getExcellentByUserStatus(orExcellentStatus, userId, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前分类的所有精品订单
	 * 
	 * @param Cid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{Cid}/ByCid/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentByCid(@PathVariable Long Cid) {
		if (null == Cid) {
			return LianjiuResult.build(401, "Cid不能为空");
		}
		return orderExcellentService.getByExcellentList(Cid);
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
	@RequestMapping(value = "/getExcellent/{Cid}/{pageNum}/{pageSize}/ByCid", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentByCid(@PathVariable Long Cid, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (null == Cid) {
			return LianjiuResult.build(401, "Cid不能为空");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderExcellentService.getByExcellentList(Cid, pageNum, pageNum);
	}

	/**
	 * 
	 * huangfu 2017年9月25日 descrption:根据主键id查询
	 * 
	 * @param excellentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{excellentId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentByCid(@PathVariable String excellentId) {
		if (Util.isEmpty(excellentId)) {
			return LianjiuResult.build(401, "请传入订单id");
		}
		return orderExcellentService.getExcellentById(excellentId);
	}

	/**
	 * 
	 * huangfu 2017年9月25日 descrption:添加精品订单
	 * 
	 * @param excellent
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/submitExcellent", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addExcellent(OrdersExcellent excellent) {
		if (null == excellent) {
			return LianjiuResult.build(401, "excellent不能为空");
		}
		return orderExcellentService.addExcellent(excellent);
	}

	/**
	 * 
	 * huangfu 2017年9月25日 descrption:添加精品订单 and 商品记录
	 * 
	 * @param excellent
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/submitExcellentOrdersItem", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addExcellentOrdersItem(ExcellentItemVo excellentItemVo) {
		if (null == excellentItemVo) {
			return LianjiuResult.build(401, "excellentItemVo不能为空");
		}
		return orderExcellentService.addExcellentOrdersItem(excellentItemVo);
	}

	/**
	 * 
	 * huangfu 2017年9月25日 descrption:更新精品订单
	 * 
	 * @param excellent
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyExcellent", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateExcellent(OrdersExcellent excellent) {
		if (null == excellent) {
			return LianjiuResult.build(401, "excellent不能为空");
		}
		if (Util.isEmpty(excellent.getOrExcellentId())) {
			return LianjiuResult.build(402, "订单id不能为空");
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
			return LianjiuResult.build(401, "订单id不能为空");
		}
		return orderExcellentService.deleteExcellent(excellentId);
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
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "订单id不能为空");
		}
		return orderExcellentService.selectAddressDefault(userId);
	}
}
