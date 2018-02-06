package com.lianjiu.mapper.orders;

import java.util.List;

import com.lianjiu.model.vo.OrdersItemExpress;
import com.lianjiu.model.OrdersFacefaceItem;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.model.vo.OrdersItemExcellent;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersItemMapper {

	int deleteByPrimaryKey(String orItemsId);

	int insert(OrdersItem record);

	int insertSelective(OrdersItem record);

	OrdersItem selectByPrimaryKey(String orItemsId);

	int updateByPrimaryKeySelective(OrdersItem record);

	int updateByPrimaryKeyWithBLOBs(OrdersItem record);

	int updateByPrimaryKey(OrdersItem record);

	List<OrdersItem> selectBySearchObjecVo(SearchObjecVo vo);

	List<OrdersFacefaceItem> selectByFaceFaceOrdersId(String ordersId);
	
	List<OrdersItemExpress> selectByExpressOrdersId(String ordersId);

	List<OrdersItemExcellent> selectByOrdersId(String ordersId);

	OrdersItemExpress selectOrdersExpressByItemId(String orExpItemId);

	List<String> selectItemsAccountPriceByOrdersId(String ordersId);
}