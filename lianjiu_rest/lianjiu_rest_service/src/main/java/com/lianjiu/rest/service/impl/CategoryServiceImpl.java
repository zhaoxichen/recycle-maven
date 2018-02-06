package com.lianjiu.rest.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.Category;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.rest.service.CategoryService;

/**
 * 商品分类服务层
 * 
 * @author zhaoxi
 *
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;

	private List<Category> categoryList = new ArrayList<Category>();

	@Override
	public LianjiuResult selectByParentId(Long pid) {
		Category category = new Category();
		category.setCategoryParentId(pid);
		SearchObjecVo vo = new SearchObjecVo(category);
		// 根据parentid查询节点列表
		List<Category> productCategorys = categoryMapper.selectByParentId(pid);
		return LianjiuResult.ok(productCategorys);
	}

	@Override
	public LianjiuResult insertCategory(Category category) {
		if (null == category) {
			return LianjiuResult.build(500, "传入对象为空");
		}
		// '状态。可选值:1(正常),2(删除)',
		category.setCategoryStatus(1);
		category.setCategorySortOrder(1);
		// 创建时间
		category.setCategoryCreated(new Date());
		category.setCategoryUpdated(new Date());
		// 插入数据库
		int rowsAffected = categoryMapper.insertSelective(category);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult delectCategoryById(long id) {
		if (0 > id) {
			return LianjiuResult.build(501, "请指定要删除的模板id");
		}
		int rowsAffected = categoryMapper.deleteByPrimaryKey(id);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult selectDescendantsByParentId(Long id) {
		// 先清空容器
		categoryList.clear();
		// 递归查询
		this.getDescendants(id);
		return LianjiuResult.ok(categoryList);
	}

	private void getDescendants(Long categoryId) {
		List<Category> categoryChilden = categoryMapper.selectByParentId(categoryId);
		// 边界条件
		if (null == categoryChilden || categoryChilden.size() == 0) {
			return;
		}
		categoryList.addAll(categoryChilden);
		// 遍历
		Long nextPid;
		for (Category category : categoryChilden) {
			// 获取id作为下个查询的父级id
			nextPid = category.getCategoryId();
			// 递归
			getDescendants(nextPid);
		}
	}

	@Override
	public LianjiuResult updateCategory(Category category) {
		if (null == category) {
			return LianjiuResult.build(500, "传入对象为空");
		}
		if (0 > category.getCategoryId()) {
			return LianjiuResult.build(501, "请指定要更新的商品id");
		}
		// 存入数据库
		int rowsAffected = categoryMapper.updateByPrimaryKeySelective(category);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult selectCategoryById(Long categoryId) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		return LianjiuResult.ok(category);
	}

}
