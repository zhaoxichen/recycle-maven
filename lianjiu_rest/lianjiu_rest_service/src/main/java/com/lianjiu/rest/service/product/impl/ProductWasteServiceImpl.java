package com.lianjiu.rest.service.product.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.lianjiu.common.exception.LianjiuException;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.tools.CalculateStringNum;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.rest.mapper.product.WasteMapper;
import com.lianjiu.rest.model.WastePriceVo;
import com.lianjiu.rest.service.product.ProductWasteService;
import com.lianjiu.rest.tool.JedisTool;

/**
 * 废品信息服务层
 * 
 * @author zhaoxi
 *
 */
@Service
public class ProductWasteServiceImpl implements ProductWasteService {
	@Autowired
	private WasteMapper wasteMapper;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public LianjiuResult getWasteByCid(Long cid, int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<WastePriceVo> wastes = wasteMapper.selectByCategory(cid);
		String wasteName = categoryMapper.selectCategoryByName(cid);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("wasteList", wastes);
		map.put("wasteName", wasteName);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		LianjiuResult result = new LianjiuResult(map);
		result.setTotal(wastes.size());
		return result;
	}

	@Override
	public LianjiuResult getWasteDetails(String id) {
		WastePriceVo wastePrice = wasteMapper.selectByPriceId(id);
		return LianjiuResult.ok(wastePrice);
	}

	/**
	 * 废品的加入，回收车
	 */
	@Override
	public LianjiuResult introductionWaste(String userId, OrdersItem wasteItem) {
		String itemNum = wasteItem.getOrItemsNum();
		if (null == itemNum || itemNum.contains("-")) {
			return LianjiuResult.build(502, "数量错误");
		}
		if (itemNum.length() > 8) {
			return LianjiuResult.build(502, "数量长度错误");
		}
		wasteItem.setOrItemsType("废品");
		wasteItem.setOrItemsCreated(new Date());
		String itemsPrice = wasteItem.getOrItemsPrice();
		if (itemsPrice.contains("-")) {
			return LianjiuResult.build(502, "价格为负数了" + itemsPrice);
		}
		if (itemsPrice.contains(".")) {
			BigDecimal bigDecimal = new BigDecimal(itemsPrice);
			itemsPrice = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		}
		wasteItem.setOrItemsPrice(itemsPrice);
		/**
		 * 计算总价
		 */
		String orItemsAccountPrice = null;
		try {
			orItemsAccountPrice = CalculateStringNum.multiply(itemsPrice, itemNum);
		} catch (LianjiuException e) {
			return LianjiuResult.build(502, e.getMessage());
		}
		wasteItem.setOrItemsAccountPrice(orItemsAccountPrice);
		/**
		 * 生成itemId
		 */
		wasteItem.setOrItemsId(IDUtils.genOrdersId());
		List<OrdersItem> list = JedisTool.checkListFormRedis(jedisClient, GlobalValueJedis.ORDERS_FACEFACE_CART+userId, OrdersItem.class);
		if (null == list) {
			list = new ArrayList<OrdersItem>();
		}
		list.add(wasteItem);
		jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_CART + userId, JsonUtils.objectToJson(list)); 
		return LianjiuResult.ok(list);
	}

}
