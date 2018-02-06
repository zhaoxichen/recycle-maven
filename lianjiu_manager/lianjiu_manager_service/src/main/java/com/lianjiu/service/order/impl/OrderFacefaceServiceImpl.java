package com.lianjiu.service.order.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.tools.CalculateDate;
import com.lianjiu.common.tools.ProductParamCheck;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.OrdersFacefaceDetails;
import com.lianjiu.model.OrdersFacefaceItem;
import com.lianjiu.model.Product;
import com.lianjiu.model.vo.OrdersItemFaceface;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceDetailsMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.rest.mapper.orders.OrdersItemMapper;
import com.lianjiu.rest.mapper.product.ProductMapper;
import com.lianjiu.service.order.OrderFacefaceService;

/**
 * 上门回收服务层
 * 
 * @author huangfu
 *
 */

@Service
public class OrderFacefaceServiceImpl implements OrderFacefaceService {

	@Autowired
	private OrdersFacefaceMapper ordersFacefaceMapper;

	@Autowired
	private OrdersFacefaceDetailsMapper ordersFacefaceDetailsMapper;

	@Autowired
	ProductMapper productMapper;

	@Autowired
	private OrdersItemMapper ordersItemMapper;

	@Override
	public LianjiuResult getFacefaceAll() {
		// 执行查询
		List<OrdersFaceface> faceface = ordersFacefaceMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(faceface);
		return result;
	}

	@Override
	public LianjiuResult getFacefaceAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersFaceface> faceface = ordersFacefaceMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersFaceface> listFaceface = (Page<OrdersFaceface>) faceface;
		System.out.println("总页数：" + listFaceface.getPages());
		LianjiuResult result = new LianjiuResult(faceface);
		result.setTotal(listFaceface.getTotal());
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
	public LianjiuResult ordersFilter(OrdersFaceface orders, int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		SearchObjecVo vo = new SearchObjecVo(orders);
		// 执行查询
		List<OrdersFaceface> ordersList = ordersFacefaceMapper.selectBySearchFilter(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersFaceface> list = (Page<OrdersFaceface>) ordersList;
		System.out.println("总页数：" + list.getPages());
		LianjiuResult result = new LianjiuResult(ordersList);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}

	@Override
	public LianjiuResult getFacefaceByState(Integer state) {
		OrdersFaceface faceface = new OrdersFaceface();
		faceface.setOrFacefaceStatus((byte) state.intValue());
		SearchObjecVo vo = new SearchObjecVo(faceface);
		// 执行查询
		List<OrdersFaceface> listFaceface = ordersFacefaceMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(listFaceface);
		return result;
	}

	@Override
	public LianjiuResult getFacefaceByState(Integer state, int pageNum, int pageSize) {
		OrdersFaceface faceface = new OrdersFaceface();
		faceface.setOrFacefaceStatus((byte) state.intValue());
		SearchObjecVo vo = new SearchObjecVo(faceface);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersFaceface> facefaces = ordersFacefaceMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersFaceface> listFaceface = (Page<OrdersFaceface>) facefaces;
		System.out.println("总页数：" + listFaceface.getPages());
		LianjiuResult result = new LianjiuResult(facefaces);
		result.setTotal(listFaceface.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getFacefaceByCategoryId(Long categoryId) {
		OrdersFaceface faceface = new OrdersFaceface();
		faceface.setCategoryId(categoryId);
		SearchObjecVo vo = new SearchObjecVo(faceface);
		// 执行查询
		List<OrdersFaceface> listFaceface = ordersFacefaceMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(listFaceface);
		return result;
	}

	@Override
	public LianjiuResult getFacefaceByCategoryId(Long categoryId, int pageNum, int pageSize) {
		OrdersFaceface faceface = new OrdersFaceface();
		faceface.setCategoryId(categoryId);
		SearchObjecVo vo = new SearchObjecVo(faceface);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersFaceface> facefaces = ordersFacefaceMapper.selectBySearchObjecVo(vo);
		// 当前时间
		Date currentTime = new Date();
		int days = 0;
		for (OrdersFaceface ordersFaceface : facefaces) {
			days = CalculateDate.differentDaysByMillisecond(ordersFaceface.getOrFacefaceCreated(), currentTime);
			if (days > 1) {
				ordersFaceface.setIsExpire(1);
			} else {
				ordersFaceface.setIsExpire(0);
			}
		}
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersFaceface> listFaceface = (Page<OrdersFaceface>) facefaces;
		System.out.println("总页数：" + listFaceface.getPages());
		LianjiuResult result = new LianjiuResult(facefaces);
		result.setTotal(listFaceface.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getFacefaceByAllianceId(String allianceId) {
		OrdersFaceface faceface = new OrdersFaceface();
		faceface.setOrFacefaceAllianceId(allianceId);
		SearchObjecVo vo = new SearchObjecVo(faceface);
		// 执行查询
		List<OrdersFaceface> listFaceface = ordersFacefaceMapper.selectBySearchFilter(vo);
		LianjiuResult result = new LianjiuResult(listFaceface);
		return result;
	}

	@Override
	public LianjiuResult getFacefaceByAllianceId(String allianceId, int pageNum, int pageSize) {
		OrdersFaceface faceface = new OrdersFaceface();
		faceface.setOrFacefaceAllianceId(allianceId);
		SearchObjecVo vo = new SearchObjecVo(faceface);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersFaceface> facefaces = ordersFacefaceMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersFaceface> listFaceface = (Page<OrdersFaceface>) facefaces;
		System.out.println("总页数：" + listFaceface.getPages());
		LianjiuResult result = new LianjiuResult(facefaces);
		result.setTotal(listFaceface.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getFacefaceById(String facefaceId) {
		if (Util.isEmpty(facefaceId)) {
			return LianjiuResult.build(501, "请指定上门回收订单id");
		}
		// 去数据库查询
		OrdersFaceface faceface = ordersFacefaceMapper.selectByPrimaryKey(facefaceId);
		return LianjiuResult.ok(faceface);
	}

	@Override
	public LianjiuResult addFaceface(OrdersFaceface faceface) {
		if (null == faceface) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		faceface.setOrFacefaceId(IDUtils.genOrdersId());
		faceface.setOrFacefaceCreated(new Date());
		faceface.setOrFacefaceUpdated(new Date());
		// 去数据库添加
		int rowsAffected = ordersFacefaceMapper.insert(faceface);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult updateFaceface(OrdersFaceface faceface) {
		if (null == faceface) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(faceface.getOrFacefaceId())) {
			return LianjiuResult.build(501, "请指定要更新的上门回收订单id");
		}
		faceface.setOrFacefaceUpdated(new Date());
		// 去数据库更新
		int rowsAffected = ordersFacefaceMapper.updateByPrimaryKeySelective(faceface);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteFaceface(String facefaceId) {
		if (Util.isEmpty(facefaceId)) {
			return LianjiuResult.build(501, "请指定要删除的上门回收订单id");
		}
		// 去数据库删除
		int rowsAffected = ordersFacefaceMapper.deleteByPrimaryKey(facefaceId);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult getItemByOrdersId(String ordersId, int pageNum, int pageSize) {
		Map<String, Object> map = new HashMap<>();
		// 取详情
		OrdersFacefaceDetails details = ordersFacefaceDetailsMapper.selectByOrdersId(ordersId);
		map.put("orderDetails", details);
		OrdersFacefaceItem item = new OrdersFacefaceItem();
		item.setOrdersId(ordersId);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersFacefaceItem> ordersItems = ordersItemMapper.selectByFaceFaceOrdersId(ordersId);
		map.put("ordersItems", ordersItems);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersFacefaceItem> listItem = (Page<OrdersFacefaceItem>) ordersItems;
		System.out.println("总页数：" + listItem.getPages());
		LianjiuResult result = new LianjiuResult(map);
		result.setTotal(listItem.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getOrderItemsById(String orFaceItemId) {
		if (Util.isEmpty(orFaceItemId)) {
			return LianjiuResult.build(501, "请指定要查询的商品记录id");
		}
		OrdersItemFaceface ordersFacefaceItem = ordersItemMapper.selectOrdersFacefaceByItemId(orFaceItemId);
		if (null != ordersFacefaceItem) {
			String param = ordersFacefaceItem.getOrItemsParam();
			String paramModify = ordersFacefaceItem.getOrItemsParamModify();
			if (Util.isEmpty(paramModify)) {
				paramModify = param;
				ordersFacefaceItem.setOrItemsParamModify(paramModify);
			}
			if (Util.notEmpty(param)) {
				System.out.println("原型：" + param);
				ordersFacefaceItem.setMachineCheckList(ProductParamCheck.checkAdditionalFromParam(param, paramModify));
			}
		}
		LianjiuResult result = new LianjiuResult(ordersFacefaceItem);
		return result;
	}

	@Override
	public LianjiuResult getParamData(String productId) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(501, "请指定要查询的订单id");
		}
		Product p = productMapper.selectByPrimaryKey(productId);
		String productParamData = null;
		if (p != null) {
			productParamData = p.getProductParamData();
		}
		return LianjiuResult.ok(productParamData);
	}

	@Override
	public LianjiuResult modifyParam(String orItemsId, String orItemsParamModify) {
		if (Util.isEmpty(orItemsId)) {
			return LianjiuResult.build(501, "请指定要更新的上门回收item是id");
		}
		if (Util.isEmpty(orItemsParamModify)) {
			return LianjiuResult.build(501, "请传入要更新的修改的产品参数模版");
		}
		OrdersFacefaceItem item = new OrdersFacefaceItem();
		item.setOrItemsId(orItemsId);
		item.setOrItemsParamModify(orItemsParamModify);
		item.setOrItemsUpdated(new Date());
		int rowsAffected = ordersItemMapper.updateByPrimaryKeySelective(item);
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 取消加盟商订单
	 */
	@SuppressWarnings("unused")
	@Override
	public LianjiuResult ordersExpireCancel(String ordersId) {
		int rowAffect = 0;
		try {
			rowAffect = ordersFacefaceMapper.ordersCancel(ordersId);
		} catch (RuntimeException e) {
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(ordersId);
	}

	@Override
	public LianjiuResult vagueQuery(OrdersFaceface faceface, int pageNum, int pageSize, String cratedStart,
			String cratedOver) {
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersFaceface> ordersList = ordersFacefaceMapper.vagueQuery(faceface,cratedStart,cratedOver);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		
		Page<OrdersFaceface> list = (Page<OrdersFaceface>) ordersList;
		Date currentTime = new Date();
		int days = 0;
		for (OrdersFaceface ordersFaceface : list) {
			days = CalculateDate.differentDaysByMillisecond(ordersFaceface.getOrFacefaceCreated(), currentTime);
			if (days > 1) {
				ordersFaceface.setIsExpire(1);
			} else {
				ordersFaceface.setIsExpire(0);
			}
		}
		System.out.println("总页数：" + list.getPages());
		LianjiuResult result = new LianjiuResult(ordersList);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}
	


}
