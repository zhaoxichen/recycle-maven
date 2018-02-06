package com.lianjiu.timer.service.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.orders.OrdersBulkMapper;

public class OrdersBulkConfirmTask implements Runnable {

	private OrdersBulkMapper ordersBulkMapper;

	private JedisClient jedisClient;

	private Set<String> ordersIdSet;

	private static Logger loggerVisit = Logger.getLogger("visit");
	
	public OrdersBulkConfirmTask(OrdersBulkMapper ordersBulkMapper, JedisClient jedisClient) {
		this.ordersBulkMapper = ordersBulkMapper;
		this.jedisClient = jedisClient;
	}

	public Set<String> getOrdersIdSet() {
		return ordersIdSet;
	}

	public synchronized void setOrdersIdSet(Set<String> ordersIdSet) {
		this.ordersIdSet = ordersIdSet;
	}

	@Override
	public void run() {
		List<String> ordersIdList = null;
		/**
		 * 业务代码块
		 */
		Iterator<String> it = ordersIdSet.iterator();
		long ttl = 0;
		boolean isExpre = false;
		while (it.hasNext()) {
			String ordersIdKey = it.next();
			String ordersId = jedisClient.get(ordersIdKey);
			System.out.println(ordersIdKey + "-->" + ordersId);
			ttl = jedisClient.ttl(GlobalValueJedis.ORDERS_BULK_CONFIRM_COUNT_DOWN_PRICE + ordersId);
			System.out.println(ordersId + "大宗交易订单确认价格剩余时间-->" + ttl);
			if (-2 == ttl) {
				if (null == ordersIdList) {
					ordersIdList = new ArrayList<String>();
				}
				ordersIdList.add(ordersId);
				isExpre = true;
				loggerVisit.info(ordersId + "订单过期");
				loggerVisit.info(ordersIdKey + "，大宗交易订单，确认价格");
				jedisClient.del(ordersIdKey);
			}
		}
		/**
		 * 状态从1变成0
		 */
		if (isExpre) {
			ordersBulkMapper.ordersAutoCancel(ordersIdList);
		}
	}

}
