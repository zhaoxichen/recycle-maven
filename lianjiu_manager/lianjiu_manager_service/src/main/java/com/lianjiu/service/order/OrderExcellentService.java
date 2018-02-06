package com.lianjiu.service.order;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersExcellent;

public interface OrderExcellentService {

	LianjiuResult getExcellentAll();

	LianjiuResult getExcellentAll(int pageNum, int pageSize);

	LianjiuResult getExcellentByCid(Long cid);

	LianjiuResult getExcellentByCid(Long cid, int pageNum, int pageSize);

	LianjiuResult getExcellentById(String excellentId);

	LianjiuResult addExcellent(OrdersExcellent excellent);

	LianjiuResult updateExcellent(OrdersExcellent excellent);

	LianjiuResult deleteExcellent(String excellentId);

	LianjiuResult deliverGoods(OrdersExcellent orders);

	LianjiuResult getOrdersDetails(String ordersId);

	LianjiuResult ordersFilter(OrdersExcellent orders, int pageNum, int pageSize);

	LianjiuResult vagueQuery(OrdersExcellent ordersExcellent, int pageNum, int pageSize, String cratedStart,
			String cratedOver);

}
