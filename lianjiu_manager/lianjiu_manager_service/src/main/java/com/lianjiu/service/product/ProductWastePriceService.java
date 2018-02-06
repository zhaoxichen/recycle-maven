package com.lianjiu.service.product;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.WastePrice;

public interface ProductWastePriceService {

	LianjiuResult getPriceAll();

	LianjiuResult getPriceAll(int pageNum, int pageSize);

	LianjiuResult getPriceByWasteId(String wasteId);

	LianjiuResult getPriceByWasteId(String wasteId, int pageNum, int pageSize);

	LianjiuResult addPrice(WastePrice wastePrice);

	LianjiuResult updatePrice(WastePrice wastePrice);

	LianjiuResult deletePrice(String wPriceId);

}
