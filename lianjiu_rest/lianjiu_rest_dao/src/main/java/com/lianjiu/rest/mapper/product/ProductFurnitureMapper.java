package com.lianjiu.rest.mapper.product;

import java.util.List;

import com.lianjiu.model.ProductFurniture;

public interface ProductFurnitureMapper {

    int deleteByPrimaryKey(String furnitureId);

    int insert(ProductFurniture furniture);

    int updateByPrimaryKeySelective(ProductFurniture furniture);

	List<ProductFurniture> getAllFurniture(ProductFurniture p);
}