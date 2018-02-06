package com.lianjiu.service.order.impl;

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
import com.lianjiu.model.OrdersRepair;
import com.lianjiu.model.OrdersRepairItem;
import com.lianjiu.model.OrdersRepairScheme;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.orders.OrdersRepairItemMapper;
import com.lianjiu.rest.mapper.orders.OrdersRepairMapper;
import com.lianjiu.rest.mapper.orders.OrdersRepairSchemeMapper;
import com.lianjiu.service.order.OrderRepairService;

/**
 * 维修订单
 * 
 * @author zhaoxi
 *
 */
@Service
public class OrderRepairServiceImpl implements OrderRepairService {

	@Autowired
	private OrdersRepairMapper ordersRepairMapper;
	@Autowired
	private OrdersRepairItemMapper ordersRepairItemMapper;
	@Autowired
	private OrdersRepairSchemeMapper ordersRepairSchemeMapper;

	@Override
	public LianjiuResult getRepairAll() {
		// 执行查询
		List<OrdersRepair> repairs = ordersRepairMapper.selectAll();
		LianjiuResult result = new LianjiuResult(repairs);
		return result;
	}

	@Override
	public LianjiuResult getRepairAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersRepair> repairs = ordersRepairMapper.selectAll();
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersRepair> listRepairs = (Page<OrdersRepair>) repairs;
		System.out.println("总页数：" + listRepairs.getPages());
		LianjiuResult result = new LianjiuResult(repairs);
		result.setTotal(listRepairs.getTotal());
		result.setStatus(200);
		result.setMsg("ok");
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
	public LianjiuResult ordersFilter(OrdersRepair orders, int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		SearchObjecVo vo = new SearchObjecVo(orders);
		// 执行查询
		List<OrdersRepair> ordersList = ordersRepairMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersRepair> list = (Page<OrdersRepair>) ordersList;
		System.out.println("总页数：" + list.getPages());
		LianjiuResult result = new LianjiuResult(ordersList);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}

	@Override
	public LianjiuResult getRepairByCid(Long cid) {
		return LianjiuResult.ok("未开发");
	}

	@Override
	public LianjiuResult getRepairByCid(Long cid, int pageNum, int pageSize) {
		return LianjiuResult.ok("未开发");
	}

	@Override
	public LianjiuResult addRepair(OrdersRepair repair) {

		if (null == repair) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		repair.setOrRepairId(IDUtils.genOrdersId());
		repair.setOrRepairCreated(new Date());
		repair.setOrRepairUpdated(new Date());
		// 去数据库添加
		int rowsAffected = ordersRepairMapper.insertSelective(repair);
		return LianjiuResult.ok(rowsAffected);

	}

	@Override
	public LianjiuResult updateRepair(OrdersRepair repair) {
		if (null == repair) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(repair.getOrRepairId())) {
			return LianjiuResult.build(501, "请指定要更新的维修品id");
		}
		repair.setOrRepairUpdated(new Date());
		// 去数据库更新
		int rowsAffected = ordersRepairMapper.updateByPrimaryKeySelective(repair);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteRepair(String repairId) {
		if (Util.isEmpty(repairId)) {
			return LianjiuResult.build(501, "请指定要删除的维修品id");
		}
		// 去数据库删除
		int rowsAffected = ordersRepairMapper.deleteByPrimaryKey(repairId);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult getRepairById(String repairId) {
		if (Util.isEmpty(repairId)) {
			return LianjiuResult.build(501, "请指定维修品id");
		}
		// 去数据库查询
		OrdersRepair repair = ordersRepairMapper.selectByPrimaryKey(repairId);
		return LianjiuResult.ok(repair);
	}

	@Override
	public LianjiuResult getItemByOrdersId(String ordersId, int pageNum, int pageSize) {
		OrdersRepairItem item = new OrdersRepairItem();
		item.setOrdersId(ordersId);
		SearchObjecVo vo = new SearchObjecVo(item);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersRepairItem> ordersItem = ordersRepairItemMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersRepairItem> listItem = (Page<OrdersRepairItem>) ordersItem;
		System.out.println("总页数：" + listItem.getPages());
		LianjiuResult result = new LianjiuResult(ordersItem);
		result.setTotal(listItem.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getRepairByRepairParamTemplate(String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(501, "请指定维修品id");
		}
		// 查维修方案
		OrdersRepairScheme scheme = ordersRepairSchemeMapper.selectSchameByOrdersId(ordersId);
		return LianjiuResult.ok(scheme);
	}

	@Override
	public LianjiuResult repairHandling(String ordersId, String orRepairTechnicians) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(501, "请指定维修品id");
		}
		OrdersRepair ordersRepair = new OrdersRepair();
		ordersRepair.setOrRepairId(ordersId);
		ordersRepair.setOrRepairUpdated(new Date());
		ordersRepair.setOrRepairHandleTime(new Date());
		ordersRepair.setOrRepairStatus(GlobalValueUtil.ORDER_REPAIR_VISITING);
		ordersRepair.setOrRepairTechnicians(orRepairTechnicians);
		// 去数据库更新
		ordersRepairMapper.updateByPrimaryKeySelective(ordersRepair);
		return LianjiuResult.ok(ordersId);
	}

	/**
	 * 保存图片到数据库
	 */
	@Override
	public LianjiuResult repairFinish(String ordersId, String orRepairPicture) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(501, "请指定维修品id");
		}
		OrdersRepairScheme scheme = new OrdersRepairScheme();
		scheme.setOrRepairId(ordersId);
		scheme.setOrRepairPicture(orRepairPicture);
		scheme.setOrReSchemeUpdated(new Date());
		ordersRepairSchemeMapper.updateByOrdersId(scheme);
		// 更新订单
		OrdersRepair ordersRepair = new OrdersRepair();
		ordersRepair.setOrRepairId(ordersId);
		ordersRepair.setOrRepairUpdated(new Date());
		ordersRepair.setOrRepairStatus(GlobalValueUtil.ORDER_REPAIR_FINISH);
		// 去数据库更新
		ordersRepairMapper.updateByPrimaryKeySelective(ordersRepair);
		return LianjiuResult.ok(ordersId);
	}

	@Override
	public LianjiuResult vagueQuery(OrdersRepair ordersRepair, int pageNum, int pageSize,String cratedStart, String cratedOver, String handleStart, String handleOven) {
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersRepair> ordersList = ordersRepairMapper.vagueQuery(ordersRepair,cratedStart,cratedOver,handleStart,handleOven);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersRepair> list = (Page<OrdersRepair>) ordersList;
		System.out.println("总页数：" + list.getPages());
		LianjiuResult result = new LianjiuResult(ordersList);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}

}
