package com.lianjiu.mapper.product;

import java.util.List;

import com.lianjiu.model.ProductParamTemplate;

public interface ProductParamTemplateMapper {

	//int insertProductParam(ProductParamTemplate template);

	ProductParamTemplate selectById(String id);

	List<ProductParamTemplate> selectAll();

	//int updateProductParam(ProductParamTemplate template);

	//int deleteById(String id);

	int insert(ProductParamTemplate template);

	int insertSelective(ProductParamTemplate template);

	// 更新
	int updateByPrimaryKey(ProductParamTemplate template);

	int updateByPrimaryKeySelective(ProductParamTemplate template);

	int deleteProductByid(String id);

	int deleteByPrimaryKey(String id);
}