package com.lianjiu.service.user;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AllianceBusinessApplication;

public interface AllianceBusinessApplicationService {

	LianjiuResult selectAlliaceApplication(int pageNum, int pageSize);

	LianjiuResult addAlliaceApplication(AllianceBusinessApplication allianceBusinessApplication);

	LianjiuResult updateAlliaceApplication(AllianceBusinessApplication allianceBusinessApplication);

	LianjiuResult selectAlliaceApplication();

	LianjiuResult getBusinessDetailsByCid(String albnessApplicationId);

	LianjiuResult deleteAllianceBusinessApplicationById(String albnessApplicationId);

	LianjiuResult getAllianceAppByCid(long categoryId, int pageNum, int pageSize);

	LianjiuResult vagueQuery(AllianceBusinessApplication allianceBusinessApplication, int pageNum, int pageSize,
			String cratedStart, String cratedOver);

}
