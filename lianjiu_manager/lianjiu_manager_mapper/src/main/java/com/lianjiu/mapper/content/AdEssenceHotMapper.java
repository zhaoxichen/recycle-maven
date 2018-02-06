package com.lianjiu.mapper.content;

import java.util.List;

import com.lianjiu.model.AdEssenceHot;

public interface AdEssenceHotMapper {

    int deleteByPrimaryKey(String essId);

    int insertAdEssenceHot(AdEssenceHot record);

    int insertSelective(AdEssenceHot record);


    AdEssenceHot selectByPrimaryKey(String essId);


    int updateByPrimaryKeySelective(AdEssenceHot record);

    int updateByPrimaryKey(AdEssenceHot record);

	List<AdEssenceHot> selectAdEssenceHotList();
}