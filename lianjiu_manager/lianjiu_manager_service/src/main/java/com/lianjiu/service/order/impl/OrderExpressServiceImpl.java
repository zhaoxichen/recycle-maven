package com.lianjiu.service.order.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.pojo.MachineCheck;
import com.lianjiu.common.pojo.ProductPriceGroup;
import com.lianjiu.common.tools.CalculateStringNum;
import com.lianjiu.common.tools.PriceEvaluate;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.HttpClientUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersExpress;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.model.Product;
import com.lianjiu.model.User;
import com.lianjiu.model.UserDetails;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.model.vo.OrdersItemExpress;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.dao.RecommendDao;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.orders.OrdersExpressMapper;
import com.lianjiu.rest.mapper.orders.OrdersItemMapper;
import com.lianjiu.rest.mapper.product.ProductMapper;
import com.lianjiu.rest.mapper.product.ProductPriceGroupMapper;
import com.lianjiu.rest.mapper.user.UserDetailsMapper;
import com.lianjiu.rest.mapper.user.UserMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;
import com.lianjiu.service.order.OrderExpressService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class OrderExpressServiceImpl implements OrderExpressService {

	@Autowired
	private OrdersExpressMapper ordersExpressMapper;
	@Autowired
	private OrdersItemMapper ordersItemMapper;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private ProductPriceGroupMapper productPriceGroupMapper;
	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;
	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserDetailsMapper userDetailsMapper;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private RecommendDao recommendDao;

	@Override
	public LianjiuResult getAllOrderExpress(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersExpress> express = ordersExpressMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersExpress> listExpress = (Page<OrdersExpress>) express;
		System.out.println("总页数：" + listExpress.getPages());
		LianjiuResult result = new LianjiuResult(express);
		result.setTotal(listExpress.getTotal());
		return result;
	}

	/**
	 * 
	 * zhaoxi 2017年11月13日 descrption:订单过滤器
	 * 
	 * @param orders
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@Override
	public LianjiuResult ordersFilter(OrdersExpress orders, int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		SearchObjecVo vo = new SearchObjecVo(orders);
		// 执行查询
		List<OrdersExpress> ordersList = ordersExpressMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersExpress> list = (Page<OrdersExpress>) ordersList;
		System.out.println("总页数：" + list.getPages());
		LianjiuResult result = new LianjiuResult(ordersList);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}

	@Override
	public LianjiuResult getAllOrderExpress() {
		// 执行查询
		List<OrdersExpress> express = ordersExpressMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(express);
		return result;
	}

	@Override
	public LianjiuResult getAllOrderExpressByStatus(Integer state) {
		OrdersExpress express = new OrdersExpress();
		express.setOrExpressStatus((byte) state.intValue());
		SearchObjecVo vo = new SearchObjecVo(express);
		// 执行查询
		List<OrdersExpress> listExpress = ordersExpressMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(listExpress);
		return result;
	}

	@Override
	public LianjiuResult getAllOrderExpressByStatus(Integer state, int pageNum, int pageSize) {
		OrdersExpress express = new OrdersExpress();
		express.setOrExpressStatus((byte) state.intValue());
		SearchObjecVo vo = new SearchObjecVo(express);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersExpress> expresss = ordersExpressMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersExpress> listExpress = (Page<OrdersExpress>) expresss;
		System.out.println("总页数：" + listExpress.getPages());
		LianjiuResult result = new LianjiuResult(expresss);
		result.setTotal(listExpress.getTotal());
		return result;
	}

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
		int rowsAffected = ordersExpressMapper.updateByPrimaryKeySelective(ordersExpress);
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 获取当前订单详情
	 */
	@Override
	public LianjiuResult getOrderById(String orderId) {
		OrdersExpress express = ordersExpressMapper.selectByPrimaryKey(orderId);
		return LianjiuResult.ok(express);
	}

	@Override
	public LianjiuResult getgetOrderByCid(Long cid) {
		OrdersExpress excellent = new OrdersExpress();
		excellent.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(excellent);
		// 执行查询
		List<OrdersExpress> listRemarks = ordersExpressMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(listRemarks);
		return result;
	}

	@Override
	public LianjiuResult getgetOrderByCid(Long cid, int pageNum, int pageSize) {
		OrdersExpress excellent = new OrdersExpress();
		excellent.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(excellent);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersExpress> excellents = ordersExpressMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersExpress> listExcellents = (Page<OrdersExpress>) excellents;
		System.out.println("总页数：" + listExcellents.getPages());
		LianjiuResult result = new LianjiuResult(excellents);
		result.setTotal(listExcellents.getTotal());
		return result;
	}

	/**
	 * 获取一个商品记录,进行验机
	 */
	@Override
	public LianjiuResult getOrderItemsById(String orExpItemId) {
		Map<String, Object> map = new HashMap<>();
		if (Util.isEmpty(orExpItemId)) {
			return LianjiuResult.build(501, "请指定要查询的商品记录id");
		}
		OrdersItemExpress item = null;
		String itemJson = jedisClient.get(GlobalValueJedis.ORDERS_EXPRESS_ITEM_CHECK + orExpItemId);
		if (Util.isEmpty(itemJson)) {
			item = ordersItemMapper.selectOrdersExpressByItemId(orExpItemId);
			// 存入缓存
			jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_ITEM_CHECK + orExpItemId, JsonUtils.objectToJson(item));
			jedisClient.expire(GlobalValueJedis.ORDERS_EXPRESS_ITEM_CHECK + orExpItemId, 1800);
		} else {
			// 从缓存取
			item = JsonUtils.jsonToPojo(itemJson, OrdersItemExpress.class);
		}
		if (null != item) {
			String param = item.getOrItemsParam();
			String paramModify = item.getOrItemsParamModify();
			if (Util.isEmpty(paramModify)) {
				paramModify = param;
				item.setOrItemsParamModify(paramModify);
			}
			if (Util.notEmpty(param)) {
				System.out.println(param);
				item.setMachineCheckList(checkAdditionalFromParam(param, paramModify));
			}
			map.put("item", item);
		}
		return LianjiuResult.ok(map);
	}

	/**
	 * 
	 * zhaoxi 2017年11月10日 descrption:验机详情
	 * 
	 * @param orItemsParam
	 * @return String
	 */
	private List<MachineCheck> checkAdditionalFromParam(String orItemsParam, String orItemsParamModify) {
		String checkKey = null;
		String checkValue = null;
		StringBuffer checkValueTemp = new StringBuffer();
		List<MachineCheck> additional = new ArrayList<MachineCheck>();
		try {
			// 筛选出具体的参数
			JSONArray paramDataArray = JSONArray.fromObject(orItemsParam);// json串转为json数组
			/**
			 * orItemsParam
			 */
			for (int i = 0; i < paramDataArray.size(); i++) {
				JSONArray children1 = JSONArray.fromObject(paramDataArray.get(i));// json串转为json数组
				for (Object object1 : children1) {
					JSONObject majorDataJsonObject1 = JSONObject.fromObject(object1);// json串转为json对象
					JSONArray children2 = (JSONArray) majorDataJsonObject1.get("children"); // children为数组
					for (int j = 0; j < children2.size(); j++) {
						JSONObject majorDataJsonObject2 = JSONObject.fromObject(children2.get(j));// json串转为json对象
						checkKey = (String) majorDataJsonObject2.get("major");
						if ("功能性问题".equals(checkKey)) {
							JSONArray children3 = (JSONArray) majorDataJsonObject2.get("children"); // children为数组
							if (0 == children3.size()) {
								additional.add(new MachineCheck(checkKey, "无", checkValue));
								break;
							}
							for (Object object2 : children3) {
								JSONObject majorDataJsonObject3 = JSONObject.fromObject(object2);
								checkValue = (String) majorDataJsonObject3.get("major");
								checkValueTemp.append(checkValue + ",");
							}
							System.err.println(
									"checkValueTemp:" + checkValueTemp.substring(0, checkValueTemp.length() - 1));
							additional.add(new MachineCheck(checkKey,
									checkValueTemp.substring(0, checkValueTemp.length() - 1), checkValue));
							continue;
						}
						JSONArray children3 = (JSONArray) majorDataJsonObject2.get("children"); // children为数组
						for (Object object2 : children3) {
							JSONObject majorDataJsonObject3 = JSONObject.fromObject(object2);
							checkValue = (String) majorDataJsonObject3.get("major");
						}
						if (Util.notEmpty(checkKey)) {
							additional.add(new MachineCheck(checkKey, checkValue, checkValue));
						}
					}
				}
			}
			/**
			 * orItemsParamModify
			 */
			List<String> checkValues = new ArrayList<String>();
			checkValueTemp = new StringBuffer();
			paramDataArray = JSONArray.fromObject(orItemsParamModify);// json串转为json数组
			for (int i = 0; i < paramDataArray.size(); i++) {
				JSONArray children1 = JSONArray.fromObject(paramDataArray.get(i));// json串转为json数组
				for (Object object1 : children1) {
					JSONObject majorDataJsonObject1 = JSONObject.fromObject(object1);// json串转为json对象
					JSONArray children2 = (JSONArray) majorDataJsonObject1.get("children"); // children为数组
					for (int j = 0; j < children2.size(); j++) {
						JSONObject majorDataJsonObject2 = JSONObject.fromObject(children2.get(j));// json串转为json对象
						checkKey = (String) majorDataJsonObject2.get("major");
						if ("功能性问题".equals(checkKey)) {
							JSONArray children3 = (JSONArray) majorDataJsonObject2.get("children"); // children为数组
							if (0 == children3.size()) {
								checkValues.add("无");
								break;
							}
							for (Object object2 : children3) {
								JSONObject majorDataJsonObject3 = JSONObject.fromObject(object2);
								checkValue = (String) majorDataJsonObject3.get("major");
								checkValueTemp.append(checkValue + ",");
							}
							System.out.println(
									"checkValueTemp:" + checkValueTemp.substring(0, checkValueTemp.length() - 1));
							checkValues.add(checkValueTemp.substring(0, checkValueTemp.length() - 1));
							continue;
						}
						JSONArray children3 = (JSONArray) majorDataJsonObject2.get("children"); // children为数组
						for (Object object2 : children3) {
							JSONObject majorDataJsonObject3 = JSONObject.fromObject(object2);
							checkValue = (String) majorDataJsonObject3.get("major");
						}
						if (Util.notEmpty(checkKey)) {
							checkValues.add(checkValue);
						}
					}
					// checkValues.add(checkValueTemp.substring(0,
					// checkValueTemp.length() - 1));
				}
			}
			/**
			 * 追加到additional
			 */
			for (int k = 0; k < checkValues.size(); k++) {
				additional.get(k).setParamValueModify(checkValues.get(k));
			}
			/**
			 * 测试
			 */
			if (checkValues.size() != additional.size()) {
				additional.add(new MachineCheck("error", "modify", "size错误"));
			}
		} catch (NullPointerException e) {
			additional.add(new MachineCheck("error", "json数据错误", "NullPointerException"));
			return additional;
		} catch (ClassCastException e) {
			additional.add(new MachineCheck("error", "json数据错误", "ClassCastException"));
			return additional;
		} catch (net.sf.json.JSONException e) {
			additional.add(new MachineCheck("error", "json数据错误", "JSONException"));
			return additional;
		} catch (java.lang.IndexOutOfBoundsException e) {
			additional.add(new MachineCheck("error", "json数据错误", "IndexOutOfBoundsException"));
			return additional;
		}
		return additional;
	}

	/**
	 * 
	 * zhaoxi 2017年11月11日 descrption:修改模版参数
	 * 
	 * @param orItemsParam
	 * @param machineCheckModify
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	private String saveItemsParamModify(String orItemsParam, MachineCheck machineCheckModify) {
		JSONArray paramDataArray = null;
		JSONArray paramDataArrayModify = new JSONArray();
		try {
			// 筛选出具体的参数
			paramDataArray = JSONArray.fromObject(orItemsParam);// json串转为json数组
			boolean objectModify = false;
			JSONObject paramDataArray3 = null;
			JSONObject paramDataArray5 = null;
			JSONArray paramDataArray2 = null;
			JSONArray paramDataArray4 = null;
			/**
			 * 基本信息，使用情况，"功能性问题（可多选）"
			 */
			for (Object objectBase : paramDataArray) {
				paramDataArray2 = JSONArray.fromObject(objectBase);// json串转为json数组
				for (Object object2 : paramDataArray2) {
					paramDataArray3 = JSONObject.fromObject(object2);// json串转为json对象
					JSONObject paramDataArray3Modify = new JSONObject();
					Set keys = paramDataArray3.keySet();
					for (Object keysObject1 : keys) {
						if ("children".equals(keysObject1)) {
							paramDataArray4 = JSONArray.fromObject(paramDataArray3.get(keysObject1));// json串转为json数组
							JSONArray paramDataArray4Modify = new JSONArray();
							for (Object object3 : paramDataArray4) {
								paramDataArray5 = JSONObject.fromObject(object3);// json串转为json对象
								JSONObject paramDataArray5Modify = new JSONObject();
								JSONArray paramDataArray6Modify = new JSONArray();
								keys = paramDataArray5.keySet();
								for (Object keysObject2 : keys) {
									/**
									 * 字段匹配区
									 */
									String paramKey = machineCheckModify.getParamKey();
									if (paramKey.equals(paramDataArray5.get(keysObject2))) {
										System.out.println("匹配上了");
										objectModify = true;
									}
									paramDataArray6Modify = (JSONArray) paramDataArray5.get("children");
									paramDataArray5Modify.put(keysObject2, paramDataArray5.get(keysObject2));
								}
								// 判断是否要修改children数据
								if (objectModify) {
									objectModify = false;
									System.out.println(
											"getParamValueModify----" + machineCheckModify.getParamValueModify());
									paramDataArray6Modify = JSONArray
											.fromObject(machineCheckModify.getParamValueModify());
									System.out.println(paramDataArray6Modify);
									paramDataArray5Modify.put("children", paramDataArray6Modify);// put到对象
								} else {
									paramDataArray5Modify.put("children", paramDataArray6Modify);// put到对象
								}
								paramDataArray4Modify.add(paramDataArray5Modify);// add到数组
							}
							paramDataArray3Modify.put(keysObject1, paramDataArray4Modify);// put到对象(条件)
							continue;
						}
						paramDataArray3Modify.put(keysObject1, paramDataArray3.get(keysObject1));// put到对象(条件)
					}
					paramDataArrayModify.add(paramDataArray3Modify);// add到数组
				}
			}
		} catch (NullPointerException e) {
			return null;
		} catch (ClassCastException e) {
			return null;
		} catch (net.sf.json.JSONException e) {
			return null;
		} catch (java.lang.IndexOutOfBoundsException e) {
			return null;
		} finally {
			if (null != paramDataArrayModify) {
				System.out.println(orItemsParam);
				System.out.println(paramDataArrayModify);
			}
		}
		return paramDataArrayModify.toString();
	}

	public static void main(String[] args) {
		// 创建一个jedis的对象。
		// Jedis jedis = new Jedis("127.0.0.1", 6379);
		// 调用jedis对象的方法，方法名称和redis的命令一致。
		// String orItemsParam = jedis.get("testZhaoxi");
		// String orItemsParamModify = jedis.get("zhaoxiTestModify");
		// String childModify = jedis.get("childModify");
		String orItemsParam = "testZhaoxi";
		String orItemsParamModify = "zhaoxiTestModify";
		String childModify = "childModify";
		// MachineCheck machineCheckaa = new MachineCheck("存储容", "A1661",
		// "64G");
		List<MachineCheck> machineChecks = new OrderExpressServiceImpl().checkAdditionalFromParam(orItemsParam,
				orItemsParamModify);
		for (MachineCheck machineCheck : machineChecks) {
			Util.printModelDetails(machineCheck);
		}
		MachineCheck machineCheckaa = new MachineCheck("功能性问题", "A1661", childModify);
		new OrderExpressServiceImpl().saveItemsParamModify(orItemsParamModify, machineCheckaa);
	}

	/**
	 * 分页获取快递订单详情
	 */
	@Override
	public LianjiuResult getItemByOrdersId(String ordersId) {
		// 执行查询
		List<OrdersItemExpress> ordersItems = null;
		// 从缓存中取出该订单修改过的item对象
		Set<String> itemRedisKey = jedisClient.keys(GlobalValueJedis.ORDERS_EXPRESS_ITEM_CHECK + ordersId + "*");
		if (0 < itemRedisKey.size()) {
			Iterator<String> it = itemRedisKey.iterator();
			OrdersItemExpress itemExpress = null;
			ordersItems = new ArrayList<OrdersItemExpress>();
			while (it.hasNext()) {
				String itemKey = (String) it.next();
				System.out.println(itemKey);
				// 从缓存取
				String itemJson = jedisClient.get(itemKey);
				if (Util.notEmpty(itemJson)) {
					// 把对象转换出来
					itemExpress = JsonUtils.jsonToPojo(itemJson, OrdersItemExpress.class);
					if (null == itemExpress) {
						continue;
					}
					ordersItems.add(itemExpress);
				}

			}
		}
		/**
		 * 去数据库查询
		 */
		if (null == ordersItems) {
			ordersItems = ordersItemMapper.selectByExpressOrdersId(ordersId);
			if (null == ordersItems) {
				return LianjiuResult.build(501, "订单无数据");
			}
			for (OrdersItemExpress item : ordersItems) {
				saveItemCheck(item);
			}
		}
		LianjiuResult result = new LianjiuResult(ordersItems);
		result.setTotal(ordersItems.size());
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 获取所有的商品记录
	 */
	@Override
	public LianjiuResult getOrderDetailsById(String orderId) {
		if (Util.isEmpty(orderId)) {
			return LianjiuResult.build(501, "请指定要查询的订单id");
		}
		OrdersItem ordersItem = new OrdersItem();
		ordersItem.setOrdersId(orderId);
		SearchObjecVo vo = new SearchObjecVo(ordersItem);
		// 商品记录列表
		List<OrdersItem> itemList = ordersItemMapper.selectBySearchObjecVo(vo);
		return LianjiuResult.ok(itemList);
	}

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
		Product p = productMapper.selectByPrimaryKey(productId);
		String majorData = null;
		if (p != null) {
			String productParamData = p.getProductParamData();
			if (Util.isEmpty(productParamData)) {
				return LianjiuResult.build(400, "产品参数错误");
			}
			// 筛选出major对应的children
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
							System.out.println("匹配上了");
							System.out.println(children1);
							return LianjiuResult.ok(children1);
						}
						JSONArray children2 = (JSONArray) majorDataJsonObject1.get("children"); // children为数组
						for (Object children : children2) {
							JSONObject majorDataJsonObject = JSONObject.fromObject(children);// json串转为json对象
							majorData = (String) majorDataJsonObject.get("major");
							if (Util.isEmpty(majorData)) {
								continue;
							}
							if (majorName.trim().equals(majorData.trim())) {
								System.out.println("匹配上了");
								System.out.println(children);
								return LianjiuResult.ok(children);
							}
						}
					}
				}
			}
		}
		return LianjiuResult.build(503, "查不到指定的参数组合,请检查查询条件");
	}

	/**
	 * 验机，更新产品参数
	 */
	@Override
	public LianjiuResult modifyParam(String orItemsId, String orItemsParam, MachineCheck machineCheckModify) {
		if (Util.isEmpty(orItemsId)) {
			return LianjiuResult.build(501, "请指定要更新的快递item是id");
		}
		if (Util.isEmpty(orItemsParam)) {
			return LianjiuResult.build(501, "请传入要更新的修改的产品参数模版");
		}
		String orItemsParamModify = this.saveItemsParamModify(orItemsParam, machineCheckModify);
		OrdersItemExpress item = null;
		String itemJson = jedisClient.get(GlobalValueJedis.ORDERS_EXPRESS_ITEM_CHECK + orItemsId);
		if (Util.isEmpty(itemJson)) {
			// 重新查询数据库
			item = ordersItemMapper.selectOrdersExpressByItemId(orItemsId);
		} else {
			// 从缓存取
			item = JsonUtils.jsonToPojo(itemJson, OrdersItemExpress.class);
		}
		if (null != item) {
			item.setOrItemsParamModify(orItemsParamModify);
			/**
			 * 估价
			 */
			String productId = item.getOrItemsProductId();
			/**
			 * 特殊价格组合
			 */
			String priceGroupsJson = jedisClient.get(GlobalValueJedis.PRODUCT_PRICE_GROUPS + productId);
			List<ProductPriceGroup> priceGroups;
			if (Util.isEmpty(priceGroupsJson)) {
				priceGroups = productPriceGroupMapper.selectByPid(productId);// 通过产品id查出价格组合
				if (0 < priceGroups.size()) {
					jedisClient.set(GlobalValueJedis.PRODUCT_PRICE_GROUPS + productId,
							JsonUtils.objectToJson(priceGroups));
					jedisClient.expire(GlobalValueJedis.PRODUCT_PRICE_GROUPS + productId, 180); // 三分钟
				}
			} else {
				priceGroups = JsonUtils.jsonToList(priceGroupsJson, ProductPriceGroup.class);
				jedisClient.expire(GlobalValueJedis.PRODUCT_PRICE_GROUPS + productId, 180); // 再延长三分钟
			}
			/**
			 * 原价
			 */
			String price = jedisClient.get(GlobalValueJedis.PRODUCT_PRICE + productId);
			if (Util.isEmpty(price)) {
				price = productMapper.checkPriceById(productId);// 查询产品原价
				if (Util.notEmpty(price)) {
					jedisClient.set(GlobalValueJedis.PRODUCT_PRICE + productId, price);
					jedisClient.expire(GlobalValueJedis.PRODUCT_PRICE + productId, 180); // 三分钟
				}
			} else {
				jedisClient.expire(GlobalValueJedis.PRODUCT_PRICE + productId, 180); // 再延长三分钟
			}
			String priceItemModify = PriceEvaluate.executePhone(priceGroups, orItemsParamModify, price);
			// 修改item的验机价格
			item.setOrItemsAccountPrice(priceItemModify);
			item.setOrItemsUpdated(new Date());
			/**
			 * 标识该订单被修改过
			 */
			String ordersId = item.getOrdersId();
			if (Util.notEmpty(ordersId)) {
				jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_IS_CHECK + ordersId, ordersId);
			}
			saveItemCheck(item);
		}
		return LianjiuResult.ok(orItemsId);
	}

	private void saveItemCheck(OrdersItemExpress item) {
		String orItemsId = item.getOrItemsId();
		// 存入缓存，验机结束才一起存入数据库
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_ITEM_CHECK + orItemsId, JsonUtils.objectToJson(item));
		jedisClient.expire(GlobalValueJedis.ORDERS_EXPRESS_ITEM_CHECK + orItemsId, 1800);
		String ordersId = item.getOrdersId();
		if (Util.isEmpty(ordersId)) {
			System.out.println("------------------------------------------****************-------------");
		}
		jedisClient.set(GlobalValueJedis.ORDERS_EXPRESS_ITEM_CHECK + ordersId + orItemsId,
				JsonUtils.objectToJson(item));
		jedisClient.expire(GlobalValueJedis.ORDERS_EXPRESS_ITEM_CHECK + ordersId + orItemsId, 1800);
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
		int rowsAffected = ordersExpressMapper.updateByPrimaryKeySelective(orders);
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 验机结束
	 */
	@Override
	public LianjiuResult checkFinish(String ordersId, Integer isModify) {
		String orExpressRecyclePrice = "0";
		String priceItemModify;
		// 从缓存中取出该订单修改过的item对象
		Set<String> itemRedisKey = jedisClient.keys(GlobalValueJedis.ORDERS_EXPRESS_ITEM_CHECK + ordersId + "*");
		if (0 < itemRedisKey.size()) {
			Iterator<String> it = itemRedisKey.iterator();
			OrdersItemExpress item = null;
			while (it.hasNext()) {
				String itemKey = (String) it.next();
				System.out.println(itemKey);
				// 从缓存取
				String itemJson = jedisClient.get(itemKey);
				if (Util.notEmpty(itemJson)) {
					// 把对象转换出来
					item = JsonUtils.jsonToPojo(itemJson, OrdersItemExpress.class);
					if (null == item) {
						continue;
					}
					item.setOrItemsUpdated(new Date());
					priceItemModify = item.getOrItemsAccountPrice();
					// 计算订单的验机总价
					if ("暂无估价".equals(priceItemModify)) {
						return LianjiuResult.build(501, "暂无估价item：" + item.getOrItemsId());
					}
					orExpressRecyclePrice = CalculateStringNum.add(orExpressRecyclePrice, priceItemModify);
					ordersItemMapper.updateByPrimaryKeySelective(item);
				}
				// 删除这组键值对
				jedisClient.del(itemKey);
				jedisClient.del(GlobalValueJedis.ORDERS_EXPRESS_ITEM_CHECK + item.getOrItemsId());
			}
		}
		/**
		 * 判断item是否被修改过了
		 */
		if (Util.notEmpty(jedisClient.get(GlobalValueJedis.ORDERS_EXPRESS_IS_CHECK + ordersId))) {
			isModify = 1;
			jedisClient.del(GlobalValueJedis.ORDERS_EXPRESS_IS_CHECK + ordersId);
		} else {
			isModify = 0;
		}
		// 修改Orders的验机总价格，状态
		OrdersExpress orders = new OrdersExpress();
		orders.setOrExpressId(ordersId);
		System.out.println("-----------------------------------------isModify:" + isModify);
		if (1 == isModify) {
			orders.setOrExpressRecyclePrice(orExpressRecyclePrice);// 验机总价格
			orders.setOrExpressStatus(GlobalValueUtil.ORDER_EXPRESS_UNSURE);// 等待用户确认价格
			// 启动超时监控,定时器在定时器系统中开启
			String rep = HttpClientUtil
					.doPost(GlobalValueUtil.TIMER_BASE_URL + "orderExpress/orderConfirm/" + ordersId);
			if (Util.notEmpty(rep)) {
				LianjiuResult repObj = JsonUtils.jsonToPojo(rep, LianjiuResult.class);
				if (null != repObj) {
					Util.printModelDetails(repObj);
				}
			}
		} else {
			orders.setOrExpressRecyclePrice(orExpressRecyclePrice);// 验机总价格
			orders.setOrExpressStatus(GlobalValueUtil.ORDER_EXPRESS_BALANCE_NO);// 价格与用户的一致
		}
		orders.setOrExpressUpdated(new Date());
		ordersExpressMapper.updateByPrimaryKeySelective(orders);
		return LianjiuResult.ok(ordersId);
	}

	/**
	 * 修改运单编号
	 */
	@Override
	public LianjiuResult modifyOrderExpressNum(OrdersExpress ordersExpress) {
		if (null == ordersExpress) {
			return LianjiuResult.build(501, "请指定要更新的快递订单id");
		}
		int rowsAffected = ordersExpressMapper.updateByPrimaryKeySelective(ordersExpress);
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 付款给用户
	 */
	@Override
	public LianjiuResult payForOrders(String ordersId) {
		/**
		 * 给用户打钱
		 */
		OrdersExpress ordersTemp = ordersExpressMapper.selectByPrimaryKey(ordersId);
		String userId = ordersTemp.getOrExpressUserId();
		String recyclePrice = ordersTemp.getOrExpressRecyclePrice();
		String productName = ordersTemp.getOrItemsNamePreview();
		System.out.println("用户----" + userId);
		WalletLianjiu walletLianjiu = walletLianjiuMapper.selectWalletByUserId(userId);
		// 判断钱包是否存在,不存在直接添加
		if (walletLianjiu == null) {
			User user = userMapper.selectByPrimaryKey(userId);
			walletLianjiu = new WalletLianjiu();
			walletLianjiu.setWalletId(IDUtils.genGUIDs());
			walletLianjiu.setWalletMoney(recyclePrice);
			walletLianjiu.setWalletTotalcount(recyclePrice);
			walletLianjiu.setWalletCreated(new Date());
			if (null != user) {
				walletLianjiu.setPayment(user.getUserPassword());
			}
			walletLianjiuMapper.insert(walletLianjiu);
		}
		walletLianjiu.setWalletMoney(recyclePrice);
		int i = walletLianjiuMapper.updateWalletMoney(walletLianjiu);
		if (i == 0) {
			return LianjiuResult.build(502, "打款失败");
		}
		// 添加一条记录到流水单
		UserWalletRecord walletRecord = new UserWalletRecord();
		walletRecord.setRecordId(IDUtils.genGUIDs());
		walletRecord.setRecordPrice(recyclePrice);
		walletRecord.setRelativeId(ordersId);
		walletRecord.setRecordCreated(new Date());
		walletRecord.setRecordUpdated(new Date());
		walletRecord.setUserId(userId);
		walletRecord.setIsIncome((byte) 6);// 快递回收赚取
		walletRecord.setRecordName(productName);
		userWalletRecordMapper.insertSelective(walletRecord);
		/**
		 * 结算,修改订单的状态
		 */
		int rowsAffected = ordersExpressMapper.updateOrdersToFinish(ordersId,
				GlobalValueUtil.ORDER_EXPRESS_BALANCE_YES);
		System.out.println(rowsAffected);
		/**
		 * 获取推荐码
		 */
		UserDetails userDetails = userDetailsMapper.getIsFirstOrder(userId + "1");
		if (null == userDetails) {
			return LianjiuResult.ok(ordersId);
		}
		/**
		 * 判断是不是用户的首单
		 */
		Integer firstOrders = userDetails.getIsFirstOrder();
		if (firstOrders != null && 1 == firstOrders) {
			return LianjiuResult.ok(ordersId);
		}
		/**
		 * 判断是不是有推荐码且不为‘aaaaaa’，
		 */
		String udetailsReferrer = userDetails.getUdetailsReferrer();
		if (Util.isEmpty(udetailsReferrer) || "aaaaaa".equals(udetailsReferrer)
				|| udetailsReferrer.matches("[7-9][0-9]{5}")) {
			return LianjiuResult.ok(ordersId);
		}
		recommendDao.payForOrdersExpressFirst(udetailsReferrer);
		// 更新首单的字段
		userDetailsMapper.modifyIsFirstOrder(1, userId + "1");
		return LianjiuResult.ok(ordersId);
	}

	@Override
	public LianjiuResult vagueQuery(OrdersExpress ordersExpress, int pageNum, int pageSize, String cratedStart,
			String cratedOver) {
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersExpress> ordersList = ordersExpressMapper.vagueQuery(ordersExpress, cratedStart, cratedOver);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersExpress> list = (Page<OrdersExpress>) ordersList;
		System.out.println("总页数：" + list.getPages());
		LianjiuResult result = new LianjiuResult(ordersList);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}
}
