package com.lianjiu.service.order;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.pojo.MachineCheck;
import com.lianjiu.model.OrdersExpress;

public interface OrderExpressService {

	LianjiuResult getAllOrderExpress(int pageNum, int pageSize);

	LianjiuResult getAllOrderExpress();

	LianjiuResult getAllOrderExpressByStatus(Integer state);

	LianjiuResult getAllOrderExpressByStatus(Integer state, int pageNum, int pageSize);

	LianjiuResult modifyOrder(OrdersExpress ordersExpress);

	LianjiuResult getOrderById(String orderId);

	LianjiuResult getgetOrderByCid(Long cid);

	LianjiuResult getgetOrderByCid(Long cid, int pageNum, int pageSize);

	LianjiuResult getOrderItemsById(String orExpItemId);

	LianjiuResult getOrderDetailsById(String orderId);

	LianjiuResult getItemByOrdersId(String ordersId);

	LianjiuResult getParamData(String productId);

	LianjiuResult modifyParam(String orItemsId, String orItemsParam,MachineCheck machineCheck);

	LianjiuResult getParamData(String productId, String majorName);

	LianjiuResult modifyOrderStatus(String ordersId, Byte ordersStatus);

	LianjiuResult modifyOrderExpressNum(OrdersExpress ordersExpress);

	LianjiuResult checkFinish(String ordersId,Integer isModify);

	LianjiuResult payForOrders(String ordersId);

	LianjiuResult ordersFilter(OrdersExpress orders, int pageNum, int pageSize);

	LianjiuResult vagueQuery(OrdersExpress ordersExpress, int pageNum, int pageSize, String cratedStart,
			String cratedOver);

}
