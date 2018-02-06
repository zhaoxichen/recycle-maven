package com.lianjiu.rest.service.user;

import java.util.List;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AllianceBusinessApplication;
import com.lianjiu.model.AllianceBusinessDetails;
import com.lianjiu.model.OrdersItem;

public interface AllianceBusinessService {

	LianjiuResult updateBusinessById(AllianceBusinessDetails details);

	LianjiuResult selectAllianceById(String userId);

	LianjiuResult selectCommentById(String userId, Integer pageNum, Integer pageSize);

	LianjiuResult selectUserWalletById(String userId, Integer pageNum, Integer pageSize);

	LianjiuResult dispatchFaceOrder(String orderFaceId);

	LianjiuResult modifyFaceOrder(String orFacefaceAllianceId, String orFacefaceId);

	LianjiuResult alreadyFaceOrder(Integer pageNum, Integer pageSize, String orFacefaceAllianceId,
			Integer orFacefaceStatus);

	LianjiuResult getOrders(String orOrdersId);

	LianjiuResult modifyOrders(OrdersItem ordersItem);

	LianjiuResult selectProductByid(String orItemsId);

	LianjiuResult Logout();

	LianjiuResult selectById(String allianceId);

	LianjiuResult selectFaceOrderById(Integer pageNum, Integer pageSize, String latitude, String longitude,
			String allianceId);

	LianjiuResult modifyMessageCenterStatus(String messageId);

	LianjiuResult messageCenter(String allianceId, Integer pageSize, Integer pageNum);

	LianjiuResult deleMessageCenter(String messageId);

	LianjiuResult reappraisal(String orItemsId, String productId, String paramJson, String itemsPrice);

	LianjiuResult modifyTotalPrice(List<String> idList, List<String> numList, List<String> priceList,
			String orFfDetailsRetrPrice, String ordersId);

	LianjiuResult selectReappraisal(String productId);

	LianjiuResult addAlliaceApplication(AllianceBusinessApplication allianceBusinessApplication);

	LianjiuResult selectFaceOrderByIds(Integer pageNum, Integer pageSize, String latitude, String longitude,
			String allianceId);

	LianjiuResult lock(String orderFaceId);

	LianjiuResult unLock(String orderFaceId);
	
	LianjiuResult checkLock(String orderFaceId);

	LianjiuResult updateSite(String allianceId, String latitude, String longitude);

}
