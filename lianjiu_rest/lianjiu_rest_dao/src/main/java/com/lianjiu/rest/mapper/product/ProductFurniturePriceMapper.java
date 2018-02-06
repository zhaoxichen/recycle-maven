package com.lianjiu.rest.mapper.product;

import java.util.List;

import com.lianjiu.model.ProductFurniturePrice;

public interface ProductFurniturePriceMapper {
	int deleteByPrimaryKey(String priceId);

	int insert(ProductFurniturePrice record);

	ProductFurniturePrice selectByPrimaryKey(String priceId);

	int updateByPrimaryKeySelective(ProductFurniturePrice record);

	List<ProductFurniturePrice> getAllfurniturePrice(ProductFurniturePrice p);

	int updateCurrentPriceByPid(String productId);

}