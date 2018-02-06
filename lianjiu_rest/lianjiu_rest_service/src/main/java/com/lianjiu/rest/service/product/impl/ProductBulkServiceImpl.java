package com.lianjiu.rest.service.product.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.lianjiu.common.exception.LianjiuException;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.tools.CalculateStringNum;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersBulkItem;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.product.ProductBulkMapper;
import com.lianjiu.rest.model.BulkVo;
import com.lianjiu.rest.service.product.ProductBulkService;
import com.lianjiu.rest.tool.JedisTool;

/**
 * 大宗回收
 * 
 * @author zhaoxi
 *
 */
@Service
public class ProductBulkServiceImpl implements ProductBulkService {
	@Autowired
	private ProductBulkMapper productBulkMapper;
	@Autowired
	private JedisClient jedisClient;

	/**
	 * 搜索
	 */
	@Override
	public LianjiuResult getBulkByKeyword(String keyword, Long categoryId, String TAKEN, int pageNum, int pageSize) {
		jedisClient.expire(GlobalValueJedis.BULK_WAREHOUSE + TAKEN, 1800);// 延长仓库编号的生存时间
		List<BulkVo> productBulkLsit = null;
		LianjiuResult result = new LianjiuResult();
		result.setStatus(200);
		result.setMsg("ok");
		result.setTotal(15);
		PageHelper.startPage(pageNum, pageSize);
		if ("1111-2222-3333".equals(keyword) || Util.isEmpty(keyword)) {
			productBulkLsit = productBulkMapper.selectBulkByCid(categoryId);
			if (null == productBulkLsit) {
				productBulkLsit = new ArrayList<BulkVo>();
			}
		} else {
			// 根据关键词查询
			PageHelper.startPage(pageNum, pageSize);
			productBulkLsit = productBulkMapper.selectBulkByCidByKeyword(categoryId, keyword);
			if (null == productBulkLsit) {
				productBulkLsit = new ArrayList<BulkVo>();
			}
			int bulkListSize = productBulkLsit.size();
			result.setTotal(bulkListSize);
			// 不足部分取关键词其余数据补全
			if (bulkListSize < pageSize) {
				if (1 == pageNum && 0 == bulkListSize) {
					result.setMsg("暂无该货物进行回收，可联系链旧客服400-1818-209");
				}
				PageHelper.startPage(1, pageSize - bulkListSize);
				List<BulkVo> othersProductBulk = productBulkMapper.selectOthersByCidByKeyword(categoryId, keyword);
				if (null != othersProductBulk) {
					productBulkLsit.addAll(othersProductBulk);
				}
			}
		}
		/**
		 * 检测redis 返回之前的选择数量
		 */
		String bulkNum = null;
		String bulkNumKey = null;
		for (BulkVo bulkVo : productBulkLsit) {
			bulkNumKey = bulkVo.getBulkId().substring(0, 14) + TAKEN;
			bulkNum = jedisClient.get(bulkNumKey);
			if (Util.isEmpty(bulkNum)) {
				continue;
			} else {
				bulkVo.setBulkNum(bulkNum);
			}
		}
		result.setLianjiuData(productBulkLsit);
		return result;
	}

	/**
	 * 修改大宗物品清单
	 */
	@Override
	public LianjiuResult setBulkItemCart(String TAKEN, OrdersBulkItem item) {
		jedisClient.expire(GlobalValueJedis.BULK_WAREHOUSE + TAKEN, 1800);// 延长仓库编号的生存时间
		// 尝试从redis读取
		List<OrdersBulkItem> itemList = JedisTool.checkListFormRedis(jedisClient, GlobalValueJedis.BULK_CART + TAKEN,
				OrdersBulkItem.class);
		if (null == itemList) {
			return LianjiuResult.build(501, "清单已过期，请重新获取");
		}
		String bulkNum = item.getBulkItemNum();
		boolean notZero = true;
		if (0 >= Double.parseDouble(bulkNum)) {// 判断数量是否为零
			notZero = false;
		}
		if (!notZero && 0 == itemList.size()) {
			jedisClient.expire(GlobalValueJedis.BULK_CART + TAKEN, 1800);
			return LianjiuResult.ok(itemList); // 添加的item数量为零，清单车为空
		}
		String productId = item.getBulkItemProductId();
		String bulkPrice = item.getBulkItemPrice();
		String bulkItemAccountPrice = null;
		String bulkNumKey = productId.substring(0, 14) + TAKEN;
		/**
		 * 记住用户的数量
		 */
		jedisClient.set(bulkNumKey, bulkNum);
		jedisClient.expire(bulkNumKey, 1800);
		if (notZero) {
			/**
			 * 计算总价
			 */
			try {
				bulkItemAccountPrice = CalculateStringNum.multiply(bulkPrice, bulkNum);
			} catch (LianjiuException e) {
				return LianjiuResult.build(502, e.getMessage());
			}
		} else {
			bulkItemAccountPrice = "0.00";
		}

		/**
		 * 判断是不是已经添加过
		 */
		boolean isNewItem = true;
		for (OrdersBulkItem ordersBulkItem : itemList) {
			if (productId.equals(ordersBulkItem.getBulkItemProductId())) {
				ordersBulkItem.setBulkItemPrice(bulkPrice);
				ordersBulkItem.setBulkItemNum(bulkNum);// 重新赋值数量
				ordersBulkItem.setBulkItemAccountPrice(bulkItemAccountPrice);
				isNewItem = false;// 标识为，不是新的元素
				break;
			}
		}
		// 加入数量不为零的新元素
		if (isNewItem && notZero) {
			/**
			 * 完善item的信息
			 */
			item.setBulkItemAccountPrice(bulkItemAccountPrice);
			item.setBulkItemCreated(new Date());
			item.setBulkItemId(IDUtils.genOrdersId());// 加上编号
			itemList.add(item);// 添加新的item到清单车
		}
		jedisClient.set(GlobalValueJedis.BULK_CART + TAKEN, JsonUtils.objectToJson(itemList));
		jedisClient.expire(GlobalValueJedis.BULK_CART + TAKEN, 1800);
		return LianjiuResult.ok(itemList);
	}

	/**
	 * 获取大宗物品清单
	 */
	@Override
	public LianjiuResult getBulkItemCart(String TAKEN) {
		jedisClient.expire(GlobalValueJedis.BULK_WAREHOUSE + TAKEN, 1800);// 延长仓库编号的生存时间
		List<OrdersBulkItem> itemList = JedisTool.checkListFormRedis(jedisClient, GlobalValueJedis.BULK_CART + TAKEN,
				OrdersBulkItem.class);
		if (null == itemList) {
			return LianjiuResult.build(501, "清单已过期，请重新进入");
		}
		if (0 == itemList.size()) {
			return LianjiuResult.build(502, "清单为空，请选择一些物品");
		}
		Iterator<OrdersBulkItem> it = itemList.iterator();
		while (it.hasNext()) {
			OrdersBulkItem ordersBulkItem = (OrdersBulkItem) it.next();
			if (0 >= Double.parseDouble(ordersBulkItem.getBulkItemNum())) {
				it.remove();// 移除数量为0的item
				continue;
			}
		}
		if (0 == itemList.size()) {
			jedisClient.set(GlobalValueJedis.BULK_CART + TAKEN, JsonUtils.objectToJson(itemList));
			jedisClient.expire(GlobalValueJedis.BULK_CART + TAKEN, 1800);
			return LianjiuResult.build(502, "清单为空，请选择一些物品");
		}
		/**
		 * 排序，根据生成时间
		 */
		Collections.sort(itemList, new Comparator<OrdersBulkItem>() {
			/*
			 * int compare(OrdersItem o1, OrdersItem o2) 返回一个基本类型的整型， 返回负数表示：o1
			 * 小于o2， 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
			 */
			@Override
			public int compare(OrdersBulkItem o1, OrdersBulkItem o2) { // 按照加入回收车的时间排序
				if (null == o2.getBulkItemCreated()) {
					return -1;
				}
				if (null == o1.getBulkItemCreated()) {
					return 1;
				}
				if (o1.getBulkItemCreated().getTime() < o2.getBulkItemCreated().getTime()) {
					return 1;
				}
				if (o1.getBulkItemCreated() == o2.getBulkItemCreated()) {
					return 0;
				}
				return -1;
			}
		});
		/**
		 * 暂存
		 */
		jedisClient.set(GlobalValueJedis.BULK_CART + TAKEN, JsonUtils.objectToJson(itemList));
		jedisClient.expire(GlobalValueJedis.BULK_CART + TAKEN, 1800);
		LianjiuResult result = new LianjiuResult(itemList);
		result.setStatus(200);
		result.setMsg("ok");
		result.setTotal(itemList.size());
		return result;
	}

}
