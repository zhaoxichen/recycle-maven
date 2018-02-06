package com.lianjiu.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.sso.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService {
	@Autowired
	private JedisClient jedisClient;

	@Override
	public String getRedisByToken(String tokenStr) {
		if (Util.isEmpty(tokenStr)) {
			return "请传入key";
		}
		String valueStr = jedisClient.get(tokenStr);
		return valueStr;
	}

	@Override
	public LianjiuResult setRedis(String key, String value) {
		if (Util.isEmpty(key)) {
			return LianjiuResult.build(501, "请传入key");
		}
		if (Util.isEmpty(value)) {
			return LianjiuResult.build(502, "请传入value");
		}
		System.out.println(key);
		System.err.println(value);
		String result = jedisClient.set(key, value);
		return LianjiuResult.ok(result);
	}

	@Override
	public LianjiuResult delRedis(String tokenStr) {
		if (Util.isEmpty(tokenStr)) {
			return LianjiuResult.build(501, "请传入key");
		}
		long result = jedisClient.del(tokenStr);
		return LianjiuResult.ok(result);
	}

	@Override
	public String hgetRedis(String htokenStr,String tokenStr) {
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
		long result = jedisClient.hdel(tokenStr,valueStr);
		return LianjiuResult.ok(result);
	}

}
