package com.lianjiu.service.product;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.ProductFurniture;

public interface ProductFurnitureService {

	LianjiuResult addFurniture(ProductFurniture furniture);

	LianjiuResult updateFurniture(ProductFurniture furniture);

	LianjiuResult getFurnitureByCid(Long cid, int pageNum, int pageSize);

	LianjiuResult deleteFurniture(String furnitureId);

}
