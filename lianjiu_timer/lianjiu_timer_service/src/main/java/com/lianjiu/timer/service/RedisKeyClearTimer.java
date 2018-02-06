package com.lianjiu.timer.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lianjiu.common.pojo.ProductPriceGroup;
import com.lianjiu.common.tools.PriceEvaluate;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.product.ProductPriceGroupMapper;
import com.lianjiu.rest.tool.JedisTool;

public class RedisKeyClearTimer {
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private ProductPriceGroupMapper productPriceGroupMapper;
	private static Logger loggerVisit = Logger.getLogger("visit");

	/**
	 * 
	 * zhaoxi 2017年12月28日 descrption: 链旧首页的redis的key，凌晨一点统一清除 void
	 */
	public void lianjiuIndex() {
		// 获取redis中所有链旧首页的redis的key
		Set<String> keySet = jedisClient.keys(GlobalValueJedis.INDEX_AD_RELATIVE);
		Iterator<String> iterator = keySet.iterator();
		/**
		 * 深夜清除缓存，用户量小不需要开线程处理
		 */
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			loggerVisit.info("清除链旧首页缓存：" + key);
			jedisClient.del(key);
		}
	}

	/**
	 * 
	 * zhaoxi 2018年1月11日 descrption:回收车的redis的key，凌晨一点统一清除 void
	 */
	public void ordersFaceFaceCart() {

		// 获取redis中所有快递回收的redis的key
		Set<String> keySet = jedisClient.keys(GlobalValueJedis.ORDERS_EXPRESS_CART_RELATIVE);
		Iterator<String> iterator = keySet.iterator();
		/**
		 * 深夜清除缓存，用户量小不需要开线程处理
		 */
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			loggerVisit.info("快递回收车缓存：" + key);
			jedisClient.del(key);
		}

		// 获取redis中所有上门回收的redis的key
		keySet = jedisClient.keys(GlobalValueJedis.ORDERS_FACEFACE_CART_RELATIVE);
		iterator = keySet.iterator();
		/**
		 * 深夜清除缓存，用户量小不需要开线程处理
		 */
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			loggerVisit.info("上门回收车缓存：" + key);
			jedisClient.del(key);
		}
	}

	/**
	 * 
	 * zhaoxi 2018年1月11日 descrption:更新用户的回收车内容 void
	 */
	public void ordersFaceFaceCartUpdate() {
		// 获取redis中存在的价格更新
		Set<String> keySet = jedisClient.keys(GlobalValueJedis.PRODUCT_PRICE_UPDATE_RELATIVE);
		if (keySet.isEmpty()) {
			System.out.println("没有更新价格");
			return;
		}
		loggerVisit.info("有价格更新");
		// 获取redis中所有快递回收的redis的key
		keySet = jedisClient.keys(GlobalValueJedis.ORDERS_EXPRESS_CART_RELATIVE);
		Iterator<String> iterator = keySet.iterator();
		/**
		 * 价格修改不频繁，开销小不需要开线程处理
		 */
		List<OrdersItem> expressList = null;
		String productId = null;
		String productParamData = null;
		String productPrice = null;
		List<ProductPriceGroup> priceGroups = null;
		String evaluatePrice = null;
		boolean isModify = false;
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			loggerVisit.info("快递回收车缓存：" + key);
			// cart_express_LJ1514435398358
			if (key == null || !key.matches("cart_express_LJ[0-9]{13}")) {
				continue;
			}
			expressList = JedisTool.checkListFormRedis(jedisClient, key, OrdersItem.class);
			if (null == expressList) {
				continue;
			}
			for (OrdersItem ordersItem : expressList) {
				productId = ordersItem.getOrItemsProductId();
				productParamData = ordersItem.getOrItemsParam();
				productPrice = jedisClient.get(GlobalValueJedis.PRODUCT_PRICE_UPDATE + productId);
				// 判断改item对应的商品有没有修改价格
				if (Util.isEmpty(productPrice)) {
					continue;
				}
				System.out.println(GlobalValueJedis.PRODUCT_PRICE_UPDATE + productId);
				jedisClient.del(GlobalValueJedis.PRODUCT_PRICE_UPDATE + productId);// 删除key
				// 手机重新估价
				priceGroups = productPriceGroupMapper.selectByPid(ordersItem.getOrItemsProductId());// 通过产品id查出价格组合
				evaluatePrice = PriceEvaluate.executePhone(priceGroups, productParamData, productPrice);
				// 与回收车物品的估价对比
				if (evaluatePrice != null && !evaluatePrice.equals(ordersItem.getOrItemsPrice())) {
					ordersItem.setOrItemsPrice(evaluatePrice);
					isModify = true;
				}
			}
			// 一个用户的回收车修改完成
			if (isModify) {
				// 更新redis
				jedisClient.set(key, JsonUtils.objectToJson(expressList));
			}
			isModify = false;
		}
		// 获取redis中所有上门回收的redis的key
		keySet = jedisClient.keys(GlobalValueJedis.ORDERS_FACEFACE_CART_RELATIVE);
		iterator = keySet.iterator();
		/**
		 * 价格修改不频繁，开销小不需要开线程处理
		 */
		List<OrdersItem> facefaceList = null;
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			loggerVisit.info("上门回收车缓存：" + key);
			// cart_faceface_LJ1514435398358
			if (key == null || !key.matches("cart_faceface_LJ[0-9]{13}")) {
				continue;
			}
			facefaceList = JedisTool.checkListFormRedis(jedisClient, key, OrdersItem.class);
			if (null == facefaceList) {
				continue;
			}
			for (OrdersItem ordersItem : facefaceList) {
				productId = ordersItem.getOrItemsProductId();
				productParamData = ordersItem.getOrItemsParam();
				productPrice = jedisClient.get(GlobalValueJedis.PRODUCT_PRICE_UPDATE + productId);
				// 判断改item对应的商品有没有修改价格
				if (Util.isEmpty(productPrice)) {
					continue;
				}
				System.out.println(GlobalValueJedis.PRODUCT_PRICE_UPDATE + productId);
				jedisClient.del(GlobalValueJedis.PRODUCT_PRICE_UPDATE + productId);// 删除key
				// 家电重新估价
				evaluatePrice = PriceEvaluate.executeHousehold(productParamData, productPrice);
				// 与回收车物品的估价对比
				if (evaluatePrice != null && !evaluatePrice.equals(ordersItem.getOrItemsPrice())) {
					ordersItem.setOrItemsPrice(evaluatePrice);
					ordersItem.setOrItemsAccountPrice(evaluatePrice);
					isModify = true;
				}
			}
			// 一个用户的回收车修改完成
			if (isModify) {
				// 更新redis
				jedisClient.set(key, JsonUtils.objectToJson(facefaceList));
			}
			isModify = false;
		}

		// 清除多余的标识
		keySet = jedisClient.keys(GlobalValueJedis.PRODUCT_PRICE_UPDATE_RELATIVE);
		iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			jedisClient.del(key);

		}
	}
}
