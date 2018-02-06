package com.lianjiu.rest.service.product;

import com.lianjiu.common.pojo.LianjiuResult;

public interface ProductParamService {

	public LianjiuResult selectParamById(String id);

	public LianjiuResult selectByParentAll(int pageNum, int pageSize);

	public LianjiuResult selectAll();
}
