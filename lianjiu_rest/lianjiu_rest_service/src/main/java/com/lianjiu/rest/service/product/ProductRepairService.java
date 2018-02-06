package com.lianjiu.rest.service.product;

import com.lianjiu.common.pojo.LianjiuResult;

public interface ProductRepairService {

	LianjiuResult getRepairByCid(Long cid);

	LianjiuResult getRepairById(String repairId);

	LianjiuResult getRepairByRepairParamTemplate(String repairId);

}
