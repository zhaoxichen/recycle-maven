package com.lianjiu.rest.mapper.product;

import java.util.List;

import com.lianjiu.model.ProductBulkPrice;

public interface ProductBulkPriceMapper {
	int deleteByPrimaryKey(String priceId);

	int insert(ProductBulkPrice record);

	ProductBulkPrice selectByPrimaryKey(String priceId);

	int updateByPrimaryKeySelective(ProductBulkPrice record);

	List<ProductBulkPrice> getBulkPriceByPid(String pid);

	String getCurrentPriceByPid(String productId);

	int updateCurrentPriceByPid(String productId);
}