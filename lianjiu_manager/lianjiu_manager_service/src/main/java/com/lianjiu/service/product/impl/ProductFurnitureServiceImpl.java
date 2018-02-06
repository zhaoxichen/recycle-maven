package com.lianjiu.service.product.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.model.ProductFurniture;
import com.lianjiu.rest.mapper.product.ProductFurnitureMapper;
import com.lianjiu.service.product.ProductFurnitureService;

/**
 * 家具
 * 
 * @author zhaoxi
 *
 */
@Service
public class ProductFurnitureServiceImpl implements ProductFurnitureService {

	@Autowired
	ProductFurnitureMapper productFurnitureMapper;
	@Autowired
	private static Logger loggerProductFurniture = Logger.getLogger("productFurniture");

	/**
	 * 添加家具
	 */
	@Override
	public LianjiuResult addFurniture(ProductFurniture furniture) {
		loggerProductFurniture.info("添加家具");
		furniture.setFurnitureId(IDUtils.genGUIDs());
		try {
			int rowsAffected = productFurnitureMapper.insert(furniture);
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			loggerProductFurniture.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(501, "数据异常！");
		}
	}

	/**
	 * 更新家具商品
	 */
	@Override
	public LianjiuResult updateFurniture(ProductFurniture furniture) {
		loggerProductFurniture.info("开始更新家具商品");
		try {
			int rowsAffected = productFurnitureMapper.updateByPrimaryKeySelective(furniture);
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			loggerProductFurniture.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(501, "数据异常！");
		}
	}

	
	@Override
	public LianjiuResult getFurnitureByCid(Long cid, int pageNum, int pageSize) {
		ProductFurniture p = new ProductFurniture();
		p.setCategoryId(cid);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<ProductFurniture> listFurniture = productFurnitureMapper.getAllFurniture(p);
		if (pageNum == 1 && 0 == listFurniture.size()) {
			loggerProductFurniture.info("家具商品为空");
			return LianjiuResult.build(501, "尚无家具商品");
		}
		if (0 == listFurniture.size()) {
			loggerProductFurniture.info("家具商品为空");
			return LianjiuResult.build(502, "尚无更多家具商品");
		} else {
			// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
			Page<ProductFurniture> pageFurniture = (Page<ProductFurniture>) listFurniture;
			loggerProductFurniture.info("总页数：" + pageFurniture.getPages());
			// 封装成相应数据对象
			LianjiuResult result = new LianjiuResult(listFurniture);
			result.setTotal(pageFurniture.getTotal());
			return LianjiuResult.ok(listFurniture);
		}
	}

	/**
	 * 删除家具商品
	 */
	@Override
	public LianjiuResult deleteFurniture(String furnitureId) {
		loggerProductFurniture.info("开始删除家具商品");
		try {
			int rowsAffected = productFurnitureMapper.deleteByPrimaryKey(furnitureId);
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			loggerProductFurniture.info("捕获异常！" + e.getMessage());
			return LianjiuResult.build(501, "数据异常！");
		}
	}

}
