package com.lianjiu.rest.mapper.search;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.rest.model.ItemSearch;

public interface SearchMapper {

	List<ItemSearch> getProductItemList(@Param("query") String queryString);
	
	List<ItemSearch> getWasteItemList(@Param("query") String queryString);

	List<ItemSearch> getProductExcellentItemList(@Param("query") String queryString);

	List<ItemSearch> getProductExcellentItemAll();

	List<ItemSearch> getProductItemAll();

	List<ItemSearch> getWasteItemAll();
}
