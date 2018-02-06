package com.lianjiu.service.user;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AllianceBusiness;

public interface AllianceBusinessService {

	LianjiuResult selectAllBusiness(int pageNum, int pageSize);

	LianjiuResult addAllianceBusiness(AllianceBusiness allianceBusiness);

	LianjiuResult deleteAllianceBusinessById(String allianceBusinessId);

	LianjiuResult selectAllanceBusinessById(String allianceBusinessId);

	LianjiuResult selectAllBusiness();

	LianjiuResult updateAllBusiness(AllianceBusiness allianceBusiness);

	LianjiuResult getAllBusinessById(String allianceBusinessId);

}
