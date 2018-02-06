package com.lianjiu.service.product;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.ProductFurniturePrice;

public interface ProductFurniturePriceService {

	LianjiuResult addFurniturePrice(ProductFurniturePrice furniturePrice);

	LianjiuResult deleteFurniturePrice(String priceId);

	LianjiuResult getFurniturePriceByPid(String pid, int pageNum, int pageSize);

}
