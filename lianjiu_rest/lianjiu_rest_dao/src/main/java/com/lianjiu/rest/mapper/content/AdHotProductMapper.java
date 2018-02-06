package com.lianjiu.rest.mapper.content;

import java.util.List;

import com.lianjiu.model.AdHotProduct;
import com.lianjiu.model.Product;

public interface AdHotProductMapper {

    AdHotProduct selectByPrimaryKey(String hotId);

	List<AdHotProduct> selectElectronicList();

	List<AdHotProduct> selectHotProductFour();

	List<AdHotProduct> selectByProductCategoryId(Long categoryId);

	List<AdHotProduct> selectHotProductTen();

	int insertHotProduct(AdHotProduct record);

	int deleteByPrimaryKey(String hotId);

	int updateByPrimaryKeySelective(AdHotProduct record);
}