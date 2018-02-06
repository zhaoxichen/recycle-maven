package com.lianjiu.timer.service.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.timer.service.OrderFacefaceService;

public class OrdersFaceFaceNightTask implements Runnable {

	private OrderFacefaceService orderFacefaceService;

	private JedisClient jedisClient;

	private Set<String> ordersIdSet;

	private static Logger loggerVisit = Logger.getLogger("visit");

	public OrdersFaceFaceNightTask(OrderFacefaceService orderFacefaceService, JedisClient jedisClient) {
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

		List<String> ordersIdList = null;
		/**
		 * 业务代码块
		 */
		Iterator<String> it = ordersIdSet.iterator();
		boolean isExpre = false;
		while (it.hasNext()) {
			String ordersIdKey = it.next();
			String ordersId = jedisClient.get(ordersIdKey);
			if (null == ordersIdList) {
				ordersIdList = new ArrayList<String>();
			}
			ordersIdList.add(ordersId);
			isExpre = true;
			loggerVisit.info(ordersId + "夜猫限制解除");
			loggerVisit.info(ordersIdKey + "上门回收订单，夜猫限制解除");
			jedisClient.del(ordersIdKey);
		}
		// 去更新释放的订单的状态，操作一下，更新了订单的时间
		if (isExpre) {
			orderFacefaceService.orderStatusReduction(ordersIdList);
			// 去处理释放的订单
			for (String ordersId : ordersIdList) {
				orderFacefaceService.orderNightDispatchHandle(ordersId);
			}
		}

	}

}
