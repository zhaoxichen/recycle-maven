package com.lianjiu.order.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.tools.CalculateStringNum;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.HttpClientUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Comment;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersExcellentDetails;
import com.lianjiu.model.OrdersExcellentRefund;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.model.OrdersPayment;
import com.lianjiu.model.UserAddress;
import com.lianjiu.order.service.OrderExcellentService;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.rest.mapper.CommentMapper;
import com.lianjiu.rest.mapper.OrdersPaymentMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentRefundMapper;
import com.lianjiu.rest.mapper.orders.OrdersItemMapper;
import com.lianjiu.rest.mapper.product.ProductExcellentMapper;
import com.lianjiu.rest.mapper.user.UserAddressMapper;
import com.lianjiu.rest.model.OrdersExcellentVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 精品订单服务层
 * 
 * @author zhaoxi
 * 
 */
@Service
public class OrderExcellentServiceImpl implements OrderExcellentService {

	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;
	@Autowired
	private UserAddressMapper userAddressMapper;
	@Autowired
	private OrdersItemMapper ordersItemMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private OrdersExcellentRefundMapper ordersExcellentRefundMapper;
	@Autowired
	private ProductExcellentMapper productExcellentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private OrdersPaymentMapper ordersPaymentMapper;
	private static Logger loggerOrdersExcellent = Logger.getLogger("ordersExcellent");

	@Override
	public LianjiuResult getExcellentById(String excellentId) {
		if (Util.isEmpty(excellentId)) {
			return LianjiuResult.build(501, "请指定精品订单id");
		}
		// 去数据库查询
		OrdersExcellent excellent = ordersExcellentMapper.selectByPrimaryKey(excellentId);
		return LianjiuResult.ok(excellent);
	}

	/**
	 * 提交订单
	 */
	@Override
	public LianjiuResult addOrdersExcellent(OrdersExcellentVo ordersExcellentVo) {
		OrdersExcellent ordersExcellent = ordersExcellentVo.getOrdersExcellent();
		OrdersExcellentDetails ordersExcellentDetails = ordersExcellentVo.getOrdersExcellentDetails();
		if (null == ordersExcellentDetails) {
			ordersExcellentDetails = new OrdersExcellentDetails();
		}
		if (null == ordersExcellent) {
			return LianjiuResult.build(502, "传入的对象为空");
			// 判断用户id 分类id 电话 是否为空
		}
		if (null == ordersExcellentVo.getItemList() || 0 > ordersExcellentVo.getItemList().size()) {
			return LianjiuResult.build(503, "传入精品id为空");
		}
		OrdersItem itemTemp = ordersExcellentVo.getItemList().get(0);
		String excellentObjectJson = jedisClient.get(itemTemp.getOrItemsProductId() + "excellentItem");
		loggerOrdersExcellent.info("取回的对象json为：" + excellentObjectJson);
		OrdersItem item = null;
		if (null != excellentObjectJson) {
			item = JsonUtils.jsonToPojo(excellentObjectJson, OrdersItem.class);
			loggerOrdersExcellent.info("打印对象");
			Util.printModelDetails(item);
		}
		if (null == item) {
			return LianjiuResult.build(503, "传入的对象为空");
		}
		// 价格
		String priceTotal = item.getOrItemsPrice();
		Long categoryId = categoryMapper.selectByCategoryName("二手精品");
		String ordersId = IDUtils.genOrdersId();
		ordersExcellent.setCategoryId(categoryId);
		ordersExcellent.setOrExcellentId(ordersId);
		ordersExcellent.setOrdersPrice(priceTotal);
		ordersExcellent.setOrItemsNamePreview(item.getOrItemsName());
		ordersExcellent.setOrItemsPictruePreview(item.getOrItemsPicture());
		ordersExcellent.setOrExcellentCreated(new Date());
		ordersExcellent.setOrExcellentUpdated(new Date());
		ordersExcellent.setOrExcellentStatus(GlobalValueUtil.ORDER_STATUS_PAY_NO);
		// 只要一条
		item.setOrItemsId(IDUtils.genOrdersId());// 重新生成id
		item.setOrdersId(ordersId);
		item.setOrItemsNum("1");
		item.setOrItemsCreated(new Date());
		item.setOrItemsUpdated(new Date());
		item.setOrItemsStatus(0);// 未完成
		item.setOrItemsStemFrom(2);
		item.setOrItemsType("优品");
		item.setOrItemsRecycleType("二手精品");
		item.setOrItemsPriceUnit((long) 7);
		BigDecimal itemsPriceTotalBigDecimal = new BigDecimal(item.getOrItemsPrice()).multiply(new BigDecimal(1));
		item.setOrItemsAccountPrice(itemsPriceTotalBigDecimal.toString());
		item.setOrItemsBeforePrice(item.getOrItemsPrice());
		Util.printModelDetails(item);
		int itemCount = 0;
		int excellentCount = 0;
		try {
			// 保存订单
			excellentCount = ordersExcellentMapper.insert(ordersExcellent);
			// 存购买记录
			itemCount = ordersItemMapper.insert(item);
			loggerOrdersExcellent.info("item添加成功：" + itemCount);
			// 添加预支付
			OrdersPayment payment = new OrdersPayment(IDUtils.genPaymentId(), ordersId, priceTotal,
					ordersExcellent.getUserId());
			ordersPaymentMapper.insert(payment);
			// 减库存
			productExcellentMapper.updateProductStockReduce(item.getOrItemsProductId());
		} catch (RuntimeException e) {
			loggerOrdersExcellent.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		// 开启倒计时
		if (0 < excellentCount) {
			// 启动超时监控,定时器在定时器系统中开启
			HttpClientUtil.doPost(GlobalValueUtil.TIMER_BASE_URL + "ordersExcellent/orderCancel/" + ordersId);
		}
		return LianjiuResult.ok(ordersId);
	}

	@Override
	public LianjiuResult updateExcellent(OrdersExcellent excellent) {
		if (null == excellent) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(excellent.getOrExcellentId())) {
			return LianjiuResult.build(501, "请指定要更新的精品订单id");
		}
		excellent.setOrExcellentUpdated(new Date());
		// 去数据库更新
		int rowsAffected = 0;
		try {
			rowsAffected = ordersExcellentMapper.updateByPrimaryKeySelective(excellent);
		} catch (RuntimeException e) {
			loggerOrdersExcellent.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteExcellent(String excellentId) {
		if (Util.isEmpty(excellentId)) {
			return LianjiuResult.build(501, "请指定要删除的精品订单id");
		}
		// 去数据库删除
		int rowsAffected = 0;
		try {
			rowsAffected = ordersExcellentMapper.deleteByPrimaryKey(excellentId);
		} catch (RuntimeException e) {
			loggerOrdersExcellent.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(rowsAffected);
	}

	// 精品订单状态的更新
	@Override
	public LianjiuResult modifyExcellentState(String excellentId, Byte orExcellentStatus) {
		loggerOrdersExcellent.info("excellentId:" + excellentId);
		if ((ordersExcellentMapper.selectByPrimaryKeyCheck(excellentId)) == 0
				|| (orExcellentStatus + "").equals("null")) {
			return LianjiuResult.build(502, "error! 要更新的精品订单不存在或状态不存在！");
		}
		if (GlobalValueUtil.ORDER_STATUS_CANCEL.equals(orExcellentStatus)) {
			return LianjiuResult.build(501, "已经取消过了！");
		}
		OrdersExcellent excellent = new OrdersExcellent();
		excellent.setOrExcellentId(excellentId);
		excellent.setOrExcellentStatus(orExcellentStatus);
		excellent.setOrExcellentUpdated(new Date());
		int rowsAffected = 0;
		try {
			rowsAffected = ordersExcellentMapper.updateByPrimaryKeySelective(excellent);
			// 订单完成，订单进入待评价状态，销量加1
			if (GlobalValueUtil.ORDER_STATUS_EVALUATE_NO.equals(orExcellentStatus)) {
				productExcellentMapper.updateProductSoldCount(excellentId);
			}
		} catch (RuntimeException e) {
			loggerOrdersExcellent.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}

		return LianjiuResult.ok(rowsAffected);
	}

	// 设置精品订单的处理时间
	@Override
	public LianjiuResult modifyExcellentHandleTime(String excellentId) {
		if (ordersExcellentMapper.selectByPrimaryKeyCheck(excellentId) == 0) {
			return LianjiuResult.build(502, "error! 要更新的精品订单不存在！");
		}
		OrdersExcellent excellent = new OrdersExcellent();
		excellent.setOrExcellentId(excellentId);
		excellent.setOrExcellentHandleTime(new Date());
		excellent.setOrExcellentUpdated(new Date());
		int rowsAffected = 0;
		try {
			rowsAffected = ordersExcellentMapper.modifyExcellentHandleTime(excellent);
		} catch (RuntimeException e) {
			loggerOrdersExcellent.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 立即购买后加载的数据
	 */
	@Override
	public LianjiuResult selectAddressDefault(String userId) {
		Map<String, Object> results = new HashMap<>();
		// 查用户的默认地址
		UserAddress address = userAddressMapper.selectDefault(userId);
		// 存入总价格
		LianjiuResult result = new LianjiuResult(results);
		if (null == address) {
			result.setStatus(502);
			result.setMsg("没有设置默认地址");
		} else {
			result.setStatus(200);
			result.setMsg("ok");
		}
		results.put("address", address);
		return result;
	}

	/**
	 * 发起退款
	 */
	@Override
	public LianjiuResult addRefund(OrdersExcellentRefund refund, Byte ordersStatus) {
		if (null == refund) {
			return LianjiuResult.build(501, "传入的对象为空");
		}
		refund.setOrExceRefundId(IDUtils.genOrdersId());
		refund.setOrExceRefundCreated(new Date());
		refund.setOrExceRefundUpdated(new Date());
		String pictures = refund.getOrExceRefundExpresspic();
		if (pictures.endsWith(",")) {
			pictures = pictures.substring(0, pictures.length() - 1);
			refund.setOrExceRefundExpresspic(pictures);
		}
		// 去数据库添加
		int rowsAffected = 0;
		try {
			String ePrice = ordersExcellentMapper.slectPriceByOrderNo(refund.getOrExcellentId());
			if (0 > CalculateStringNum.compareTo(ePrice, refund.getOrExceRefundMoney())) {
				return LianjiuResult.build(502, "申请退款金额与订单金额不符合");
			}
			rowsAffected = ordersExcellentRefundMapper.insert(refund);
			// 去修改订单状态
			rowsAffected += ordersExcellentMapper.modifyOrdersState(refund.getOrExcellentId(), ordersStatus);
		} catch (RuntimeException e) {
			loggerOrdersExcellent.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 退货退款 修改订单的运单号
	 */
	@Override
	public LianjiuResult modifyRefundByOrdersId(String orExcellentId, String refundExpress, String refundExpressNum) {
		if (Util.isEmpty(orExcellentId)) {
			return LianjiuResult.build(501, "请指定要更新的退款精品订单id");
		}
		OrdersExcellentRefund refund = new OrdersExcellentRefund();
		refund.setOrExcellentId(orExcellentId);
		refund.setOrExceRefundExpress(refundExpress);
		refund.setOrExceRefundExpressnum(refundExpressNum);
		refund.setOrExceRefundUpdated(new Date());
		// 去数据库更新
		int rowsAffected = 0;
		try {
			rowsAffected = ordersExcellentRefundMapper.updateByOrdersId(refund);
		} catch (RuntimeException e) {
			loggerOrdersExcellent.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(510, "数据异常！");
		}
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 查看当前订单的items
	 */
	@Override
	public LianjiuResult getExcellentItems(String excellentId) {
		Byte orExcellentStatus = ordersExcellentMapper.getOrdersStatus(excellentId);
		if (null == orExcellentStatus) {
			orExcellentStatus = 0;
		}
		List<OrdersItem> items = ordersItemMapper.getItemByOrdersExcellentId(excellentId);
		for (OrdersItem item : items) {
			String orItemsParam = item.getOrItemsParam();
			String additional = null;
			if (Util.notEmpty(orItemsParam)) {
				additional = this.checkAdditionalFromParam(orItemsParam);
				item.setOrItemsParam(additional);
				item.setOrItemsStatus(orExcellentStatus.intValue());
			}

		}
		return LianjiuResult.ok(items);
	}

	private String checkAdditionalFromParam(String orItemsParam) {
		String retrieveTypeData = null;
		StringBuffer additional = new StringBuffer();
		try {
			// 筛选出具体的参数
			JSONArray paramDataArray1 = JSONArray.fromObject(orItemsParam);// json串转为json数组
			for (Object object1 : paramDataArray1) {
				JSONArray paramDataArray2 = JSONArray.fromObject(object1);// json串转为json数组
				for (Object object2 : paramDataArray2) {
					JSONArray paramDataArray3 = JSONArray.fromObject(object2);// json串转为json数组
					for (Object children1 : paramDataArray3) {
						JSONObject majorDataJsonObject1 = JSONObject.fromObject(children1);// json串转为json对象
						JSONArray children2 = (JSONArray) majorDataJsonObject1.get("children"); // children为数组
						for (int i = 0; i < 3; i++) {
							JSONObject majorDataJsonObject = JSONObject.fromObject(children2.get(i));// json串转为json对象
							retrieveTypeData = (String) majorDataJsonObject.get("retrieveType");
							if (Util.notEmpty(retrieveTypeData)) {
								additional.append(retrieveTypeData + ",");
							}
						}
					}
				}
			}
		} catch (NullPointerException e) {
			return "精品json参数有问题";
		} catch (ClassCastException e) {
			return "精品json参数有问题";
		}
		return additional.substring(0, additional.length() - 1);
	}

	/**
	 * 获取评论
	 */
	@Override
	public LianjiuResult getOneComment(String ordersId) {
		List<Comment> comments = commentMapper.selectByOrdersId(ordersId);
		LianjiuResult result = new LianjiuResult();
		result.setStatus(200);
		result.setMsg("ok");
		if (comments.size() > 0) {
			result.setLianjiuData(comments.get(0));
			result.setTotal(1);
		} else {
			result.setLianjiuData("未评论");
			result.setTotal(0);
		}
		return result;
	}

	/**
	 * 获取当前订单剩余的处理时间
	 */
	@Override
	public LianjiuResult getTimeRemaining(String ordersId) {
		long ttl = jedisClient.ttl(GlobalValueJedis.ORDERS_EXCELLENT_COUNT_DOWN_PRICE + ordersId);
		// 如果计时结束
		if (-2 == ttl) {
			return LianjiuResult.ok(0);
		}
		return LianjiuResult.ok(ttl);
	}
}
