package com.lianjiu.timer.service.task;

import java.util.Iterator;
import java.util.Set;

import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;

public class OrdersFaceFacePayTask implements Runnable {

	private OrdersFacefaceMapper ordersFacefaceMapper;

	private JedisClient jedisClient;

	private Set<String> ordersIdSet;

	public OrdersFaceFacePayTask(OrdersFacefaceMapper ordersFacefaceMapper, Set<String> ordersIdSet,
			JedisClient jedisClient) {
		this.ordersFacefaceMapper = ordersFacefaceMapper;
		this.ordersIdSet = ordersIdSet;
		this.jedisClient = jedisClient;
	}

	@Override
	public void run() {
		/**
		 * 业务代码块
		 */
		Iterator<String> it = ordersIdSet.iterator();
		while (it.hasNext()) {
			String ordersIdKey = it.next();
			String ordersId = jedisClient.get(ordersIdKey);
			System.out.println(ordersIdKey + "-->" + ordersId);
			Byte status = ordersFacefaceMapper.selectOrdersStatus(ordersId);
			System.out.println(status);
			/**
			 * 清理redis
			 */
			if (GlobalValueUtil.ORDER_FACEFACE_CANCEL.equals(status)
					|| GlobalValueUtil.ORDER_FACEFACE_FINISH.equals(status)
					|| GlobalValueUtil.ORDER_FACEFACE_EVALUATE_YES.equals(status)) {
				System.out.println("删除锁：" + ordersId);
				jedisClient.del(ordersIdKey);
			}
		}
	}

}
