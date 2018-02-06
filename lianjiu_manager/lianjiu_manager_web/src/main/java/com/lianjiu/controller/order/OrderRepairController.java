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
import com.lianjiu.model.OrdersRepair;
import com.lianjiu.service.order.OrderRepairService;

/**
 * 维修手机的订单
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/orderRepair")
public class OrderRepairController {
	@Autowired
	private OrderRepairService orderRepairService;

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:查询所有维修订单
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepair/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getRepairAll() {
		return orderRepairService.getRepairAll();
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:分页查询所有维修订单
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepair/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRepairAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderRepairService.getRepairAll(pageNum, pageSize);
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
	public LianjiuResult ordersFilter(OrdersRepair orders, @RequestParam(defaultValue = "1") int pageNum,
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
		return orderRepairService.ordersFilter(orders, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前分类的所有维修订单
	 * 
	 * @param Cid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepair/{Cid}/ByCid/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getRepairByCid(@PathVariable Long Cid) {
		if(null == Cid){
			return LianjiuResult.build(401, "Cid绑定错误");
		}
		return orderRepairService.getRepairByCid(Cid);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前分类的所有维修订单，分页
	 * 
	 * @param Cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepair/{Cid}/{pageNum}/{pageSize}/ByCid", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRepairByCid(@PathVariable Long Cid, @PathVariable int pageNum, @PathVariable int pageSize) {
		if(null == Cid){
			return LianjiuResult.build(401, "Cid绑定错误");
		}
		return orderRepairService.getRepairByCid(Cid, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:根据主键id查询
	 * 
	 * @param repairId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepair/{repairId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRepairByCid(@PathVariable String repairId) {
		if(Util.isEmpty(repairId)){
			return LianjiuResult.build(401, "请传入正确的repairId");
		}
		return orderRepairService.getRepairById(repairId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:添加维修订单
	 * 
	 * @param repair
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addRepair", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addRepair(OrdersRepair repair) {
		if(null == repair){
			return LianjiuResult.build(401, "repair对象绑定错误");
		}
		Util.printModelDetails(repair);
		return orderRepairService.addRepair(repair);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:更新维修订单
	 * 
	 * @param repair
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyRepair", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateRepair(OrdersRepair repair) {
		if(null == repair){
			return LianjiuResult.build(401, "repair对象绑定错误");
		}
		return orderRepairService.updateRepair(repair);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:删除维修订单
	 * 
	 * @param repairId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeRepair/{repairId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteRepair(@PathVariable String repairId) {
		if(Util.isEmpty(repairId)){
			return LianjiuResult.build(401, "请传入正确的repairId");
		}
		return orderRepairService.deleteRepair(repairId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月15日 descrption:获取本订单中的所有维修方案
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
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderRepairService.getItemByOrdersId(ordersId, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年10月17日 descrption:获取维修方案
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getParamTemplate/{ordersId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRepairByRepairParamTemplate(@PathVariable String ordersId) {
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderRepairService.getRepairByRepairParamTemplate(ordersId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月23日 descrption:操作人员点击上门维修
	 * 
	 * @param ordersId
	 * @param orRepairTechnicians
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/handling", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult repairHandling(String ordersId, String orRepairTechnicians) {
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		if(Util.isEmpty(orRepairTechnicians)){
			return LianjiuResult.build(402, "请传入正确的orRepairTechnicians");
		}
		return orderRepairService.repairHandling(ordersId, orRepairTechnicians);
	}

	/**
	 * 
	 * zhaoxi 2017年10月23日 descrption:操作人员点击上门维修完成，上传订单
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/finish", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult repairFinish(String ordersId, String orRepairPicture) {
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		if(Util.isEmpty(orRepairPicture)){
			return LianjiuResult.build(402, "请传入正确的orRepairPicture");
		}
		return orderRepairService.repairFinish(ordersId, orRepairPicture);
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
	public LianjiuResult vagueQuery(OrdersRepair ordersRepair, int pageNum,int pageSize,String cratedStart,String cratedOver,
			String handleStart,String handleOven) {
//		if(null == ordersRepair){
//			return LianjiuResult.build(401, "ordersRepair对象绑定错误");
//		}
		System.out.println("ordersRepair getUserId："+ordersRepair.getUserId());
		System.out.println("ordersRepair getOrRepairId："+ordersRepair.getOrRepairId());
		System.out.println("ordersRepair getOrRepairPhone："+ordersRepair.getOrRepairPhone());
		System.out.println("ordersRepair getOrRepairTechnicians："+ordersRepair.getOrRepairTechnicians());
		System.out.println("ordersRepair getOrRepairStatus："+ordersRepair.getOrRepairStatus());
		System.out.println("cratedStart："+cratedStart);
		System.out.println("cratedOver："+cratedOver);
		System.out.println("handleStart："+handleStart);
		System.out.println("handleOven："+handleOven);
		System.out.println("pageNum："+pageNum);
		System.out.println("pageSize："+pageSize);
		
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderRepairService.vagueQuery(ordersRepair, pageNum,pageSize,cratedStart,cratedOver,handleStart,handleOven);
	}
}
