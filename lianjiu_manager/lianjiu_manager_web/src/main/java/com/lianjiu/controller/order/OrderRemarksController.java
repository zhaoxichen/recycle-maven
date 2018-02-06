package com.lianjiu.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersRemarks;
import com.lianjiu.service.order.OrderRemarksService;

/**
 * 订单的备注
 * 
 * @author huangfu
 *
 */
@Controller
@RequestMapping("/orderRemark")
public class OrderRemarksController {

	@Autowired
	private OrderRemarksService orderRemarksService;

	/**
	 * 
	 * huangfu 2017年9月12日 descrption:查询所有订单评论
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRemarks/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getRemarksAll() {
		return orderRemarksService.getRemarksAll();
	}

	/**
	 * 
	 * huangfu 2017年9月12日 descrption:分页查询所有订单评论
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRemarks/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRemarksAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}	
		return orderRemarksService.getRemarksAll(pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2017年9月7日 descrption:获取当前方案的所有订单评论
	 * 
	 * @param Cid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRemarks/{ordersId}/BySid/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getRemarksBySid(@PathVariable String ordersId) {
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderRemarksService.getRemarksBySid(ordersId);
	}

	/**
	 * 
	 * huangfu 2017年9月7日 descrption:获取当前分类的所有订单评论，分页
	 * 
	 * @param Cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRemarks/{ordersId}/{pageNum}/{pageSize}/BySid", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRemarksBySid(@PathVariable String ordersId, @PathVariable int pageNum, @PathVariable int pageSize) {
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderRemarksService.getRemarksBySid(ordersId, pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2017年9月7日 descrption:根据主键id查询
	 * 
	 * @param RemarksId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRemarks/{remarksId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRemarksByCid(@PathVariable String remarksId) {
		if(Util.isEmpty(remarksId)){
			return LianjiuResult.build(401, "请传入正确的remarksId");
		}
		return orderRemarksService.getRemarksById(remarksId);
	}

	/**
	 * 
	 * huangfu 2017年9月12日 descrption:添加订单评论
	 * 
	 * @param remarks
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addRemarks", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addRemarks(OrdersRemarks remarks) {
		if(null == remarks){
			return LianjiuResult.build(401, "remarks对象绑定错误");
		}
		Util.printModelDetails(remarks);
		return orderRemarksService.addRemarks(remarks);
	}

	/**
	 * 
	 * huangfu 2017年9月12日 descrption:更新订单评论
	 * 
	 * @param repair
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyRemarks", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateRemarks(OrdersRemarks remarks) {
		if(null == remarks){
			return LianjiuResult.build(401, "remarks对象绑定错误");
		}
		return orderRemarksService.updateRemarks(remarks);
	}

	/**
	 * 
	 * huangfu 2017年9月12日 descrption:删除订单评论
	 * 
	 * @param remarksId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeRemarks/{remarksId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteRemarks(@PathVariable String remarksId) {
		if(Util.isEmpty(remarksId)){
			return LianjiuResult.build(401, "请传入正确的remarksId");
		}
		return orderRemarksService.deleteRemarks(remarksId);
	}
}
