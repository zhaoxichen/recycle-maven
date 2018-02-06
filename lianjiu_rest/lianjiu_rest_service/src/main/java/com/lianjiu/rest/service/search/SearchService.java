package com.lianjiu.rest.service.search;

import com.lianjiu.common.pojo.LianjiuResult;

public interface SearchService {

	LianjiuResult search(String queryString, int page, int rows) throws Exception;

	LianjiuResult searchBySql(String queryString, int page, int rows);

	LianjiuResult getCategoryByName(String categoryName);

	LianjiuResult getSearchHistory(String userId);

	LianjiuResult searchExcellentBySql(String keyword, Integer pageNum, Integer pageSize);
}
