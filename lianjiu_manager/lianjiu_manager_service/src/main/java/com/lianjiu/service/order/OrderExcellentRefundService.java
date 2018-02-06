package com.lianjiu.service.order;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersExcellentRefund;

public interface OrderExcellentRefundService {

	LianjiuResult getRefundAll();

	LianjiuResult getRefundAll(int pageNum, int pageSize);

	LianjiuResult getRefundByExcellentId(String excellentId);

	LianjiuResult getRefundById(String refundId);

	LianjiuResult addRefund(OrdersExcellentRefund refund);

	LianjiuResult updateRefund(OrdersExcellentRefund refund);

	LianjiuResult deleteRefund(String refundId);

	LianjiuResult confirmRefund(OrdersExcellentRefund refund);

	LianjiuResult selectRefoundList();
	
}
