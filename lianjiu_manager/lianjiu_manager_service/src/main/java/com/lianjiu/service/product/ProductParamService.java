package com.lianjiu.service.product;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.ProductParamTemplate;

public interface ProductParamService {

	public LianjiuResult addProductParam(ProductParamTemplate template);

	public LianjiuResult selectParamById(String id);

	public LianjiuResult selectByParentAll(int pageNum, int pageSize);

	// 更新
	public LianjiuResult updateProductParam(ProductParamTemplate template);

	// 删除
	public LianjiuResult deleteProductParam(String id);

	public LianjiuResult selectAll();
}
