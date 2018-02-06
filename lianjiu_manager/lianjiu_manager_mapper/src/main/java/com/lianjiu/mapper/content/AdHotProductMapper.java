	package com.lianjiu.mapper.content;

import java.util.List;

import com.lianjiu.model.AdHotProduct;

public interface AdHotProductMapper {
	
	int deleteByPrimaryKey(String hotId);
	
	int insertSelective(AdHotProduct record);
	
	int updateByPrimaryKeySelective(AdHotProduct record);
	
	int updateByPrimaryKey(AdHotProduct record);
	
	 AdHotProduct selectByPrimaryKey(String hotId);

	int insertHotProduct(AdHotProduct record);

	List<AdHotProduct> selectElectronicList();
}