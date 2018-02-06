package com.lianjiu.mapper.product;

import java.util.List;

import com.lianjiu.model.WastePrice;
import com.lianjiu.model.vo.SearchObjecVo;

public interface WastePriceMapper {

	int deleteByPrimaryKey(String wPriceId);

	int insert(WastePrice record);

	int insertSelective(WastePrice record);

	WastePrice selectByPrimaryKey(String wPriceId);

	int updateByPrimaryKeySelective(WastePrice record);

	int updateByPrimaryKey(WastePrice record);
	
	List<WastePrice> selectBySearchObjecVo(SearchObjecVo vo);
}