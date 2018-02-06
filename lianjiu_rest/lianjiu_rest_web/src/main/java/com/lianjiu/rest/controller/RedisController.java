package com.lianjiu.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.rest.service.RedisService;

/**
 * 缓存同步Controller
 * <p>
 * Title: RedisController
 * </p>
 * <p>
 * Description:
 * </p>
 */
@Controller
@RequestMapping("/cache")
public class RedisController {
	@Autowired
	RedisService redisService;

	/**
	 * 
	 * zhaoxi 2017年9月28日 descrption:取redist库
	 * 
	 * @param tokenStr
	 * @return String
	 */
	@RequestMapping(value = "/sync", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getRedisByToken(String tokenStr) {
		if (Util.isEmpty(tokenStr)) {
			return LianjiuResult.build(401, "请传入key");
		}
		return redisService.getRedisByToken(tokenStr);
	}

	/**
	 * 
	 * zhaoxi 2017年9月28日 descrption:存redist库
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/setRedis", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult setRedis(String tokenStr, String valueStr,
			@RequestParam(value = "aliveTime", defaultValue = "3600") Integer ttl) {
		if (Util.isEmpty(tokenStr)) {
			return LianjiuResult.build(401, "请传入key");
		}
		return redisService.setRedis(tokenStr, valueStr, ttl);
	}

	/**
	 * 
	 * zhaoxi 2017年11月21日 descrption:删除
	 * 
	 * @param tokenStr
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/delRedis", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult delRedis(String tokenStr) {
		if (Util.isEmpty(tokenStr)) {
			return LianjiuResult.build(401, "请传入key");
		}
		return redisService.delRedis(tokenStr);
	}
}
