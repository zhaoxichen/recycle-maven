package com.lianjiu.timer.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lianjiu.common.tools.CalculateDate;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.orders.OrdersBulkMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersExpressMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.rest.mapper.orders.OrdersFurnitureMapper;
import com.lianjiu.timer.service.task.OrdersBulkConfirmTask;
import com.lianjiu.timer.service.task.OrdersExcellentTask;
import com.lianjiu.timer.service.task.OrdersExpressConfirmTask;
import com.lianjiu.timer.service.task.OrdersFaceFaceNightTask;
import com.lianjiu.timer.service.task.OrdersFaceFacePayTask;
import com.lianjiu.timer.service.task.OrdersFaceFaceTask;
import com.lianjiu.timer.service.task.OrdersFacefaceConfirmTask;
import com.lianjiu.timer.service.task.OrdersFurnitureTask;

/**
 * 基于xml的定时器 新增监控，需要到springmvc.xml里面添加拦截；监控订单各种超时
 * 
 * @author zhaoxi
 *
 */
public class OrdersTimer {
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;
	@Autowired
	private OrdersFacefaceMapper ordersFacefaceMapper;
	@Autowired
	private OrderFacefaceService orderFacefaceService;
	@Autowired
	private OrdersExpressMapper ordersExpressMapper;
	@Autowired
	private OrdersBulkMapper ordersBulkMapper;
	@Autowired
	private OrdersFurnitureMapper ordersFurnitureMapper;

	private OrdersExcellentTask ordersExcellentTask;

	private OrdersBulkConfirmTask ordersBulkConfirmTask;

	private OrdersFaceFaceTask ordersFaceFaceTask;

	private OrdersFacefaceConfirmTask ordersFacefaceConfirmTask;

	private OrdersFaceFaceNightTask ordersFaceFaceNightTask;

	private OrdersExpressConfirmTask ordersExpressConfirmTask;

	private OrdersFurnitureTask ordersFurnitureTask;

	/**
	 * 
	 * zhaoxi 2018年1月23日 descrption:精品订单倒计时，三十分钟 void
	 */
	public void ordersExcellentCountDown() {
		// 获取redis中所有相关订单的key
		Set<String> ordersIdSet = jedisClient.keys(GlobalValueJedis.ORDERS_EXCELLENT_COUNT_DOWN + "*");
		if (ordersIdSet.isEmpty()) {
			System.out.println("XMl:orders excellent paying run");
			return;
		}
		System.out.println("精品订单确认支付，筛选完毕，当前剩余倒计时的订单数：" + ordersIdSet.size());
		if (null == ordersExcellentTask) {
			ordersExcellentTask = new OrdersExcellentTask(ordersExcellentMapper, jedisClient);// 减轻垃圾回收期的压力
		}
		ordersExcellentTask.setOrdersIdSet(ordersIdSet);
		new Thread(ordersExcellentTask).start();
	}

	/**
	 * 
	 * zhaoxi 2018年1月23日 descrption:提交订单后，30分钟无加盟商抢单，自动派单的定时器 void
	 */
	public void ordersFaceFaceCountDown() {
		// 获取redis中所有相关订单的key
		Set<String> ordersIdSet = jedisClient.keys(GlobalValueJedis.ORDERS_FACEFACE_COUNT_DOWN + "*");
		if (ordersIdSet.isEmpty()) {
			System.out.println("XMl:orders faceface delivery run");
			return;
		}
		System.out.println("上门回收自动派单，筛选完毕，当前剩余倒计时的订单数：" + ordersIdSet.size());
		if (null == ordersFaceFaceTask) {
			System.out.println("创建对象OrdersFaceFaceTask一次");
			ordersFaceFaceTask = new OrdersFaceFaceTask(orderFacefaceService, jedisClient);// 减轻垃圾回收期的压力
		}
		ordersFaceFaceTask.setOrdersIdSet(ordersIdSet);
		new Thread(ordersFaceFaceTask).start();
	}

	/**
	 * 
	 * zhaoxi 2018年1月23日 descrption:加盟商修改价格了，用户30分钟不确认价格，自动取消订单 void
	 */
	public void ordersFacefacePriceConfirmCountDown() {
		// 获取redis中所有相关订单的key
		Set<String> ordersIdSet = jedisClient.keys(GlobalValueJedis.ORDERS_FACEFACE_CONFIRM_COUNT_DOWN + "*");
		if (ordersIdSet.isEmpty()) {
			System.out.println("XMl:orders faceface confirm run");
			return;
		}
		System.out.println("上门回收订单价格确认，筛选完毕，当前剩余倒计时的订单数：" + ordersIdSet.size());
		if (null == ordersFacefaceConfirmTask) {
			System.out.println("创建对象OrdersFacefaceConfirmTask一次");
			ordersFacefaceConfirmTask = new OrdersFacefaceConfirmTask(ordersFacefaceMapper, jedisClient);// 减轻垃圾回收期的压力
		}
		ordersFacefaceConfirmTask.setOrdersIdSet(ordersIdSet);
		new Thread(ordersFacefaceConfirmTask).start();
	}

	/**
	 * 
	 * zhaoxi 2017年11月28日 descrption: 夜猫订单 void
	 */
	public void ordersFaceOfNight() {
		// 获取redis中所有相关订单的key
		Set<String> ordersIdSet = jedisClient.keys(GlobalValueJedis.ORDERS_FACEFACE_NIGHT_LOCK + "*");
		if (ordersIdSet.isEmpty()) {
			System.out.println("XMl:orders faceface night run");
			return;
		}
		/**
		 * 如果某个时间段之内21:00-09:00 ,订单不做处理,存到缓存
		 */
		if (!CalculateDate.isBelong("09:00", "21:00")) {
			System.out.println("在夜猫时间段内的订单：" + ordersIdSet);
			return;
		}
		System.out.println("上门回收夜猫收订单，筛选完毕，当前剩余倒计时的订单数：" + ordersIdSet.size());
		if (null == ordersFaceFaceNightTask) {
			ordersFaceFaceNightTask = new OrdersFaceFaceNightTask(orderFacefaceService, jedisClient);// 减轻垃圾回收期的压力
		}
		ordersFaceFaceNightTask.setOrdersIdSet(ordersIdSet);
		new Thread(ordersFaceFaceNightTask).start();
	}

	/**
	 * 
	 * zhaoxi 2018年1月23日 descrption:快递回收订单价格确认定时器 void
	 */
	public void ordersPriceExpressConfirmCountDown() {
		// 获取redis中所有相关订单的key
		Set<String> ordersIdSet = jedisClient.keys(GlobalValueJedis.ORDERS_EXPRESS_CONFIRM_COUNT_DOWN + "*");
		if (ordersIdSet.isEmpty()) {
			System.out.println("XMl:orders express confirm run");
			return;
		}
		System.out.println("快递回收订单价格确认，筛选完毕，当前剩余倒计时的订单数：" + ordersIdSet.size());
		if (null == ordersExpressConfirmTask) {
			ordersExpressConfirmTask = new OrdersExpressConfirmTask(ordersExpressMapper, jedisClient);// 减轻垃圾回收期的压力
		}
		ordersExpressConfirmTask.setOrdersIdSet(ordersIdSet);
		new Thread(ordersExpressConfirmTask).start();
	}

	/**
	 * 
	 * zhaoxi 2017年12月26日 descrption:加盟商支付，订单加锁，清理订单锁
	 */
	public void ordersFaceFaceLockCountDown() {
		// 获取redis中所有相关订单的key
		Set<String> ordersIdSet = jedisClient.keys(GlobalValueJedis.ORDERS_FACEFACE_COUNT_DOWN_PAY_SYN + "*");
		if (ordersIdSet.isEmpty()) {
			System.out.println("XMl:orders faceface paying run");
			return;
		}
		new Thread(new OrdersFaceFacePayTask(ordersFacefaceMapper, ordersIdSet, jedisClient)).start();
	}

	/**
	 * 
	 * zhaoxi 2018年1月16日 descrption:大宗单修改价格后确认价格，24小时后自动取消 void
	 */
	public void ordersPriceBulkConfirmCountDown() {
		// 获取redis中所有相关订单的key
		Set<String> ordersIdSet = jedisClient.keys(GlobalValueJedis.ORDERS_BULK_CONFIRM_COUNT_DOWN + "*");
		if (ordersIdSet.isEmpty()) {
			System.out.println("XMl:orders bulk confirm run");
			return;
		}
		System.out.println("大宗订单确认价格，筛选完毕，当前剩余倒计时的订单数：" + ordersIdSet.size());
		if (null == ordersBulkConfirmTask) {
			ordersBulkConfirmTask = new OrdersBulkConfirmTask(ordersBulkMapper, jedisClient);// 减轻垃圾回收期的压力
		}
		ordersBulkConfirmTask.setOrdersIdSet(ordersIdSet);
		new Thread(ordersBulkConfirmTask).start();
	}

	/**
	 * 
	 * zhaoxi 2018年1月22日 descrption:家具订单付款倒计时 void
	 */
	public void ordersFurnitureCountDown() {
		// 获取redis中所有相关订单的key
		Set<String> ordersIdSet = jedisClient.keys(GlobalValueJedis.ORDERS_FURNITURE_COUNT_DOWN + "*");
		if (ordersIdSet.isEmpty()) {
			System.out.println("XMl:orders Furniture paying run");
			return;
		}
		System.out.println("家具订单付款倒计时，筛选完毕，当前剩余倒计时的订单数：" + ordersIdSet.size());
		if (null == ordersFurnitureTask) {
			ordersFurnitureTask = new OrdersFurnitureTask(ordersFurnitureMapper, jedisClient);// 减轻垃圾回收期的压力
		}
		ordersFurnitureTask.setOrdersIdSet(ordersIdSet);
		new Thread(ordersFurnitureTask).start();
	}
}
