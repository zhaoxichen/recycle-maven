package com.lianjiu.rest.mapper.product;

import java.util.List;

import com.lianjiu.model.ProductParamTemplate;

public interface ProductParamTemplateMapper {

	ProductParamTemplate selectById(String id);

	List<ProductParamTemplate> selectAll();

	int insert(ProductParamTemplate template);

	int updateByPrimaryKeySelective(ProductParamTemplate template);

	int deleteByPrimaryKey(String id);

}