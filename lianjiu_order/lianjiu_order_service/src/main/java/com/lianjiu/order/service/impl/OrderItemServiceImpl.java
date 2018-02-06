package com.lianjiu.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.order.service.OrderItemService;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersItemMapper;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	private OrdersItemMapper ordersItemMapper;
	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;
	
	@Override
	public LianjiuResult getItemAll() {
		// 执行查询
		List<OrdersItem> ordersItem = ordersItemMapper.getItemAll();
		LianjiuResult result = new LianjiuResult(ordersItem);
		return result;
	}
	
	@Override
	public LianjiuResult getItemAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersItem> ordersItem = ordersItemMapper.getItemAll();
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersItem> listItem = (Page<OrdersItem>) ordersItem;
		System.out.println("总页数：" + listItem.getPages());
		LianjiuResult result = new LianjiuResult(ordersItem);
		result.setTotal(listItem.getTotal());
		return result;
	}
	
	@Override
	public LianjiuResult getItemByOrdersId(String ordersId) {
		if(ordersExcellentMapper.selectByPrimaryKeyCheck(ordersId)==0){
			return LianjiuResult.build(502, "error! 订单不存在！");
		}
		OrdersItem item = new OrdersItem();
		item.setOrdersId(ordersId);
		// 执行查询
		List<OrdersItem> ordersItem = ordersItemMapper.getItemByOrdersId(ordersId);
		if(ordersItem.size()==0){
			return LianjiuResult.ok("此订单没有商品记录~！");
		}
		LianjiuResult result = new LianjiuResult(ordersItem);
		return result;
	}

	@Override
	public LianjiuResult getItemByOrdersId(String ordersId, int pageNum, int pageSize) {
		if(ordersExcellentMapper.selectByPrimaryKeyCheck(ordersId)==0){
			return LianjiuResult.build(502, "error! 要更新的精品订单不存在！");
		}
		OrdersItem item = new OrdersItem();
		item.setOrdersId(ordersId);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersItem> ordersItem = ordersItemMapper.getItemByOrdersId(ordersId);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersItem> listItem = (Page<OrdersItem>) ordersItem;
		System.out.println("总页数：" + listItem.getPages());
		if(ordersItem.size()==0){
			return LianjiuResult.ok("此订单没有商品记录~！");
		}
		LianjiuResult result = new LianjiuResult(ordersItem);
		result.setTotal(listItem.getTotal());
		return result;
	}
	
	@Override
	public LianjiuResult getItemById(String itemId) {
		if (Util.isEmpty(itemId)) {
			return LianjiuResult.build(501, "请指定精品订单id");
		}
		// 去数据库查询
		OrdersItem excellent = ordersItemMapper.selectByPrimaryKey(itemId);
		return LianjiuResult.ok(excellent);
	}

	@Override
	public LianjiuResult addItem(OrdersItem item) {
		if (null == item) {
			return LianjiuResult.build(502, "传入的对象为空");
		}else if(item.getOrdersId()==null||item.getOrdersId().length()==0
				||item.getOrItemsProductId()==null||item.getOrItemsProductId().length()==0
				||item.getOrItemsNum() == null
				){
			return LianjiuResult.build(503, "传入的信息有误，不能缺少订单id/产品id/数量");
		}else if (ordersExcellentMapper.selectByPrimaryKeyCheck(item.getOrdersId())==0){
			return LianjiuResult.build(503, "传入的信息有误，订单id不存在/产品id不存在");
		}
		item.setOrItemsId(IDUtils.genOrdersId());
		item.setOrItemsCreated(new Date());
		item.setOrItemsUpdated(new Date());
		item.setOrItemsStatus(0);
		// 去数据库添加
		int rowsAffected = ordersItemMapper.insert(item);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult updateItem(OrdersItem item) {
		if (null == item) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(item.getOrdersId())) {
			return LianjiuResult.build(501, "请指定要更新的精品订单id");
		}
		item.setOrItemsUpdated(new Date());
		// 去数据库更新
		int rowsAffected = ordersItemMapper.updateByPrimaryKeySelective(item);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteItem(String itemId) {
		if (Util.isEmpty(itemId)) {
			return LianjiuResult.build(501, "请指定要删除的精品订单id");
		}
		// 去数据库删除
		int rowsAffected = ordersItemMapper.deleteByPrimaryKey(itemId);
		return LianjiuResult.ok(rowsAffected);
	}
	

	@Override
	public LianjiuResult updateByStatus(String ordersId,int orItemsStatus) {
		if(ordersId==null||ordersId.length()==0){
			return LianjiuResult.build(501, "请指定精品订单id");
		}else if(ordersExcellentMapper.selectByPrimaryKeyCheck(ordersId)==0){
			return LianjiuResult.build(502, "订单不存在！或者改订单没有商品记录");
		}
		OrdersItem item = new OrdersItem();
		item.setOrdersId(ordersId);
		item.setOrItemsStatus(orItemsStatus);
		item.setOrItemsUpdated(new Date());
		int rowsAffected = ordersItemMapper.updateByStatus(item);
		return LianjiuResult.ok(rowsAffected);
	}

}
