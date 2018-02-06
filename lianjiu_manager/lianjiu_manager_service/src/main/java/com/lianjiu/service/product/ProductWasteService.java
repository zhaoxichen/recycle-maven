package com.lianjiu.service.product;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.Waste;

public interface ProductWasteService {

	LianjiuResult getWasteAll();

	LianjiuResult getWasteAll(int pageNum, int pageSize);

	LianjiuResult addWaste(Waste waste);

	LianjiuResult updateWaste(Waste waste);

	LianjiuResult deleteWaste(String wasteId);

	LianjiuResult getWasteByCid(Long cid);

	LianjiuResult getWasteByCid(Long cid, int pageNum, int pageSize);

}
