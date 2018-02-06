package com.lianjiu.rest.mapper;

import com.lianjiu.model.AppVersion;

public interface AppVersionMapper {

	int inerstVersion(AppVersion version);

	AppVersion getNewVersion(Integer appType);

}
