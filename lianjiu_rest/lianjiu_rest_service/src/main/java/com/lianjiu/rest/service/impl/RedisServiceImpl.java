package com.lianjiu.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService {
	@Autowired
	private JedisClient jedisClient;

	@Override
	public LianjiuResult getRedisByToken(String tokenStr) {
		if (Util.isEmpty(tokenStr)) {
			return LianjiuResult.build(501, "请传入key");
		}
		String valueStr = jedisClient.get(tokenStr);
		return LianjiuResult.ok(valueStr);
	}

	/**
	 * 外部存入的redis设置失效时间
	 */
	@Override
	public LianjiuResult setRedis(String key, String value, Integer ttl) {
		if (Util.isEmpty(value)) {
			return LianjiuResult.build(501, "请传入value");
		}
		jedisClient.set(key, value);
		System.out.println(ttl);
		if (0 == ttl) {
			return LianjiuResult.ok(key);
		}
		// 设置失效时间
		jedisClient.expire(key, ttl);
		return LianjiuResult.ok(key);
	}

	@Override
	public LianjiuResult delRedis(String tokenStr) {
		long result = jedisClient.del(tokenStr);
		return LianjiuResult.ok(result);
	}

	@Override
	public String hgetRedis(String htokenStr, String tokenStr) {
		if (Util.isEmpty(htokenStr)) {
			return "请传入htokenStr";
		}
		if (Util.isEmpty(tokenStr)) {
			return "请传入key";
		}
		String valueStr = jedisClient.hget(htokenStr, tokenStr);
		return valueStr;
	}

	@Override
	public LianjiuResult hsetRedis(String htokenStr, String tokenStr, String valueStr) {
		if (Util.isEmpty(htokenStr)) {
			return LianjiuResult.build(501, "请传入htokenStr");
		}
		if (Util.isEmpty(tokenStr)) {
			return LianjiuResult.build(501, "请传入tokenStr");
		}
		if (Util.isEmpty(valueStr)) {
			return LianjiuResult.build(501, "请传入valueStr");
		}
		Long result = jedisClient.hset(htokenStr, tokenStr, valueStr);
		return LianjiuResult.ok(result);
	}

	@Override
	public Long incr(String tokenStr) {
		Long valueStr = jedisClient.incr(tokenStr);
		return valueStr;
	}

	@Override
	public Long ttl(String tokenStr) {
		Long valueStr = jedisClient.ttl(tokenStr);
		return valueStr;
	}

	@Override
	public LianjiuResult hdel(String tokenStr, String valueStr) {
		if (Util.isEmpty(tokenStr)) {
			return LianjiuResult.build(501, "请传入key");
		}
		if (Util.isEmpty(valueStr)) {
			return LianjiuResult.build(501, "请传入valueStr");
		}
		long result = jedisClient.hdel(tokenStr, valueStr);
		return LianjiuResult.ok(result);
	}

}
