package com.lianjiu.rest.mapper.content;

import java.util.List;

import com.lianjiu.model.AdCarousel;

public interface AdCarouselMapper {



    AdCarousel selectByPrimaryKey(String caId);


	List<AdCarousel> selectActivityList();


	List<AdCarousel> selectCararouseFour();


	List<AdCarousel> selectCararouseFourType(int type);


	int insertCarousel(AdCarousel record);


	int deleteByPrimaryKey(String caId);


	int updateByPrimaryKeySelective(AdCarousel record);
}