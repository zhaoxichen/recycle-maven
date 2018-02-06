package com.lianjiu.service.order;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersFaceface;

public interface OrderFacefaceService {

	LianjiuResult getFacefaceAll();

	LianjiuResult getFacefaceAll(int pageNum, int pageSize);

	LianjiuResult getFacefaceByState(Integer state);

	LianjiuResult getFacefaceByState(Integer state, int pageNum, int pageSize);

	LianjiuResult getFacefaceById(String facefaceId);

	LianjiuResult addFaceface(OrdersFaceface faceface);

	LianjiuResult updateFaceface(OrdersFaceface faceface);

	LianjiuResult deleteFaceface(String facefaceId);

	LianjiuResult getFacefaceByCategoryId(Long categoryId);

	LianjiuResult getFacefaceByCategoryId(Long categoryId, int pageNum, int pageSize);

	LianjiuResult getFacefaceByAllianceId(String allianceId);

	LianjiuResult getFacefaceByAllianceId(String allianceId, int pageNum, int pageSize);

	LianjiuResult getItemByOrdersId(String ordersId, int pageNum, int pageSize);

	LianjiuResult getOrderItemsById(String orFaceItemId);

	LianjiuResult getParamData(String productId);

	LianjiuResult modifyParam(String orItemsId, String orItemsParamModify);

	LianjiuResult ordersFilter(OrdersFaceface orders, int pageNum, int pageSize);

	LianjiuResult ordersExpireCancel(String ordersId);

	LianjiuResult vagueQuery(OrdersFaceface faceface, int pageNum, int pageSize, String cratedStart, String cratedOver);

}
