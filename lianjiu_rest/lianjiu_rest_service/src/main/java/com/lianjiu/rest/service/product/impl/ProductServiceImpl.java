package com.lianjiu.rest.service.product.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.pojo.ProductPriceGroup;
import com.lianjiu.common.tools.PriceEvaluate;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.common.utils.sendSMS;
import com.lianjiu.model.AdElectronic;
import com.lianjiu.model.AdHotProduct;
import com.lianjiu.model.Category;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.model.Product;
import com.lianjiu.model.ProductParamTemplate;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.rest.mapper.CommentMapper;
import com.lianjiu.rest.mapper.content.AdElectronicMapper;
import com.lianjiu.rest.mapper.content.AdHotProductMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.rest.mapper.orders.OrdersItemMapper;
import com.lianjiu.rest.mapper.product.ProductMapper;
import com.lianjiu.rest.mapper.product.ProductParamTemplateMapper;
import com.lianjiu.rest.mapper.product.ProductPriceGroupMapper;
import com.lianjiu.rest.model.OrdersItemInfo;
import com.lianjiu.rest.model.ProductMaxprice;
import com.lianjiu.rest.service.product.ProductService;
import com.lianjiu.rest.tool.JedisTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;

/**
 * 商品管理服务层
 * 
 * @author zhaoxi
 *
 */
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductMapper productMapper;

	@Autowired
	ProductParamTemplateMapper productParamTemplateMapper;

	@Autowired
	OrdersItemMapper ordersItemMapper;

	@Autowired
	CommentMapper commentMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private JedisClient jedisClient;

	@Autowired
	private AdHotProductMapper adHotProductMapper;

	@Autowired
	private ProductPriceGroupMapper productPriceGroupMapper;

	@Autowired
	private OrdersFacefaceMapper ordersFacefaceMapper;

	@Autowired
	private AdElectronicMapper adElectronicMapper;

	@Override
	public LianjiuResult getBrandBycId(Long cId) {
		List<Category> electronicBrands = categoryMapper.selectByParentId(cId);
		Map<String, Object> results = new HashMap<>();
		results.put("brands", electronicBrands);
		LianjiuResult result = new LianjiuResult(results);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	@Override
	public LianjiuResult getParamTemplateFromCategory(String cid) {
		ProductParamTemplate paramTemplate = productParamTemplateMapper.selectById(cid);
		return LianjiuResult.ok(paramTemplate);
	}

	/**
	 * 查询一个商品
	 */
	@Override
	public LianjiuResult selectProductByid(String productId) {
		Product product = productMapper.selectByPrimaryKey(productId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("product", product);
		return LianjiuResult.ok(map);
	}

	/**
	 * 通过分类id查询出商品
	 */
	@Override
	public LianjiuResult selectByProductCategoryId(long categoryId, int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<Product> product = productMapper.selectByProductCategoryId(categoryId);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Product> listTemplate = (Page<Product>) product;
		System.out.println("总页数：" + listTemplate.getPages());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("product", product);
		map.put("totalPage", listTemplate.getPages());
		return LianjiuResult.ok(map);
	}

	/**
	 * 卖手机，估价之后加载数据
	 */
	@Override
	public LianjiuResult calculationPrice(String TAKEN) {
		// 通过productParamData 估算出价格
		String productParamData = jedisClient.get(GlobalValueJedis.EVALUATE_PARAM + TAKEN);
		String price = jedisClient.get(GlobalValueJedis.EVALUATE_PRICE + TAKEN);
		String productId = jedisClient.get(GlobalValueJedis.EVALUATE_PRODUCTID + TAKEN);
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(502, "TAKEN已失效!");
		}
		if (Util.isEmpty(price)) {
			return LianjiuResult.build(503, "TAKEN已失效");
		}
		System.out.println("取出手机估算价格：" + price);
		// 去item表中查询出所有已成交的手机 or_item_product_id
		List<OrdersItem> itemList = ordersItemMapper.getItemByPhoneType();
		Product product = productMapper.selectByPrimaryKey(productId);
		product.setProductParamData(productParamData);
		product.setProductPrice(price);
		product.setProductPriceAlliance(null);
		// jedisClient.del(GlobalValueJedis.EVALUATE_PARAM+TAKEN);
		// jedisClient.del(GlobalValueJedis.EVALUATE_PRICE+TAKEN);
		jedisClient.expire(GlobalValueJedis.EVALUATE_PARAM + TAKEN, 1800);
		jedisClient.expire(GlobalValueJedis.EVALUATE_PRICE + TAKEN, 1800);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productPrice", price);
		map.put("item", itemList);
		map.put("product", product);
		return LianjiuResult.ok(map);
	}

	/**
	 * 卖家电估价之后加载数据
	 */
	@Override
	public LianjiuResult calculationJDPrice(String TAKEN) {
		// 通过productParamData 估算出价格
		String productParamData = jedisClient.get(GlobalValueJedis.EVALUATE_PARAM + TAKEN);
		String price = jedisClient.get(GlobalValueJedis.EVALUATE_PRICE + TAKEN);
		String productId = jedisClient.get(GlobalValueJedis.EVALUATE_PRODUCTID + TAKEN);
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(502, "TAKEN已失效");
		}
		if (Util.isEmpty(price)) {
			return LianjiuResult.build(503, "TAKEN已失效");
		}
		System.out.println(price);
		// 回收方式待定
		// 去item表中查询出所有已售出的家电 or_item_product_id
		List<OrdersItem> itemList = ordersItemMapper.getItemByHouseType();
		Product product = productMapper.selectByPrimaryKey(productId);
		product.setProductParamData(productParamData);
		product.setProductPrice(price);
		product.setProductPriceAlliance(null);
		// jedisClient.del(GlobalValueJedis.EVALUATE_PARAM+TAKEN);
		// jedisClient.del(GlobalValueJedis.EVALUATE_PRICE+TAKEN);
		jedisClient.expire(GlobalValueJedis.EVALUATE_PARAM + TAKEN, 1800);
		jedisClient.expire(GlobalValueJedis.EVALUATE_PRICE + TAKEN, 1800);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productPrice", price);
		map.put("item", itemList);
		map.put("product", product);
		return LianjiuResult.ok(map);
	}

	/**
	 * 手机，马上估价
	 */
	@Override
	public LianjiuResult calculationImmediately(String productParamData, String productId, String productPrice) {
		/**
		 * ios暂时未能解决格式化json的办法
		 */
		try {
			productParamData = JSONArray.fromObject(productParamData).toString();
		} catch (JSONException e) {
			System.out.println("error  productParamData数据异常!");
		}
		// 通过productParamData 估算出价格待定
		List<ProductPriceGroup> priceGroups = productPriceGroupMapper.selectByPid(productId);// 通过产品id查出价格组合
		String price = PriceEvaluate.executePhone(priceGroups, productParamData, productPrice);
		// 回收方式待定
		String TAKEN = IDUtils.genToken();
		jedisClient.set(GlobalValueJedis.EVALUATE_PARAM + TAKEN, productParamData);
		jedisClient.set(GlobalValueJedis.EVALUATE_PRICE + TAKEN, price);
		jedisClient.set(GlobalValueJedis.EVALUATE_PRODUCTID + TAKEN, productId);
		jedisClient.expire(GlobalValueJedis.EVALUATE_PARAM + TAKEN, 3600);
		jedisClient.expire(GlobalValueJedis.EVALUATE_PRICE + TAKEN, 3600);
		jedisClient.expire(GlobalValueJedis.EVALUATE_PRODUCTID + TAKEN, 3600);
		return LianjiuResult.ok(TAKEN);
	}

	/**
	 * 家电，马上估价
	 */
	@Override
	public LianjiuResult calculationJDImmediately(String productParamData, String productId, String productPrice) {
		/**
		 * ios暂时未能解决格式化json的办法
		 */
		try {
			productParamData = JSONArray.fromObject(productParamData).toString();
		} catch (JSONException e) {
			System.out.println("error  productParamData数据异常!");
		}
		// 通过productParamData 估算出价格待定
		String price = PriceEvaluate.executeHousehold(productParamData, productPrice);
		// 回收方式待定
		// 去item表中查询出所有已售出的家电 or_item_product_id
		String TAKEN = IDUtils.genToken();
		jedisClient.set(GlobalValueJedis.EVALUATE_PARAM + TAKEN, productParamData);
		jedisClient.set(GlobalValueJedis.EVALUATE_PRICE + TAKEN, price);
		System.out.println("估价存到redis：" + price);
		jedisClient.set(GlobalValueJedis.EVALUATE_PRODUCTID + TAKEN, productId);
		jedisClient.expire(GlobalValueJedis.EVALUATE_PARAM + TAKEN, 3600);
		jedisClient.expire(GlobalValueJedis.EVALUATE_PRICE + TAKEN, 3600);
		jedisClient.expire(GlobalValueJedis.EVALUATE_PRODUCTID + TAKEN, 3600);
		return LianjiuResult.ok(TAKEN);
	}

	@Override
	public LianjiuResult getBrands() {
		// 精品的分类id 品牌墙
		Long idExcellent = categoryMapper.selectByCategoryName("精品信息");
		if (null == idExcellent) {
			return LianjiuResult.build(501, "没有查到精品信息");
		}
		List<Category> productBrands = categoryMapper.selectByParentId(idExcellent);
		return LianjiuResult.ok(productBrands);
	}

	/**
	 * 获取产品的json参数模版
	 */
	@Override
	public LianjiuResult getProductByProductParamTemplate(String productId) {
		// 去数据库查询
		Product product = productMapper.selectProductParam(productId);
		return LianjiuResult.ok(product);
	}

	private List<Long> categoryList = new ArrayList<Long>();

	/**
	 * 
	 * zhaoxi 2017年11月9日 descrption:获取分类下的所有分类
	 * 
	 * @param categoryId
	 *            void
	 */
	private void getDescendants(Long categoryId) {
		List<Long> categoryIds = categoryMapper.selectCidsByPid(categoryId);
		// 边界条件
		if (null == categoryIds || categoryIds.size() == 0) {
			return;
		}
		categoryList.addAll(categoryIds);
		// 遍历
		for (Long cId : categoryIds) {
			// 递归
			getDescendants(cId);
		}
	}

	/**
	 * 电子产品信息种类切换
	 */
	@Override
	public LianjiuResult productCategorySwitch(Long idChildrenElectronics, Integer order) {
		Map<String, Object> results = new HashMap<>();
		LianjiuResult result = new LianjiuResult(results);
		// 电子产品信息导航栏切换
		if (null == idChildrenElectronics) {
			return LianjiuResult.build(501, "请传入导航id");
		}
		// 不同类别的电子产品广告图
		AdElectronic adElectronic = adElectronicMapper.getImageByOrderId(order.toString());
		results.put("adElectronic", adElectronic);

		if (order == 4 || order == 5) {
			// 电子产品品牌导航栏
			List<Category> category = categoryMapper.selectByParentId(idChildrenElectronics);
			results.put("brands", category);
			if (order == 4) {
				// 摄影首页单反相机品牌
				Long ca = category.get(0).getCategoryId();
				List<Category> electronicBrands = categoryMapper.selectByParentId(ca);
				results.put("product", electronicBrands);
			} else {
				Long ca = category.get(0).getCategoryId();
				List<Category> electronicBrands = categoryMapper.selectByParentId(ca);
				results.put("product", electronicBrands);
			}
		} else {
			// 先清空容器
			categoryList.clear();
			// 电子产品品牌导航栏
			List<Category> electronicBrands = categoryMapper.selectByParentId(idChildrenElectronics);
			results.put("brands", electronicBrands);
			// 第一个品牌下的所有产品（苹果）
			List<Product> product = new ArrayList<Product>();
			if (order == 2) {
				// 递归查询
				this.getDescendants(idChildrenElectronics);
				product = productMapper.selectFrontPageHotProduct(categoryList);
			} else if (order == 3) {
				// 递归查询
				this.getDescendants(idChildrenElectronics);
				product = productMapper.selectFrontPageHotProduct(categoryList);
			} else if (order == 1) {
				// 递归查询
				this.getDescendants(idChildrenElectronics);
				product = productMapper.selectFrontPageHotProduct(categoryList);
			}
			results.put("product", product);
		}
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 品牌的切换
	 */
	@Override
	public LianjiuResult brandSwitch(long categoryId, int pageNum, int pageSize) {
		Map<String, Object> results = new HashMap<>();
		LianjiuResult result = new LianjiuResult(results);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 查询该品牌下的所有产品
		List<ProductMaxprice> product = productMapper.selectMaxByProductCategoryId(categoryId);
		Page<ProductMaxprice> listProduct = (Page<ProductMaxprice>) product;
		result.setTotal(listProduct.getTotal());
		results.put("product", product);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	@Override
	public LianjiuResult categorySwitch(long categoryId) {
		Map<String, Object> results = new HashMap<>();
		LianjiuResult result = new LianjiuResult(results);
		// 查询该品牌下的所有产品
		List<Category> electronicBrands = categoryMapper.selectByParentId(categoryId);
		results.put("brands", electronicBrands);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	@Override
	public LianjiuResult getRecentDealInfo(String orItemsId) {
		OrdersItemInfo ordersItem = ordersItemMapper.selectByPrimaryKeys(orItemsId);
		Map<String, Object> results = new HashMap<>();
		LianjiuResult result = new LianjiuResult(results);
		results.put("OrdersItem", ordersItem);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 卖家电，加入回收车
	 */
	@Override
	public LianjiuResult introductionProduct(OrdersItem itemProduct, String userId) {
		itemProduct.setOrItemsType("家电");
		itemProduct.setOrItemsCreated(new Date());

		/**
		 * 计算总价
		 */
		itemProduct.setOrItemsNum("1");
		itemProduct.setOrItemsAccountPrice(itemProduct.getOrItemsPrice());
		Util.printModelDetails(itemProduct);
		/**
		 * 生成itemId
		 */
		itemProduct.setOrItemsId(IDUtils.genOrdersId());
		System.out.println("token:" + GlobalValueJedis.ORDERS_FACEFACE_CART + userId);
		List<OrdersItem> list = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.ORDERS_FACEFACE_CART + userId, OrdersItem.class);
		if (null == list) {
			list = new ArrayList<OrdersItem>();
		}
		list.add(itemProduct);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART + userId, JsonUtils.objectToJson(list));
		return LianjiuResult.ok(list);
	}

	/**
	 * 快递回收，加入回收车
	 */
	@Override
	public LianjiuResult addToExpressProductCar(OrdersItem itemProduct, String userId) {
		itemProduct.setOrItemsType("手机");
		itemProduct.setOrItemsCreated(new Date());
		/**
		 * 计算总价
		 */
		itemProduct.setOrItemsNum("1");
		itemProduct.setOrItemsAccountPrice(itemProduct.getOrItemsPrice());
		Util.printModelDetails(itemProduct);
		/**
		 * 生成itemId
		 */
		itemProduct.setOrItemsId(IDUtils.genOrdersId());
		System.out.println("token:" + GlobalValueJedis.ORDERS_EXPRESS_CART + userId);
		List<OrdersItem> list = JedisTool.checkListFormRedis(jedisClient, GlobalValueJedis.ORDERS_EXPRESS_CART + userId,
				OrdersItem.class);
		if (null == list) {
			list = new ArrayList<OrdersItem>();
		}
		list.add(itemProduct);
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_CART + userId, JsonUtils.objectToJson(list));
		return LianjiuResult.ok(list);
	}

	/**
	 * 查看回收车
	 */
	@Override
	public LianjiuResult selectProductFromCar(String userId) {
		List<OrdersItem> faceface = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.ORDERS_FACEFACE_CART + userId, OrdersItem.class);
		List<OrdersItem> express = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.ORDERS_EXPRESS_CART + userId, OrdersItem.class);
		if (null == express && null == faceface) {
			return LianjiuResult.build(501, "购物车上门回收为空");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != faceface) {
			/**
			 * 排序，根据生成时间
			 */
			Collections.sort(faceface, new Comparator<OrdersItem>() {
				/*
				 * int compare(OrdersItem o1, OrdersItem o2) 返回一个基本类型的整型，
				 * 返回负数表示：o1 小于o2， 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
				 */
				@Override
				public int compare(OrdersItem o1, OrdersItem o2) { // 按照加入回收车的时间排序
					if (null == o2.getOrItemsCreated()) {
						return -1;
					}
					if (null == o1.getOrItemsCreated()) {
						return 1;
					}
					if (o1.getOrItemsCreated().getTime() < o2.getOrItemsCreated().getTime()) {
						return 1;
					}
					if (o1.getOrItemsCreated() == o2.getOrItemsCreated()) {
						return 0;
					}
					return -1;
				}
			});
			map.put("facefaceList", faceface);
		} else {
			map.put("facefaceList", new ArrayList<OrdersItem>(0));
		}
		if (null != express) {
			/**
			 * 排序，根据生成时间
			 */
			Collections.sort(express, new Comparator<OrdersItem>() {
				/*
				 * int compare(OrdersItem o1, OrdersItem o2) 返回一个基本类型的整型，
				 * 返回负数表示：o1 小于o2， 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
				 */
				@Override
				public int compare(OrdersItem o1, OrdersItem o2) { // 按照加入回收车的时间排序
					if (null == o2.getOrItemsCreated()) {
						return -1;
					}
					if (null == o1.getOrItemsCreated()) {
						return 1;
					}
					if (o1.getOrItemsCreated().getTime() < o2.getOrItemsCreated().getTime()) {
						return 1;
					}
					if (o1.getOrItemsCreated() == o2.getOrItemsCreated()) {
						return 0;
					}
					return -1;
				}
			});
			map.put("expressList", express);
		} else {
			map.put("expressList", new ArrayList<OrdersItem>(0));
		}
		return LianjiuResult.ok(map);
	}

	@Override
	public LianjiuResult updateFaceFaceState(String orFacefaceId, int orFacefaceStatus) {
		if (StringUtil.hasEmpty(orFacefaceId)) {
			return LianjiuResult.build(501, "error 指定订单号不能为空！");
		} else if (ordersFacefaceMapper.selectByOrdersFacefaceCheck(orFacefaceId) == 0) {
			return LianjiuResult.build(502, "订单不存在！或者改订单没有商品记录");
		}
		OrdersFaceface ordersFaceFace = new OrdersFaceface();
		ordersFaceFace.setOrFacefaceId(orFacefaceId);
		ordersFaceFace.setOrFacefaceStatus((byte) orFacefaceStatus);
		ordersFaceFace.setOrFacefaceUpdated(new Date());
		int num = ordersFacefaceMapper.updateByStatus(ordersFaceFace);
		OrdersItem item = new OrdersItem();
		item.setOrdersId(orFacefaceId);
		if (orFacefaceStatus == (byte) 3) {
			item.setOrItemsStatus(0);
			item.setOrItemsUpdated(new Date());
			ordersItemMapper.updateByStatus(item);
		} else if (orFacefaceStatus == (byte) 2) {
			item.setOrItemsStatus(1);
			item.setOrItemsUpdated(new Date());
			ordersItemMapper.updateByStatus(item);
		}
		return LianjiuResult.ok(num);
	}

	@Override
	public LianjiuResult productSearch(String keyword) {
		if (StringUtil.hasEmpty(keyword)) {
			return LianjiuResult.build(400, "搜索关键字不能为空");
		}
		List<ProductMaxprice> product = productMapper.selectProductByKeyword(keyword);
		if (product != null && product.size() != 0) {
			for (ProductMaxprice productMaxprice : product) {
				// 改产品的最高回收价格
				String price = ordersItemMapper.selectMaxPricebyPid(productMaxprice.getProductId());
				productMaxprice.setMaxrecyclePrice(price);
			}
			return LianjiuResult.ok(product);
		} else {
			return LianjiuResult.build(501, "抱歉,没有搜索到相关内容");
		}
	}

	@Override
	public LianjiuResult hotProductRecommend() {
		List<AdHotProduct> product = adHotProductMapper.selectHotProductTen();
		if (product != null && product.size() != 0) {
			for (AdHotProduct AdHotProduct : product) {
				// 改产品的最高回收价格
				AdHotProduct.setProPrice("");
				String price = ordersItemMapper.selectMaxPricebyPid(AdHotProduct.getProductId());
				AdHotProduct.setProPrice(price);
			}
			return LianjiuResult.ok(product);
		} else {
			return LianjiuResult.build(501, "抱歉,没有搜索到相关内容");
		}
	}

	/**
	 * 短信验证码
	 */
	@Override
	public LianjiuResult sendExpressOrderSMS(String userId, String phone) {
		System.out.println("用户" + userId + "的手机号：" + phone);
		System.out.println("开始获取短信验证码");
		// 获取随机短信验证码
		String code = IDUtils.genPWDId();
		// 给用户发送随机验证码
		sendSMS.sendMessage("8", "+86", phone, code);
		// 把用户信息写入redis
		jedisClient.set(userId + "_LIAN_JIU_EXPRESS_CODE", code);
		// 设置session的过期时间
		jedisClient.expire(userId + "_LIAN_JIU_EXPRESS_CODE", 300);
		// 设置验证码redis键为全局变量
		return LianjiuResult.ok("发送成功");
	}
}
