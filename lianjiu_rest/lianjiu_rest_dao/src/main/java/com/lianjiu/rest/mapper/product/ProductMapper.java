package com.lianjiu.rest.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.Product;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.model.ProductMaxprice;

public interface ProductMapper {

	List<Product> selectBySearchObjecVo(SearchObjecVo vo);

	Product selectByPrimaryKey(String id);

	List<Product> selectByProductCategoryId(long categoryId);

	List<Product> selectByCategoryId(Long categoryId);

	List<ProductMaxprice> selectMaxByProductCategoryId(long categoryId);

	List<ProductMaxprice> selectProductByKeyword(String keyword);

	int updateProductSoldCount(String ordersId);

	List<Product> selectFrontPageHotProduct(@Param("categoryIdList") List<Long> categoryIdList);

	Product selectProductParam(String productId);

	List<Product> selectProductInfoByCategorys(@Param("categoryIdList") List<Long> cIds, @Param("orderBy") int orderBy);

	int deleteByPrimaryKey(String id);

	int updateByPrimaryKeySelective(Product product);

	int insert(Product product);

	String checkPriceById(String productId);
}