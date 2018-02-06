package com.lianjiu.rest.mapper.product;

import java.util.List;

import com.lianjiu.model.Waste;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.model.WastePriceVo;

public interface WasteMapper {

	int deleteByPrimaryKey(String wasteId);

	int insert(Waste record);

	int insertSelective(Waste record);

	Waste selectByPrimaryKey(String wasteId);

	int updateByPrimaryKeySelective(Waste record);

	int updateByPrimaryKey(Waste record);

	List<Waste> selectBySearchObjecVo(SearchObjecVo vo);

	List<WastePriceVo> selectByCategory(Long cid);
	
	WastePriceVo selectByPriceId(String id);
}