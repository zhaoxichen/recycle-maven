package com.lianjiu.rest.tool;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.rest.dao.JedisClient;

public class JedisTool {
	/**
	 * 
	 * zhaoxi 2017年12月28日 descrption:从redis中获取并转换出对象列表
	 * 
	 * @param jedisClient
	 * @param key
	 * @param beanType
	 * @return List<T>
	 */
	public static <T> List<T> checkListFormRedis(JedisClient jedisClient, String key, Class<T> beanType) {
		String jsonListData = jedisClient.get(key);
		if (Util.notEmpty(jsonListData)) {
			return JsonUtils.jsonToList(jsonListData, beanType);
		}
		return null;
	}

	/**
	 * 
	 * zhaoxi 2017年12月29日 descrption:从redis中获取并转换出对象
	 * 
	 * @param jedisClient
	 * @param key
	 * @param beanType
	 * @return T
	 */
	public static <T> T checkObjectFormRedis(JedisClient jedisClient, String key, Class<T> beanType) {
		String jsonPojoData = jedisClient.get(key);
		if (Util.notEmpty(jsonPojoData)) {
			return JsonUtils.jsonToPojo(jsonPojoData, beanType);
		}
		return null;
	}

	/**
	 * 
	 * zhaoxi 2017年12月29日 descrption:批量清除redis中key
	 * 
	 * @param jedisClient
	 * @param key
	 * @param beanType
	 * @return T
	 */
	public int deleteRedisKeys(JedisClient jedisClient, String keyPrefix) {

		int i = 0;
		// 获取redis中所有链旧首页的redis的key
		Set<String> keySet = jedisClient.keys(keyPrefix);
		Iterator<String> iterator = keySet.iterator();
		/**
		 * 深夜清除缓存，用户量小不需要开线程处理
		 */
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			jedisClient.del(key);
			i++;
		}
		return i;
	}
}
