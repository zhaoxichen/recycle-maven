package com.lianjiu.rest.mapper.orders;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersFurniture;

public interface OrdersFurnitureMapper {


    int deleteByPrimaryKey(String orFurnitureId);

    int insert(OrdersFurniture record);

    OrdersFurniture selectByPrimaryKey(String orFurnitureId);

    int updateByPrimaryKeySelective(OrdersFurniture record);

	int ordersAutoCancel(@Param(value = "ordersIdList") List<String> ordersIdList);

}