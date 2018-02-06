package com.lianjiu.sso.service;

import com.lianjiu.common.pojo.LianjiuResult;

public interface RedisService {

	String getRedisByToken(String tokenStr);

	LianjiuResult setRedis(String token, String code);

	LianjiuResult delRedis(String tokenStr);

	String hgetRedis(String htokenStr, String tokenStr);

	LianjiuResult hsetRedis(String htokenStr, String tokenStr, String valueStr);

	Long incr(String tokenStr);

	Long ttl(String tokenStr);

	LianjiuResult hdel(String tokenStr, String valueStr);

}
