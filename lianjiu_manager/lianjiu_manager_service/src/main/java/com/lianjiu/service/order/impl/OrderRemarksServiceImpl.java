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
import com.lianjiu.model.OrdersRemarks;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.orders.OrdersRemarksMapper;
import com.lianjiu.service.order.OrderRemarksService;

/**
 * 一般评论
 * 
 * @author huangfu
 *
 */
@Service
public class OrderRemarksServiceImpl implements OrderRemarksService {

	@Autowired
	private OrdersRemarksMapper ordersRemarksMapper;

	@Override
	public LianjiuResult getRemarksAll() {
		// 执行查询
		List<OrdersRemarks> remarks = ordersRemarksMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(remarks);
		return result;
	}

	@Override
	public LianjiuResult getRemarksAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersRemarks> remarks = ordersRemarksMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersRemarks> listRemarks = (Page<OrdersRemarks>) remarks;
		System.out.println("总页数：" + listRemarks.getPages());
		LianjiuResult result = new LianjiuResult(remarks);
		result.setTotal(listRemarks.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getRemarksBySid(String ordersId) {
		OrdersRemarks remarks = new OrdersRemarks();
		remarks.setOrdersId(ordersId);
		SearchObjecVo vo = new SearchObjecVo(remarks);
		// 执行查询
		List<OrdersRemarks> listRemarks = ordersRemarksMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(listRemarks);
		result.setTotal(listRemarks.size());
		return result;
	}

	@Override
	public LianjiuResult getRemarksBySid(String ordersId, int pageNum, int pageSize) {
		OrdersRemarks remark = new OrdersRemarks();
		remark.setOrdersId(ordersId);
		SearchObjecVo vo = new SearchObjecVo(remark);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersRemarks> remarks = ordersRemarksMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersRemarks> listRemarks = (Page<OrdersRemarks>) remarks;
		System.out.println("总页数：" + listRemarks.getPages());
		LianjiuResult result = new LianjiuResult(remarks);
		result.setTotal(listRemarks.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getRemarksById(String remarksId) {
		if (Util.isEmpty(remarksId)) {
			return LianjiuResult.build(501, "请指定维修品id");
		}
		// 去数据库查询
		OrdersRemarks remarks = ordersRemarksMapper.selectByPrimaryKey(remarksId);
		return LianjiuResult.ok(remarks);
	}

	@Override
	public LianjiuResult addRemarks(OrdersRemarks remarks) {
		if (null == remarks) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		remarks.setOrRemarksId(IDUtils.genOrdersId());
		remarks.setOrRemarksCreated(new Date());
		remarks.setOrRemarksUpdated(new Date());
		// 去数据库添加
		int rowsAffected = ordersRemarksMapper.insertSelective(remarks);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult updateRemarks(OrdersRemarks remarks) {
		if (null == remarks) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(remarks.getOrRemarksId())) {
			return LianjiuResult.build(501, "请指定要更新的维修品id");
		}
		remarks.setOrRemarksUpdated(new Date());
		// 去数据库更新
		int rowsAffected = ordersRemarksMapper.updateByPrimaryKeySelective(remarks);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteRemarks(String remarksId) {
		if (Util.isEmpty(remarksId)) {
			return LianjiuResult.build(501, "请指定要删除的维修品id");
		}
		// 去数据库删除
		int rowsAffected = ordersRemarksMapper.deleteByPrimaryKey(remarksId);
		return LianjiuResult.ok(rowsAffected);
	}

}
