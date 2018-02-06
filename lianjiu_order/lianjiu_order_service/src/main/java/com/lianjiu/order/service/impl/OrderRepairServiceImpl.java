package com.lianjiu.order.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersRepair;
import com.lianjiu.model.OrdersRepairItem;
import com.lianjiu.model.OrdersRepairScheme;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.order.service.OrderRepairService;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.rest.mapper.orders.OrdersRepairItemMapper;
import com.lianjiu.rest.mapper.orders.OrdersRepairMapper;
import com.lianjiu.rest.mapper.orders.OrdersRepairSchemeMapper;

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
	private CategoryMapper categoryMapper;
	@Autowired
	private OrdersRepairSchemeMapper ordersRepairSchemeMapper;
	@Autowired
	private JedisClient jedisClient;
	private static Logger loggerOrdersRepair = Logger.getLogger("ordersRepair");

	@Override
	public LianjiuResult getRepairAll() {
		// 执行查询
		List<OrdersRepair> repairs = ordersRepairMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(repairs);
		return result;
	}

	@Override
	public LianjiuResult getRepairAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersRepair> repairs = ordersRepairMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersRepair> listRepairs = (Page<OrdersRepair>) repairs;
		loggerOrdersRepair.info("总页数：" + listRepairs.getPages());
		LianjiuResult result = new LianjiuResult(repairs);
		result.setTotal(listRepairs.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getRepairByCid(Long cid) {
		OrdersRepair repair = new OrdersRepair();
		repair.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(repair);
		// 执行查询
		List<OrdersRepair> repairs = ordersRepairMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(repairs);
		return result;
	}

	@Override
	public LianjiuResult getRepairByCid(Long cid, int pageNum, int pageSize) {
		OrdersRepair repair = new OrdersRepair();
		repair.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(repair);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersRepair> repairs = ordersRepairMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersRepair> listRepairs = (Page<OrdersRepair>) repairs;
		loggerOrdersRepair.info("总页数：" + listRepairs.getPages());
		LianjiuResult result = new LianjiuResult(repairs);
		result.setTotal(listRepairs.getTotal());
		return result;
	}

	@Override
	public LianjiuResult addRepair(OrdersRepair repair, OrdersRepairScheme scheme) {

		// 查分类
		Long categoryId = categoryMapper.selectByCategoryName("维修订单");
		if (null == repair) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		String productId = scheme.getProductRepairId();
		// 查询redis
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(503, "请传入产品id");
		}
		String productRepairName = jedisClient.get("repair-" + productId);
		String ordersId = IDUtils.genOrdersId();
		scheme.setOrReSchemeId(IDUtils.genOrdersId());
		scheme.setOrRepairId(ordersId);
		if (Util.isEmpty(scheme.getProductRepairName())) {
			loggerOrdersRepair.info("没有传入产品名称，要去redis查询");
			scheme.setProductRepairName(productRepairName);
		}
		scheme.setOrReSchemeCreated(new Date());
		scheme.setOrReSchemeUpdated(new Date());
		// ordersRepairSchemeMapper.insertSelective(scheme);

		repair.setOrRepairId(ordersId);
		repair.setCategoryId(categoryId);
		repair.setOrRepairCreated(new Date());
		repair.setOrRepairUpdated(new Date());
		// 去数据库添加
		// ordersRepairMapper.insertSelective(repair);
		try {
			ordersRepairSchemeMapper.insertSelective(scheme);
			ordersRepairMapper.insertSelective(repair);
		} catch (RuntimeException e) {
			loggerOrdersRepair.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(ordersId);
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
		int rowsAffected = 0;
		try {
			rowsAffected = ordersRepairMapper.updateByPrimaryKeySelective(repair);
		} catch (RuntimeException e) {
			loggerOrdersRepair.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteRepair(String repairId) {
		if (Util.isEmpty(repairId)) {
			return LianjiuResult.build(501, "请指定要删除的维修品id");
		}
		// 去数据库删除
		int rowsAffected = 0;
		try {
			rowsAffected = ordersRepairMapper.deleteByPrimaryKey(repairId);
		} catch (RuntimeException e) {
			loggerOrdersRepair.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
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
		loggerOrdersRepair.info("总页数：" + listItem.getPages());
		LianjiuResult result = new LianjiuResult(ordersItem);
		result.setTotal(listItem.getTotal());
		return result;
	}

	@Override
	public LianjiuResult updateStatus(String ordersId, Byte status) {
		try {
			ordersRepairMapper.updateOrderStatus(ordersId, status);
		} catch (RuntimeException e) {
			loggerOrdersRepair.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(ordersId);
	}

	@Override
	public LianjiuResult getRepairByUid(String uid, Byte status) {
		List<OrdersRepair> orderList = ordersRepairMapper.getRepairByUserId(uid, status);
		return LianjiuResult.ok(orderList);
	}

	@Override
	public LianjiuResult getRepairByUid(String uid, List<Byte> status) {
		List<OrdersRepair> orderList = ordersRepairMapper.getRepairByStatus(uid, status);
		return LianjiuResult.ok(orderList);
	}

}
