package com.lianjiu.service.order.impl;

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
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.orders.OrdersItemMapper;
import com.lianjiu.service.order.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	private OrdersItemMapper ordersItemMapper;
	
	@Override
	public LianjiuResult getItemAll() {
		// 执行查询
		List<OrdersItem> ordersItem = ordersItemMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(ordersItem);
		return result;
	}

	@Override
	public LianjiuResult getItemAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersItem> ordersItem = ordersItemMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersItem> listItem = (Page<OrdersItem>) ordersItem;
		System.out.println("总页数：" + listItem.getPages());
		LianjiuResult result = new LianjiuResult(ordersItem);
		result.setTotal(listItem.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getItemByOrdersId(String ordersId) {
		OrdersItem item = new OrdersItem();
		item.setOrdersId(ordersId);
		SearchObjecVo vo = new SearchObjecVo(item);
		// 执行查询
		List<OrdersItem> ordersItem = ordersItemMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(ordersItem);
		return result;
	}

	@Override
	public LianjiuResult getItemByOrdersId(String ordersId, int pageNum, int pageSize) {
		OrdersItem item = new OrdersItem();
		item.setOrdersId(ordersId);
		SearchObjecVo vo = new SearchObjecVo(item);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersItem> ordersItem = ordersItemMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersItem> listItem = (Page<OrdersItem>) ordersItem;
		System.out.println("总页数：" + listItem.getPages());
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
		}
		item.setOrItemsId(IDUtils.genOrdersId());
		item.setOrItemsCreated(new Date());
		item.setOrItemsUpdated(new Date());
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

}
