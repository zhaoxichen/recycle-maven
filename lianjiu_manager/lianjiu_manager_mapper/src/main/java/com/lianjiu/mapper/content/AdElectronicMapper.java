package com.lianjiu.mapper.content;

import java.util.List;

import com.lianjiu.model.AdElectronic;

public interface AdElectronicMapper {
	
	int deleteByPrimaryKey(String eleId);
	
	int insertElectronic(AdElectronic record);
	
	int insertSelective(AdElectronic record);
	
	int updateByPrimaryKeySelective(AdElectronic record);
	
	int updateByPrimaryKey(AdElectronic record);
	
	AdElectronic selectByPrimaryKey(String eleId);

	List<AdElectronic> selectElectronicList();


}