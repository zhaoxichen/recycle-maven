package com.lianjiu.rest.dao;

import java.util.Set;

public interface JedisClient {

	String get(String key);
	String set(String key, String value);
	String hget(String hkey, String key);//获取key004对应的值
	long hset(String hkey, String key, String value);//向名称为key的hash中添加元素field
	long incr(String key);
	long expire(String key, int second);//设置key的生命时间
	long ttl(String key);//查看key001的剩余生存时间
	long del(String key);
	long hdel(String hkey, String key);
	Set<String> keys(String string);
	
}
