package com.lianjiu.rest.mapper.orders;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersBulkItem;

public interface OrdersBulkItemMapper {
    int deleteByPrimaryKey(String bulkItemId);

    int insert(OrdersBulkItem record);

    OrdersBulkItem selectByPrimaryKey(String bulkItemId);

    int updateByPrimaryKeySelective(OrdersBulkItem record);

	List<OrdersBulkItem> selectItemByOrdersId(@Param(value = "ordersId") String orBulkId);

	int getCreatedByItemId(OrdersBulkItem ordersBulkItem);

	int addItemList(List<OrdersBulkItem> itemList);
	
	int updateItemList(List<OrdersBulkItem> itemList);
	
	Date getCreatedById(@Param(value = "ordersId") String orBulkId);

	String getPidByOid(String ordersId);
}