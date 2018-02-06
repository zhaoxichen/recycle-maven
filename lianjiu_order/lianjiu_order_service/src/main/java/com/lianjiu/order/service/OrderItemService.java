package com.lianjiu.order.service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersItem;

public interface OrderItemService {
	
	LianjiuResult getItemAll();
	
	LianjiuResult getItemAll(int pageNum, int pageSize);
	
	LianjiuResult getItemByOrdersId(String ordersId);

	LianjiuResult getItemByOrdersId(String ordersId, int pageNum, int pageSize);
	
	LianjiuResult getItemById(String itemId);

	LianjiuResult addItem(OrdersItem item);

	LianjiuResult updateItem(OrdersItem item);

	LianjiuResult deleteItem(String itemId);
	
	LianjiuResult updateByStatus(String ordersId,int orItemsStatus);

}
