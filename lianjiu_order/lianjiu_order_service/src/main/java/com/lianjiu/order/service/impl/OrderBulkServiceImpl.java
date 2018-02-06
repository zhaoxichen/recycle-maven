package com.lianjiu.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.tools.CalculateStringNum;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.HttpClientUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.SendEmail;
import com.lianjiu.common.utils.Util;
import com.lianjiu.common.utils.sendSMS;
import com.lianjiu.model.OrdersBulk;
import com.lianjiu.model.OrdersBulkItem;
import com.lianjiu.model.User;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.order.service.OrderBulkService;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.orders.OrdersBulkItemMapper;
import com.lianjiu.rest.mapper.orders.OrdersBulkMapper;
import com.lianjiu.rest.mapper.product.ProductBulkMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;
import com.lianjiu.rest.tool.JedisTool;
import com.lianjiu.rest.tool.WalletLianjiuTool;

/**
 * 家具订单
 * 
 * @author zhaoxi
 *
 */
@Service
public class OrderBulkServiceImpl implements OrderBulkService {

	@Autowired
	private OrdersBulkMapper ordersBulkMapper;
	@Autowired
	private OrdersBulkItemMapper ordersBulkItemMapper;
	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;
	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;
	@Autowired
	private ProductBulkMapper productBulkMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private JedisClient jedisClient;
	private static Logger loggerOrdersBulk = Logger.getLogger("ordersBulk");

	/**
	 * 获取订单价格
	 */
	@Override
	public LianjiuResult getOrdersPrice(String TAKEN) {
		/**
		 * 从redis中取出清单
		 */
		List<OrdersBulkItem> itemList = JedisTool.checkListFormRedis(jedisClient, GlobalValueJedis.BULK_CART + TAKEN,
				OrdersBulkItem.class);
		if (null == itemList) {
			return LianjiuResult.build(501, "清单已过期，请重新选择物品加入清单");
		}
		if (0 == itemList.size()) {
			return LianjiuResult.build(502, "清单为空，请选择一些物品");
		}
		/**
		 * 补全item信息，计算订单总价格
		 */
		String ordersId = IDUtils.genOrdersId();
		String ordersPrice = "0.00";
		Iterator<OrdersBulkItem> it = itemList.iterator();
		while (it.hasNext()) {
			OrdersBulkItem ordersBulkItem = (OrdersBulkItem) it.next();
			if (0 >= Double.parseDouble(ordersBulkItem.getBulkItemNum())) {
				it.remove();// 移除数量为0的item
				continue;
			}
			ordersPrice = CalculateStringNum.add(ordersPrice, ordersBulkItem.getBulkItemAccountPrice());
			ordersBulkItem.setOrdersId(ordersId);
		}
		if (0 == itemList.size()) {
			jedisClient.set(GlobalValueJedis.BULK_CART + TAKEN, JsonUtils.objectToJson(itemList));
			jedisClient.expire(GlobalValueJedis.BULK_CART + TAKEN, 1800);
			return LianjiuResult.build(502, "清单为空，请选择一些物品");
		}
		/**
		 * 暂存
		 */
		jedisClient.set(GlobalValueJedis.BULK_CART + TAKEN, JsonUtils.objectToJson(itemList));
		jedisClient.expire(GlobalValueJedis.BULK_CART + TAKEN, 1800);
		jedisClient.set(GlobalValueJedis.BULK_CART_PRICE + TAKEN, ordersPrice);
		jedisClient.expire(GlobalValueJedis.BULK_CART_PRICE + TAKEN, 1800);
		jedisClient.set(GlobalValueJedis.BULK_CART_ORDERS_ID + TAKEN, ordersId);
		jedisClient.expire(GlobalValueJedis.BULK_CART_ORDERS_ID + TAKEN, 1800);

		return LianjiuResult.ok(ordersPrice);
	}

	/**
	 * 获取订单预览
	 */
	@Override
	public LianjiuResult getOrdersPreview(String TAKEN) {
		Map<String, String> results = new HashMap<String, String>();
		/**
		 * redis中取出订单总价
		 */
		String ordersPrice = jedisClient.get(GlobalValueJedis.BULK_CART_PRICE + TAKEN);// 从上一个接口中获取订单总价
		if (Util.isEmpty(ordersPrice)) {
			/**
			 * 从redis中取出清单
			 */
			List<OrdersBulkItem> itemList = JedisTool.checkListFormRedis(jedisClient,
					GlobalValueJedis.BULK_CART + TAKEN, OrdersBulkItem.class);
			if (null == itemList) {
				return LianjiuResult.build(501, "清单已过期，请重新选择物品加入清单");
			}
			if (0 == itemList.size()) {
				return LianjiuResult.build(502, "清单为空，请选择一些物品");
			}
			/**
			 * 补全item信息，计算订单总价格
			 */
			String ordersId = IDUtils.genOrdersId();
			ordersPrice = "0.00";
			Iterator<OrdersBulkItem> it = itemList.iterator();
			while (it.hasNext()) {
				OrdersBulkItem ordersBulkItem = (OrdersBulkItem) it.next();
				if (0 >= Double.parseDouble(ordersBulkItem.getBulkItemNum())) {
					it.remove();// 移除数量为0的item
					continue;
				}
				ordersPrice = CalculateStringNum.add(ordersPrice, ordersBulkItem.getBulkItemAccountPrice());
				ordersBulkItem.setOrdersId(ordersId);
			}
			if (0 == itemList.size()) {
				jedisClient.set(GlobalValueJedis.BULK_CART + TAKEN, JsonUtils.objectToJson(itemList));
				jedisClient.expire(GlobalValueJedis.BULK_CART + TAKEN, 1800);
				return LianjiuResult.build(502, "清单为空，请选择一些物品");
			}
			/**
			 * 暂存
			 */
			jedisClient.set(GlobalValueJedis.BULK_CART + TAKEN, JsonUtils.objectToJson(itemList));
			jedisClient.set(GlobalValueJedis.BULK_CART_PRICE + TAKEN, ordersPrice);
			jedisClient.set(GlobalValueJedis.BULK_CART_ORDERS_ID + TAKEN, ordersId);
		}
		results.put("ordersPrice", ordersPrice);
		/**
		 * 延长缓存时间
		 */
		jedisClient.expire(GlobalValueJedis.BULK_CART + TAKEN, 1800);
		jedisClient.expire(GlobalValueJedis.BULK_CART_PRICE + TAKEN, 1800);
		jedisClient.expire(GlobalValueJedis.BULK_CART_ORDERS_ID + TAKEN, 1800);
		// 在redis中查询仓库id
		Long categoryId = JedisTool.checkObjectFormRedis(jedisClient, GlobalValueJedis.BULK_WAREHOUSE + TAKEN,
				Long.class);
		String address = productBulkMapper.getDetailedAddress(categoryId);
		results.put("address", address);
		return LianjiuResult.ok(results);
	}

	/**
	 * 提交订单
	 */
	@Override
	public LianjiuResult submit(String TAKEN, OrdersBulk ordersBulk) {
		String userId = ordersBulk.getUserId();
		/**
		 * 从redis中取出用户
		 */
		User user = JedisTool.checkObjectFormRedis(jedisClient, GlobalValueJedis.REDIS_USER_SESSION_KEY + userId,
				User.class);
		if (null == user) {
			// 发个邮件通知工作人员
			SendEmail.sendText(899, GlobalValueJedis.REDIS_USER_SESSION_KEY + userId + "--提交大宗交易订单");
			return LianjiuResult.build(503, "用户已经掉线");
		}
		/**
		 * 从redis中取出清单
		 */
		List<OrdersBulkItem> itemList = JedisTool.checkListFormRedis(jedisClient, GlobalValueJedis.BULK_CART + TAKEN,
				OrdersBulkItem.class);
		if (null == itemList) {
			return LianjiuResult.build(501, "清单已过期，请重新选择物品加入清单");
		}
		if (0 == itemList.size()) {
			return LianjiuResult.build(502, "清单为空，请选择一些物品");
		}
		/**
		 * 订单总价格
		 */
		String ordersId = jedisClient.get(GlobalValueJedis.BULK_CART_ORDERS_ID + TAKEN);// 从上一个接口中获取订单id
		String ordersPrice = jedisClient.get(GlobalValueJedis.BULK_CART_PRICE + TAKEN);// 从上一个接口中获取订单总价
		if (Util.isEmpty(ordersPrice) || Util.isEmpty(ordersId)) {
			ordersId = IDUtils.genOrdersId();
			Iterator<OrdersBulkItem> it = itemList.iterator();
			while (it.hasNext()) {
				OrdersBulkItem ordersBulkItem = (OrdersBulkItem) it.next();
				if (0 >= Double.parseDouble(ordersBulkItem.getBulkItemNum())) {
					it.remove();// 移除数量为0的item
					continue;
				}
				ordersPrice = CalculateStringNum.add(ordersPrice, ordersBulkItem.getBulkItemAccountPrice());
				ordersBulkItem.setOrdersId(ordersId);
			}
			if (0 == itemList.size()) {
				jedisClient.set(GlobalValueJedis.BULK_CART + TAKEN, JsonUtils.objectToJson(itemList));
				jedisClient.expire(GlobalValueJedis.BULK_CART + TAKEN, 1800);
				return LianjiuResult.build(502, "清单为空，请选择一些物品");
			}
		}
		// 在redis中查询仓库id
		Long categoryId = JedisTool.checkObjectFormRedis(jedisClient, GlobalValueJedis.BULK_WAREHOUSE + TAKEN,
				Long.class);
		// 如果缓存丢失，查出商品编号再查出分类编号再查仓库地址
		if (null == categoryId) {
			Long pid = itemList.get(0).getBulkItemProductCid();
			categoryId = categoryMapper.selectParentIdByPrimaryKey(pid);
		}
		ordersBulk.setOrBulkId(ordersId);
		ordersBulk.setOrdersPrice(ordersPrice);
		ordersBulk.setOrdersRetrPrice(ordersPrice);
		ordersBulk.setOrBulkStatus(GlobalValueUtil.ORDER_BLUK_INSPECTION_NO);// 待验货
		ordersBulk.setCategoryId(categoryId);// 仓库id
		// 插入
		try {
			ordersBulkItemMapper.addItemList(itemList);
			ordersBulkMapper.insert(ordersBulk);
		} catch (RuntimeException e) {
			return LianjiuResult.build(510, "数据异常");
		}
		String address = productBulkMapper.getDetailedAddress(categoryId);
		System.out.println("仓库地址：" + address);
		if (Util.isEmpty(address)) {
			loggerOrdersBulk.info("通过产品编号获取仓库地址失败");
		}
		// 推送短信
		String memberPhone = user.getUsername();
		loggerOrdersBulk.info("用户" + userId + "的手机号：" + memberPhone);
		sendSMS.sendMessage("19", "+86", memberPhone, address);
		// 清空redis
		jedisClient.del(GlobalValueJedis.BULK_CART + TAKEN);
		jedisClient.del(GlobalValueJedis.BULK_CART_ORDERS_ID + TAKEN);
		jedisClient.del(GlobalValueJedis.BULK_CART_PRICE + TAKEN);
		jedisClient.del(GlobalValueJedis.BULK_WAREHOUSE + TAKEN);

		// 发个邮件通知工作人员
		SendEmail.sendText(799, ordersId);

		return LianjiuResult.ok(ordersId);
	}

	/**
	 * 查看订单 0 全部，1 交货，2结算，3取消
	 */
	@Override
	public LianjiuResult getOrdersByUserStatus(Integer status, String userId, int pageNum, int pageSize) {
		List<Byte> statusList = new ArrayList<>();
		switch (status) {
		case 0:// 全部
			statusList.add(GlobalValueUtil.ORDER_BLUK_CANCEL);
			statusList.add(GlobalValueUtil.ORDER_BLUK_INSPECTION_NO);
			statusList.add(GlobalValueUtil.ORDER_BLUK_TRUE_NO);
			statusList.add(GlobalValueUtil.ORDER_BLUK_FINISH);
			statusList.add(GlobalValueUtil.ORDER_BLUK_EVALUATE_YES);
			break;
		case 1:// 交货
			statusList.add(GlobalValueUtil.ORDER_BLUK_INSPECTION_NO);// 待验货
			statusList.add(GlobalValueUtil.ORDER_BLUK_TRUE_NO);// 待确认
			break;
		case 2:// 结算
			statusList.add(GlobalValueUtil.ORDER_BLUK_FINISH);// 已结算
			statusList.add(GlobalValueUtil.ORDER_BLUK_EVALUATE_YES);// 已评论
			break;
		case 3:// 取消
			statusList.add(GlobalValueUtil.ORDER_BLUK_CANCEL);// 已取消
			break;
		default:
			break;
		}
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 去数据库查询
		List<OrdersBulk> listOrdersBulk = ordersBulkMapper.getOrdersListByStatus(userId, statusList);
		if (null == listOrdersBulk) {
			listOrdersBulk = new ArrayList<OrdersBulk>();
		}
		Page<OrdersBulk> listBulk = (Page<OrdersBulk>) listOrdersBulk;
		LianjiuResult result = new LianjiuResult(listOrdersBulk);
		result.setTotal(listBulk.getTotal());
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 讲订单状态设为取消
	 * 
	 * @param orBulkId
	 * @return
	 */
	public LianjiuResult cancelOrders(String ordersId) {
		try {
			int rowsAffected = ordersBulkMapper.modifyOrdersStatus(GlobalValueUtil.ORDER_BLUK_CANCEL, ordersId);
			if (1 == rowsAffected) {
				loggerOrdersBulk.info("取消成功");
				HttpClientUtil.doPost(GlobalValueUtil.TIMER_BASE_URL + "orderBulk/orderConfirmDel/" + ordersId);
				return LianjiuResult.ok(ordersId);
			}
			loggerOrdersBulk.info("取消失败");
			return LianjiuResult.build(501, "取消失败");
		} catch (RuntimeException e) {
			loggerOrdersBulk.info("数据异常：" + e.getMessage());
			return LianjiuResult.build(502, "数据异常");
		}
	}

	/**
	 * 订单确认,修改订单状态，加钱到给用户
	 */
	@Override
	public LianjiuResult agreeOrders(String ordersId) {
		try {
			/**
			 * 查询订单信息
			 */
			OrdersBulk ordersBulk = ordersBulkMapper.selectByPrimaryKey(ordersId);
			if (null == ordersBulk) {
				loggerOrdersBulk.info("订单不存在" + ordersId);
				return LianjiuResult.build(501, "订单不存在");
			}
			ordersBulk.setOrBulkStatus(GlobalValueUtil.ORDER_BLUK_FINISH);// 修改为已结算
			ordersBulk.setOrBulkPaytime(new Date());// 支付时间
			int rowsAffected = ordersBulkMapper.updateByPrimaryKeySelective(ordersBulk);
			String userId = ordersBulk.getUserId();
			String ordersRetrPrice = ordersBulk.getOrdersRetrPrice();
			// 流水
			UserWalletRecord walletRecord = new UserWalletRecord(userId, "大宗交易赚取", ordersRetrPrice, ordersId,
					(byte) 16);
			WalletLianjiuTool.walletAdd(walletRecord, walletLianjiuMapper, userWalletRecordMapper);
			if (1 == rowsAffected) {
				loggerOrdersBulk.info("确认成功");
				HttpClientUtil.doPost(GlobalValueUtil.TIMER_BASE_URL + "orderBulk/orderConfirmDel/" + ordersId);
				return LianjiuResult.ok(ordersId);
			}
			loggerOrdersBulk.info("确认失败");
			return LianjiuResult.build(501, "确认失败");
		} catch (RuntimeException e) {
			loggerOrdersBulk.info("数据异常：" + e.getMessage());
			return LianjiuResult.build(502, "数据异常");
		}
	}

	/**
	 * 获取大宗订单详情
	 */
	@Override
	public LianjiuResult ordersDetails(String ordersId) {
		List<OrdersBulkItem> itemList = ordersBulkItemMapper.selectItemByOrdersId(ordersId);
		return LianjiuResult.ok(itemList);
	}

}
