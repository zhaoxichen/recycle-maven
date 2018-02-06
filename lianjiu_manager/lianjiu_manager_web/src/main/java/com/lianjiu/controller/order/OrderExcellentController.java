package com.lianjiu.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.service.order.OrderExcellentService;

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
	 * zhaoxi 2017年9月6日 descrption:查询所有精品订单
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getExcellentAll() {
		return orderExcellentService.getExcellentAll();
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:分页查询所有精品订单
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentAll(@PathVariable int pageNum, @PathVariable int pageSize) {
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
	 * zhaoxi 2017年9月7日 descrption:获取当前分类的所有精品订单
	 * 
	 * @param Cid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{Cid}/ByCid/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getExcellentByCid(@PathVariable Long Cid) {
		if(null == Cid){
			return LianjiuResult.build(401, "Cid对象绑定错误");
		}
		return orderExcellentService.getExcellentByCid(Cid);
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
	public LianjiuResult ordersFilter(OrdersExcellent orders, @RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(defaultValue = "15") int pageSize) {
		if(null == orders){
			return LianjiuResult.build(401, "orders对象绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderExcellentService.ordersFilter(orders, pageNum, pageSize);
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
		if(null == Cid){
			return LianjiuResult.build(401, "Cid对象绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderExcellentService.getExcellentByCid(Cid, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:根据主键id查询
	 * 
	 * @param excellentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{excellentId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentByCid(@PathVariable String excellentId) {
		if(Util.isEmpty(excellentId)){
			return LianjiuResult.build(401, "请传入正确的excellentId");
		}
		return orderExcellentService.getExcellentById(excellentId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:添加精品订单
	 * 
	 * @param excellent
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addExcellent", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addExcellent(OrdersExcellent excellent) {
		if(null == excellent){
			return LianjiuResult.build(401, "excellent对象绑定错误");
		}
		Util.printModelDetails(excellent);
		return orderExcellentService.addExcellent(excellent);
		
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
		if(null == excellent){
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
		if(Util.isEmpty(excellentId)){
			return LianjiuResult.build(401, "请传入正确的excellentId");
		}
		return orderExcellentService.deleteExcellent(excellentId);
	}

	/**
	 * 
	 * zhaoxi 2017年11月10日 descrption:
	 * 
	 * @param orders
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/deliverGoods", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deliverGoods(OrdersExcellent orders) {
		if(null == orders){
			return LianjiuResult.build(401, "orders对象绑定错误");
		}
		return orderExcellentService.deliverGoods(orders);
	}

	/**
	 * 
	 * zhaoxi 2017年11月10日 descrption:获取订单详情
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getOrdersDetails/{ordersId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getOrdersDetails(@PathVariable String ordersId) {
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderExcellentService.getOrdersDetails(ordersId);
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
	public LianjiuResult vagueQuery(OrdersExcellent ordersExcellent, int pageNum,int pageSize,String cratedStart,String cratedOver) {
//		if(null == ordersRepair){
//			return LianjiuResult.build(401, "ordersRepair对象绑定错误");
//		}
		System.out.println("ordersExcellent getUserId："+ordersExcellent.getUserId());
		System.out.println("ordersExcellent getOrExcellentId："+ordersExcellent.getOrExcellentId());
		System.out.println("ordersExcellent getOrExcellentStatus："+ordersExcellent.getOrExcellentStatus());
		System.out.println("ordersExcellent getOrExcellentPhone："+ordersExcellent.getOrExcellentPhone());
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
		return orderExcellentService.vagueQuery(ordersExcellent, pageNum,pageSize,cratedStart,cratedOver);
	}
}
