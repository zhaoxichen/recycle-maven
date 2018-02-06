package com.lianjiu.timer.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.timer.service.OrderBulkService;

/**
 * 家具订单
 * 
 * @author zhaoxi
 *
 */
@Service
public class OrderBulkServiceImpl implements OrderBulkService {

	@Autowired
	private JedisClient jedisClient;
	private static Logger loggerOrdersBulk = Logger.getLogger("ordersBulk");

	@Override
	public LianjiuResult orderConfirm(String ordersId) {
		loggerOrdersBulk.info("大宗订单确认价格：" + ordersId);
		jedisClient.set(GlobalValueJedis.ORDERS_BULK_CONFIRM_COUNT_DOWN + ordersId, ordersId);
		jedisClient.set(GlobalValueJedis.ORDERS_BULK_CONFIRM_COUNT_DOWN_PRICE + ordersId, ordersId);// （当前测试设置为10分钟）
		/**
		 * 无操作，3600 * 24 二十四小时后订单被自动取消
		 */
		jedisClient.expire(GlobalValueJedis.ORDERS_BULK_CONFIRM_COUNT_DOWN_PRICE + ordersId, 600);
		return LianjiuResult.ok(ordersId);
	}

	@Override
	public LianjiuResult orderConfirmDel(String ordersId) {
		loggerOrdersBulk.info(ordersId + "订单已确认");
		jedisClient.del(GlobalValueJedis.ORDERS_BULK_CONFIRM_COUNT_DOWN + ordersId);
		jedisClient.del(GlobalValueJedis.ORDERS_BULK_CONFIRM_COUNT_DOWN_PRICE + ordersId);
		return LianjiuResult.ok(ordersId);
	}

}
