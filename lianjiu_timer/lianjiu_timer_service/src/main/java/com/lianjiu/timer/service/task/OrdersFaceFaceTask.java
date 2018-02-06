package com.lianjiu.timer.service.task;

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.timer.service.OrderFacefaceService;

public class OrdersFaceFaceTask implements Runnable {

	private OrderFacefaceService orderFacefaceService;

	private JedisClient jedisClient;

	private Set<String> ordersIdSet;

	private static Logger loggerVisit = Logger.getLogger("visit");

	public OrdersFaceFaceTask(OrderFacefaceService orderFacefaceService, JedisClient jedisClient) {
		this.orderFacefaceService = orderFacefaceService;
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
		/**
		 * 业务代码块
		 */
		Iterator<String> it = ordersIdSet.iterator();
		long ttl = 0;

		while (it.hasNext()) {
			String ordersIdKey = it.next();
			String ordersId = jedisClient.get(ordersIdKey);
			System.out.println(ordersIdKey + "-->" + ordersId);
			ttl = jedisClient.ttl(GlobalValueJedis.ORDERS_FACEFACE_COUNT_DOWN_DELIVERY + ordersId);
			System.out.println(ordersId + "上门订单派单剩余时间-->" + ttl);
			if (-2 == ttl) {
				loggerVisit.info(ordersId + "订单过期");
				loggerVisit.info(ordersIdKey + "，上门回收订单，自动派单");
				orderFacefaceService.orderAutoDispatch(ordersId);
				jedisClient.del(ordersIdKey);
			}
		}
	}

}
