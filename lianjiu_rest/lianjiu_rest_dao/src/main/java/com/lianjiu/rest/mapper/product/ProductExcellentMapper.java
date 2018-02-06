package com.lianjiu.rest.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.ProductExcellent;

public interface ProductExcellentMapper {

	ProductExcellent selectByPrimaryKey(String excellentId);

	List<ProductExcellent> selectProductInfoByCategorys(@Param("categoryIdList") List<Long> categoryIds,
			@Param("orderBy") int orderBy);

	List<ProductExcellent> selectAll();

	List<ProductExcellent> selectProductInfoByCidsOrderByPrice(@Param("categoryIdList") List<Long> cIds);

	List<ProductExcellent> selectProductInfoByCidsOrderBySold(@Param("categoryIdList") List<Long> cIds);

	int updateProductSoldCount(String ordersId);

	List<ProductExcellent> selectBySearchObjecVo(Object object);

	int insert(ProductExcellent excellent);

	int updateByPrimaryKeySelective(ProductExcellent excellent);

	int deleteByPrimaryKey(String excellentId);
	
	int updateProductStockReduce(String excellentId);
	
	int updateProductStockAdd(String excellentId);
	
}