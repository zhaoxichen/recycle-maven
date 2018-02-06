package com.lianjiu.rest.service.product;

import java.util.List;

import com.lianjiu.common.pojo.LianjiuResult;

public interface ProductExcellentService {

	LianjiuResult getExcellentAll(int pageNum, int pageSize);

	LianjiuResult getExcellentById(String excellentId);

	LianjiuResult getBrands();

	LianjiuResult getCategoryByName(String categoryName);

	LianjiuResult getBrandsByPid(List<Long> categoryPids);

	LianjiuResult getProductByBrands(List<Long> categoryIds, List<String> brands, int orderBy, int pageNum,
			int pageSize);

	LianjiuResult getProductByBrand(String brand, int orderBy, int pageNum, int pageSize);

}
