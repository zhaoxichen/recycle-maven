package com.lianjiu.rest.service.product;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersBulkItem;

public interface ProductBulkService {

	LianjiuResult getBulkByKeyword(String keyword, Long categoryId, String TAKEN, int pageNum, int pageSize);

	LianjiuResult setBulkItemCart(String TAKEN, OrdersBulkItem item);

	LianjiuResult getBulkItemCart(String TAKEN);

}
