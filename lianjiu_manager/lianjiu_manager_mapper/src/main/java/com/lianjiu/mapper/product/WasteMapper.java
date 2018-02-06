package com.lianjiu.mapper.product;

import java.util.List;

import com.lianjiu.model.Waste;
import com.lianjiu.model.vo.SearchObjecVo;

public interface WasteMapper {

	int deleteByPrimaryKey(String wasteId);

	int insert(Waste record);

	int insertSelective(Waste record);

	Waste selectByPrimaryKey(String wasteId);

	int updateByPrimaryKeySelective(Waste record);

	int updateByPrimaryKey(Waste record);

	List<Waste> selectBySearchObjecVo(SearchObjecVo vo);
}