package com.lianjiu.rest.service.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.model.UserAddress;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersItemMapper;
import com.lianjiu.rest.mapper.user.UserAddressMapper;
import com.lianjiu.rest.model.ExcellentItemVo;
import com.lianjiu.rest.model.OrdersExcellentItemVo;
import com.lianjiu.rest.model.OrdersView;
import com.lianjiu.rest.service.orders.OrderExcellentService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

@Service
public class OrderExcellentServiceImpl implements OrderExcellentService {

	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;

	@Autowired
	private OrdersItemMapper ordersItemMapper;
	@Autowired
	private UserAddressMapper userAddressMapper;

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
			// 判断用户id 分类id 电话 是否为空
		} else if (excellent.getUserId() == null || excellent.getUserId().length() == 0
				|| excellent.getOrExcellentPhone() == null || excellent.getOrExcellentPhone().length() == 0
				|| excellent.getCategoryId() == null) {
			return LianjiuResult.build(503, "error 传入的对象信息不全");
			// 判断用户的详细地址 是否为空
		} else if (excellent.getOrExcellentLocation() == null || excellent.getOrExcellentLocation().isEmpty()) {
			return LianjiuResult.build(504, "error 传入对象地址为空！");
		}
		String excellentId = IDUtils.genOrdersId();
		excellent.setOrExcellentId(excellentId);
		excellent.setOrExcellentCreated(new Date());
		excellent.setOrExcellentUpdated(new Date());
		// excellent.setOrExcellentHandleTime(new Date());
		excellent.setOrExcellentStatus((byte) 5);
		// 去数据库添加
		int rowsAffected = ordersExcellentMapper.insert(excellent);
		LianjiuResult ljr = new LianjiuResult(excellentId);
		ljr.setTotal(rowsAffected);
		ljr.setStatus(200);
		return ljr;
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

	// 精品订单状态的更新
	@Override
	public LianjiuResult modifyExcellentState(String excellentId, Byte orExcellentStatus) {
		System.out.println("excellentId:" + excellentId);
		if ((ordersExcellentMapper.selectByPrimaryKeyCheck(excellentId)) == 0
				|| (orExcellentStatus + "").equals("null")) {
			return LianjiuResult.build(502, "error! 要更新的精品订单不存在或状态不存在！");
		}
		OrdersExcellent excellent = new OrdersExcellent();
		excellent.setOrExcellentId(excellentId);
		excellent.setOrExcellentStatus(orExcellentStatus);
		excellent.setOrExcellentUpdated(new Date());
		int rowsAffected = ordersExcellentMapper.modifyExcellentState(excellent);
		return LianjiuResult.ok(rowsAffected);
	}

	// 设置精品订单的处理时间
	@Override
	public LianjiuResult modifyExcellentHandleTime(String excellentId) {
		if (ordersExcellentMapper.selectByPrimaryKeyCheck(excellentId) == 0) {
			return LianjiuResult.build(502, "error! 要更新的精品订单不存在！");
		}
		OrdersExcellent excellent = new OrdersExcellent();
		excellent.setOrExcellentId(excellentId);
		excellent.setOrExcellentHandleTime(new Date());
		excellent.setOrExcellentUpdated(new Date());
		int rowsAffected = ordersExcellentMapper.modifyExcellentHandleTime(excellent);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult addExcellentOrdersItem(ExcellentItemVo excellentItemVo) {
		OrdersExcellent excellent = excellentItemVo.getOrdersExcellent();
		List<OrdersItem> list = excellentItemVo.getList();
		System.out.println("excellentItemVo" + excellentItemVo);
		System.out.println("list.size():" + list.size());
		if (null == excellentItemVo || null == excellent || list.size() == 0) {
			return LianjiuResult.build(502, "传入的对象为空");
			// 判断用户id 分类id 电话 是否为空
		} else if (excellent.getUserId() == null || excellent.getUserId().length() == 0
				|| excellent.getOrExcellentPhone() == null || excellent.getOrExcellentPhone().length() == 0
				|| excellent.getCategoryId() == null) {
			return LianjiuResult.build(503, "error 传入的对象信息不全");
			// 判断用户的详细地址 是否为空
		} else if (excellent.getOrExcellentLocation() == null || excellent.getOrExcellentLocation().isEmpty()) {
			return LianjiuResult.build(504, "error 传入对象地址为空！");
		}
		String excellentId = IDUtils.genOrdersId();
		excellent.setOrExcellentId(excellentId);
		excellent.setOrExcellentCreated(new Date());
		excellent.setOrExcellentUpdated(new Date());
		// excellent.setOrExcellentHandleTime(new Date());
		excellent.setOrExcellentStatus((byte) 5);
		// 赋值给item的主键id
		System.out.println("list:" + list);
		for (OrdersItem item : list) {
			item.setOrItemsId(IDUtils.genOrdersId());
		}
		System.out.println("list:" + list);
		int rowsAffected = ordersExcellentMapper.insert(excellent);
		int count = ordersItemMapper.addItemList(list);
		LianjiuResult ljr = new LianjiuResult(excellentId);
		ljr.setTotal(rowsAffected + count);
		ljr.setStatus(200);
		return ljr;
	}

	@Override
	public LianjiuResult getByExcellentList(Long categoryId) {
		if (null == categoryId) {
			return LianjiuResult.build(501, "请指定精品订单id");
		}
		// 去数据库查询
		List<OrdersExcellent> list = ordersExcellentMapper.getByExcellentList(categoryId);
		return LianjiuResult.ok(list);
	}

	@Override
	public LianjiuResult getByExcellentList(Long categoryId, int pageNum, int pageSize) {
		if (null == categoryId) {
			return LianjiuResult.build(501, "请指定精品订单id");
		}
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 根据parentid查询节点列表
		List<OrdersExcellent> list = ordersExcellentMapper.getByExcellentList(categoryId);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersExcellent> listExcellent = (Page<OrdersExcellent>) list;
		System.out.println("总页数：" + listExcellent.getPages());
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(list);
		result.setTotal(listExcellent.getTotal());
		result.setStatus(200);
		return result;
	}

	@Override
	public LianjiuResult selectAddressDefault(String userId) {
		Map<String, Object> results = new HashMap<>();
		// 查用户地址列表
		UserAddress address = userAddressMapper.selectDefault(userId);
		// 查链旧钱包余额
		Integer lianjiuWallet = 10000;
		LianjiuResult result = new LianjiuResult(results);
		if (null == address) {
			result.setStatus(502);
			result.setMsg("没有设置默认地址");
		} else {
			result.setStatus(200);
			result.setMsg("ok");
		}
		results.put("address", address);
		results.put("lianjiuWallet", lianjiuWallet);
		return result;
	}

	@Override
	public LianjiuResult getExcellentAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersExcellent> excellent = ordersExcellentMapper.findAll();
		List<ExcellentItemVo> excellentItemVoList = new ArrayList<ExcellentItemVo>();
		for (OrdersExcellent et : excellent) {
			ExcellentItemVo eiv = new ExcellentItemVo();
			eiv.setOrdersExcellent(et);
			List<OrdersItem> itemList = ordersItemMapper.getItemByOrdersId(et.getOrExcellentId());
			eiv.setList(itemList);
			excellentItemVoList.add(eiv);
		}
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		// Page<ExcellentItemVo> listExcellent = (Page<ExcellentItemVo>)
		// excellentItemVoList;
		// System.out.println("总页数：" + listExcellent.getPages());
		LianjiuResult result = new LianjiuResult(excellentItemVoList);
		result.setTotal(excellentItemVoList.size());
		return result;
	}

	@Override
	public LianjiuResult findAllByStatus(Byte orExcellentStatus, int pageNum, int pageSize) {
		if (null == orExcellentStatus) {
			return LianjiuResult.build(501, "请指定精品订单状态");
		}
		OrdersExcellent ordersExcellent = new OrdersExcellent();
		ordersExcellent.setOrExcellentStatus(orExcellentStatus);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersExcellent> excellent = ordersExcellentMapper.findAllByStatus(ordersExcellent);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		List<OrdersExcellentItemVo> ItemVoLits = new ArrayList<OrdersExcellentItemVo>();
		for (OrdersExcellent et : excellent) {
			OrdersExcellentItemVo ordersExcellentItemVo = new OrdersExcellentItemVo();
			OrdersItem itemVo = ordersItemMapper.getItemByOrdersStatus(et.getOrExcellentId());
			ordersExcellentItemVo.setOrdersItem(itemVo);
			ordersExcellentItemVo.setOrdersExcellent(et);
			ItemVoLits.add(ordersExcellentItemVo);
		}
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		if (ItemVoLits.size() == 0) {
			return LianjiuResult.build(503, "error 没有数据");
		}
		LianjiuResult result = new LianjiuResult(ItemVoLits);
		result.setTotal(ItemVoLits.size());
		return result;
	}

	@Override
	public LianjiuResult getExcellentByUserId(String userId, int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersView> ordersList = ordersExcellentMapper.getExcellentByUserId(userId);
		for (OrdersView ordersView : ordersList) {
			String orItemsParam = ordersView.getOrItemsParam();
			String additional = null;
			if (Util.notEmpty(orItemsParam)) {
				additional = this.checkAdditionalFromParam(orItemsParam);
				ordersView.setOrItemsParam(additional);
			}
		}
		return LianjiuResult.ok(ordersList);
	}

	@Override
	public LianjiuResult getExcellentByUserStatus(Byte state, String userId, int pageNum, int pageSize) {
		if (null == state) {
			return LianjiuResult.build(501, "请指定精品订单状态");
		}
		// 待付款1 待发货2 待收货3 待评价4 退货/售后5
		List<Byte> statusList = new ArrayList<Byte>();
		switch (state) {
		case 1:
			statusList.add(GlobalValueUtil.ORDER_STATUS_PAY_NO);// 未付款
			break;
		case 2:
			statusList.add(GlobalValueUtil.ORDER_STATUS_DELIVERY_NO);// 未发货
			statusList.add(GlobalValueUtil.ORDER_STATUS_DELIVERY_WARN);// 提醒发货
			break;
		case 3:
			statusList.add(GlobalValueUtil.ORDER_STATUS_DELIVERY_YES);// 已发货
			break;
		case 4:
			statusList.add(GlobalValueUtil.ORDER_STATUS_EVALUATE_NO);// 待评价
			statusList.add(GlobalValueUtil.ORDER_STATUS_EVALUATE_YES);// 已评价
			break;
		case 5:
			statusList.add(GlobalValueUtil.ORDER_STATUS_REFUND);// 仅退款
			statusList.add(GlobalValueUtil.ORDER_STATUS_REFUND_GOODS);// 退货退款
			statusList.add(GlobalValueUtil.ORDER_STATUS_REFUND_YES);// 已退款
			statusList.add(GlobalValueUtil.ORDER_STATUS_CANCEL);// 取消订单
			break;
		default:
			return LianjiuResult.ok(new ArrayList<>(0));
		}
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersView> ordersList = ordersExcellentMapper.getExcellentByUserStatus(userId, statusList);
		Page<OrdersView> listExcellent = (Page<OrdersView>) ordersList;
		for (OrdersView ordersView : ordersList) {
			String orItemsParam = ordersView.getOrItemsParam();
			String additional = null;
			if (Util.notEmpty(orItemsParam)) {
				additional = this.checkAdditionalFromParam(orItemsParam);
				ordersView.setOrItemsParam(additional);
			}
		}
		LianjiuResult result = new LianjiuResult(ordersList);
		result.setMsg("ok");
		result.setStatus(200);
		result.setTotal(listExcellent.getTotal());
		return result;
	}

	private String checkAdditionalFromParam(String orItemsParam) {
		String retrieveTypeData = null;
		StringBuffer additional = new StringBuffer();
		try {
			// 筛选出具体的参数
			JSONArray paramDataArray1 = JSONArray.fromObject(orItemsParam);// json串转为json数组
			for (Object object1 : paramDataArray1) {
				JSONArray paramDataArray2 = JSONArray.fromObject(object1);// json串转为json数组
				for (Object object2 : paramDataArray2) {
					JSONArray paramDataArray3 = JSONArray.fromObject(object2);// json串转为json数组
					for (Object children1 : paramDataArray3) {
						JSONObject majorDataJsonObject1 = JSONObject.fromObject(children1);// json串转为json对象
						JSONArray children2 = (JSONArray) majorDataJsonObject1.get("children"); // children为数组
						for (int i = 0; i < 3; i++) {
							JSONObject majorDataJsonObject = JSONObject.fromObject(children2.get(i));// json串转为json对象
							retrieveTypeData = (String) majorDataJsonObject.get("retrieveType");
							if (Util.notEmpty(retrieveTypeData)) {
								additional.append(retrieveTypeData + ",");
							}
						}
					}
				}
			}
		} catch (NullPointerException e) {
			return "精品json参数有问题";
		} catch (ClassCastException e) {
			return "精品json参数有问题";
		}
		return additional.substring(0, additional.length() - 1);
	}

	/**
	 * 
	 * zhaoxi 2017年11月6日 descrption:测试使用
	 * 
	 * @param args
	 *            void
	 */
	public static void main(String[] args) {
		// 创建一个jedis的对象。
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		// 调用jedis对象的方法，方法名称和redis的命令一致。
		String orItemsParam = jedis.get("testZhaoxi");
		String retrieveTypeData = null;
		StringBuffer additional = new StringBuffer();
		// 筛选出具体的参数
		JSONArray paramDataArray1 = JSONArray.fromObject(orItemsParam);// json串转为json数组
		for (Object object1 : paramDataArray1) {
			JSONArray paramDataArray2 = JSONArray.fromObject(object1);// json串转为json数组
			for (Object object2 : paramDataArray2) {
				JSONArray paramDataArray3 = JSONArray.fromObject(object2);// json串转为json数组
				for (Object children1 : paramDataArray3) {
					JSONObject majorDataJsonObject1 = JSONObject.fromObject(children1);// json串转为json对象
					retrieveTypeData = (String) majorDataJsonObject1.get("retrieveTypeData");
					JSONArray children2 = (JSONArray) majorDataJsonObject1.get("children"); // children为数组
					for (int i = 0; i < 3; i++) {
						JSONObject majorDataJsonObject = JSONObject.fromObject(children2.get(i));// json串转为json对象
						retrieveTypeData = (String) majorDataJsonObject.get("retrieveType");
						if (Util.notEmpty(retrieveTypeData)) {
							additional.append("," + retrieveTypeData);
						}
					}
				}
			}
		}
		System.out.println(additional);
	}

}
