package com.lianjiu.order.service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersRepairScheme;

public interface OrderRepairSchemeService {

	LianjiuResult getSchemeAll();

	LianjiuResult getSchemeAll(int pageNum, int pageSize);

	LianjiuResult getSchemeByOid(String ordersid);

	LianjiuResult getSchemeById(String schemeId);

	LianjiuResult addScheme(OrdersRepairScheme scheme);

	LianjiuResult updateScheme(OrdersRepairScheme scheme);

	LianjiuResult deleteScheme(String schemeId);

}
