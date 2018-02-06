package com.lianjiu.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AppVersion;
import com.lianjiu.rest.mapper.AppVersionMapper;
import com.lianjiu.rest.service.AppVersionService;

@Service
public class AppVersionServiceImpl implements AppVersionService {

	@Autowired
	AppVersionMapper appVersionMapper;

	/**
	 * 加盟商
	 */
	@Override
	public LianjiuResult inerstAllianceVersion(AppVersion version) {
		version.setUrl("https://lianjiuhuishou.com/download/app-lianjiu-alliance-" + version.getVersionName() + ".apk");
		version.setAppType(0);
		appVersionMapper.inerstVersion(version);
		return LianjiuResult.ok(version.getUrl());
	}

	@Override
	public LianjiuResult getAllianceNewVersion() {
		AppVersion version = appVersionMapper.getNewVersion(0);
		return LianjiuResult.ok(version);
	}

	/**
	 * 链旧
	 */
	@Override
	public LianjiuResult inerstLianjiuVersion(AppVersion version) {
		version.setUrl("https://lianjiuhuishou.com/download/app-lianjiu-" + version.getVersionName() + ".apk");
		version.setAppType(1);
		appVersionMapper.inerstVersion(version);
		return LianjiuResult.ok(version.getUrl());
	}

	@Override
	public LianjiuResult getLianjiuNewVersion() {
		AppVersion version = appVersionMapper.getNewVersion(1);
		return LianjiuResult.ok(version);
	}
}
