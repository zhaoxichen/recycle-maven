package com.lianjiu.rest.mapper.orders;

import java.util.List;

import com.lianjiu.model.OrdersExpressItem;
import com.lianjiu.model.OrdersFacefaceItem;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.model.vo.OrdersItemExcellent;
import com.lianjiu.model.vo.OrdersItemExpress;
import com.lianjiu.model.vo.OrdersItemFaceface;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.model.OrProductData;
import com.lianjiu.rest.model.OrdersItemInfo;

public interface OrdersItemMapper {

	int deleteByPrimaryKey(String orItemsId);

	int insert(OrdersItem record);

	// int insertSelective(OrdersItem record);

	OrdersItem selectByPrimaryKey(String orItemsId);

	int updateByPrimaryKeySelective(OrdersItem record);

	List<Integer> getItemsNumCount(String orItemsId);

	List<OrdersItem> getItemAll();

	List<OrdersItem> getItemByOrdersId(String ordersId);
	
	List<OrdersItem> getItemByOrdersExcellentId(String ordersId);

	List<OrdersItem> getItemByProductId(String productId);
	
	List<OrdersItem> getItemByHouseType();
	
	List<OrdersItem> getItemByPhoneType();

	int updateByStatus(OrdersItem item);

	int selectByOrdersItemCheck(String orItemsId);

	int addItemList(List<OrdersItem> item);

	OrdersItem getItemByOrdersStatus(String ordersId);

	String selectMaxPricebyPid(String productId);

	List<OrdersItem> slectMaxPriceItem(String productId);

	OrdersItemInfo selectByPrimaryKeys(String orItemsId);

	List<OrdersItem> selectProductByOrderId(String orders_id);

	List<OrdersItem> selectFaceFaceItemsByOrderId(String orderId);
	
	List<OrdersItem> selectExpressItemsByOrdersId(String ordersId);
	
	List<OrdersItem> getItemByordersId(String orExcellentId);

	String selectPriceMaxImage(String facefaceId);

	int updateBuyway(String orExpressId, String orItemsBuyway, String phone);

	List<OrdersItemInfo> selectByOrderId(String orOrdersId);

	List<OrdersItem> updateByOrderId(OrdersItem ordersItem);

	List<OrdersItem> selectFrontPageMatter();

	List<String> selectFromByOrderId(String orderFaceId);

	//void modifyNum(String orItemsProductId, Integer orItemsNum);

	//void modifyNum(@Param("idList") List<String> idList, @Param("numList") List<Long> numList,
		//	@Param("priceList") List<String> priceList);

	//void modifyNum(OrProductData orProductData, OrProductData orProductData2, OrProductData orProductData3);

	int modifyNum(OrProductData orProductData);

	String selectPriceByOrderId(String orItemsId);

	String selectPriceByProductId(String productId);

	List<OrdersExpressItem> selectExpressItemByOrdersId(String ordersId);

	OrdersItemInfo qualityCheckingFaceface(String orItemsId);

	OrdersItemInfo qualityCheckingExpress(String orItemsId);

	List<OrdersItem> selectBySearchObjecVo(Object object);

	List<OrdersItem> selectBySearchObjecVo(SearchObjecVo vo);

	List<OrdersItemExcellent> selectByOrdersId(String ordersId);

	OrdersItemExpress selectOrdersExpressByItemId(String orExpItemId);

	OrdersItemFaceface selectOrdersFacefaceByItemId(String orFaceItemId);
	
	List<OrdersItemExpress> selectByExpressOrdersId(String ordersId);

	List<String> selectItemsAccountPriceByOrdersId(String ordersId);

	List<OrdersFacefaceItem> selectByFaceFaceOrdersId(String ordersId);


}