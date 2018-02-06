package com.lianjiu.timer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.timer.service.OrderExcellentService;

/**
 * 精品订单服务层
 * 
 * @author zhaoxi
 * 
 */
@Service
public class OrderExcellentServiceImpl implements OrderExcellentService {
	@Autowired
	private JedisClient jedisClient;

	/**
	 * 
	 * zhaoxi 2018年1月22日 descrption:30分钟不付款，取消
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@Override
	public LianjiuResult orderCancel(String ordersId) {
		jedisClient.set(GlobalValueJedis.ORDERS_EXCELLENT_COUNT_DOWN + ordersId, ordersId);
		jedisClient.set(GlobalValueJedis.ORDERS_EXCELLENT_COUNT_DOWN_PRICE + ordersId, ordersId);// （当前测试设置为10分钟）
		jedisClient.expire(GlobalValueJedis.ORDERS_EXCELLENT_COUNT_DOWN_PRICE + ordersId, 1800);

		return LianjiuResult.ok(ordersId);
	}

}
