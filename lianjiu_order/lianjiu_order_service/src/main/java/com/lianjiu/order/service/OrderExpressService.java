package com.lianjiu.order.service;

import java.util.List;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersExpress;
import com.lianjiu.model.OrdersItem;

public interface OrderExpressService {

	LianjiuResult modifyOrder(OrdersExpress ordersExpress);

	LianjiuResult getOrderById(String orderId);

	LianjiuResult getOrderItemsById(String orExpItemId);

	LianjiuResult getParamData(String productId);

	LianjiuResult modifyParam(String orItemsId, String orItemsParamModify);

	LianjiuResult getParamData(String productId, String majorName);

	LianjiuResult modifyOrderStatus(String ordersId, Byte ordersStatus);

	LianjiuResult modifyOrderExpressNum(String ordersId, Byte ordersStatus, String ordersExpressNum);

	LianjiuResult submit(OrdersExpress ordersExpress, String orItemsBuyway, String checkCode);

	LianjiuResult addExpresssNum(OrdersExpress ordersExpress);

	LianjiuResult getExpressList(String userId, int pageNum, int pageSize);

	LianjiuResult getExpressStutsList(String userId, int type, int pageNum, int pageSize);

	LianjiuResult ModifyExpressOrderStatus(OrdersExpress ordersExpress);

	LianjiuResult productBalance(List<String> productIdlist, String userId);

	LianjiuResult productBalanceAfter(String userId);

	LianjiuResult reduceFromExpressCar(String itemId, String userId);

	LianjiuResult directExpressSale(OrdersItem productItem, String userId);

	LianjiuResult orderPriceConfirm(String ordersId);

	LianjiuResult orderPriceRefuse(String ordersId);

	LianjiuResult orderCancel(String ordersId);

	LianjiuResult qualityCheckingDetails(String orItemsId);

	LianjiuResult selectExpressItemByOrderId(String ordersId);
}
