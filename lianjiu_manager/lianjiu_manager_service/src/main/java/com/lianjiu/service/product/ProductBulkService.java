package com.lianjiu.service.product;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.ProductBulk;

public interface ProductBulkService {

	LianjiuResult getBulkByCid(Long cid, int pageNum, int pageSize);

	LianjiuResult addBulk(ProductBulk bulk);

	LianjiuResult updateBulk(ProductBulk bulk);

	LianjiuResult deleteBulk(String bulkId);

	LianjiuResult getAll(int pageNum, int pageSize);

	LianjiuResult modifyBulkAddress(Long cid, String address);

	LianjiuResult getBulkAddress(Long cid);

}
