package com.lianjiu.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.sso.service.RedisService;

/**
 * 外部访问redis接口
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/redisMemory")
public class RedisController {

	@Autowired
	RedisService redisService;

	/**
	 * 
	 * zhaoxi 2017年9月28日 descrption:取redist库
	 * 
	 * @param tokenStr
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRedisByToken/{tokenStr}", method = RequestMethod.GET)
	@ResponseBody
	public String getRedisByToken(@PathVariable String tokenStr) {
		return redisService.getRedisByToken(tokenStr);
	}

	/**
	 * 
	 * zhaoxi 2017年9月28日 descrption:存redist库
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/setRedis/{tokenStr}/{valueStr}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult setRedis(@PathVariable String tokenStr, @PathVariable String valueStr) {
		return redisService.setRedis(tokenStr, valueStr);
	}

	/**
	 * 
	 * zhaoxi 2017年9月28日 descrption:存redist库,json对象
	 * 
	 * @param tokenStr
	 * @param valueJsonStr
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/setRedisJson/{tokenStr}",consumes = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult setRedisJson(@PathVariable String tokenStr, @RequestBody String valueJsonStr) {
		System.out.println(valueJsonStr);
		return redisService.setRedis(tokenStr, valueJsonStr);
	}

	/**
	 * 
	 * zhaoxi 2017年9月28日 descrption:删除
	 * 
	 * @param tokenStr
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/delRedisByToken/{tokenStr}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult delRedis(@PathVariable String tokenStr) {
		return redisService.delRedis(tokenStr);
	}
	/**
	 * 获取在哈希表中指定 key 的所有字段和值
	 * @param hkey
	 * @param key
	 * @return
	 */
	@RequestMapping(value = "/hgetRedisByToken/{htokenStr}/{tokenStr}", method = RequestMethod.POST)
	@ResponseBody
	public String hgetRedis(@PathVariable String htokenStr,@PathVariable String tokenStr){
		return redisService.hgetRedis(htokenStr,tokenStr);
	}
	/**
	 * 为哈希表中的字段赋值
	 * @param hkey
	 * @param key
	 * @param value
	 * @return
	 */
	@RequestMapping(value = "/hsetRedisByToken/{htokenStr}/{tokenStr}/{valueStr}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult hsetRedis(@PathVariable String htokenStr,@PathVariable String tokenStr,@PathVariable String valueStr){
		return redisService.hsetRedis(htokenStr,tokenStr,valueStr);
	}
	/**
	 * 将 key 中储存的数字值增一,如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行INCR操作
	 * @param hkey
	 * @param key
	 * @param value
	 * @return
	 */
	@RequestMapping(value = "/incrRedisByToken/{tokenStr}", method = RequestMethod.POST)
	@ResponseBody
	public Long incrRedis(@PathVariable String tokenStr){
		return redisService.incr(tokenStr);
	}
	/**
	 * 以秒为单位返回 key 的剩余过期时间
	 * @param tokenStr
	 * @return
	 */
	@RequestMapping(value = "/ttlRedisBytoken/{tokenStr}", method = RequestMethod.POST)
	@ResponseBody
	public Long ttl(@PathVariable String tokenStr){
		return redisService.ttl(tokenStr);
	}
	/**
	 * 删除哈希表key中的一个或多个指定字段
	 * @param tokenStr
	 * @param valueStr
	 * @return
	 */
	@RequestMapping(value = "/delRedisByToken/{tokenStr}/{valueStr}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult hdel(@PathVariable String tokenStr,@PathVariable String valueStr){
		return redisService.hdel(tokenStr,valueStr);
	}

}
