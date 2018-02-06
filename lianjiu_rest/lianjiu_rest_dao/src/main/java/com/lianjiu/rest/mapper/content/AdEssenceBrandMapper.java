package com.lianjiu.rest.mapper.content;

import java.util.List;

import com.lianjiu.model.AdEssenceBrand;

public interface AdEssenceBrandMapper {

	int deleteByPrimaryKey(String brId);

	int insert(AdEssenceBrand record);

	int insertSelective(AdEssenceBrand record);

	AdEssenceBrand selectByPrimaryKey(String brId);

	int updateByPrimaryKeySelective(AdEssenceBrand record);

	int updateByPrimaryKey(AdEssenceBrand record);

	List<AdEssenceBrand> findAll();

	int insertAdEssenceBrand(AdEssenceBrand record);

	List<AdEssenceBrand> selectAdEssenceBrandList();
}