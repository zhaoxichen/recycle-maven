package com.lianjiu.rest.service;

import com.lianjiu.common.pojo.LianjiuResult;

public interface RedisService {

	LianjiuResult getRedisByToken(String tokenStr);

	LianjiuResult setRedis(String token, String value,Integer ttl);

	LianjiuResult delRedis(String tokenStr);

	String hgetRedis(String htokenStr, String tokenStr);

	LianjiuResult hsetRedis(String htokenStr, String tokenStr, String valueStr);

	Long incr(String tokenStr);

	Long ttl(String tokenStr);

	LianjiuResult hdel(String tokenStr, String valueStr);

}
