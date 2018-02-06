package com.lianjiu.service.order.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.tools.CalculateDate;
import com.lianjiu.common.tools.CalculateStringNum;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.HttpClientUtil;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.common.utils.sendSMS;
import com.lianjiu.model.OrdersBulk;
import com.lianjiu.model.OrdersBulkItem;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.orders.OrdersBulkItemMapper;
import com.lianjiu.rest.mapper.orders.OrdersBulkMapper;
import com.lianjiu.rest.mapper.product.ProductBulkPriceMapper;
import com.lianjiu.rest.mapper.user.UserMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;
import com.lianjiu.rest.tool.JedisTool;
import com.lianjiu.rest.tool.WalletLianjiuTool;
import com.lianjiu.service.order.OrdersBulkService;

/**
 * 家具订单
 * 
 * @author zhaoxi
 *
 */
@Service
public class OrdersBulkServiceImpl implements OrdersBulkService {

	@Autowired
	private OrdersBulkMapper ordersBulkMapper;
	@Autowired
	private OrdersBulkItemMapper ordersBulkItemMapper;
	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;
	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ProductBulkPriceMapper productBulkPriceMapper;
	@Autowired
	private JedisClient jedisClient;
	private static Logger loggerOrdersBulk = Logger.getLogger("ordersBulk");

	/**
	 * 分页获取大宗回收订单
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public LianjiuResult getOrders(int pageNum, int pageSize) {
		OrdersBulk ordersBulk = new OrdersBulk();
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<OrdersBulk> listOrdersBulk = ordersBulkMapper.getAllOrdersBulk(ordersBulk);
		if (pageNum == 1 && 0 == listOrdersBulk.size()) {
			loggerOrdersBulk.info("大宗订单为空");
			return LianjiuResult.build(501, "大宗订单为空");
		}
		if (0 == listOrdersBulk.size()) {
			return LianjiuResult.build(502, "尚无更多大宗订单");
		} else {
			// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
			Page<OrdersBulk> pageOrdersBulk = (Page<OrdersBulk>) listOrdersBulk;
			// loggerProductFurniture.info("总页数：" + pageFurniture.getPages());
			// 封装成相应数据对象
			LianjiuResult result = new LianjiuResult(listOrdersBulk);
			System.out.println("总数" + pageOrdersBulk.getTotal());
			result.setTotal(pageOrdersBulk.getTotal());
			return result;
		}
	}

	/**
	 * 讲订单状态设为取消
	 * 
	 * @param orBulkId
	 * @return
	 */
	public LianjiuResult cancelOrders(String orBulkId) {
		OrdersBulk ordersBulk = new OrdersBulk();
		ordersBulk.setOrBulkId(orBulkId);
		ordersBulk.setOrBulkStatus(GlobalValueUtil.ORDER_BLUK_CANCEL);
		try {
			int rowsAffected = ordersBulkMapper.modifyCancle(ordersBulk);
			if (1 == rowsAffected) {
				loggerOrdersBulk.info("取消成功");
				return LianjiuResult.ok(rowsAffected);
			}
			loggerOrdersBulk.info("取消失败");
			return LianjiuResult.build(501, "取消失败");
		} catch (RuntimeException e) {
			loggerOrdersBulk.info("数据异常：" + e.getMessage());
			return LianjiuResult.build(502, "数据异常");
		}
	}

	/**
	 * 订单详情
	 */
	@Override
	public LianjiuResult getOrdersItem(String ordersId) {
		List<OrdersBulkItem> ordersBulkItemList = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.ORDERS_BULK_ITEM_MODIFY + ordersId, OrdersBulkItem.class);
		if (null != ordersBulkItemList) {
			return LianjiuResult.ok(ordersBulkItemList);
		}
		ordersBulkItemList = ordersBulkItemMapper.selectItemByOrdersId(ordersId);
		if (null == ordersBulkItemList) {
			loggerOrdersBulk.info("订单查询错误");
			return LianjiuResult.build(501, "订单查询错误");
		}
		// 判断订单距离当前的时间是否大于5
		boolean isFiveDay = false;
		if (0 < ordersBulkItemList.size() && GlobalValueUtil.FIVE_TIME <= CalculateDate
				.currentDaysDistance(ordersBulkItemList.get(0).getBulkItemCreated())) {
			isFiveDay = true;
		}
		if (isFiveDay) {
			String currentPrice = null;
			String bulkItemAccountRetrPrice = null;
			for (OrdersBulkItem ordersBulkItem : ordersBulkItemList) {
				currentPrice = productBulkPriceMapper.getCurrentPriceByPid(ordersBulkItem.getBulkItemProductId());
				if (currentPrice != null && currentPrice.equals(ordersBulkItem.getBulkItemPriceCurrent())) {
					continue;
				} else {
					/**
					 * 大于五天的订单，价格被刷新，需要用户确认
					 */
					jedisClient.set(GlobalValueJedis.ORDERS_BULK_IS_MODIFY + ordersId, "true");
					jedisClient.expire(GlobalValueJedis.ORDERS_BULK_IS_MODIFY + ordersId, 1800);
				}
				bulkItemAccountRetrPrice = CalculateStringNum.multiply(ordersBulkItem.getBulkItemNumModify(),
						currentPrice);// 修改的数量*当前价格
				ordersBulkItem.setBulkItemPriceCurrent(currentPrice);// 最新价格
				ordersBulkItem.setBulkItemAccountRetrPrice(bulkItemAccountRetrPrice);// 重新计算总价
			}
		}
		jedisClient.set(GlobalValueJedis.ORDERS_BULK_ITEM_MODIFY + ordersId,
				JsonUtils.objectToJson(ordersBulkItemList));
		jedisClient.expire(GlobalValueJedis.ORDERS_BULK_ITEM_MODIFY + ordersId, 1800);
		return LianjiuResult.ok(ordersBulkItemList);
	}

	/**
	 * 修改订单详情
	 */
	@Override
	public LianjiuResult modiFyOrdersItem(OrdersBulkItem item) {
		String ordersId = item.getOrdersId();
		List<OrdersBulkItem> ordersBulkItemList = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.ORDERS_BULK_ITEM_MODIFY + ordersId, OrdersBulkItem.class);
		if (null == ordersBulkItemList) {
			ordersBulkItemList = ordersBulkItemMapper.selectItemByOrdersId(ordersId);
			if (null == ordersBulkItemList) {
				loggerOrdersBulk.info("订单查询错误");
				return LianjiuResult.build(501, "订单查询错误");
			}
			jedisClient.set(GlobalValueJedis.ORDERS_BULK_ITEM_MODIFY + ordersId,
					JsonUtils.objectToJson(ordersBulkItemList));
		}
		/**
		 * 判断是不是修改数量了
		 */
		String itemId = item.getBulkItemId();
		String bulkItemNumModify = item.getBulkItemNumModify();
		String bulkItemAccountRetrPrice = null;
		for (OrdersBulkItem ordersBulkItem : ordersBulkItemList) {
			if (itemId.equals(ordersBulkItem.getBulkItemId())) {
				bulkItemAccountRetrPrice = CalculateStringNum.multiply(bulkItemNumModify,
						ordersBulkItem.getBulkItemPriceCurrent());// 修改的数量*当前价格
				ordersBulkItem.setBulkItemNumModify(bulkItemNumModify); // 修改后的数量
				ordersBulkItem.setBulkItemAccountRetrPrice(bulkItemAccountRetrPrice);// 重新计算总价
				jedisClient.set(GlobalValueJedis.ORDERS_BULK_IS_MODIFY + ordersId, "true");
				jedisClient.expire(GlobalValueJedis.ORDERS_BULK_IS_MODIFY + ordersId, 1800);
				break;
			}
		}
		// 重新赋值给redis
		jedisClient.set(GlobalValueJedis.ORDERS_BULK_ITEM_MODIFY + ordersId,
				JsonUtils.objectToJson(ordersBulkItemList));
		jedisClient.expire(GlobalValueJedis.ORDERS_BULK_ITEM_MODIFY + ordersId, 1800);
		// 返回修改后的数据
		return LianjiuResult.ok(ordersBulkItemList);
	}

	/**
	 * 修改完毕，1更新数据库的orders_bulk 2更新数据库的orders_bulk_item 3发送短信通知用户确认价格 4启动定时任务
	 */
	@Override
	public LianjiuResult modiFyOrdersFinish(OrdersBulk ordersBulk) {
		String ordersId = ordersBulk.getOrBulkId();
		String isModify = jedisClient.get(GlobalValueJedis.ORDERS_BULK_IS_MODIFY + ordersBulk.getOrBulkId());
		if (Util.isEmpty(isModify)) {
			/**
			 * 修改订状态，删除redis，打钱给用户
			 */
			ordersBulk.setOrBulkStatus(GlobalValueUtil.ORDER_BLUK_FINISH);// 价格默认，订单完成
			ordersBulk.setOrBulkPaytime(new Date());
			ordersBulkMapper.updateByPrimaryKeySelective(ordersBulk);
			String userId = ordersBulk.getUserId();
			String ordersRetrPrice = ordersBulk.getOrdersRetrPrice();
			// 流水
			UserWalletRecord walletRecord = new UserWalletRecord(userId, "大宗交易赚取", ordersRetrPrice, ordersId,
					(byte) 16);
			WalletLianjiuTool.walletAdd(walletRecord, walletLianjiuMapper, userWalletRecordMapper);
			jedisClient.del(GlobalValueJedis.ORDERS_BULK_IS_MODIFY + ordersId);
			jedisClient.del(GlobalValueJedis.ORDERS_BULK_ITEM_MODIFY + ordersId);
			return LianjiuResult.ok("订单无修改");
		}
		/**
		 * 修改订单价格和状态，更新item表，删除redis
		 */
		List<OrdersBulkItem> ordersBulkItemList = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.ORDERS_BULK_ITEM_MODIFY + ordersId, OrdersBulkItem.class);
		if (null == ordersBulkItemList) {
			loggerOrdersBulk.info("重新修改" + ordersId);
			return LianjiuResult.build(502, "缓存过期，请重新修改");
		}
		String ordersRetrPrice = "0";
		for (OrdersBulkItem ordersBulkItem : ordersBulkItemList) {
			ordersRetrPrice = CalculateStringNum.add(ordersRetrPrice, ordersBulkItem.getBulkItemAccountRetrPrice());
		}
		ordersBulk.setOrdersRetrPrice(ordersRetrPrice);
		ordersBulk.setOrBulkStatus(GlobalValueUtil.ORDER_BLUK_TRUE_NO);// 待确认
		ordersBulkMapper.updateByPrimaryKeySelective(ordersBulk);
		ordersBulkItemMapper.updateItemList(ordersBulkItemList);
		jedisClient.del(GlobalValueJedis.ORDERS_BULK_IS_MODIFY + ordersId);
		jedisClient.del(GlobalValueJedis.ORDERS_BULK_ITEM_MODIFY + ordersId);
		String userId = ordersBulk.getUserId();
		/**
		 * 发短信通知用户
		 */

		String userPhone = userMapper.getPhoneById(userId);
		if (Util.isEmpty(userPhone)) {
			loggerOrdersBulk.info("该订单的用户信息不完整" + ordersId);
			return LianjiuResult.build(502, "该订单的用户信息不完整");
		}
		sendSMS.sendMessage("17", "+86", userPhone, ordersId);

		/**
		 * 启动定时器
		 */
		// 启动超时监控,定时器在定时器系统中开启
		String rep = HttpClientUtil.doPost(GlobalValueUtil.TIMER_BASE_URL + "orderBulk/orderConfirm/" + ordersId);
		if (Util.notEmpty(rep)) {
			LianjiuResult repObj = JsonUtils.jsonToPojo(rep, LianjiuResult.class);
			if (null != repObj) {
				Util.printModelDetails(repObj);
			}
		}
		return LianjiuResult.ok(ordersId);
	}

	/**
	 * 多参数搜索订单
	 */
	@Override
	public LianjiuResult searchOrders(String parameter, int pageNum, int pageSize) {
		loggerOrdersBulk.info("多参数搜索订单");
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<OrdersBulk> listOrdersBulk = ordersBulkMapper.searchOrders(parameter);
		if (pageNum == 1 && 0 == listOrdersBulk.size()) {
			loggerOrdersBulk.info("大宗订单为空");
			return LianjiuResult.build(501, "订单为空");
		}
		if (0 == listOrdersBulk.size()) {
			// loggerProductFurniture.info("家具商品为空");
			return LianjiuResult.build(502, "尚无更多订单");
		} else {
			// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
			Page<OrdersBulk> pageOrdersBulk = (Page<OrdersBulk>) listOrdersBulk;
			// loggerProductFurniture.info("总页数：" + pageFurniture.getPages());
			// 封装成相应数据对象
			LianjiuResult result = new LianjiuResult(listOrdersBulk);
			result.setTotal(pageOrdersBulk.getTotal());
			return result;
		}
	}

	/**
	 * 根据订单编号查询订单详情
	 */
	@Override
	public LianjiuResult getOrdersById(String orBulkId) {
		OrdersBulk ordersBulk = ordersBulkMapper.getOrderById(orBulkId);
		if (null == ordersBulk) {
			loggerOrdersBulk.info("订单对象为空");
			return LianjiuResult.build(501, "数据为空");
		}
		return LianjiuResult.ok(ordersBulk);
	}

}
