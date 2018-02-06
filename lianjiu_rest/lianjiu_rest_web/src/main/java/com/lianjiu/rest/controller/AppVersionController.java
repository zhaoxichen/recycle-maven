package com.lianjiu.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.AppVersion;
import com.lianjiu.rest.service.AppVersionService;

/**
 * 版本表 只有查的功能
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/version")
public class AppVersionController {

	@Autowired
	private AppVersionService appVersionService;

	/**
	 * 
	 * huangfu 2017年11月28日 descrption:获取最新版本
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getNewVersion", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getNewVersion() {
		LianjiuResult result = appVersionService.getAllianceNewVersion();
		return result;
	}

	/**
	 * 
	 * huangfu 2017年11月28日 descrption:添加版本信息
	 * 
	 * @param parentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/inerstVersion", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult inerstVersion(AppVersion version) {
		if (Util.isEmpty(version.getVersionName())) {
			return LianjiuResult.build(401, "请传入正确的versionName");
		}
		LianjiuResult result = appVersionService.inerstAllianceVersion(version);
		return result;
	}
	
	/**
	 * 
	 * huangfu 2017年11月28日 descrption:获取最新版本
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getLianjiuVersion", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getLianjiuNewVersion() {
		LianjiuResult result = appVersionService.getLianjiuNewVersion();
		return result;
	}

	/**
	 * 
	 * huangfu 2017年11月28日 descrption:添加版本信息
	 * 
	 * @param parentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/inerstLianjiuVersion", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult inerstLianjiuVersion(AppVersion version) {
		if (Util.isEmpty(version.getVersionName())) {
			return LianjiuResult.build(401, "请传入正确的versionName");
		}
		LianjiuResult result = appVersionService.inerstLianjiuVersion(version);
		return result;
	}
}
