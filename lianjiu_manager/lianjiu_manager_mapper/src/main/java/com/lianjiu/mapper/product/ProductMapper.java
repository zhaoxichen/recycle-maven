package com.lianjiu.mapper.product;

import java.util.List;

import com.lianjiu.model.Product;
import com.lianjiu.model.vo.SearchObjecVo;

public interface ProductMapper {

	List<Product> selectBySearchObjecVo(SearchObjecVo vo);

	int insert(Product product);

	// 更新
	int updateByPrimaryKeySelective(Product product);

	int deleteByPrimaryKey(String id);

	Product selectByPrimaryKey(String id);

}