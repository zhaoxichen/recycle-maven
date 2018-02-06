package com.lianjiu.order.service;

import java.text.ParseException;
import java.util.List;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.OrdersFacefaceDetails;
import com.lianjiu.model.OrdersItem;

public interface OrderFacefaceService {
	LianjiuResult getFacefaceById(String facefaceId);

	LianjiuResult submit(OrdersFaceface faceface) throws ParseException;

	LianjiuResult updateFaceface(OrdersFaceface faceface);

	LianjiuResult deleteFaceface(String facefaceId);

	LianjiuResult getOrderItemsById(String orFaceItemId);

	LianjiuResult getParamData(String productId);

	LianjiuResult modifyParam(String orItemsId, String orItemsParamModify);

	LianjiuResult addHomeVisitTime(String orFacefaceId, String visitTime);

	LianjiuResult getHomeVistStutsList(String userId, Integer type, int pageNum, int pageSize);

	LianjiuResult getHomeVistList(String userId, int pageNum, int pageSize);

	LianjiuResult selectOrderDetails(String orders_id);
	
	LianjiuResult selectFaceFaceItemByOrderId(String ordersId);

	LianjiuResult modifyVisitPrice(OrdersFacefaceDetails ordersFacefaceDetails, Byte orFacefaceStatus);

	LianjiuResult updateFaceFaceState(OrdersFaceface ordersFaceface, int orFacefaceStatus);

	LianjiuResult qualityCheckingDetails(String orItemsId);

	// 从rest中迁移
	LianjiuResult getFaceFaceAll(String userId, int pageNum, int pageSize);

	LianjiuResult getFaceFaceAll(int pageNum, int pageSize);

	LianjiuResult getFaceFaceByState(Byte state, int pageNum, int pageSize);

	LianjiuResult updateFaceFaceState(String facefaceId, Byte state);

	LianjiuResult getByOrdersItem(String facefaceId);

	LianjiuResult addWaste(String userId, OrdersItem ordersItem);

	LianjiuResult submitFace(String userId, String orFacefaceId);

	LianjiuResult getWasteCar(String userId);

	LianjiuResult deleteWasteCar(String userId, String orItemsProductId);

	LianjiuResult introductionFaceface(String userId, OrdersItem productItem);

	LianjiuResult productBalance(List<String> productlist, String userId);

	LianjiuResult productBalanceAfter(String userId);

	LianjiuResult reduceProductFromCar(String itemId, String userId);

	LianjiuResult directSubmit(OrdersItem productItem, String userId);

	LianjiuResult directSubmitWaste(OrdersItem ordersItem, String userId);

	LianjiuResult orderPriceRefuse(String ordersId);

}
