package com.lianjiu.mapper.content;

import java.util.List;

import com.lianjiu.model.AdCarousel;

public interface AdCarouselMapper {
	
	int deleteByPrimaryKey(String caId);
	
	int insertSelective(AdCarousel record);
	
	int updateByPrimaryKeySelective(AdCarousel record);
	
	int updateByPrimaryKey(AdCarousel record);
	
	AdCarousel selectByPrimaryKey(String caId);

	int insertCarousel(AdCarousel record);

	List<AdCarousel> selectActivityList();

}