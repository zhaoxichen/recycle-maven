package com.lianjiu.service.product;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.ProductRepair;

public interface ProductRepairService {

	LianjiuResult getRepairAll();

	LianjiuResult getRepairAll(int pageNum, int pageSize);

	LianjiuResult getRepairByCid(Long cid);

	LianjiuResult getRepairByCid(Long cid, int pageNum, int pageSize);

	LianjiuResult addRepair(ProductRepair repair);

	LianjiuResult updateRepair(ProductRepair repair);

	LianjiuResult deleteRepair(String repairId);

	LianjiuResult getRepairById(String repairId);

}
