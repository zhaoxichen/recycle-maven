package com.lianjiu.service.order;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersCompany;

public interface OrderCompanyService {

	LianjiuResult getCompanyAll();

	LianjiuResult getCompanyAll(int pageNum, int pageSize);

	LianjiuResult getCompanyByCid(Long cid);

	LianjiuResult getCompanyByCid(Long cid, int pageNum, int pageSize);

	LianjiuResult getCompanyById(String companyId);

	LianjiuResult addCompany(OrdersCompany company);

	LianjiuResult updateCompany(OrdersCompany company);

	LianjiuResult deleteCompany(String companyId);

	LianjiuResult vagueQuery(OrdersCompany ordersCompany, int pageNum, int pageSize, String cratedStart,
			String cratedOver);
	
}
