package com.lianjiu.mapper;

import java.util.List;

import com.lianjiu.model.WxOrder;

public interface WxOrderMapper {

  /* int deleteByPrimaryKey(String outTradeNo);*/

 /*   int insert(WxOrder record);*/

 //   int insertSelective(WxOrder record);

    WxOrder selectByPrimaryKey(String outTradeNo);

   int updateByPrimaryKeySelective(WxOrder record);

//    int updateByPrimaryKey(WxOrder record);

	List<WxOrder> queryWXOrderList(WxOrder order);

	int insertWXOrder(WxOrder wxOrder);

	WxOrder queryWXOrder(WxOrder order);


}