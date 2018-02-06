package com.lianjiu.rest.mapper.content;

import java.util.List;

import com.lianjiu.model.AdElectronic;

public interface AdElectronicMapper {



    List<AdElectronic> selectElectronicList();

    AdElectronic selectByPrimaryKey(String eleId);

	AdElectronic getImageByOrderId(String str);

	int insertElectronic(AdElectronic record);

	int deleteByPrimaryKey(String eleId);

	int updateByPrimaryKeySelective(AdElectronic record);

}