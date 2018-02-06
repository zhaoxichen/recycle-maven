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
import com.lianjiu.model.OrdersRepairScheme;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.order.service.OrderRepairSchemeService;
import com.lianjiu.rest.mapper.orders.OrdersRepairSchemeMapper;
/**
 * 维修方案
 * @author zhaoxi
 *
 */
@Service
public class OrderRepairSchemeServiceImpl implements OrderRepairSchemeService {
	@Autowired
	private OrdersRepairSchemeMapper ordersRepairSchemeMapper;

	@Override
	public LianjiuResult getSchemeAll() {
		// 执行查询
		List<OrdersRepairScheme> scheme = ordersRepairSchemeMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(scheme);
		return result;
	}

	@Override
	public LianjiuResult getSchemeAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersRepairScheme> scheme = ordersRepairSchemeMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersRepairScheme> listScheme = (Page<OrdersRepairScheme>) scheme;
		System.out.println("总页数：" + listScheme.getPages());
		LianjiuResult result = new LianjiuResult(scheme);
		result.setTotal(listScheme.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getSchemeByOid(String ordersid) {
		OrdersRepairScheme scheme = new OrdersRepairScheme();
		scheme.setOrRepairId(ordersid);
		SearchObjecVo vo = new SearchObjecVo(scheme);
		// 执行查询
		List<OrdersRepairScheme> listScheme = ordersRepairSchemeMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(listScheme);
		return result;
	}

	@Override
	public LianjiuResult getSchemeById(String schemeId) {
		if (Util.isEmpty(schemeId)) {
			return LianjiuResult.build(501, "请指定维修品id");
		}
		// 去数据库查询
		OrdersRepairScheme scheme = ordersRepairSchemeMapper.selectByPrimaryKey(schemeId);
		return LianjiuResult.ok(scheme);
	}

	@Override
	public LianjiuResult addScheme(OrdersRepairScheme scheme) {

		if (null == scheme) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		scheme.setOrReSchemeId(IDUtils.genOrdersId());
		scheme.setOrReSchemeCreated(new Date());
		scheme.setOrReSchemeUpdated(new Date());
		// 去数据库添加
		int rowsAffected = ordersRepairSchemeMapper.insertSelective(scheme);
		return LianjiuResult.ok(rowsAffected);

	}

	@Override
	public LianjiuResult updateScheme(OrdersRepairScheme scheme) {
		if (null == scheme) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(scheme.getOrReSchemeId())) {
			return LianjiuResult.build(501, "请指定要更新的维修品id");
		}
		scheme.setOrReSchemeUpdated(new Date());
		// 去数据库更新
		int rowsAffected = ordersRepairSchemeMapper.updateByPrimaryKeySelective(scheme);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteScheme(String schemeId) {
		if (Util.isEmpty(schemeId)) {
			return LianjiuResult.build(501, "请指定要删除的维修品id");
		}
		// 去数据库删除
		int rowsAffected = ordersRepairSchemeMapper.deleteByPrimaryKey(schemeId);
		return LianjiuResult.ok(rowsAffected);
	}
}
