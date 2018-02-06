package com.lianjiu.service.product.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.model.ProductBulkPrice;
import com.lianjiu.rest.mapper.product.ProductBulkPriceMapper;
import com.lianjiu.service.product.ProductBulkPriceService;

@Service
public class ProductBulkPriceServiceImpl implements ProductBulkPriceService {

	@Autowired
	ProductBulkPriceMapper bulkPriceMapper;
	@Autowired
	private static Logger loggerBulkPrice = Logger.getLogger("bulkPrice");

	/**
	 * 添加价格
	 */
	@Override
	public LianjiuResult addBulkPrice(ProductBulkPrice bulkPrice) {
		loggerBulkPrice.info("开始添加价格");
		bulkPrice.setPriceId(IDUtils.genGUIDs());
		try {
			bulkPriceMapper.updateCurrentPriceByPid(bulkPrice.getProductId());
			int rowsAffected = bulkPriceMapper.insert(bulkPrice);
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			loggerBulkPrice.info("数据异常：" + e.getMessage());
			return LianjiuResult.build(501, "数据异常！");
		}
	}

	@Override
	public LianjiuResult deleteBulkPrice(String priceId) {
		loggerBulkPrice.info("开始删除价格");
		try {
			int rowsAffected = bulkPriceMapper.deleteByPrimaryKey(priceId);
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			loggerBulkPrice.info("数据异常：" + e.getMessage());
			return LianjiuResult.build(501, "数据异常！");
		}
	}

	@Override
	public LianjiuResult getBulkPriceByPid(String pid, int pageNum, int pageSize) {
		// ProductBulkPrice p = new ProductBulkPrice();
		// p.setProductId(pid);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<ProductBulkPrice> listbulkPrice = bulkPriceMapper.getBulkPriceByPid(pid);
		if (pageNum == 1 && 0 == listbulkPrice.size()) {
			loggerBulkPrice.info("商品价格为空");
			return LianjiuResult.build(501, "尚无商品价格");
		}
		if (0 == listbulkPrice.size()) {
			loggerBulkPrice.info("商品价格为空");
			return LianjiuResult.build(502, "尚无更多商品价格");
		} else {
			// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
			Page<ProductBulkPrice> pageBulkPrice = (Page<ProductBulkPrice>) listbulkPrice;
			loggerBulkPrice.info("总页数：" + pageBulkPrice.getPages());
			// 封装成相应数据对象
			LianjiuResult result = new LianjiuResult(listbulkPrice);
			result.setTotal(pageBulkPrice.getTotal());
			return LianjiuResult.ok(listbulkPrice);
		}
	}

}
