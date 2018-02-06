package com.lianjiu.rest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.Category;
import com.lianjiu.rest.model.CategoryInfo;

public interface CategoryMapper {

	int deleteByPrimaryKey(Long categoryId);

	int insert(Category record);

	int insertSelective(Category record);

	List<Category> selectByParentId(Long categoryParentId);

	List<Long> selectCidsByPid(Long categoryParentId);

	List<CategoryInfo> selectCategoryInfoByPId(Long categoryParentId);

	List<CategoryInfo> selectCategoryInfoByPIds(List<Long> categoryParentIds);

	int updateByPrimaryKeySelective(Category record);

	int updateByPrimaryKey(Category record);

	Category selectByPrimaryKey(Long id);

	Long selectByCategoryName(String categoryName);

	List<Long> selectParentIdByFilter(@Param("categoryIdList") List<Long> categoryIds,
			@Param("brandList") List<String> brands);

	List<Long> selectCidByCategoryName(String brand);

	List<Long> selectCidsByCategoryName(@Param("brandList") List<String> brands);

	String selectCategoryByName(Long cid);
	
	Long selectParentIdByPrimaryKey(Long categoryId);
}