package com.lianjiu.mapper.content;

import java.util.List;

import com.lianjiu.model.AdEssenceBrand;

public interface AdEssenceBrandMapper {

	int deleteByPrimaryKey(String brId);

	int insertAdEssenceBrand(AdEssenceBrand record);

	int insertSelective(AdEssenceBrand record);

	AdEssenceBrand selectByPrimaryKey(String brId);

	int updateByPrimaryKeySelective(AdEssenceBrand record);

	int updateByPrimaryKey(AdEssenceBrand record);

	List<AdEssenceBrand> selectAdEssenceBrandList();
}