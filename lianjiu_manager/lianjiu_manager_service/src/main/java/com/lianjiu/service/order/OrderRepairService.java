package com.lianjiu.service.order;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersRepair;

public interface OrderRepairService {

	LianjiuResult getRepairAll();

	LianjiuResult getRepairAll(int pageNum, int pageSize);

	LianjiuResult getRepairByCid(Long cid);

	LianjiuResult getRepairByCid(Long cid, int pageNum, int pageSize);

	LianjiuResult getRepairById(String repairId);

	LianjiuResult addRepair(OrdersRepair repair);

	LianjiuResult updateRepair(OrdersRepair repair);

	LianjiuResult deleteRepair(String repairId);

	LianjiuResult getItemByOrdersId(String ordersId, int pageNum, int pageSize);

	LianjiuResult getRepairByRepairParamTemplate(String ordersId);

	LianjiuResult repairHandling(String ordersId, String orRepairTechnicians);

	LianjiuResult repairFinish(String ordersId, String orRepairPicture);

	LianjiuResult ordersFilter(OrdersRepair orders, int pageNum, int pageSize);

	LianjiuResult vagueQuery(OrdersRepair ordersRepair, int pageNum, int pageSize, String cratedStart, String cratedOver, String handleStart, String handleOven);

}
