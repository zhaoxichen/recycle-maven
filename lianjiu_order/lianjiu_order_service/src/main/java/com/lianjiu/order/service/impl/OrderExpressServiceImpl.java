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
import com.lianjiu.common.tools.CalculateStringNum;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.HttpClientUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersExpress;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.model.OrdersPayment;
import com.lianjiu.model.Product;
import com.lianjiu.model.UserDetails;
import com.lianjiu.order.service.OrderExpressService;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.rest.mapper.OrdersPaymentMapper;
import com.lianjiu.rest.mapper.orders.OrdersExpressMapper;
import com.lianjiu.rest.mapper.orders.OrdersItemMapper;
import com.lianjiu.rest.mapper.product.ProductMapper;
import com.lianjiu.rest.mapper.user.UserDetailsMapper;
import com.lianjiu.rest.mapper.user.UserMapper;
import com.lianjiu.rest.model.OrdersExpressItemVo;
import com.lianjiu.rest.model.OrdersItemInfo;
import com.lianjiu.rest.tool.JedisTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class OrderExpressServiceImpl implements OrderExpressService {

	@Autowired
	private OrdersExpressMapper ordersExpressMapper;
	@Autowired
	private OrdersItemMapper ordersItemMapper;
	@Autowired
	ProductMapper productMapper;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private UserDetailsMapper userDetailsMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private OrdersPaymentMapper ordersPaymentMapper;
	private static Logger loggerOrdersExpress = Logger.getLogger("ordersExpress");

	/**
	 * 快递回收的结算
	 */
	@Override
	public LianjiuResult productBalance(List<String> productIdList, String userId) {
		loggerOrdersExpress.info(userId + "快递订单结算：" + productIdList);
		if (null == userId || userId.length() == 0) {
			return LianjiuResult.build(501, "error 用户id不能为空！");
		}
		// 从redis中获取usercare
		List<OrdersItem> itemList = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.ORDERS_EXPRESS_CART + userId, OrdersItem.class);
		if (null == itemList || 0 == itemList.size()) {
			return LianjiuResult.build(502, "购物车信息丢失");
		}
		// 订单总价
		String orderPrice = "0.00";
		// 价格最高的商品
		Double cmpResult = 0.00;
		BigDecimal maxPrice = new BigDecimal("0");
		// 最高价格商品的图片
		String maximage = "";
		/**
		 * 遍历redis中的产品
		 */
		BigDecimal itemsPriceBigDecimal = null;
		String orItemsAccountPrice = null;
		for (OrdersItem ordersItem : itemList) {
			// 判断是否为选中的产品
			if (!productIdList.contains(ordersItem.getOrItemsId())) {
				continue;
			}
			loggerOrdersExpress.info("订单商品：" + ordersItem.getOrItemsName());
			/**
			 * 筛选最高价格的item的图片
			 */
			String itemsPrice = ordersItem.getOrItemsPrice();// 单价
			String itemNum = ordersItem.getOrItemsNum();// 数量
			orItemsAccountPrice = CalculateStringNum.multiply(itemsPrice, itemNum);// 相乘
			orderPrice = CalculateStringNum.add(orderPrice, orItemsAccountPrice);//// 累加为订单的预估总价
			/**
			 * 取出最高价格的item对应的图片
			 */
			itemsPriceBigDecimal = new BigDecimal(itemsPrice); // 单价
			cmpResult = maxPrice.subtract(itemsPriceBigDecimal).doubleValue();
			if (0 > cmpResult) {
				maxPrice = itemsPriceBigDecimal; // 暂存最高价格
				maximage = ordersItem.getOrItemsPicture();// 取出最高价格的item对应的图片
			}
			/**
			 * 给item追加赋值
			 */
			ordersItem.setOrItemsBeforePrice(itemsPrice);
			ordersItem.setOrItemsAccountPrice(orItemsAccountPrice);
			ordersItem.setOrItemsChooseFlag(1);// 选中标志位，不存数据库
			ordersItem.setOrItemsType("手机");
			ordersItem.setOrItemsStatus(0);
			// 回收类型
			ordersItem.setOrItemsRecycleType("快递回收");
			ordersItem.setOrItemsCreated(new Date());
			ordersItem.setOrItemsUpdated(new Date());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 总价
		map.put("totalprice", orderPrice);
		map.put("maximage", maximage);
		map.put("productIdList", productIdList);
		/**
		 * 把剩余的产品存回去
		 */
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CART + userId, JsonUtils.objectToJson(itemList));
		/**
		 * 缓存结算之后要加载的数据
		 */
		if (0 == productIdList.size()) {
			return LianjiuResult.build(503, "未选择商品");
		}
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CART_BALANCE_PRICE + userId, orderPrice);
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CART_ITEM_IMAGE_MAX + userId, maximage);
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CART_ITEM_CHOOSER + userId,
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

		String totalprice = jedisClient.get(GlobalValueJedis.ORDERS_EXPRESS_CART_BALANCE_PRICE + userId);
		String maximage = jedisClient.get(GlobalValueJedis.ORDERS_EXPRESS_CART_ITEM_IMAGE_MAX + userId);
		Map<String, Object> map = new HashMap<String, Object>();
		// 总价
		map.put("totalprice", totalprice);
		map.put("maximage", maximage);
		return LianjiuResult.ok(map);
	}

	/**
	 * 从快递回收车中删除一个商品
	 */
	@Override
	public LianjiuResult reduceFromExpressCar(String itemId, String userId) {
		String userCart = jedisClient.get(GlobalValueJedis.ORDERS_EXPRESS_CART + userId);
		List<OrdersItem> list = null;
		if (Util.isEmpty(userCart)) {
			return LianjiuResult.build(501, "回收车为空");
		} else {
			list = JsonUtils.jsonToList(userCart, OrdersItem.class);
			for (OrdersItem productItem : list) {
				if (productItem.getOrItemsId().equals(itemId)) {
					list.remove(productItem);
					jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CART + userId, JsonUtils.objectToJson(list));
					return LianjiuResult.ok("删除成功");
				}
			}
			Util.printModelDetails(list);
		}
		return LianjiuResult.build(501, "删除失败");
	}

	/**
	 * 
	 * zhaoxi 2017年10月30日 descrption:提交快递回收订单
	 * 
	 * @param ordersExpress
	 * @param orItemsBuyway
	 * @param checkCode
	 * @param productIdList
	 * @return LianjiuResult
	 */
	@Override
	public LianjiuResult submit(OrdersExpress ordersExpress, String orItemsBuyway, String checkCode) {
		String userId = ordersExpress.getOrExpressUserId();
		if (Util.isEmpty(userId) || userId.equals("{userId}")) {
			loggerOrdersExpress.info("501,用户id不能为空！");
			return LianjiuResult.build(501, "error 用户id不能为空！");
		}
		/**
		 * 校验验证码
		 */
		String userCheck = jedisClient.get(userId + "_LIAN_JIU_EXPRESS_CODE");
		if (Util.isEmpty(userCheck)) {
			loggerOrdersExpress.info("502,验证码已超时" + userId);
			return LianjiuResult.build(502, "验证码已超时，请重新获取");
		} else {
			if (checkCode.equals(userCheck)) {
				jedisClient.del(userId + "_LIAN_JIU_EXPRESS_CODE");
			} else {
				loggerOrdersExpress.info("503,验证码输入错误" + userId);
				return LianjiuResult.build(503, "验证码输入错误");
			}
		}
		List<String> productIdList = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.ORDERS_EXPRESS_CART_ITEM_CHOOSER + userId, String.class);
		if (null == productIdList || 0 == productIdList.size()) {
			loggerOrdersExpress.info("504 productIdList错误" + userId);
			return LianjiuResult.build(504, "productIdList错误");
		}
		loggerOrdersExpress.info("itemId列表：" + productIdList);
		// 从redis中获取usercare
		String userCartExpress = jedisClient.get(GlobalValueJedis.ORDERS_EXPRESS_CART + userId);
		if (Util.isEmpty(userCartExpress)) {
			loggerOrdersExpress.info("505 购物车信息丢失" + userId);
			return LianjiuResult.build(505, "购物车信息丢失");
		}
		List<OrdersItem> itemList = JsonUtils.jsonToList(userCartExpress, OrdersItem.class);
		if (null == itemList) {
			loggerOrdersExpress.info("506 redis存储错误" + userId);
			return LianjiuResult.build(506, "redis存储错误");
		}
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
		// 生成一个订单id
		String ordersId = IDUtils.genOrdersId();
		/**
		 * 遍历redis中的产品
		 */
		for (OrdersItem ordersItem : itemList) {
			// 判断是否为选中的产品
			if (!productIdList.contains(ordersItem.getOrItemsId())) {
				/**
				 * 保存到未选中的列表
				 */
				loggerOrdersExpress.info(ordersItem.getOrItemsId());
				itemListRedis.add(ordersItem);
				continue;
			}
			/**
			 * 筛选最高价格的item的图片
			 */
			String itemsPrice = ordersItem.getOrItemsPrice();
			if (null != itemsPrice) {
				loggerOrdersExpress.info("当前价格---" + ordersItem.getOrItemsPrice());
				cmpResult = maxPrice.compareTo(itemsPrice);
				if (0 > cmpResult) {
					maxPrice = itemsPrice; // 作为最高价格的item
					maxName = ordersItem.getOrItemsName();// 取出对应的名称
					maximage = ordersItem.getOrItemsPicture();// 取出对应的图片
				}
				orderPrice = orderPrice.add(new BigDecimal(itemsPrice));
			}
			/**
			 * 给item追加赋值
			 */
			ordersItem.setOrItemsType("手机");
			ordersItem.setOrItemsBuyway(orItemsBuyway);
			ordersItem.setOrItemsStatus(0);
			// 订单id
			ordersItem.setOrdersId(ordersId);
			// 回收类型
			ordersItem.setOrItemsRecycleType("快递回收");
			ordersItem.setOrItemsCreated(new Date());
			ordersItem.setOrItemsUpdated(new Date());
			/**
			 * 暂存
			 */
			itemListTemp.add(ordersItem);
		}
		/**
		 * 生成快递订单
		 */
		if (null == maxName) {
			loggerOrdersExpress.info("507 重复提交订单" + userId);
			return LianjiuResult.build(507, "重复提交订单！");
		}
		ordersExpress.setOrExpressId(ordersId);
		ordersExpress.setOrItemsNamePreview(maxName);// item预览名称
		ordersExpress.setOrItemsPictruePreview(maximage);// item预览图
		Long idExcellent = categoryMapper.selectByCategoryName("快递回收");
		ordersExpress.setCategoryId(idExcellent);
		ordersExpress.setOrExpressUserId(userId);
		ordersExpress.setOrExpressCreated(new Date());
		ordersExpress.setOrExpressUpdated(new Date());
		ordersExpress.setOrExpressEvaluatedPrice(orderPrice.toString());// 用户预估价格
		ordersExpress.setOrExpressRecyclePrice("");// 默认同意预估价格为回收价格
		ordersExpress.setOrExpressStatus(GlobalValueUtil.ORDER_EXPRESS_DELIVERY_NO); // 待发货
		// 添加顶大数据
		// ordersExpressMapper.insert(ordersExpress);
		// 添加item到数据库
		loggerOrdersExpress.info("查看是不是有值");
		for (OrdersItem ordersItem2 : itemListTemp) {
			loggerOrdersExpress.info(ordersItem2);
		}
		// ordersItemMapper.addItemList(itemListTemp);
		// 用户积分添加
		UserDetails userDetails = new UserDetails();
		try {
			ordersExpressMapper.insert(ordersExpress);
			ordersItemMapper.addItemList(itemListTemp);
			// 预支付
			OrdersPayment payment = new OrdersPayment(IDUtils.genPaymentId(), ordersId, orderPrice.toString(), userId);
			ordersPaymentMapper.insert(payment);
			String udetailsId = userMapper.getDetailsId(userId);
			userDetails.setUdetailsId(udetailsId);
			Integer integral = userDetailsMapper.selectIntegralById(udetailsId);
			if (10 == integral) {
				// 首次提交积分+100
				userDetails.setUdetailsIntegral(integral + GlobalValueUtil.FIRST_INTEGRAL);

			} else {
				userDetails.setUdetailsIntegral(integral + GlobalValueUtil.AFTER_INTEGRAL);
			}
			Integer counts = userDetailsMapper.modifyIntegeralById(userDetails);
			if (counts == 0) {
				loggerOrdersExpress.info("508 积分添加出错" + userId);
				return LianjiuResult.build(508, "积分添加出错");
			}
		} catch (RuntimeException e) {
			loggerOrdersExpress.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 总价
		map.put("totalprice", orderPrice.toString());
		map.put("maximage", maximage);
		map.put("ordersId", ordersId);
		/**
		 * 把剩余的产品存回去
		 */
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CART + userId, JsonUtils.objectToJson(itemListRedis));
		/**
		 * 删除之前的缓存
		 */
		jedisClient.del(GlobalValueJedis.ORDERS_EXPRESS_CART_ITEM_CHOOSER + userId);
		jedisClient.del(GlobalValueJedis.ORDERS_EXPRESS_CART_BALANCE_PRICE + userId);
		jedisClient.del(GlobalValueJedis.ORDERS_EXPRESS_CART_ITEM_IMAGE_MAX + userId);
		return LianjiuResult.ok(map);
	}

	/**
	 * 修改订单
	 */
	@Override
	public LianjiuResult modifyOrder(OrdersExpress ordersExpress) {
		if (null == ordersExpress) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(ordersExpress.getOrExpressId())) {
			return LianjiuResult.build(501, "请指定要更新的快递订单id");
		}
		ordersExpress.setOrExpressUpdated(new Date());
		// 去数据库更新
		int rowsAffected = 0;
		try {
			rowsAffected = ordersExpressMapper.updateByPrimaryKeySelective(ordersExpress);
		} catch (RuntimeException e) {
			loggerOrdersExpress.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult getOrderById(String orderId) {
		OrdersExpress express = ordersExpressMapper.selectByPrimaryKey(orderId);
		return LianjiuResult.ok(express);
	}

	/**
	 * 获取一个商品记录
	 */
	@Override
	public LianjiuResult getOrderItemsById(String orExpItemId) {
		Map<String, Object> map = new HashMap<>();
		if (Util.isEmpty(orExpItemId)) {
			return LianjiuResult.build(501, "请指定要查询的商品记录id");
		}
		OrdersItem item = ordersItemMapper.selectByPrimaryKey(orExpItemId);
		if (null != item) {
			map.put("item", item);
		}
		OrdersExpress oExpress = ordersExpressMapper.selectByPrimaryKey(item.getOrdersId());
		if (null != oExpress) {
			map.put("orExpressNum", oExpress.getOrExpressNum());
		}
		return LianjiuResult.ok(map);
	}

	/**
	 * 获取所有的商品记录
	 */
	@Override
	public LianjiuResult getParamData(String productId) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(501, "请指定要查询的产品id");
		}
		Product p = productMapper.selectByPrimaryKey(productId);
		String productParamData = null;
		if (p != null) {
			productParamData = p.getProductParamData();
		}
		return LianjiuResult.ok(productParamData);
	}

	@Override
	public LianjiuResult getParamData(String productId, String majorName) {

		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(501, "请指定要查询的产品id");
		}
		if (Util.isEmpty(majorName)) {
			return LianjiuResult.build(501, "请指定要查询参数名");
		}
		Product p = productMapper.selectByPrimaryKey(productId);
		String majorData = null;
		if (p != null) {
			String productParamData = p.getProductParamData();
			// 筛选出具体的参数
			JSONArray paramDataArray1 = JSONArray.fromObject(productParamData);// json串转为json数组
			for (Object object1 : paramDataArray1) {
				JSONArray paramDataArray2 = JSONArray.fromObject(object1);// json串转为json数组
				for (Object object2 : paramDataArray2) {
					JSONArray paramDataArray3 = JSONArray.fromObject(object2);// json串转为json数组
					for (Object children1 : paramDataArray3) {
						JSONObject majorDataJsonObject1 = JSONObject.fromObject(children1);// json串转为json对象
						majorData = (String) majorDataJsonObject1.get("major");
						if (Util.isEmpty(majorData)) {
							continue;
						}
						if (majorName.trim().equals(majorData.trim())) {
							return LianjiuResult.ok(children1);
						}
						JSONArray children2 = (JSONArray) majorDataJsonObject1.get("children"); // children为数组
						for (Object children : children2) {
							JSONObject majorDataJsonObject = JSONObject.fromObject(children);// json串转为json对象
							majorData = (String) majorDataJsonObject.get("major");
							loggerOrdersExpress.info(majorData);
							if (Util.isEmpty(majorData)) {
								continue;
							}
							if (majorName.trim().equals(majorData.trim())) {
								return LianjiuResult.ok(children);
							}
						}
					}
				}
			}
		}
		return LianjiuResult.build(503, "查不到指定的参数组合,请检查查询条件");
	}

	@Override
	public LianjiuResult modifyParam(String orItemsId, String orItemsParamModify) {
		if (Util.isEmpty(orItemsId)) {
			return LianjiuResult.build(501, "请指定要更新的快递item是id");
		}
		if (Util.isEmpty(orItemsParamModify)) {
			return LianjiuResult.build(501, "请传入要更新的修改的产品参数模版");
		}
		OrdersItem item = new OrdersItem();
		item.setOrItemsId(orItemsId);
		item.setOrItemsParamModify(orItemsParamModify);
		item.setOrItemsUpdated(new Date());
		int rowsAffected = 0;
		try {
			rowsAffected = ordersItemMapper.updateByPrimaryKeySelective(item);
		} catch (RuntimeException e) {
			loggerOrdersExpress.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 修改订单状态
	 */
	@Override
	public LianjiuResult modifyOrderStatus(String ordersId, Byte ordersStatus) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(501, "请指定要更新的快递订单id");
		}
		OrdersExpress orders = new OrdersExpress();
		orders.setOrExpressId(ordersId);
		orders.setOrExpressStatus(ordersStatus);
		orders.setOrExpressUpdated(new Date());
		int rowsAffected = 0;
		try {
			rowsAffected = ordersExpressMapper.updateByPrimaryKeySelective(orders);
		} catch (RuntimeException e) {
			loggerOrdersExpress.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 修改运单编号
	 */
	@Override
	public LianjiuResult modifyOrderExpressNum(String ordersId, Byte ordersStatus, String ordersExpressNum) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(501, "请指定要更新的快递订单id");
		}
		OrdersExpress orders = new OrdersExpress();
		orders.setOrExpressId(ordersId);
		orders.setOrExpressStatus(ordersStatus);
		orders.setOrExpressNum(ordersExpressNum);
		orders.setOrExpressUpdated(new Date());
		int rowsAffected = 0;
		try {
			rowsAffected = ordersExpressMapper.updateByPrimaryKeySelective(orders);
		} catch (RuntimeException e) {
			loggerOrdersExpress.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(rowsAffected);
	}


	@Override
	public LianjiuResult addExpresssNum(OrdersExpress ordersExpress) {
		if (null == ordersExpress) {
			return LianjiuResult.build(501, "数据为空");
		}
		int rows = 0;
		try {
			rows = ordersExpressMapper.updateExpressNum(ordersExpress);
		} catch (RuntimeException e) {
			loggerOrdersExpress.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		if (rows < 1) {
			return LianjiuResult.build(501, "快递单号修改失败");
		}
		return LianjiuResult.ok("快递单号修改成功");
	}

	@Override
	public LianjiuResult getExpressList(String userId, int pageNum, int pageSize) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(501, "请指定正确的用户id");
		}
		Map<String, Object> results = new HashMap<>();
		LianjiuResult result = new LianjiuResult(results);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<OrdersExpressItemVo> orderList = ordersExpressMapper.selectListByUserId(userId);
		for (OrdersExpressItemVo ordersExpress : orderList) {
			List<OrdersItem> ordersitem = ordersItemMapper.getItemByOrdersId(ordersExpress.getOrExpressId());
			ordersExpress.setProductNum(ordersitem.size());
			int i = 0;
			for (OrdersItem ordersItem2 : ordersitem) {
				if (Integer.parseInt(ordersItem2.getOrItemsPrice()) > i) {
					i = Integer.parseInt(ordersItem2.getOrItemsPrice());
					ordersExpress.setImage(ordersItem2.getOrItemsPicture());
					ordersExpress.setProductName(ordersItem2.getOrItemsName());
				}
			}
		}
		Page<OrdersExpressItemVo> listFaceface = (Page<OrdersExpressItemVo>) orderList;
		result.setTotal(listFaceface.getTotal());
		results.put("orderList", orderList);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 订单状态(0代发货1待验机2结算3取消4全部)
	 */
	@Override
	public LianjiuResult getExpressStutsList(String userId, int type, int pageNum, int pageSize) {
		Map<String, Object> results = new HashMap<>();
		LianjiuResult result = new LianjiuResult(results);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		OrdersExpress ordersExpress1 = new OrdersExpress();
		ordersExpress1.setOrExpressUserId(userId);
		ordersExpress1.setOrExpressStatus((byte) type);
		List<OrdersExpressItemVo> orderList = ordersExpressMapper.getExpressStutsList(ordersExpress1);
		if (orderList != null && orderList.size() != 0) {
			for (OrdersExpressItemVo ordersExpress : orderList) {
				List<OrdersItem> ordersitem = ordersItemMapper.getItemByOrdersId(ordersExpress.getOrExpressId());
				ordersExpress.setProductNum(ordersitem.size());
				double i = 0.00;
				for (OrdersItem ordersItem2 : ordersitem) {
					if (Double.parseDouble(ordersItem2.getOrItemsPrice()) > i) {
						i = Double.parseDouble(ordersItem2.getOrItemsPrice());
						ordersExpress.setImage(ordersItem2.getOrItemsPicture());
						ordersExpress.setProductName(ordersItem2.getOrItemsName());
					}
				}
			}
		}
		Page<OrdersExpressItemVo> listFaceface = (Page<OrdersExpressItemVo>) orderList;
		result.setTotal(listFaceface.getTotal());
		results.put("orderList", orderList);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	@Override
	public LianjiuResult ModifyExpressOrderStatus(OrdersExpress ordersExpress) {
		if (null == ordersExpress) {
			return LianjiuResult.build(501, "请指定正确的数据");
		}
		OrdersItem item = new OrdersItem();
		item.setOrdersId(ordersExpress.getOrExpressId());
		int rows = 0;
		try {
			if (ordersExpress.getOrExpressStatus() == (byte) 4) {
				item.setOrItemsStatus(0);
				item.setOrItemsUpdated(new Date());
				ordersItemMapper.updateByStatus(item);
			} else if (ordersExpress.getOrExpressStatus() == (byte) 5) {
				item.setOrItemsStatus(1);
				item.setOrItemsUpdated(new Date());
				ordersItemMapper.updateByStatus(item);
			}
			rows = ordersExpressMapper.ModifyExpressOrderStatus(ordersExpress);
		} catch (RuntimeException e) {
			loggerOrdersExpress.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		if (rows == 1) {
			return LianjiuResult.ok("快递单号修改成功");
		} else {
			return LianjiuResult.build(501, "状态修改失败");
		}
	}

	/**
	 * 立即卖掉
	 */
	@Override
	public LianjiuResult directExpressSale(OrdersItem ordersItem, String userId) {
		ordersItem.setOrItemsCreated(new Date());// 设置时间
		if (null == userId || userId.length() == 0) {
			return LianjiuResult.build(501, "error 用户id不能为空！");
		}
		// 从redis中获取usercare
		List<OrdersItem> itemList = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.ORDERS_EXPRESS_CART + userId, OrdersItem.class);
		if (null == itemList) {
			itemList = new ArrayList<OrdersItem>();
		}
		// 最高价格商品的图片
		String maximage = null;
		/**
		 * 筛选最高价格的item的图片
		 */
		String itemsPrice = ordersItem.getOrItemsPrice();// 单价
		String itemNum = ordersItem.getOrItemsNum();// 数量

		String orItemsAccountPrice = CalculateStringNum.multiply(itemsPrice, itemNum);// 相乘
		maximage = ordersItem.getOrItemsPicture();// 取出最高价格的item对应的图片
		/**
		 * 给item追加赋值
		 */
		String itemsId = IDUtils.genOrdersId();
		ordersItem.setOrItemsId(itemsId);
		ordersItem.setOrItemsBeforePrice(itemsPrice);
		ordersItem.setOrItemsAccountPrice(orItemsAccountPrice);
		ordersItem.setOrItemsChooseFlag(1);// 被选中
		ordersItem.setOrItemsType("手机");
		ordersItem.setOrItemsStatus(0);
		// 回收类型
		ordersItem.setOrItemsRecycleType("快递回收");
		ordersItem.setOrItemsCreated(new Date());
		ordersItem.setOrItemsUpdated(new Date());

		List<String> productIdList = new ArrayList<String>();
		productIdList.add(itemsId);
		Map<String, Object> map = new HashMap<String, Object>();
		// 最高Item的总价
		map.put("maxPrice", itemsPrice);
		map.put("maximage", maximage);
		map.put("productIdList", productIdList);
		/**
		 * 把剩余的产品存回去
		 */
		itemList.add(ordersItem);
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CART + userId, JsonUtils.objectToJson(itemList));
		/**
		 * 缓存结算之后要加载的数据
		 */
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CART_BALANCE_PRICE + userId, orItemsAccountPrice); // 立即卖掉，只有一种产品
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CART_ITEM_PRICE_MAX + userId, itemsPrice);
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CART_ITEM_IMAGE_MAX + userId, maximage);
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CART_ITEM_CHOOSER + userId,
				JsonUtils.objectToJson(productIdList));
		loggerOrdersExpress.info("开始立即卖掉" + JsonUtils.objectToJson(productIdList));
		return LianjiuResult.ok(map);
	}

	/**
	 * 质检明细
	 */
	@Override
	public LianjiuResult qualityCheckingDetails(String orItemsId) {
		if (null == orItemsId) {
			return LianjiuResult.build(501, "请指定正确的数据参数");
		}
		OrdersItemInfo ordersItem = ordersItemMapper.qualityCheckingExpress(orItemsId);
		return LianjiuResult.ok(ordersItem);
	}

	/**
	 * 确认价格
	 */
	@Override
	public LianjiuResult orderPriceConfirm(String ordersId) {
		OrdersExpress orders = new OrdersExpress(ordersId, new Date());
		orders.setOrExpressStatus(GlobalValueUtil.ORDER_EXPRESS_BALANCE_NO);// 待结算
		int rowsAffected = 0;
		try {
			rowsAffected = ordersExpressMapper.updateByPrimaryKeySelective(orders);
		} catch (RuntimeException e) {
			loggerOrdersExpress.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		loggerOrdersExpress.info(rowsAffected);
		// 清除在定时器系统的倒计时
		HttpClientUtil.doPost(GlobalValueUtil.TIMER_BASE_URL + "orderExpress/orderConfirmDel/" + ordersId);
		return LianjiuResult.ok(ordersId);
	}

	/**
	 * 不同意价格
	 */
	@Override
	public LianjiuResult orderPriceRefuse(String ordersId) {
		OrdersExpress orders = new OrdersExpress(ordersId, new Date());
		orders.setOrExpressStatus(GlobalValueUtil.ORDER_EXPRESS_DISPATCH);// 退货中
		int rowsAffected = 0;
		try {
			rowsAffected = ordersExpressMapper.updateByPrimaryKeySelective(orders);
		} catch (RuntimeException e) {
			loggerOrdersExpress.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		loggerOrdersExpress.info(rowsAffected);
		// 清除在定时器系统的倒计时
		HttpClientUtil.doPost(GlobalValueUtil.TIMER_BASE_URL + "orderExpress/orderConfirmDel/" + ordersId);
		return LianjiuResult.ok(ordersId);
	}

	/**
	 * 取消订单
	 */
	@Override
	public LianjiuResult orderCancel(String ordersId) {
		OrdersExpress orders = new OrdersExpress(ordersId, new Date());
		orders.setOrExpressStatus(GlobalValueUtil.ORDER_EXPRESS_CANCEL);// 取消
		int rowsAffected = 0;
		try {
			rowsAffected = ordersExpressMapper.updateByPrimaryKeySelective(orders);
		} catch (RuntimeException e) {
			loggerOrdersExpress.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		loggerOrdersExpress.info(rowsAffected);
		return LianjiuResult.ok(ordersId);
	}

	/**
	 * 查询订单中所有产品详情页
	 */
	@Override
	public LianjiuResult selectExpressItemByOrderId(String ordersId) {
		List<OrdersItem> OrdersItem = ordersItemMapper.selectExpressItemsByOrdersId(ordersId);
		return LianjiuResult.ok(OrdersItem);
	}

}
