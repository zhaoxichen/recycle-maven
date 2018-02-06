package com.lianjiu.mapper;

import java.util.List;

import com.lianjiu.model.Category;
import com.lianjiu.model.vo.SearchObjecVo;

public interface CategoryMapper {

    int deleteByPrimaryKey(Long categoryId);

    int insert(Category record);

    List<Category> selectByParentId(Long categoryParentId);

    int updateByPrimaryKeySelective(Category record);

	List<Category> selectBySearchVo(SearchObjecVo vo);

	Category selectByPrimaryKey(Long id);
}