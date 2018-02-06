package com.lianjiu.service.content.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.model.AdHotTopic;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.content.AdHotTopicMapper;
import com.lianjiu.service.content.HotTopicService;

/**
 * 评论，，服务层
 * 
 * @author zhaoxi
 *
 */
@Service
public class HotTopicServiceImpl implements HotTopicService {
	@Autowired
	private AdHotTopicMapper adHotTopicMapper;
	@Autowired
	private JedisClient jedisClient;

	/*
	 * 热门话题添加 (non-Javadoc)
	 * 
	 * @see
	 * com.lianjiu.service.content.AdService#insertHotProduct(com.lianjiu.model.
	 * AdHotProduct)
	 */
	@Override
	public LianjiuResult insertHotTopic(AdHotTopic record) {
		record.setTopId(IDUtils.genGUIDs());
		record.setAddTime(new Date());
		int i = adHotTopicMapper.insertAdHotTopic(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_TOPICS);
		return LianjiuResult.ok(i);
	}

	/*
	 * 删除指定的热门话题
	 */
	@Override
	public LianjiuResult deleteHotTopic(String topId) {
		int i = adHotTopicMapper.deleteByPrimaryKey(topId);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_TOPICS);
		return LianjiuResult.ok(i);
	}

	/*
	 * 修改指定热门话题内容
	 */
	@Override
	public LianjiuResult updateHotTopic(AdHotTopic record) {
		int i = adHotTopicMapper.updateByPrimaryKeySelective(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_TOPICS);
		return LianjiuResult.ok(i);
	}

	/*
	 * 查询指定热门产品id的内容
	 * 
	 */
	@Override
	public LianjiuResult selectHotTopicById(String topId) {
		AdHotTopic record = adHotTopicMapper.selectByPrimaryKey(topId);
		return LianjiuResult.ok(record);
	}

	/*
	 * 分页查询热门产品 (non-Javadoc)
	 * 
	 * @see com.lianjiu.service.content.ActivityService#selectActivityList(int,
	 * int)
	 */
	public LianjiuResult selectHotTopicList(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<AdHotTopic> list = adHotTopicMapper.selectElectronicList();
		Page<AdHotTopic> topiclist = (Page<AdHotTopic>) list;
		LianjiuResult result = new LianjiuResult();
		result.setTotal(topiclist.getTotal());
		result.setLianjiuData(list);
		return result;
	}

	@Override
	public LianjiuResult vagueQuery(AdHotTopic adHotTopic, int pageNum, int pageSize, String cratedStart,
			String cratedOver) {
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<AdHotTopic> adHotList = adHotTopicMapper.vagueQuery(adHotTopic,cratedStart,cratedOver);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<AdHotTopic> list = (Page<AdHotTopic>) adHotList;
		System.out.println("总页数：" + list.getPages());
		LianjiuResult result = new LianjiuResult(adHotList);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}
}
