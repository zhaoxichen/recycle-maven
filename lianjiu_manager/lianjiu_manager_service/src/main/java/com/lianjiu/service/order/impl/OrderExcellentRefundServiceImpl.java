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
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersExcellentRefund;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentRefundMapper;
import com.lianjiu.service.order.OrderExcellentRefundService;

/**
 * 精品订单退款详情
 * 
 * @author zhaoxi
 *
 */
@Service
public class OrderExcellentRefundServiceImpl implements OrderExcellentRefundService {

	@Autowired
	private OrdersExcellentRefundMapper OrdersExcellentRefundMapper;
	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;

	@Override
	public LianjiuResult getRefundAll() {
		// 执行查询
		List<OrdersExcellentRefund> refund = OrdersExcellentRefundMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(refund);
		return result;
	}

	@Override
	public LianjiuResult getRefundAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersExcellentRefund> refund = OrdersExcellentRefundMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersExcellentRefund> listRefund = (Page<OrdersExcellentRefund>) refund;
		System.out.println("总页数：" + listRefund.getPages());
		LianjiuResult result = new LianjiuResult(refund);
		result.setTotal(listRefund.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getRefundByExcellentId(String excellentId) {
		OrdersExcellentRefund refund = new OrdersExcellentRefund();
		refund.setOrExcellentId(excellentId);
		SearchObjecVo vo = new SearchObjecVo(refund);
		// 执行查询
		List<OrdersExcellentRefund> listDetails = OrdersExcellentRefundMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(listDetails);
		return result;
	}

	@Override
	public LianjiuResult getRefundById(String refundId) {
		if (Util.isEmpty(refundId)) {
			return LianjiuResult.build(501, "请指定退款精品订单id");
		}
		// 去数据库查询
		OrdersExcellentRefund details = OrdersExcellentRefundMapper.selectByPrimaryKey(refundId);
		return LianjiuResult.ok(details);
	}

	@Override
	public LianjiuResult addRefund(OrdersExcellentRefund refund) {
		if (null == refund) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		refund.setOrExceRefundId(IDUtils.genOrdersId());
		refund.setOrExceRefundCreated(new Date());
		refund.setOrExceRefundUpdated(new Date());
		// 去数据库添加
		int rowsAffected = OrdersExcellentRefundMapper.insert(refund);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult updateRefund(OrdersExcellentRefund refund) {
		if (null == refund) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(refund.getOrExceRefundId())) {
			return LianjiuResult.build(501, "请指定要更新的退款精品订单id");
		}
		refund.setOrExceRefundUpdated(new Date());
		// 去数据库更新
		int rowsAffected = OrdersExcellentRefundMapper.updateByPrimaryKeySelective(refund);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteRefund(String refundId) {
		if (Util.isEmpty(refundId)) {
			return LianjiuResult.build(501, "请指定要删除的退款精品订单id");
		}
		// 去数据库删除
		int rowsAffected = OrdersExcellentRefundMapper.deleteByPrimaryKey(refundId);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult confirmRefund(OrdersExcellentRefund refund) {
		if (null == refund) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(refund.getOrExceRefundId())) {
			return LianjiuResult.build(501, "请指定要更新的退款精品订单id");
		}
		
		refund.setOrExceRefundUpdated(new Date());
		// 去数据库更新
		int rowsAffected = OrdersExcellentRefundMapper.updateByPrimaryKeySelective(refund);
		// 更新订单表
		if (Util.isEmpty(refund.getOrExcellentId())) {
			return LianjiuResult.build(503, "精品订单id");
		}
		OrdersExcellent ordersExcellent = new OrdersExcellent();
		ordersExcellent.setOrExcellentId(refund.getOrExcellentId());
		//修改订单状态为  已经退款
		ordersExcellent.setOrExcellentStatus(GlobalValueUtil.ORDER_STATUS_REFUND_YES);
		ordersExcellentMapper.updateByPrimaryKeySelective(ordersExcellent);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult selectRefoundList() {
		// 执行查询
		List<OrdersExcellentRefund> refund = OrdersExcellentRefundMapper.selectRefoundList();
		LianjiuResult result = new LianjiuResult(refund);
		return result;
	}

}
