package com.lianjiu.mapper.product;

import java.util.List;

import com.lianjiu.common.pojo.ProductPriceGroup;

public interface ProductPriceGroupMapper {

	int deleteByPrimaryKey(String priceId);

	int insert(ProductPriceGroup record);

	int insertSelective(ProductPriceGroup record);

	ProductPriceGroup selectByPrimaryKey(String priceId);

	int updateByPrimaryKeySelective(ProductPriceGroup record);

	int updateByPrimaryKey(ProductPriceGroup record);

	List<ProductPriceGroup> selectByPid(String productId);
}