package com.lianjiu.rest.service.product.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.ProductRepair;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.rest.mapper.product.ProductRepairMapper;
import com.lianjiu.rest.model.ProductInfo;
import com.lianjiu.rest.model.ProductParamInfo;
import com.lianjiu.rest.service.product.ProductRepairService;

/**
 * 维修方案服务层
 * 
 * @author zhaoxi
 *
 */
@Service
public class ProductRepairServiceImpl implements ProductRepairService {
	@Autowired
	private ProductRepairMapper productRepairMapper;
	
	@Autowired
	JedisClient jedisClient;

	@Autowired
	CategoryMapper categoryMapper;

	@Override
	public LianjiuResult getRepairByCid(Long cid) {
		// 执行查询
		Map<String, Object> map = new HashMap<String,Object>();
		String name = categoryMapper.selectCategoryByName(cid);
		List<ProductInfo> repairs = productRepairMapper.selectByCid(cid);
		map.put("repairs", repairs);
		map.put("name", name);
		LianjiuResult result = new LianjiuResult(map);
		return result;
	}

	@Override
	public LianjiuResult getRepairById(String repairId) {
		if (Util.isEmpty(repairId)) {
			return LianjiuResult.build(501, "请指定维修品id");
		}
		// 去数据库查询
		ProductRepair repair = productRepairMapper.selectByPrimaryKey(repairId);
		return LianjiuResult.ok(repair);
	}

	@Override
	public LianjiuResult getRepairByRepairParamTemplate(String repairId) {
		if (Util.isEmpty(repairId)) {
			return LianjiuResult.build(501, "请指定维修品id");
		}
		// 去数据库查询
		ProductParamInfo paramInfo = productRepairMapper.selectParamById(repairId);
		// 存redis
		if (null == paramInfo) {
			return LianjiuResult.build(502, "查询错误");
		}
		String productId = paramInfo.getProductId();
		if (Util.notEmpty(productId)) {
			jedisClient.set("repair-" + productId, paramInfo.getProductName());
			// 设置过期时间
			jedisClient.expire("repair-" + productId, 3600);
		}
		return LianjiuResult.ok(paramInfo);
	}

}
