package com.lianjiu.mapper.product;

import java.util.List;

import com.lianjiu.model.ProductExcellent;
import com.lianjiu.model.vo.SearchObjecVo;

public interface ProductExcellentMapper {

	int deleteByPrimaryKey(String excellentId);

	int insert(ProductExcellent record);

	ProductExcellent selectByPrimaryKey(String excellentId);

	int updateByPrimaryKeySelective(ProductExcellent record);

	List<ProductExcellent> selectBySearchObjecVo(SearchObjecVo vo);
}