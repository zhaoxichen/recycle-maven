package com.lianjiu.service.order;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersBulk;
import com.lianjiu.model.OrdersBulkItem;

public interface OrdersBulkService {

	LianjiuResult getOrders(int pageNum, int pageSize);

	LianjiuResult cancelOrders(String ordersId);

	LianjiuResult getOrdersItem(String orBulkId);

	LianjiuResult modiFyOrdersItem(OrdersBulkItem item);

	LianjiuResult modiFyOrdersFinish(OrdersBulk ordersBulk);

	LianjiuResult searchOrders(String parameter, int pageNum, int pageSize);

	LianjiuResult getOrdersById(String orBulkId);

}
