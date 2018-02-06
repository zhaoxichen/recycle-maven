package com.lianjiu.rest.service.product;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersItem;

public interface ProductService {

	LianjiuResult getBrandBycId(Long cId);

	// 从分类表中取出参数模板
	LianjiuResult getParamTemplateFromCategory(String cid);

	LianjiuResult selectProductByid(String productId);

	LianjiuResult selectByProductCategoryId(long categoryId, int pageNum, int pageSize);

	LianjiuResult calculationPrice(String TAKEN);

	LianjiuResult getBrands();

	LianjiuResult getProductByProductParamTemplate(String productId);

	LianjiuResult productCategorySwitch(Long idElectronic, Integer order);

	LianjiuResult brandSwitch(long categoryId, int pageNum, int pageSize);

	LianjiuResult getRecentDealInfo(String orItemsId);

	LianjiuResult introductionProduct(OrdersItem productItem, String userId);

	LianjiuResult selectProductFromCar(String userId);

	LianjiuResult updateFaceFaceState(String orFacefaceId, int orFacefaceStatus);

	LianjiuResult productSearch(String keyword);

	LianjiuResult hotProductRecommend();

	LianjiuResult addToExpressProductCar(OrdersItem productItem, String userId);

	LianjiuResult sendExpressOrderSMS(String userId, String phone);

	LianjiuResult categorySwitch(long categoryId);

	LianjiuResult calculationImmediately(String userId, String productParamData, String productId);

	LianjiuResult calculationJDImmediately(String userId, String productParamData, String productId);

	LianjiuResult calculationJDPrice(String TAKEN);

}
