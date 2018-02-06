package com.lianjiu.mapper.orders;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersFacefaceMapper {

	int deleteByPrimaryKey(String orFacefaceId);

	int insert(OrdersFaceface record);

	int insertSelective(OrdersFaceface record);

	OrdersFaceface selectByPrimaryKey(String orFacefaceId);

	int updateByPrimaryKeySelective(OrdersFaceface record);

	int updateByPrimaryKeyWithBLOBs(OrdersFaceface record);

	int updateByPrimaryKey(OrdersFaceface record);

	List<OrdersFaceface> selectBySearchObjecVo(SearchObjecVo vo);
	
	List<OrdersFaceface> selectBySearchFilter(SearchObjecVo vo);
	
	int modifyOrdersStatus(@Param("status") Byte status, @Param("ordersId") String ordersId);
}