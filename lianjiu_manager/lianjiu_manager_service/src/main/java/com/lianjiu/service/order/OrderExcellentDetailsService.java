package com.lianjiu.service.order;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersExcellentDetails;

public interface OrderExcellentDetailsService {

	LianjiuResult getDetailsAll();

	LianjiuResult getDetailsAll(int pageNum, int pageSize);

	LianjiuResult getDetailsByExcellentId(String excellentId);

	LianjiuResult getDetailsById(String detailsId);

	LianjiuResult addDetails(OrdersExcellentDetails details);

	LianjiuResult updateDetails(OrdersExcellentDetails details);

	LianjiuResult deleteDetails(String detailsId);


}
