package com.lianjiu.order.service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersExcellentRefund;
import com.lianjiu.rest.model.OrdersExcellentVo;

public interface OrderExcellentService {

	LianjiuResult getExcellentById(String excellentId);

	LianjiuResult updateExcellent(OrdersExcellent excellent);

	LianjiuResult deleteExcellent(String excellentId);

	LianjiuResult modifyExcellentState(String excellentId, Byte orExcellentStatus);

	LianjiuResult modifyExcellentHandleTime(String excellentId);

	LianjiuResult selectAddressDefault(String userId);

	LianjiuResult addRefund(OrdersExcellentRefund refund, Byte ordersStatus);

	LianjiuResult modifyRefundByOrdersId(String orExcellentId, String refundExpress, String refundExpressNum);

	LianjiuResult getExcellentItems(String excellentId);

	LianjiuResult addOrdersExcellent(OrdersExcellentVo ordersExcellentVo);

	LianjiuResult getOneComment(String ordersId);

	LianjiuResult getTimeRemaining(String ordersId);

}
