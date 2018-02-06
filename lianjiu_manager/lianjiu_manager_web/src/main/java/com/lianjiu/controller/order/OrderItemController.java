package com.lianjiu.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.service.order.OrderItemService;

/**
 * 一条商品记录
 * 
 * @author huangfu
 *
 */
@Controller
@RequestMapping("/orderItem")
public class OrderItemController {

	@Autowired
	private OrderItemService orderItemService;

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:查询所有商品记录
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getItem/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getItemAll() {
		return orderItemService.getItemAll();
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:分页查询所有商品记录
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getItem/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getItemAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderItemService.getItemAll(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前订单id的所有商品记录
	 * 
	 * @param Cid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getItem/{ordersId}/ByOrdersId/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getItemByOrdersId(@PathVariable String ordersId) {
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderItemService.getItemByOrdersId(ordersId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前订单id的所有商品记录，分页
	 * 
	 * @param Cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getItem/{ordersId}/{pageNum}/{pageSize}/ByOrdersId", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getItemByOrdersId(@PathVariable String ordersId, @PathVariable int pageNum, @PathVariable int pageSize) {
		if(Util.isEmpty(ordersId)){
			return LianjiuResult.build(401, "请传入正确的commentId");
		}
		return orderItemService.getItemByOrdersId(ordersId, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:根据主键id查询
	 * 
	 * @param ItemId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getItem/{itemId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getItemByItemId(@PathVariable String itemId) {
		if(Util.isEmpty(itemId)){
			return LianjiuResult.build(401, "请传入正确的itemId");
		}
		return orderItemService.getItemById(itemId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:添加商品记录
	 * 
	 * @param Item
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addItem", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addItem(OrdersItem Item) {
		if(null == Item){
			return LianjiuResult.build(401, "Item对象绑定错误");
		}
		Util.printModelDetails(Item);
		return orderItemService.addItem(Item);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:更新商品记录
	 * 
	 * @param Item
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyItem", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateItem(OrdersItem Item) {
		if(null == Item){
			return LianjiuResult.build(401, "Item对象绑定错误");
		}
		return orderItemService.updateItem(Item);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:删除商品记录
	 * 
	 * @param ItemId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeItem/{itemId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteItem(@PathVariable String itemId) {
		if(Util.isEmpty(itemId)){
			return LianjiuResult.build(401, "请传入正确的itemId");
		}
		return orderItemService.deleteItem(itemId);
	}
}
