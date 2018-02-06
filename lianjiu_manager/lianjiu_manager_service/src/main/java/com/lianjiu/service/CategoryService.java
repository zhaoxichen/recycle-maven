package com.lianjiu.service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.Category;

public interface CategoryService {


	LianjiuResult selectByParentId(Long pid);

	LianjiuResult insertCategory(Category category);

	LianjiuResult delectCategoryById(long id);

	// 查询所有后代
	LianjiuResult selectDescendantsByParentId(Long id);

	LianjiuResult updateCategory(Category category);

	LianjiuResult selectCategoryById(Long categoryId);
	
}
