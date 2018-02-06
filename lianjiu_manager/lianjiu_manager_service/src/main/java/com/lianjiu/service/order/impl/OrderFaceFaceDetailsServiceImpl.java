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
import com.lianjiu.model.OrdersFacefaceDetails;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceDetailsMapper;
import com.lianjiu.service.order.OrderFaceFaceDetailsService;

/**
 * 上门上门回收订单详情详情
 * 
 * @author huangfu
 *
 */
@Service
public class OrderFaceFaceDetailsServiceImpl implements OrderFaceFaceDetailsService {

	@Autowired
	private OrdersFacefaceDetailsMapper ordersFacefaceDetailsMapper;
	
	@Override
	public LianjiuResult getDetailsAll() {
		// 执行查询
		List<OrdersFacefaceDetails> details = ordersFacefaceDetailsMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(details);
		return result;
	}

	@Override
	public LianjiuResult getDetailsAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersFacefaceDetails> details = ordersFacefaceDetailsMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersFacefaceDetails> listDetails = (Page<OrdersFacefaceDetails>) details;
		System.out.println("总页数：" + listDetails.getPages());
		LianjiuResult result = new LianjiuResult(details);
		result.setTotal(listDetails.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getDetailsByFacefaceId(String facefaceId) {
		OrdersFacefaceDetails details = new OrdersFacefaceDetails();
		details.setOrFacefaceId(facefaceId);
		SearchObjecVo vo = new SearchObjecVo(details);
		// 执行查询
		List<OrdersFacefaceDetails> listDetails = ordersFacefaceDetailsMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(listDetails);
		return result;
	}

	@Override
	public LianjiuResult getDetailsById(String detailsId) {
		if (Util.isEmpty(detailsId)) {
			return LianjiuResult.build(501, "请指定上门回收订单详情id");
		}
		// 去数据库查询
		OrdersFacefaceDetails details = ordersFacefaceDetailsMapper.selectByPrimaryKey(detailsId);
		return LianjiuResult.ok(details);
	}

	@Override
	public LianjiuResult addDetails(OrdersFacefaceDetails details) {
		if (null == details) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		details.setOrFfDetailsId(IDUtils.genOrdersId());
		details.setOrFfDetailsUpdated(new Date());
		// 去数据库添加
		int rowsAffected = ordersFacefaceDetailsMapper.insertSelective(details);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult updateDetails(OrdersFacefaceDetails details) {
		if (null == details) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(details.getOrFfDetailsId())) {
			return LianjiuResult.build(501, "请指定要更新的上门回收订单详情id");
		}
		details.setOrFfDetailsUpdated(new Date());
		// 去数据库更新
		int rowsAffected = ordersFacefaceDetailsMapper.updateByPrimaryKeySelective(details);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteDetails(String detailsId) {
		if (Util.isEmpty(detailsId)) {
			return LianjiuResult.build(501, "请指定要删除的上门回收订单详情id");
		}
		// 去数据库删除
		int rowsAffected = ordersFacefaceDetailsMapper.deleteByPrimaryKey(detailsId);
		return LianjiuResult.ok(rowsAffected);
	}

	

}
