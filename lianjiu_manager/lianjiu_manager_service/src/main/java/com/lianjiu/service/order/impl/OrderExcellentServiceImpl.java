package com.lianjiu.service.order.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.vo.OrdersItemExcellent;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersItemMapper;
import com.lianjiu.service.order.OrderExcellentService;

/**
 * 精品订单服务层
 * 
 * @author zhaoxi
 *
 */
@Service
public class OrderExcellentServiceImpl implements OrderExcellentService {

	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;
	@Autowired
	private OrdersItemMapper ordersItemMapper;

	@Override
	public LianjiuResult getExcellentAll() {
		// 执行查询
		List<OrdersExcellent> excellent = ordersExcellentMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(excellent);
		return result;

	}

	@Override
	public LianjiuResult getExcellentAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersExcellent> excellent = ordersExcellentMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersExcellent> listExcellent = (Page<OrdersExcellent>) excellent;
		System.out.println("总页数：" + listExcellent.getPages());
		LianjiuResult result = new LianjiuResult(excellent);
		result.setTotal(listExcellent.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
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
	@Override
	public LianjiuResult ordersFilter(OrdersExcellent orders, int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		SearchObjecVo vo = new SearchObjecVo(orders);
		// 执行查询
		List<OrdersExcellent> ordersList = ordersExcellentMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersExcellent> list = (Page<OrdersExcellent>) ordersList;
		System.out.println("总页数：" + list.getPages());
		LianjiuResult result = new LianjiuResult(ordersList);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}

	@Override
	public LianjiuResult getExcellentByCid(Long cid) {
		OrdersExcellent excellent = new OrdersExcellent();
		excellent.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(excellent);
		// 执行查询
		List<OrdersExcellent> listRemarks = ordersExcellentMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(listRemarks);
		return result;
	}

	@Override
	public LianjiuResult getExcellentByCid(Long cid, int pageNum, int pageSize) {
		OrdersExcellent excellent = new OrdersExcellent();
		excellent.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(excellent);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersExcellent> excellents = ordersExcellentMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersExcellent> listExcellents = (Page<OrdersExcellent>) excellents;
		System.out.println("总页数：" + listExcellents.getPages());
		LianjiuResult result = new LianjiuResult(excellents);
		result.setTotal(listExcellents.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getExcellentById(String excellentId) {
		if (Util.isEmpty(excellentId)) {
			return LianjiuResult.build(501, "请指定精品订单id");
		}
		// 去数据库查询
		OrdersExcellent excellent = ordersExcellentMapper.selectByPrimaryKey(excellentId);
		return LianjiuResult.ok(excellent);
	}

	@Override
	public LianjiuResult addExcellent(OrdersExcellent excellent) {
		if (null == excellent) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		excellent.setOrExcellentId(IDUtils.genOrdersId());
		excellent.setOrExcellentCreated(new Date());
		excellent.setOrExcellentUpdated(new Date());
		// 去数据库添加
		int rowsAffected = ordersExcellentMapper.insert(excellent);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult updateExcellent(OrdersExcellent excellent) {
		if (null == excellent) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(excellent.getOrExcellentId())) {
			return LianjiuResult.build(501, "请指定要更新的精品订单id");
		}
		excellent.setOrExcellentUpdated(new Date());
		// 去数据库更新
		int rowsAffected = ordersExcellentMapper.updateByPrimaryKeySelective(excellent);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteExcellent(String excellentId) {
		if (Util.isEmpty(excellentId)) {
			return LianjiuResult.build(501, "请指定要删除的精品订单id");
		}
		// 去数据库删除
		int rowsAffected = ordersExcellentMapper.deleteByPrimaryKey(excellentId);
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 发货
	 */
	@Override
	public LianjiuResult deliverGoods(OrdersExcellent orders) {
		if (Util.isEmpty(orders.getOrExcellentId())) {
			return LianjiuResult.build(501, "请传入订单id");
		}
		orders.setOrExcellentUpdated(new Date());
		orders.setOrExcellentHandleTime(new Date());// 订单处理时间
		orders.setOrExcellentStatus(GlobalValueUtil.ORDER_STATUS_DELIVERY_YES);// 已发货
		int rowsAffected = ordersExcellentMapper.updateByPrimaryKeySelective(orders);
		if (0 < rowsAffected) {
			return LianjiuResult.ok(orders.getOrExcellentId());
		}
		return LianjiuResult.build(502, "上传订单号失败");
	}

	/**
	 * 获取精品订单详情
	 */
	@Override
	public LianjiuResult getOrdersDetails(String ordersId) {
		List<OrdersItemExcellent> items = ordersItemMapper.selectByOrdersId(ordersId);
		if (items.size() > 0) {
			OrdersItemExcellent item = items.get(0);
			item.setOrItemsParamModify(null);
			return LianjiuResult.ok(item);
		}
		return LianjiuResult.ok(new ArrayList<>(0));
	}

	@Override
	public LianjiuResult vagueQuery(OrdersExcellent ordersExcellent, int pageNum, int pageSize, String cratedStart,
			String cratedOver) {
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersExcellent> ordersList = ordersExcellentMapper.vagueQuery(ordersExcellent,cratedStart,cratedOver);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersExcellent> list = (Page<OrdersExcellent>) ordersList;
		System.out.println("总页数：" + list.getPages());
		LianjiuResult result = new LianjiuResult(ordersList);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}

}
