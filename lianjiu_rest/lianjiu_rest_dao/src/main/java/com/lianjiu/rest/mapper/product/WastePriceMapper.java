package com.lianjiu.rest.mapper.product;

import java.util.List;

import com.lianjiu.model.WastePrice;

public interface WastePriceMapper {

	int deleteByPrimaryKey(String wPriceId);

	int insert(WastePrice record);

	int insertSelective(WastePrice record);

	WastePrice selectByPrimaryKey(String wPriceId);

	int updateByPrimaryKeySelective(WastePrice record);

	int updateByPrimaryKey(WastePrice record);

	List<WastePrice> selectByWasteId(String id);
	
	String selectPriceByWasteId(String id);
	
	String selectPriceByPriceId(String id);

	List<WastePrice> selectBySearchObjecVo(Object object);
	
	
}