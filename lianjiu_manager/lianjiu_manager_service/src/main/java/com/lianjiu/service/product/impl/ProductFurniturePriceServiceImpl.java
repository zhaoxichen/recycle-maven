package com.lianjiu.service.product.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.model.ProductFurniturePrice;
import com.lianjiu.rest.mapper.product.ProductFurniturePriceMapper;
import com.lianjiu.service.product.ProductFurniturePriceService;

/**
 * 家具产品价格关联
 * 
 * @author wuhongda
 *
 */
@Service
public class ProductFurniturePriceServiceImpl implements ProductFurniturePriceService {

	@Autowired
	ProductFurniturePriceMapper furniturePriceMapper;
	@Autowired
	private static Logger loggerFurniturePrice = Logger.getLogger("furniturePrice");

	/**
	 * 添加价格
	 */
	@Override
	public LianjiuResult addFurniturePrice(ProductFurniturePrice furniturePrice) {
		loggerFurniturePrice.info("开始添加价格");
		furniturePrice.setPriceId(IDUtils.genGUIDs());
		try {
			furniturePriceMapper.updateCurrentPriceByPid(furniturePrice.getProductId());
			int rowsAffected = furniturePriceMapper.insert(furniturePrice);
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			loggerFurniturePrice.info("数据异常：" + e.getMessage());
			return LianjiuResult.build(501, "数据异常！");
		}
	}

	@Override
	public LianjiuResult deleteFurniturePrice(String priceId) {
		loggerFurniturePrice.info("开始删除价格");
		try {
			int rowsAffected = furniturePriceMapper.deleteByPrimaryKey(priceId);
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			loggerFurniturePrice.info("数据异常：" + e.getMessage());
			return LianjiuResult.build(501, "数据异常！");
		}
	}

	@Override
	public LianjiuResult getFurniturePriceByPid(String pid, int pageNum, int pageSize) {
		ProductFurniturePrice p = new ProductFurniturePrice();
		p.setProductId(pid);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<ProductFurniturePrice> listfurniturePrice = furniturePriceMapper.getAllfurniturePrice(p);
		if (pageNum == 1 && 0 == listfurniturePrice.size()) {
			loggerFurniturePrice.info("商品价格为空");
			return LianjiuResult.build(501, "尚无商品价格");
		}
		if (0 == listfurniturePrice.size()) {
			loggerFurniturePrice.info("商品价格为空");
			return LianjiuResult.build(502, "尚无更多商品价格");
		} else {
			// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
			Page<ProductFurniturePrice> pageFurniturePrice = (Page<ProductFurniturePrice>) listfurniturePrice;
			loggerFurniturePrice.info("总页数：" + pageFurniturePrice.getPages());
			// 封装成相应数据对象
			LianjiuResult result = new LianjiuResult(listfurniturePrice);
			result.setTotal(pageFurniturePrice.getTotal());
			return LianjiuResult.ok(listfurniturePrice);
		}
	}

}
