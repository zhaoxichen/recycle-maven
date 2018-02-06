package com.lianjiu.timer.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.timer.service.OrderExpressService;

/**
 * 快递回收
 * 
 * @author zhaoxi
 *
 */

@Service
public class OrderExpressServiceImpl implements OrderExpressService {
	@Autowired
	private JedisClient jedisClient;

	private static Logger loggerExpress = Logger.getLogger("express");

	@Override
	public LianjiuResult orderConfirm(String ordersId) {
		loggerExpress.info("快递订单确认价格：" + ordersId);
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CONFIRM_COUNT_DOWN + ordersId, ordersId);
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CONFIRM_COUNT_DOWN_PRICE + ordersId, ordersId);// （当前测试设置为10分钟）
		jedisClient.expire(GlobalValueJedis.ORDERS_EXPRESS_CONFIRM_COUNT_DOWN_PRICE + ordersId, 1800);

		return LianjiuResult.ok(ordersId);

	}

	@Override
	public LianjiuResult orderConfirmDel(String ordersId) {
		loggerExpress.info(ordersId + "订单已确认");
		jedisClient.del(GlobalValueJedis.ORDERS_EXPRESS_CONFIRM_COUNT_DOWN + ordersId);
		jedisClient.del(GlobalValueJedis.ORDERS_EXPRESS_CONFIRM_COUNT_DOWN_PRICE + ordersId);
		return LianjiuResult.ok(ordersId);
	}

}
