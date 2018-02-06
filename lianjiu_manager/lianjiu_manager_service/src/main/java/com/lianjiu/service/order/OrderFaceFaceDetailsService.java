package com.lianjiu.service.order;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersFacefaceDetails;

public interface OrderFaceFaceDetailsService {

	LianjiuResult getDetailsAll();

	LianjiuResult getDetailsAll(int pageNum, int pageSize);

	LianjiuResult getDetailsByFacefaceId(String facefaceId);

	LianjiuResult getDetailsById(String detailsId);

	LianjiuResult addDetails(OrdersFacefaceDetails details);

	LianjiuResult updateDetails(OrdersFacefaceDetails details);

	LianjiuResult deleteDetails(String detailsId);
}
