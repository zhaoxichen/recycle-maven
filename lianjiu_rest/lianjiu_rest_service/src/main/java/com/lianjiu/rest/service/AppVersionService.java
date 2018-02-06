package com.lianjiu.rest.service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AppVersion;

public interface AppVersionService {

	LianjiuResult inerstAllianceVersion(AppVersion version);

	LianjiuResult getAllianceNewVersion();
	
	LianjiuResult inerstLianjiuVersion(AppVersion version);

	LianjiuResult getLianjiuNewVersion();

}
