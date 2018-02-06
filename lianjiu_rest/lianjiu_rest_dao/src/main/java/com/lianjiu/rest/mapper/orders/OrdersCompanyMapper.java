package com.lianjiu.rest.mapper.orders;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersCompany;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersCompanyMapper {

	int deleteByPrimaryKey(String orCompanyId);

	int insert(OrdersCompany record);

	int insertSelective(OrdersCompany record);

	OrdersCompany selectByPrimaryKey(String orCompanyId);

	int updateByPrimaryKeySelective(OrdersCompany record);

	int updateByPrimaryKeyWithBLOBs(OrdersCompany record);

	int updateByPrimaryKey(OrdersCompany record);

	List<OrdersCompany> selectBySearchObjecVo(SearchObjecVo vo);

	List<OrdersCompany> vagueQuery(@Param(value = "company") OrdersCompany ordersCompany,@Param(value = "cratedStart")  String cratedStart,@Param(value = "cratedOver") String cratedOver);
}