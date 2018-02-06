package com.lianjiu.timer.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.dao.OrdersFacefaceDao;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.timer.service.OrderFacefaceService;

/**
 * 上门回收服务层
 * 
 * @author zhaoxi
 *
 */

@Service
public class OrderFacefaceServiceImpl implements OrderFacefaceService {

	@Autowired
	private OrdersFacefaceMapper ordersFacefaceMapper;
	@Autowired
	private OrdersFacefaceDao ordersFacefaceDao;

	@Autowired
	private JedisClient jedisClient;

	private static Logger loggerFaceface = Logger.getLogger("faceface");

	/**
	 * 下列代码块功能：接收其他系统发送的请求，将相应的订单编号记录到定时器中
	 */

	/**
	 * 加盟商修改价格了，用户30分钟不确认价格，自动取消订单
	 */
	@Override
	public LianjiuResult orderConfirm(String ordersId) {
		loggerFaceface.info("上门回收订单确认价格：" + ordersId);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CONFIRM_COUNT_DOWN + ordersId, ordersId);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CONFIRM_COUNT_DOWN_PRICE + ordersId, ordersId);// （当前测试设置为10分钟）
		jedisClient.expire(GlobalValueJedis.ORDERS_FACEFACE_CONFIRM_COUNT_DOWN_PRICE + ordersId, 1800);
		return LianjiuResult.ok(ordersId);
	}

	@Override
	public LianjiuResult orderConfirmDel(String ordersId) {
		loggerFaceface.info(ordersId + "订单已确认");
		jedisClient.del(GlobalValueJedis.ORDERS_FACEFACE_CONFIRM_COUNT_DOWN + ordersId);
		jedisClient.del(GlobalValueJedis.ORDERS_FACEFACE_CONFIRM_COUNT_DOWN_PRICE + ordersId);
		return LianjiuResult.ok(ordersId);
	}

	/**
	 * 订单放出后，30分钟无加盟商抢单，自动派单给附近的加盟商
	 */
	@Override
	public LianjiuResult orderDispatch(String ordersId) {
		loggerFaceface.info("30分钟后自动派单-->" + ordersId);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_COUNT_DOWN + ordersId, ordersId);// （当前测试设置为10分钟）
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_COUNT_DOWN_DELIVERY + ordersId, ordersId);// （当前测试设置为10分钟）
		jedisClient.expire(GlobalValueJedis.ORDERS_FACEFACE_COUNT_DOWN_DELIVERY + ordersId, 1800);// 无操作，半小时后订单被自动取消
		return LianjiuResult.ok(ordersId);
	}

	@Override
	public LianjiuResult orderDispatchDel(String ordersId) {
		loggerFaceface.info(ordersId + "订单已被抢");
		jedisClient.del(GlobalValueJedis.ORDERS_FACEFACE_COUNT_DOWN + ordersId);
		jedisClient.del(GlobalValueJedis.ORDERS_FACEFACE_COUNT_DOWN_DELIVERY + ordersId);
		return LianjiuResult.ok(ordersId);
	}

	/**
	 * 夜猫订单，锁定
	 */
	@Override
	public LianjiuResult orderNightDispatch(String ordersId, String price) {
		loggerFaceface.info("夜猫订单-->" + ordersId);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_NIGHT_PRICE + ordersId, price);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_NIGHT_LOCK + ordersId, ordersId);// （当前测试设置为10分钟）
		return LianjiuResult.ok(ordersId);
	}

	/**
	 * 下列代码块功能：定时器时间到，进行逻辑处理
	 */

	/**
	 * 夜猫订单时间到,释放夜猫订单
	 */
	@Override
	public LianjiuResult orderStatusReduction(List<String> ordersIdList) {
		int rowAffact = ordersFacefaceMapper.updateForOrderStatusReduction(ordersIdList);
		return LianjiuResult.ok(rowAffact);
	}

	/**
	 * 夜猫订单释放后调用
	 */
	@Override
	public LianjiuResult orderNightDispatchHandle(String ordersId) {
		String price = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_NIGHT_PRICE + ordersId);// 从redis中读取对应订单的价格
		if (Util.notEmpty(price) && GlobalValueUtil.ORDER_ACCESS_PRICE <= Double.parseDouble(price)) {
			loggerFaceface.info("回收价格大于100，后台派单给指定的加盟商");
			// 后台自动派单给指定加盟商
			ordersFacefaceDao.dispatchFaceOrderFixed(ordersId);
		} else {
			loggerFaceface.info("订单小于100块，订单放出等待加盟商抢单。开启定时器，定时器结束后，强制派单给指定加盟商");
			// 消息推送给附近加盟商
			ordersFacefaceDao.pushFaceOrder(ordersId);
			// 启动30分钟倒计时
			this.orderDispatch(ordersId);
		}
		jedisClient.del(GlobalValueJedis.ORDERS_FACEFACE_NIGHT_PRICE + ordersId);// 清缓存
		return LianjiuResult.ok(ordersId);
	}

	/**
	 * 订单放出的时限到期，无加盟商抢单，强制派单给指定加盟商
	 */
	@Override
	public LianjiuResult orderAutoDispatch(String ordersId) {
		loggerFaceface.info("超时自动派单的id：" + ordersId);
		// 查看订单状态
		Integer status = ordersFacefaceMapper.getOrdersStatusByOrdersId(ordersId);
		System.err.println("订单状态:" + status);
		if (GlobalValueUtil.ORDER_FACEFACE_DISPATCH.equals(status.byteValue())) {
			return ordersFacefaceDao.dispatchFaceOrder(ordersId);
		}
		return LianjiuResult.ok(ordersId + "已经抢单");
	}

}
