package com.lianjiu.service.product;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.ProductExcellent;

public interface ProductExcellentService {

	LianjiuResult getExcellentAll();

	LianjiuResult getExcellentAll(int pageNum, int pageSize);

	LianjiuResult getExcellentByCid(Long cid);

	LianjiuResult getExcellentByCid(Long cid, int pageNum, int pageSize);

	LianjiuResult addExcellent(ProductExcellent excellent);

	LianjiuResult updateExcellent(ProductExcellent excellent);

	LianjiuResult deleteExcellent(String excellentId);

	LianjiuResult getExcellentById(String excellentId);

}
