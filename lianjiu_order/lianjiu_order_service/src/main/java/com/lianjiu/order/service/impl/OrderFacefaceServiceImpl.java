package com.lianjiu.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.tools.CalculateDate;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.HttpClientUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.common.utils.sendSMS;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.OrdersFacefaceDetails;
import com.lianjiu.model.OrdersFacefaceItem;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.model.OrdersPayment;
import com.lianjiu.model.Product;
import com.lianjiu.model.User;
import com.lianjiu.model.UserDetails;
import com.lianjiu.order.service.OrderFacefaceService;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.dao.OrdersFacefaceDao;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.rest.mapper.OrdersPaymentMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceDetailsMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.rest.mapper.orders.OrdersItemMapper;
import com.lianjiu.rest.mapper.product.ProductMapper;
import com.lianjiu.rest.mapper.user.UserDetailsMapper;
import com.lianjiu.rest.mapper.user.UserMapper;
import com.lianjiu.rest.model.OrdersFacefaceFull;
import com.lianjiu.rest.model.OrdersFacefaceItemVo;
import com.lianjiu.rest.model.OrdersItemInfo;
import com.lianjiu.rest.tool.JedisTool;

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
	private UserMapper userMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private OrdersItemMapper ordersItemMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private OrdersFacefaceDetailsMapper ordersFacefaceDetailsMapper;

	@Autowired
	private UserDetailsMapper userDetailsMapper;

	@Autowired
	private OrdersPaymentMapper ordersPaymentMapper;

	@Autowired
	private JedisClient jedisClient;

	private String memberPhone;
	private static Logger loggerOrdersFaceFace = Logger.getLogger("faceface");

	@Override
	public LianjiuResult getFacefaceById(String facefaceId) {
		if (Util.isEmpty(facefaceId)) {
			return LianjiuResult.build(501, "请指定上门回收订单id");
		}
		// 去数据库查询
		OrdersFaceface faceface = ordersFacefaceMapper.selectByPrimaryKey(facefaceId);
		return LianjiuResult.ok(faceface);
	}

	/**
	 * 上门回收结算
	 */
	@Override
	public LianjiuResult productBalance(List<String> productIdList, String userId) {
		loggerOrdersFaceFace.info(userId + "上门回收订单结算：" + productIdList);
		// 生成订单id
		String ordersId = IDUtils.genOrdersId();
		// 从redis中获取usercare
		List<OrdersItem> itemList = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.ORDERS_FACEFACE_CART + userId, OrdersItem.class);
		if (null == itemList) {
			return LianjiuResult.build(502, "购物车信息丢失");
		}
		// 暂存要插入数据库的数据
		List<OrdersItem> itemListTemp = new ArrayList<OrdersItem>();
		// 总价
		BigDecimal orderPrice = new BigDecimal("0");
		// 价格最高的商品
		Double cmpResult = 0.00;
		BigDecimal maxPrice = new BigDecimal("0");
		// 最高价格商品的图片
		String maximage = null;
		/**
		 * 遍历redis中的产品
		 */
		for (OrdersItem ordersItem : itemList) {
			// 判断是否为选中的产品
			if (!productIdList.contains(ordersItem.getOrItemsId())) {
				continue;
			}
			loggerOrdersFaceFace.info("订单商品：" + ordersItem.getOrItemsName());
			/**
			 * 筛选最高价格的item的图片
			 */
			String itemsPrice = ordersItem.getOrItemsPrice();// 单价
			String itemNum = ordersItem.getOrItemsNum();// 数量
			loggerOrdersFaceFace.info("运算前：" + itemNum);
			BigDecimal itemsPriceBigDecimal = null;
			BigDecimal itemNumBigDecimal = null;
			BigDecimal itemsPriceTotalBigDecimal = null;
			if (null != itemsPrice && null != itemNum) {
				itemsPriceBigDecimal = new BigDecimal(itemsPrice); // 单价
				itemNumBigDecimal = new BigDecimal(itemNum); // 数量
				itemsPriceTotalBigDecimal = itemsPriceBigDecimal.multiply(itemNumBigDecimal);// 相乘
				itemsPriceTotalBigDecimal = itemsPriceTotalBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);// 保留两位小数,四舍五入
				orderPrice = orderPrice.add(itemsPriceTotalBigDecimal);// 累加为订单的预估总价
				/**
				 * 取出最高价格的item对应的图片
				 */
				cmpResult = maxPrice.subtract(itemsPriceBigDecimal).doubleValue();
				if (0 > cmpResult) {
					maxPrice = itemsPriceBigDecimal; // 暂存最高价格
					maximage = ordersItem.getOrItemsPicture();// 取出最高价格的item对应的图片
				}
			}
			loggerOrdersFaceFace.info("运算后：" + ordersItem.getOrItemsNum());
			/**
			 * 给item追加赋值
			 */
			ordersItem.setOrItemsBeforePrice(itemsPrice);
			ordersItem.setOrItemsAccountPrice(itemsPriceTotalBigDecimal.toString());
			ordersItem.setOrItemsChooseFlag(1);// 被选中
			ordersItem.setOrItemsStatus(0);
			// 订单id
			ordersItem.setOrdersId(ordersId);
			// 回收类型
			ordersItem.setOrItemsRecycleType("上门回收");
			ordersItem.setOrItemsCreated(new Date());
			ordersItem.setOrItemsUpdated(new Date());
			/**
			 * 暂存
			 */
			itemListTemp.add(ordersItem);

		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 最高Item的总价
		map.put("maxPrice", maxPrice.toString());
		map.put("maximage", maximage);
		// 总价
		map.put("totalprice", orderPrice.toString());
		map.put("productIdList", productIdList);
		/**
		 * 把剩余的产品存回去
		 */
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART + userId, JsonUtils.objectToJson(itemList));
		/**
		 * 缓存结算之后要加载的数据
		 */
		if (0 == productIdList.size()) {
			return LianjiuResult.build(503, "未选择商品");
		}
		System.err.println(orderPrice.toString() + "------------------" + maximage);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART_BALANCE_PRICE + userId, orderPrice.toString());
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART_ITEM_PRICE_MAX + userId, maxPrice.toString());
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART_ITEM_IMAGE_MAX + userId, maximage);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART_ITEM_CHOOSER + userId,
				JsonUtils.objectToJson(productIdList));
		return LianjiuResult.ok(map);
	}

	/**
	 * 
	 * zhaoxi 2017年10月31日 descrption:结算之后的数据
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@Override
	public LianjiuResult productBalanceAfter(String userId) {

		String totalprice = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_CART_BALANCE_PRICE + userId);
		String maxPrice = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_CART_ITEM_PRICE_MAX + userId);
		String maximage = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_CART_ITEM_IMAGE_MAX + userId);
		/**
		 * 转成字符串
		 */
		loggerOrdersFaceFace.info(totalprice + "------------------" + maximage);
		Map<String, Object> map = new HashMap<String, Object>();
		// 总价
		map.put("totalprice", totalprice);
		map.put("maxPrice", maxPrice);
		map.put("maximage", maximage);
		return LianjiuResult.ok(map);
	}

	/**
	 * 从redis的回收车中删除一个商品
	 */
	@Override
	public LianjiuResult reduceProductFromCar(String itemId, String userId) {
		loggerOrdersFaceFace.info("token:" + GlobalValueJedis.ORDERS_FACEFACE_CART + userId);
		List<OrdersItem> list = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.ORDERS_FACEFACE_CART + userId, OrdersItem.class);
		if (null == list) {
			return LianjiuResult.build(501, "回收车为空");
		} else {
			for (OrdersItem productItem : list) {
				if (productItem.getOrItemsId().equals(itemId)) {
					list.remove(productItem);
					jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART + userId, JsonUtils.objectToJson(list));
					return LianjiuResult.ok("删除成功");
				}
			}
			Util.printModelDetails(list);
		}
		return LianjiuResult.build(501, "删除失败");
	}

	/**
	 * 提交上门回收订单
	 */
	@Override
	public LianjiuResult submit(OrdersFaceface faceface) {
		String userId = faceface.getUserId();
		/**
		 * 从redis中取出用户
		 */
		User user = JedisTool.checkObjectFormRedis(jedisClient, GlobalValueJedis.REDIS_USER_SESSION_KEY + userId,
				User.class);
		if (null == user) {
			// 发个短信通知工作人员
			sendSMS.sendMessage("16", "+86", GlobalValueJedis.ADMIN_PHONE,
					GlobalValueJedis.REDIS_USER_SESSION_KEY + userId + "--提交上门回收订单");
			loggerOrdersFaceFace.info("掉线用户的key：" + GlobalValueJedis.REDIS_USER_SESSION_KEY + userId);
			loggerOrdersFaceFace
					.info("掉线用户的value：" + jedisClient.get(GlobalValueJedis.REDIS_USER_SESSION_KEY + userId));
			return LianjiuResult.build(502, "用户已经掉线");
		}
		String productIdListJson = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_CART_ITEM_CHOOSER + userId);
		if (Util.isEmpty(productIdListJson)) {

		}
		List<String> productIdList = JsonUtils.jsonToList(productIdListJson, String.class);
		String totalprice = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_CART_BALANCE_PRICE + userId);
		// 生成订单id
		String ordersId = IDUtils.genOrdersId();
		// 从redis中获取usercare
		String userCartFaceface = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_CART + userId);
		if (Util.isEmpty(userCartFaceface)) {
			return LianjiuResult.build(503, "购物车信息丢失");
		}
		List<OrdersItem> itemList = JsonUtils.jsonToList(userCartFaceface, OrdersItem.class);
		// 暂存redis剩余的数据
		List<OrdersItem> itemListRedis = new ArrayList<OrdersItem>();
		// 暂存要插入数据库的数据
		List<OrdersItem> itemListTemp = new ArrayList<OrdersItem>();
		// 总价
		BigDecimal orderPrice = new BigDecimal("0");
		// 价格最高的商品
		int cmpResult = 0;
		String maxPrice = "0";
		// 最高价格商品的名称
		String maxName = null;
		// 最高价格商品的图片
		String maximage = null;
		/**
		 * 遍历redis中的产品
		 */
		for (OrdersItem ordersItem : itemList) {
			// 判断是否为选中的产品
			if (!productIdList.contains(ordersItem.getOrItemsId())) {
				/**
				 * 保存到未选中的列表
				 */
				itemListRedis.add(ordersItem);
				continue;
			}
			/**
			 * 筛选最高价格的item的图片
			 */
			String itemsPrice = ordersItem.getOrItemsPrice();
			if (null != itemsPrice) {
				loggerOrdersFaceFace.info("当前价格---" + ordersItem.getOrItemsPrice());
				cmpResult = maxPrice.compareTo(itemsPrice);
				if (0 > cmpResult) {
					maxPrice = itemsPrice; // 作为最高价格的item
					maxName = ordersItem.getOrItemsName();// 取出对应的名称
					maximage = ordersItem.getOrItemsPicture();// 取出对应的图片
				}
				orderPrice = orderPrice.add(new BigDecimal(itemsPrice));
			}
			loggerOrdersFaceFace.info("结算时候：" + ordersItem.getOrItemsNum());
			/**
			 * 给item追加赋值
			 */
			ordersItem.setOrItemsStatus(0);
			// ordersItem.setOrItemsBeforePrice(orItemsBeforePrice);
			// 订单id
			ordersItem.setOrdersId(ordersId);
			// 回收类型
			ordersItem.setOrItemsRecycleType("上门回收");
			ordersItem.setOrItemsCreated(new Date());
			ordersItem.setOrItemsUpdated(new Date());
			/**
			 * 暂存
			 */
			itemListTemp.add(ordersItem);

		}
		/**
		 * 生成订单
		 */
		if (null == maxName) {
			return LianjiuResult.build(504, "重复提交订单！");
		}
		faceface.setOrFacefaceId(ordersId);// 订单id
		faceface.setOrFacefaceBrothersId("waitting");
		faceface.setOrItemsNamePreview(maxName);// item预览名称
		faceface.setOrItemsPictruePreview(maximage);// item预览图
		Long categoryId = categoryMapper.selectByCategoryName("上门回收");
		faceface.setCategoryId(categoryId);
		faceface.setUserId(userId);
		faceface.setUsername(user.getUsername());
		faceface.setOrFacefaceUpdated(new Date());
		faceface.setOrFacefaceCreated(new Date());
		faceface.setOrFacefaceStatus(GlobalValueUtil.ORDER_FACEFACE_DISPATCH);// 派单中
		// 添加订单详情
		OrdersFacefaceDetails details = new OrdersFacefaceDetails();
		String orFfDetailsId = IDUtils.genOrdersId();
		details.setOrFfDetailsId(orFfDetailsId);
		details.setOrFacefaceId(ordersId);
		details.setOrFfDetailsPrice(totalprice); // 预估价格
		details.setOrFfDetailsRetrPrice(totalprice);// 默认同意预估价格
		details.setOrFfDetailsUpdated(new Date());
		// 存到数据库
		try {
			ordersFacefaceDetailsMapper.insert(details);
			ordersFacefaceMapper.insert(faceface);// 订单成功提交
			int count = ordersItemMapper.addItemList(itemListTemp);
			if (count == 0) {
				// 删除空订单
				ordersFacefaceMapper.deleteByPrimaryKey(ordersId);
				ordersFacefaceDetailsMapper.deleteByPrimaryKey(orFfDetailsId);
				return LianjiuResult.build(504, "空订单");
			}
			// 预支付
			OrdersPayment payment = new OrdersPayment(IDUtils.genPaymentId(), ordersId, totalprice, userId);
			ordersPaymentMapper.insert(payment);
		} catch (RuntimeException e) {
			loggerOrdersFaceFace.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		/**
		 * 把剩余的产品存回去
		 */
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART + userId, JsonUtils.objectToJson(itemListRedis));
		memberPhone = userMapper.getPhoneById(userId);
		// 用户积分添加
		UserDetails userDetails = new UserDetails();
		String udetailsId = user.getUserDetailsId();
		userDetails.setUdetailsId(udetailsId);
		Integer integral = userDetailsMapper.selectIntegralById(udetailsId);
		// 用户首次提交订单，积分+100
		if (10 == integral) {
			userDetails.setUdetailsIntegral(integral + GlobalValueUtil.FIRST_INTEGRAL);
		} else {
			userDetails.setUdetailsIntegral(integral + GlobalValueUtil.AFTER_INTEGRAL);
		}
		Integer counts = 0;
		try {
			counts = userDetailsMapper.modifyIntegeralById(userDetails);
		} catch (RuntimeException e) {
			loggerOrdersFaceFace.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		if (counts == 0) {
			return LianjiuResult.build(505, "积分添加出错");
		}
		/**
		 * 正常处理订单的时间段09:00-21:00
		 */
		if (CalculateDate.isBelong("09:00", "21:00")) {
			// 给用户发送信息提示提交成功
			new Thread(new Runnable() {
				public void run() {
					sendSMS.sendMessage("7", "+86", memberPhone, "");
				}
			}).start();
		} else {
			loggerOrdersFaceFace.info("totalPrice：" + totalprice);
			String rep = HttpClientUtil.doPost(
					GlobalValueUtil.TIMER_BASE_URL + "orderFaceface/orderNightDispatch/" + ordersId + "/" + totalprice);
			loggerOrdersFaceFace.info(rep);
			// 给用户发送信息提示提交成功，但属于夜猫订单
			new Thread(new Runnable() {
				public void run() {
					sendSMS.sendMessage("7", "+86", memberPhone, "但此时是深夜，订单将会在下一个工作日进行处理");
				}
			}).start();
			return LianjiuResult.ok(ordersId);
		}
		if (Util.notEmpty(totalprice) && GlobalValueUtil.ORDER_ACCESS_PRICE <= Double.parseDouble(totalprice)) {
			// 强制派单给指定的加盟商
			loggerOrdersFaceFace.info("回收价格大于100，后台派单给指定的加盟商");
			ordersFacefaceDao.dispatchFaceOrderFixed(ordersId);
		} else {
			loggerOrdersFaceFace.info("订单小于100块，订单放出等待加盟商抢单。开启定时器，定时器结束后，强制派单给距离最近的加盟商");
			// 消息推送给附近加盟商
			ordersFacefaceDao.pushFaceOrder(ordersId);
			// 启动30分钟倒计时
			String rep = HttpClientUtil
					.doPost(GlobalValueUtil.TIMER_BASE_URL + "orderFaceface/orderDispatch/" + ordersId);
			loggerOrdersFaceFace.info(rep);
		}
		return LianjiuResult.ok(ordersId);
	}

	@Override
	public LianjiuResult updateFaceface(OrdersFaceface faceface) {
		if (null == faceface) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(faceface.getOrFacefaceId())) {
			return LianjiuResult.build(501, "请指定要更新的上门回收订单id");
		}
		faceface.setOrFacefaceUpdated(new Date());
		// 去数据库更新
		int rowsAffected = 0;
		try {
			rowsAffected = ordersFacefaceMapper.updateByPrimaryKeySelective(faceface);
		} catch (RuntimeException e) {
			loggerOrdersFaceFace.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteFaceface(String facefaceId) {
		if (Util.isEmpty(facefaceId)) {
			return LianjiuResult.build(501, "请指定要删除的上门回收订单id");
		}
		// 去数据库删除
		int rowsAffected = 0;
		try {
			rowsAffected = ordersFacefaceMapper.deleteByPrimaryKey(facefaceId);
		} catch (RuntimeException e) {
			loggerOrdersFaceFace.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult getOrderItemsById(String orFaceItemId) {
		if (Util.isEmpty(orFaceItemId)) {
			return LianjiuResult.build(501, "请指定要查询的商品记录id");
		}
		OrdersItem item = ordersItemMapper.selectByPrimaryKey(orFaceItemId);
		LianjiuResult result = new LianjiuResult(item);
		return result;
	}

	@Override
	public LianjiuResult getParamData(String productId) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(501, "请指定要查询的订单id");
		}
		Product p = productMapper.selectByPrimaryKey(productId);
		String productParamData = null;
		if (p != null) {
			productParamData = p.getProductParamData();
		}
		return LianjiuResult.ok(productParamData);
	}

	@Override
	public LianjiuResult modifyParam(String orItemsId, String orItemsParamModify) {
		if (Util.isEmpty(orItemsId)) {
			return LianjiuResult.build(501, "请指定要更新的上门回收item是id");
		}
		if (Util.isEmpty(orItemsParamModify)) {
			return LianjiuResult.build(501, "请传入要更新的修改的产品参数模版");
		}
		OrdersFacefaceItem item = new OrdersFacefaceItem();
		item.setOrFfItemId(orItemsId);
		item.setOrFfItemUpdated(new Date());
		int rowsAffected = 0;
		try {
			rowsAffected = ordersItemMapper.updateByPrimaryKeySelective(item);
		} catch (RuntimeException e) {
			loggerOrdersFaceFace.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult addHomeVisitTime(String orFacefaceId, String visitTime) {
		if (StringUtil.hasEmpty(orFacefaceId, visitTime)) {
			return LianjiuResult.build(501, "请指定正确的数据");
		}
		LianjiuResult ljr = new LianjiuResult();
		// 去数据库添加
		int count = 0;
		try {
			count = ordersFacefaceMapper.addHomeVisitTime(orFacefaceId, visitTime);
		} catch (RuntimeException e) {
			loggerOrdersFaceFace.info("捕获异常：" + e.getMessage());
			ljr.setStatus(510);
			ljr.setMsg("数据异常");
		}
		ljr.setStatus(200);
		ljr.setMsg(count + "");
		return LianjiuResult.ok();
	}

	@Override
	public LianjiuResult getHomeVistList(String userId, int pageNum, int pageSize) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(501, "请指定正确的用户id");
		}
		Map<String, Object> results = new HashMap<>();
		LianjiuResult result = new LianjiuResult(results);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<OrdersFacefaceFull> orderList = ordersFacefaceMapper.selectListByUserId(userId);
		if (orderList != null && orderList.size() != 0) {
			for (OrdersFacefaceFull ordersFacefaceFull : orderList) {
				OrdersFacefaceDetails maxprice = ordersFacefaceDetailsMapper
						.selectByFacefaceId(ordersFacefaceFull.getOrFacefaceId());
				if (maxprice != null) {
					ordersFacefaceFull.setOrFfDetailsPrice(maxprice.getOrFfDetailsPrice());
					if (null == maxprice.getOrFfDetailsRetrPrice()) {
						ordersFacefaceFull.setOrFfDetailsRetrPrice("");
					} else {
						ordersFacefaceFull.setOrFfDetailsRetrPrice(maxprice.getOrFfDetailsRetrPrice());
					}
				}
				String maxImage = ordersItemMapper.selectPriceMaxImage(ordersFacefaceFull.getOrFacefaceId());
				ordersFacefaceFull.setMaxImage(maxImage);
			}
		}
		Page<OrdersFacefaceFull> listFaceface = (Page<OrdersFacefaceFull>) orderList;
		result.setTotal(listFaceface.getTotal());
		results.put("orderList", orderList);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 回收状态(type=1派单中 type=2上门中 type=3已结算 type=0取消)
	 */
	@Override
	public LianjiuResult getHomeVistStutsList(String userId, Integer type, int pageNum, int pageSize) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(501, "请指定正确的数据");
		}
		Map<String, Object> results = new HashMap<>();
		LianjiuResult result = new LianjiuResult(results);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);

		/*
		 * OrdersFaceface info = new OrdersFaceface(); info.setUserId(userId);
		 * info.setOrFacefaceStatus((byte) status); List<OrdersFacefaceFull>
		 * orderList = ordersFacefaceMapper.getHomeVistStutsList(info);
		 */
		List<Byte> statusList = new ArrayList<>();
		switch (type) {
		case 0:// 取消
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_CANCEL);
			break;
		case 1:// 派单中
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_DISPATCH);
			break;
		case 2:// 上门中
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_VISITING);// 派单上门中
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_VISITING_GRAB);// 抢单上门中
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_VISITING_GRAB_MODIFY);// 派单上门中修改价格
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_VISITING_GRAB_AGREE);// 派单上门中确认价格
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_VISITING_DISPATCH_MODIFY);// 抢单上门中修改价格
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_VISITING_DISPATCH_AGREE);// 抢单上门中确认价格
			break;
		case 3:// 已结算
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_FINISH);
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_EVALUATE_YES);
			break;
		default:
			break;
		}
		// 去数据库查询
		List<OrdersFacefaceFull> orderList = ordersFacefaceMapper.getOrdersListByStatus(userId, statusList);
		if (orderList != null && orderList.size() != 0) {
			for (OrdersFacefaceFull ordersFacefaceFull : orderList) {
				OrdersFacefaceDetails maxprice = ordersFacefaceDetailsMapper
						.selectByFacefaceId(ordersFacefaceFull.getOrFacefaceId());
				if (maxprice != null) {
					ordersFacefaceFull.setOrFfDetailsPrice(maxprice.getOrFfDetailsPrice());
				}
				String maxImage = ordersItemMapper.selectPriceMaxImage(ordersFacefaceFull.getOrFacefaceId());
				ordersFacefaceFull.setMaxImage(maxImage);
			}
		}
		Page<OrdersFacefaceFull> listFaceface = (Page<OrdersFacefaceFull>) orderList;
		result.setTotal(listFaceface.getTotal());
		results.put("orderList", orderList);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 查看订单明细，通过订单id
	 */
	@Override
	public LianjiuResult selectOrderDetails(String orders_id) {
		if (StringUtil.isEmpty(orders_id)) {
			return LianjiuResult.build(501, "请指定正确的数据");
		}
		List<OrdersItem> OrdersItem = ordersItemMapper.selectProductByOrderId(orders_id);
		return LianjiuResult.ok(OrdersItem);
	}

	/**
	 * 查看订单明细，通过订单id
	 */
	@Override
	public LianjiuResult selectFaceFaceItemByOrderId(String ordersId) {
		if (StringUtil.isEmpty(ordersId)) {
			return LianjiuResult.build(501, "请指定正确的数据");
		}
		List<OrdersItem> OrdersItem = ordersItemMapper.selectFaceFaceItemsByOrderId(ordersId);
		return LianjiuResult.ok(OrdersItem);
	}

	/**
	 * 确认价格
	 */
	@Override
	public LianjiuResult modifyVisitPrice(OrdersFacefaceDetails ordersFacefaceDetails, Byte orFacefaceStatus) {
		ordersFacefaceDetails.setOrFfDetailsUpdated(new Date());
		if (GlobalValueUtil.ORDER_FACEFACE_VISITING_GRAB_MODIFY.equals(orFacefaceStatus)) {
			orFacefaceStatus = GlobalValueUtil.ORDER_FACEFACE_VISITING_GRAB_AGREE;
		} else if (GlobalValueUtil.ORDER_FACEFACE_VISITING_DISPATCH_MODIFY.equals(orFacefaceStatus)) {
			orFacefaceStatus = GlobalValueUtil.ORDER_FACEFACE_VISITING_DISPATCH_AGREE;
		}
		ordersFacefaceDetails.setOrFacefaceStatus(orFacefaceStatus);
		int num = 0;
		try {
			num = ordersFacefaceDetailsMapper.modifyVisitPrice(ordersFacefaceDetails);
		} catch (RuntimeException e) {
			loggerOrdersFaceFace.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}

		if (num == 0) {
			return LianjiuResult.build(501, "确认失败");
		}
		// 清除在定时器系统的倒计时
		String ordersId = ordersFacefaceDetails.getOrFacefaceId();
		HttpClientUtil.doPost(GlobalValueUtil.TIMER_BASE_URL + "orderFaceface/orderConfirmDel/" + ordersId);
		return LianjiuResult.ok(ordersId);
	}

	/**
	 * 取消价格
	 */
	@Override
	public LianjiuResult orderPriceRefuse(String orFacefaceId) {
		if (Util.isEmpty(orFacefaceId)) {
			return LianjiuResult.build(501, "请指定正确的数据参数");
		}
		int rowAffect = 0;
		try {
			rowAffect = ordersFacefaceMapper.orderPriceRefuse(orFacefaceId, new Date());
		} catch (RuntimeException e) {
			loggerOrdersFaceFace.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		loggerOrdersFaceFace.info(rowAffect);
		// 清理在定时器系统的倒计时
		HttpClientUtil.doPost(GlobalValueUtil.TIMER_BASE_URL + "orderFaceface/orderConfirmDel/" + orFacefaceId);
		return LianjiuResult.ok(orFacefaceId);
	}

	/**
	 * 废弃
	 */
	@Override
	public LianjiuResult updateFaceFaceState(OrdersFaceface ordersFaceface, int orFacefaceStatus) {
		if (null == ordersFaceface.getOrFacefaceId()) {
			return LianjiuResult.build(501, "请指定正确的数据参数");
		}
		ordersFaceface.setOrFacefaceStatus((byte) orFacefaceStatus);
		int num = ordersFacefaceMapper.updateFaceFaceState(ordersFaceface);
		return LianjiuResult.ok(num);
	}

	@Override
	public LianjiuResult qualityCheckingDetails(String orItemsId) {
		if (null == orItemsId) {
			return LianjiuResult.build(501, "请指定正确的数据参数");
		}
		OrdersItemInfo ordersItem = ordersItemMapper.qualityCheckingFaceface(orItemsId);
		return LianjiuResult.ok(ordersItem);
	}

	@Override
	public LianjiuResult getFaceFaceAll(String userId, int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersFaceface> faceface = ordersFacefaceMapper.findByUserId(userId);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersFaceface> listFaceface = (Page<OrdersFaceface>) faceface;
		loggerOrdersFaceFace.info("总页数：" + listFaceface.getPages());
		LianjiuResult result = new LianjiuResult(faceface);
		result.setTotal(listFaceface.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getFaceFaceAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersFacefaceItemVo> ordersFacefaceItemVoList = new ArrayList<OrdersFacefaceItemVo>();
		List<OrdersFaceface> faceface = ordersFacefaceMapper.findAll();
		for (OrdersFaceface face : faceface) {
			OrdersFacefaceItemVo ordersFacefaceItemVo = new OrdersFacefaceItemVo();
			ordersFacefaceItemVo.setOrdersFaceface(face);
			if (null == face.getOrFacefaceId() || face.getOrFacefaceId().length() == 0) {
				return LianjiuResult.build(501, "error facefaceId不能为空！");
			}
			ordersFacefaceItemVo.setItemList(ordersItemMapper.getItemByOrdersId(face.getOrFacefaceId()));
			ordersFacefaceItemVoList.add(ordersFacefaceItemVo);
		}
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		// Page<OrdersFaceface> listFaceface = (Page<OrdersFaceface>) faceface;
		// loggerOrdersFaceFace.info("总页数：" + listFaceface.getPages());
		LianjiuResult result = new LianjiuResult(ordersFacefaceItemVoList);
		result.setTotal(ordersFacefaceItemVoList.size());
		return result;
	}

	@Override
	public LianjiuResult getFaceFaceByState(Byte state, int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		loggerOrdersFaceFace.info("state__------------------:" + state);
		loggerOrdersFaceFace.info("pageNum------------------:" + pageNum);
		loggerOrdersFaceFace.info("pageSize------------------:" + pageSize);
		List<OrdersFaceface> faceface = ordersFacefaceMapper.getFaceFaceByState(state);

		loggerOrdersFaceFace.info("sssssssssssssssss:" + pageSize);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersFaceface> listFaceface = (Page<OrdersFaceface>) faceface;
		loggerOrdersFaceFace.info("总页数：" + listFaceface.getPages());
		LianjiuResult result = new LianjiuResult(faceface);
		result.setTotal(listFaceface.getTotal());
		return result;
	}

	@Override
	public LianjiuResult updateFaceFaceState(String facefaceId, Byte state) {
		if (facefaceId == null || facefaceId.length() == 0) {
			return LianjiuResult.build(501, "请指定订单id");
		} else if (ordersFacefaceMapper.selectByOrdersFacefaceCheck(facefaceId) == 0) {
			return LianjiuResult.build(502, "订单不存在！或者改订单没有商品记录");
		}
		OrdersFaceface ordersFaceface = new OrdersFaceface();
		ordersFaceface.setOrFacefaceId(facefaceId);
		ordersFaceface.setOrFacefaceStatus(state);
		ordersFaceface.setOrFacefaceUpdated(new Date());
		int rowsAffected = 0;
		try {
			rowsAffected = ordersFacefaceMapper.updateByStatus(ordersFaceface);
		} catch (RuntimeException e) {
			loggerOrdersFaceFace.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}

		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult getByOrdersItem(String facefaceId) {
		List<OrdersItem> item = ordersItemMapper.getItemByOrdersId(facefaceId);
		return LianjiuResult.ok(item);
	}

	/**
	 * 废品的加单回收车，redis缓存
	 */
	@Override
	public LianjiuResult addWaste(String userId, OrdersItem ordersItem) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(501, "error 用户id不能为空！");
		}
		ordersItem.setOrItemsCreated(new Date());
		ordersItem.setOrItemsRecycleType("上门回收");
		ordersItem.setOrItemsStatus(0);
		ordersItem.setOrItemsStemFrom(3);// 来源(0卖手机、1卖家电、2卖精品 3,卖废品)
		loggerOrdersFaceFace.info("加单时用户id：" + userId);
		// 取出redis的加单车的数据
		loggerOrdersFaceFace.info("token:" + GlobalValueJedis.ORDERS_FACEFACE_SINGLE_CART + userId);
		List<OrdersItem> list = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.ORDERS_FACEFACE_SINGLE_CART + userId, OrdersItem.class);
		// 如果没有，新建加单车
		if (null == list) {
			loggerOrdersFaceFace.info("开始添加第一个废品：");
			list = new ArrayList<OrdersItem>();
		}
		list.add(ordersItem);
		loggerOrdersFaceFace.info(list);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_SINGLE_CART + userId, JsonUtils.objectToJson(list));
		return LianjiuResult.ok(list);
	}

	/**
	 * 上门回收，加单结算，废品加单结算
	 */
	@Override
	public LianjiuResult submitFace(String userId, String orFacefaceId) {
		String synOrdersId = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_WITH_A_SINGLE + orFacefaceId);// 问锁
		if (null != synOrdersId) {
			return LianjiuResult.build(501, "订单正在结算，不可重复加单");
		}
		/**
		 * 检查订单是否正在支付
		 */
		synOrdersId = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_COUNT_DOWN_PAY_SYN + orFacefaceId);// 问锁
		if (null != synOrdersId) {
			return LianjiuResult.build(502, "订单正在支付，不可加单，请重新下单");
		} else {
			jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_WITH_A_SINGLE + orFacefaceId, orFacefaceId);// 加锁
			loggerOrdersFaceFace.info("用户加单，锁住" + orFacefaceId);
		}
		try {
			if (Util.isEmpty(orFacefaceId)) {
				return LianjiuResult.build(501, "订单id不能为空！");
			}
			OrdersFaceface face = ordersFacefaceMapper.selectFullByPrimaryKey(orFacefaceId);
			// 判断状态
			Byte orderStatus = face.getOrFacefaceStatus();
			if (GlobalValueUtil.ORDER_FACEFACE_VISITING_GRAB_MODIFY.equals(orderStatus)
					|| GlobalValueUtil.ORDER_FACEFACE_VISITING_DISPATCH_MODIFY.equals(orderStatus)) {
				return LianjiuResult.build(502, "该订单已被修改价格，您已无法加单！");
			}
			if (GlobalValueUtil.ORDER_FACEFACE_FINISH.equals(orderStatus)) {
				return LianjiuResult.build(503, "该订单已被结算，您已无法加单！");
			}
			String orFfDetailsPrice = face.getOrFfDetailsPrice();
			String orFfDetailsRetrPrice = face.getOrFfDetailsRetrPrice();// 加单前的价格
			// 取出redis的加单车的数据
			List<OrdersItem> list = JedisTool.checkListFormRedis(jedisClient,
					GlobalValueJedis.ORDERS_FACEFACE_SINGLE_CART + userId, OrdersItem.class);
			if (null == list || 0 == list.size()) {
				loggerOrdersFaceFace.info("token:" + GlobalValueJedis.ORDERS_FACEFACE_SINGLE_CART + userId);
				return LianjiuResult.build(504, "请先选择废品（加单车里为空）！");
			}
			String ordersId = IDUtils.genOrdersId();
			// 总价
			BigDecimal orderPrice = new BigDecimal("0");
			// 价格最高的商品
			Double cmpResult = 0.00;
			BigDecimal maxPrice = new BigDecimal("0");
			// 最高价格商品的图片
			String maximage = null;
			// 最高价格商品的名称
			String maxName = null;
			for (OrdersItem ordersItem : list) {
				/**
				 * 筛选最高价格的item的图片
				 */
				String itemsPrice = ordersItem.getOrItemsPrice();// 单价
				String itemNum = ordersItem.getOrItemsNum();// 数量
				BigDecimal itemsPriceBigDecimal = null;
				BigDecimal itemNumBigDecimal = null;
				BigDecimal itemsPriceTotalBigDecimal = null;
				if (null != itemsPrice && null != itemNum) {
					itemsPriceBigDecimal = new BigDecimal(itemsPrice); // 单价
					itemNumBigDecimal = new BigDecimal(itemNum); // 数量
					itemsPriceTotalBigDecimal = itemsPriceBigDecimal.multiply(itemNumBigDecimal);// 相乘
					itemsPriceTotalBigDecimal = itemsPriceTotalBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);// 保留两位小数,四舍五入
					orderPrice = orderPrice.add(itemsPriceTotalBigDecimal);// 累加为订单的预估总价
					/**
					 * 取出最高价格的item对应的图片
					 */
					cmpResult = maxPrice.subtract(itemsPriceBigDecimal).doubleValue();
					if (0 > cmpResult) {
						maxPrice = itemsPriceBigDecimal; // 暂存最高价格
						maximage = ordersItem.getOrItemsPicture();// 取出最高价格的item对应的图片
						maxName = ordersItem.getOrItemsName();
					}
				}
				/**
				 * 给item追加赋值
				 */
				ordersItem.setOrItemsId(IDUtils.genOrdersId());
				ordersItem.setOrdersId(ordersId);// 设置item输入该订单
				ordersItem.setOrItemsBeforePrice(itemsPrice);
				ordersItem.setOrItemsAccountPrice(itemsPriceTotalBigDecimal.toString());
				ordersItem.setOrItemsType("废品");
				ordersItem.setOrItemsStemFrom(3);
				ordersItem.setOrItemsRecycleType("上门回收");
				ordersItem.setOrItemsStatus(0);
			}
			// 计算加单后的价格
			BigDecimal userPrice = new BigDecimal(orFfDetailsPrice);
			BigDecimal retrPrice = new BigDecimal(orFfDetailsRetrPrice);
			loggerOrdersFaceFace.info("修改价格前" + userPrice + "-----" + retrPrice);
			loggerOrdersFaceFace.info("加" + orderPrice);
			userPrice = userPrice.add(orderPrice);
			retrPrice = retrPrice.add(orderPrice);
			loggerOrdersFaceFace.info("修改价格后" + userPrice + "-----" + retrPrice);
			// 让订单与加单订单号关联起来 修改时间等
			face.setOrFacefaceBrothersId(ordersId);
			face.setOrFacefaceUpdated(new Date());
			face.setOrFfDetailsPrice(userPrice.toString());
			face.setOrFfDetailsRetrPrice(retrPrice.toString());
			Util.printModelDetails(face);
			try {
				/**
				 * 更新之前的订单
				 */
				int count = ordersFacefaceMapper.updateFacefaceBrothersId(face);
				/**
				 * 开始加单，把兄弟订单的信息做修改
				 */
				face.setOrFacefaceUpdated(new Date());
				face.setOrFacefaceCreated(new Date());
				face.setOrFacefaceBrothersId("WithaSingle-" + orFacefaceId);// 加单特有标识
				face.setOrFacefaceId(ordersId);// 订单id
				face.setOrItemsNamePreview(maxName);// item预览名称
				face.setOrItemsPictruePreview(maximage);// item预览图
				Util.printModelDetails(face);
				OrdersFacefaceDetails details = new OrdersFacefaceDetails();
				details.setOrFfDetailsId(IDUtils.genGUIDs());
				details.setOrFfDetailsPrice(orderPrice + "");
				details.setOrFacefaceId(ordersId);
				details.setOrFfDetailsUpdated(new Date());
				Util.printModelDetails(details);
				count += ordersFacefaceMapper.insert(face);
				count += ordersFacefaceDetailsMapper.insert(details);
				loggerOrdersFaceFace.info("修改数据的条数：" + count);
				int count4 = ordersItemMapper.addItemList(list);
				loggerOrdersFaceFace.info("添加item的条数：" + count4);
			} catch (RuntimeException e) {
				loggerOrdersFaceFace.info("捕获异常：" + e.getMessage());
				loggerOrdersFaceFace.info(e.getCause());
				return LianjiuResult.build(510, "数据异常！");
			}
			// 清空加单车
			jedisClient.del(GlobalValueJedis.ORDERS_FACEFACE_SINGLE_CART + userId);
			return LianjiuResult.ok(list);
		} finally {
			// 保证最终一定要解锁
			jedisClient.del(GlobalValueJedis.ORDERS_FACEFACE_WITH_A_SINGLE + orFacefaceId);// 解锁
			loggerOrdersFaceFace.info("用户加单，解锁" + orFacefaceId);
		}
	}

	/**
	 * 查询家电车
	 */
	@Override
	public LianjiuResult getWasteCar(String userId) {
		// 取出redis的加单车的数据
		List<OrdersItem> list = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.ORDERS_FACEFACE_SINGLE_CART + userId, OrdersItem.class);
		if (null == list) {
			loggerOrdersFaceFace.info("token:" + GlobalValueJedis.ORDERS_FACEFACE_SINGLE_CART + userId);
			return LianjiuResult.build(501, "加单车暂无数据，快去加单吧！");
		}
		return LianjiuResult.ok(list);
	}

	@Override
	public LianjiuResult deleteWasteCar(String userId, String orItemsProductId) {
		if (null == userId || userId.length() == 0) {
			return LianjiuResult.build(501, "error 用户id不能为空！");
		}
		String userCart = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_SINGLE_CART + userId);
		loggerOrdersFaceFace.info("token:" + GlobalValueJedis.ORDERS_FACEFACE_SINGLE_CART + userId);
		loggerOrdersFaceFace.info("userCart:" + userCart);
		List<OrdersItem> list = null;
		if (null == userCart || "".equals(userCart) || "null".equals(userCart)) {
			return LianjiuResult.build(501, "error 加单车没有数据！");
		} else {
			list = JsonUtils.jsonToList(userCart, OrdersItem.class);
			for (int i = 0; i <= list.size() - 1; i++) {
				loggerOrdersFaceFace.info("size:" + list.size());
				if (orItemsProductId.equals(list.get(i).getOrItemsProductId())) {
					loggerOrdersFaceFace.info("开始删除加单车内productId：" + orItemsProductId);
					list.remove(i);
					loggerOrdersFaceFace.info("删除成功：");
					break;
				}
			}
		}
		loggerOrdersFaceFace.info(" JsonUtils.objectToJson(list):" + JsonUtils.objectToJson(list));
		// HttpClientUtil.doPostJson(GlobalValueUtil.REDIS_URL_SET_JSON +
		// waste.getUserId() + "cart_waste/",
		// JsonUtils.objectToJson(list));
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_SINGLE_CART + userId, JsonUtils.objectToJson(list));
		return LianjiuResult.ok(list);
	}

	/**
	 * 上门回收，加入回收车,卖家电
	 */
	@Override
	public LianjiuResult introductionFaceface(String userId, OrdersItem productItem) {
		productItem.setOrItemsType("家电");
		productItem.setOrItemsCreated(new Date());
		productItem.setOrItemsCreated(new Date());// 设置时间
		String userCart = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_CART + userId);
		Util.printModelDetails(productItem);
		loggerOrdersFaceFace.info("token:" + GlobalValueJedis.ORDERS_FACEFACE_CART + userId);
		loggerOrdersFaceFace.info("userCart:" + userCart);
		List<OrdersItem> list = null;
		if (null == userCart || "".equals(userCart) || "null".equals(userCart)) {
			list = new ArrayList<OrdersItem>();
			list.add(productItem);
		} else {
			list = JsonUtils.jsonToList(userCart, OrdersItem.class);
			list.add(productItem);
			Util.printModelDetails(list);
		}
		loggerOrdersFaceFace.info(" JsonUtils.objectToJson(list):" + JsonUtils.objectToJson(list));
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART + userId, JsonUtils.objectToJson(list));

		return LianjiuResult.ok(list);
	}

	/**
	 * 家电直接提交上门回收订单，显示订单预览
	 */
	@Override
	public LianjiuResult directSubmit(OrdersItem ordersItem, String userId) {
		ordersItem.setOrItemsCreated(new Date());// 设置时间
		if (null == userId || userId.length() == 0) {
			return LianjiuResult.build(501, "error 用户id不能为空！");
		}
		// 从redis中获取usercare
		String userCartFaceface = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_CART + userId);
		List<OrdersItem> itemList = null;
		if (Util.notEmpty(userCartFaceface)) {
			itemList = JsonUtils.jsonToList(userCartFaceface, OrdersItem.class);
		}
		if (null == itemList) {
			itemList = new ArrayList<OrdersItem>();
		}
		// 总价
		BigDecimal orderPrice = new BigDecimal("0");
		// 价格最高的商品
		Double cmpResult = 0.00;
		BigDecimal maxPrice = new BigDecimal("0");
		// 最高价格商品的图片
		String maximage = null;
		/**
		 * 筛选最高价格的item的图片
		 */
		String itemsPrice = ordersItem.getOrItemsPrice();// 单价
		String itemNum = ordersItem.getOrItemsNum();// 数量
		BigDecimal itemsPriceBigDecimal = null;
		BigDecimal itemNumBigDecimal = null;
		BigDecimal itemsPriceTotalBigDecimal = null;
		if (null != itemsPrice && null != itemNum) {
			itemsPriceBigDecimal = new BigDecimal(itemsPrice); // 单价
			itemNumBigDecimal = new BigDecimal(itemNum); // 数量
			itemsPriceTotalBigDecimal = itemsPriceBigDecimal.multiply(itemNumBigDecimal);// 相乘
			itemsPriceTotalBigDecimal = itemsPriceTotalBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);// 保留两位小数,四舍五入
			orderPrice = orderPrice.add(itemsPriceTotalBigDecimal);// 累加为订单的预估总价
			/**
			 * 取出最高价格的item对应的图片
			 */
			cmpResult = maxPrice.subtract(itemsPriceBigDecimal).doubleValue();
			if (0 > cmpResult) {
				maxPrice = itemsPriceBigDecimal; // 暂存最高价格
				maximage = ordersItem.getOrItemsPicture();// 取出最高价格的item对应的图片
			}
		}

		/**
		 * 给item追加赋值
		 */
		String itemsId = IDUtils.genOrdersId();
		ordersItem.setOrItemsId(itemsId);
		ordersItem.setOrItemsBeforePrice(itemsPrice);
		ordersItem.setOrItemsAccountPrice(itemsPriceTotalBigDecimal.toString());
		ordersItem.setOrItemsChooseFlag(1);// 被选中
		ordersItem.setOrItemsType("家电");
		ordersItem.setOrItemsStatus(0);
		// 回收类型
		ordersItem.setOrItemsRecycleType("上门回收");
		ordersItem.setOrItemsCreated(new Date());
		ordersItem.setOrItemsUpdated(new Date());

		List<String> productIdList = new ArrayList<String>();
		productIdList.add(itemsId);
		Map<String, Object> map = new HashMap<String, Object>();
		// 最高Item的总价
		map.put("maxPrice", itemsPrice);
		map.put("maximage", maximage);
		map.put("productIdList", productIdList);
		// 总价
		map.put("totalprice", orderPrice.toString());
		/**
		 * 把剩余的产品存回去
		 */
		itemList.add(ordersItem);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART + userId, JsonUtils.objectToJson(itemList));
		/**
		 * 缓存结算之后要加载的数据
		 */
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART_BALANCE_PRICE + userId, orderPrice.toString());
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART_ITEM_PRICE_MAX + userId, itemsPrice);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART_ITEM_IMAGE_MAX + userId, maximage);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART_ITEM_CHOOSER + userId,
				JsonUtils.objectToJson(productIdList));
		return LianjiuResult.ok(map);
	}

	/**
	 * 直接提交上门回收订单，显示订单预览,卖废品
	 */
	@Override
	public LianjiuResult directSubmitWaste(OrdersItem ordersItem, String userId) {
		ordersItem.setOrItemsCreated(new Date());// 设置时间
		if (null == userId || userId.length() == 0) {
			return LianjiuResult.build(501, "error 用户id不能为空！");
		}
		// 从redis中获取usercare
		String userCartFaceface = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_CART + userId);
		List<OrdersItem> itemList = null;
		if (Util.notEmpty(userCartFaceface)) {
			itemList = JsonUtils.jsonToList(userCartFaceface, OrdersItem.class);
		}
		if (null == itemList) {
			itemList = new ArrayList<OrdersItem>();
		}
		// 总价
		BigDecimal orderPrice = new BigDecimal("0");
		// 价格最高的商品
		Double cmpResult = 0.00;
		BigDecimal maxPrice = new BigDecimal("0");
		// 最高价格商品的图片
		String maximage = null;
		/**
		 * 筛选最高价格的item的图片
		 */
		String itemsPrice = ordersItem.getOrItemsPrice();// 单价
		String itemNum = ordersItem.getOrItemsNum();// 数量
		BigDecimal itemsPriceBigDecimal = null;
		BigDecimal itemNumBigDecimal = null;
		BigDecimal itemsPriceTotalBigDecimal = null;
		if (null != itemsPrice && null != itemNum) {
			itemsPriceBigDecimal = new BigDecimal(itemsPrice); // 单价
			itemNumBigDecimal = new BigDecimal(itemNum); // 数量
			itemsPriceTotalBigDecimal = itemsPriceBigDecimal.multiply(itemNumBigDecimal);// 相乘
			itemsPriceTotalBigDecimal = itemsPriceTotalBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);// 保留两位小数,四舍五入
			orderPrice = orderPrice.add(itemsPriceTotalBigDecimal);// 累加为订单的预估总价
			/**
			 * 取出最高价格的item对应的图片
			 */
			cmpResult = maxPrice.subtract(itemsPriceBigDecimal).doubleValue();
			if (0 > cmpResult) {
				maxPrice = itemsPriceBigDecimal; // 暂存最高价格
				maximage = ordersItem.getOrItemsPicture();// 取出最高价格的item对应的图片
			}
		}

		/**
		 * 给item追加赋值
		 */
		String itemsId = IDUtils.genOrdersId();
		ordersItem.setOrItemsId(itemsId);
		ordersItem.setOrItemsBeforePrice(itemsPrice);
		ordersItem.setOrItemsAccountPrice(itemsPriceTotalBigDecimal.toString());
		ordersItem.setOrItemsChooseFlag(1);// 被选中
		ordersItem.setOrItemsType("废品");
		ordersItem.setOrItemsStatus(0);
		// 回收类型
		ordersItem.setOrItemsRecycleType("上门回收");
		ordersItem.setOrItemsCreated(new Date());
		ordersItem.setOrItemsUpdated(new Date());

		List<String> productIdList = new ArrayList<String>();
		productIdList.add(itemsId);
		Map<String, Object> map = new HashMap<String, Object>();
		// 最高Item的总价
		map.put("maxPrice", itemsPrice);
		map.put("maximage", maximage);
		map.put("productIdList", productIdList);
		// 总价
		map.put("totalprice", orderPrice.toString());
		/**
		 * 把剩余的产品存回去
		 */
		itemList.add(ordersItem);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART + userId, JsonUtils.objectToJson(itemList));
		/**
		 * 缓存结算之后要加载的数据
		 */
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART_BALANCE_PRICE + userId, orderPrice.toString());
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART_ITEM_PRICE_MAX + userId, itemsPrice);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART_ITEM_IMAGE_MAX + userId, maximage);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART_ITEM_CHOOSER + userId,
				JsonUtils.objectToJson(productIdList));
		return LianjiuResult.ok(map);
	}

}
