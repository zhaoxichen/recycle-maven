package com.lianjiu.service.user;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.RecyclingWarehouseman;

public interface WarehousemanService {

	LianjiuResult getWarehousemanAll(int pageNum, int pageSize);

	LianjiuResult getWarehousemanById(String recyclerId);

	LianjiuResult addWarehouseman(RecyclingWarehouseman warehouseman);

	LianjiuResult modifyWarehouseman(RecyclingWarehouseman warehouseman);

	LianjiuResult removeWarehouseman(String recyclerId);

}
