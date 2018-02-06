package com.lianjiu.rest.service.product;

import com.lianjiu.common.pojo.LianjiuResult;

public interface ProductFurnitureService {

	LianjiuResult getFurnitureByCid(Long cid, int pageNum, int pageSize);

	LianjiuResult getFurnitureDetails(String furniturePriceId);

}
