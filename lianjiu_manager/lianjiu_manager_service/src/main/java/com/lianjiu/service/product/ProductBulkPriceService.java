package com.lianjiu.service.product;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.ProductBulkPrice;

public interface ProductBulkPriceService {

	LianjiuResult getBulkPriceByPid(String pid, int pageNum, int pageSize);

	LianjiuResult addBulkPrice(ProductBulkPrice bulkPrice);

	LianjiuResult deleteBulkPrice(String priceId);

}
